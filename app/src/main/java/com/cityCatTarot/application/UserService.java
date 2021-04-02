package com.cityCatTarot.application;

import com.cityCatTarot.domain.User;
import com.cityCatTarot.domain.UserRepository;
import com.cityCatTarot.dto.UserModificationData;
import com.cityCatTarot.dto.UserRegistrationData;
import com.cityCatTarot.errors.UserEmailDuplicationException;
import com.cityCatTarot.errors.UserNotFoundException;
import com.github.dozermapper.core.Mapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원에 관한 비즈니스 로직을 담당합니다.
 */
@Service
@Transactional
public class UserService {

    private final Mapper mapper;
    private final UserRepository userRepository;

    public UserService(Mapper dozerMapper,
                       UserRepository userRepository) {
        this.mapper = dozerMapper;
        this.userRepository = userRepository;
    }

    /**
     * 전달된 회원 정보로 회원을 생성한 뒤, 그 회원을 리턴합니다.
     *
     * @param registrationData 회원 정보
     * @return 생성된 회원
     * @throws UserEmailDuplicationException 이메일이 중복된 경우
     */
    public User registerUser(UserRegistrationData registrationData) {
        String email = registrationData.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }

        User user = userRepository.save(
                mapper.map(registrationData, User.class));

        return user;
    }

    /**
     * id로 회원을 찾아 주어진 정보로 수정한 다음 리턴합니다.
     *
     * @param id               수정할 회원 식별자
     * @param modificationData 수정할 회원 정보
     * @return 수정된 회원
     * @throws AccessDeniedException 수정 권한이 없을 경우
     */
    public User updateUser(Long id, UserModificationData modificationData) {
        User user = findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * 전달된 식별자에 해당하는 회원을 삭제합니다.
     *
     * @param id 회원 식별자
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }

    /**
     * 전달된 식별자에 해당하는 회원을 찾습니다.
     *
     * @param id 회원 식별자
     */
    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
  
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
