package com.cityCatTarot.application;

import com.cityCatTarot.domain.User;
import com.cityCatTarot.domain.UserRepository;
import com.cityCatTarot.dto.UserModificationData;
import com.cityCatTarot.dto.UserRegistrationData;
import com.cityCatTarot.errors.UserEmailDuplicationException;
import com.cityCatTarot.errors.UserNotFoundException;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    private UserService userService;

    private final UserRepository userRepository =
            mock(UserRepository.class);

    private final String USER_EMAIL = "test@example.com";
    private final String USER_NICKNAME = "testNickName";
    private final String USER_PASSWORD = "testPassword";
    private final Long USER_ID = 1L;

    private final String EXISTING_EMAIL_ADDRESS = "existed@example.com";
    private final String REVISED_NICKNAME = "revisedNickName";
    private final String REVISED_PASSWORD = "revisedPassword";

    private final Long NOT_EXISTING_ID = 999L;

    @BeforeEach
    void setUp() {
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        userService = new UserService(mapper, userRepository);

        given(userRepository.save(any(User.class))).will(invocation -> {
            User source = invocation.getArgument(0);
            return User.builder()
                    .id(USER_ID)
                    .email(source.getEmail())
                    .nickName(source.getNickName())
                    .build();
        });

        given(userRepository.existsByEmail(EXISTING_EMAIL_ADDRESS))
                .willReturn(true);

        given(userRepository.findById(1L))
                .willReturn(Optional.of(
                        User.builder()
                                .id(USER_ID)
                                .email(EXISTING_EMAIL_ADDRESS)
                                .nickName(USER_NICKNAME)
                                .password(USER_PASSWORD)
                                .build()));
    }

    @Test
    @DisplayName("회웝가입 시 유효한 정보가 주어지면 가입 된다.")
    void register() {
        UserRegistrationData registrationData = UserRegistrationData.builder()
                .email(USER_EMAIL)
                .nickName(USER_NICKNAME)
                .password(USER_PASSWORD)
                .build();

        User user = userService.registerUser(registrationData);

        assertThat(user.getId()).isEqualTo(USER_ID);
        assertThat(user.getEmail()).isEqualTo(USER_EMAIL);
        assertThat(user.getNickName()).isEqualTo(USER_NICKNAME);

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 시 중복된 이메일이 주어지면 예외를 던진다.")
    void registerUserWithDuplicatedEmail() {
        UserRegistrationData registrationData = UserRegistrationData.builder()
                .email(EXISTING_EMAIL_ADDRESS)
                .nickName("test")
                .password("test")
                .build();

        assertThatThrownBy(() -> userService.registerUser(registrationData))
                .isInstanceOf(UserEmailDuplicationException.class);

        verify(userRepository).existsByEmail(EXISTING_EMAIL_ADDRESS);
    }

    @Test
    @DisplayName("회원정보 수정 시 등록된 아이디와 수정 정보가 주어지면 회원 정보가 수정된다.")
    void updateUserWithExistingId() throws AccessDeniedException {

        UserModificationData modificationData = UserModificationData.builder()
                .nickName(REVISED_NICKNAME)
                .password(REVISED_PASSWORD)
                .build();

        User user = userService.updateUser(USER_ID, modificationData);

        assertThat(user.getId()).isEqualTo(USER_ID);
        assertThat(user.getEmail()).isEqualTo(EXISTING_EMAIL_ADDRESS);
        assertThat(user.getNickName()).isEqualTo(REVISED_NICKNAME);

        verify(userRepository).findById(USER_ID);
    }

    @Test
    @DisplayName("회원정보 수정 시 등록되지 않은 아이디가 주어지면 예외를 던진다.")
    void updateUserWithNotExistingId() {

        UserModificationData modificationData = UserModificationData.builder()
                .nickName(REVISED_NICKNAME)
                .password(REVISED_PASSWORD)
                .build();

        assertThatThrownBy(
                () -> userService.updateUser(NOT_EXISTING_ID, modificationData)
        )
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(NOT_EXISTING_ID);
    }

    @Test
    @DisplayName("회원삭제 시 등록된 회원이 주어지면 삭제한다.")
    void deleteUserWithExistingId() {
        User user = userService.deleteUser(USER_ID);

        assertThat(user.getId()).isEqualTo(USER_ID);
        assertThat(user.isDeleted()).isTrue();

        verify(userRepository).findById(USER_ID);
    }

    @Test
    @DisplayName("회원삭제 시 등록되지 않은 회원이 주어지면 예외를 던진다.")
    void deleteUserWithNotExistingId() {
        assertThatThrownBy(() -> userService.deleteUser(NOT_EXISTING_ID))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(NOT_EXISTING_ID);
    }

}
