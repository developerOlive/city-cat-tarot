package com.cityCatTarot.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoryUserRepository implements UserRepository {

    private final EntityManager entityManager;

    public MemoryUserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.of(entityManager.find(User.class, id));
    }

    @Override
    public List<User> findByEmail(String email) {
        return entityManager.createQuery("select u from User u where u.email = :email",
                        User.class)
                .setParameter("email", email)
                .getResultList();
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("select u from User u", User.class)
                .getResultList();
    }

    @Override
    public boolean existsByEmail(String email) {
        if (!findByEmail(email).isEmpty()){
            return true;
        }
        return false;
    }
}
