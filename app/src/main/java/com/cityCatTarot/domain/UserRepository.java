package com.cityCatTarot.domain;

import java.util.List;
import java.util.Optional;

/**
 * 회원 저장소.
 */
public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    List<User> findByEmail(String email);

    List<User> findAll();

    boolean existsByEmail(String email);
}
