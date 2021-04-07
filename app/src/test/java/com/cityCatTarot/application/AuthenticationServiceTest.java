package com.cityCatTarot.application;

import com.cityCatTarot.domain.Role;
import com.cityCatTarot.domain.RoleRepository;
import com.cityCatTarot.domain.User;
import com.cityCatTarot.domain.UserRepository;
import com.cityCatTarot.dto.SessionRequestData;
import com.cityCatTarot.errors.EncoderFailException;
import com.cityCatTarot.errors.InvalidTokenException;
import com.cityCatTarot.errors.LoginFailWithNotFoundEmailException;
import com.cityCatTarot.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AuthenticationServiceTest {

    private JwtUtil jwtUtil;
    private AuthenticationService authenticationService;
    private UserRepository userRepository = mock(UserRepository.class);
    private RoleRepository roleRepository = mock(RoleRepository.class);

    private static final String SECRET = "11112222333344445555666677778888";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxfQ.uGy7d5cSkGfI6SR9rHrbwb6ebZW9gs3DOEOAoVKl9RM";

    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxfQ.uGy7d5cSkGfI6SR9rHrbwb6ebZW9gs3DOEOAoVKl911";

    private static final String NULL_TOKEN = null;

    private final Long USER_ID = 1L;
    private final String USER_EMAIL = "olive@email.coz";
    private final String USER_PASSWORD = "password1234";

    private User user;
    private SessionRequestData sessionRequestData;
    private SessionRequestData sessionRequestDataWithoutExistingEmail;
    private SessionRequestData sessionRequestDataWithWrongPassword;

    @BeforeEach
    void setUp() {

        jwtUtil = new JwtUtil(SECRET);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        authenticationService = new AuthenticationService(
                userRepository, roleRepository, jwtUtil, passwordEncoder);


        user = User.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        sessionRequestData = SessionRequestData.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
        user.changePassword(USER_PASSWORD, passwordEncoder);

        sessionRequestDataWithoutExistingEmail = SessionRequestData.builder()
                .email("invalid" + USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        sessionRequestDataWithWrongPassword = SessionRequestData.builder()
                .email(USER_EMAIL)
                .password("wrong" + USER_PASSWORD)
                .build();

        given(userRepository.findByEmailForLogin(sessionRequestData.getEmail()))
                .willReturn(Optional.of(user));

        given(userRepository.findByEmailForLogin(sessionRequestDataWithoutExistingEmail.getEmail()))
                .willReturn(Optional.empty());

        given(userRepository.findByEmailForLogin(sessionRequestDataWithWrongPassword.getEmail()))
                .willReturn(Optional.of(user));

        given(roleRepository.findAllByUserId(1L))
                .willReturn(Arrays.asList(new Role("USER")));
    }

    @Test
    @DisplayName("login은 유효한 로그인 정보가 주어지면 인증 토큰을 리턴한다.")
    void loginWithRightEmailAndPassword() {
        String accessToken = authenticationService.login(USER_EMAIL, USER_PASSWORD);

        assertThat(accessToken).isEqualTo(VALID_TOKEN);

        verify(userRepository).findByEmailForLogin(USER_EMAIL);
    }

    @Test
    @DisplayName("login은 유효한 등록되지 않은 이메일이 주어지면 예외를 던진다.")
    void loginWithWrongEmail() {
        assertThatThrownBy(
                () -> authenticationService.login("invalid" + USER_EMAIL, USER_PASSWORD)
        ).isInstanceOf(LoginFailWithNotFoundEmailException.class);

        verify(userRepository).findByEmailForLogin("invalid" + USER_EMAIL);
    }

    @Test
    @DisplayName("login은 주어진 로그인 정보의 비밀번호가 일치하지 않는다면 예외를 던진다.")
    void loginWithWrongPassword() {
        assertThatThrownBy(
                () -> authenticationService.login(USER_EMAIL, "wrong" + USER_PASSWORD)
        ).isInstanceOf(EncoderFailException.class);

        verify(userRepository).findByEmailForLogin(USER_EMAIL);
    }

    @Test
    @DisplayName("parseToken은 유효한 토큰이 주어지면 파싱된 값을 리턴한다.")
    void parseTokenWithValidToken() {
        Long userId = authenticationService.parseToken(VALID_TOKEN);

        assertThat(userId).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("parseToken은 유효하지 않은 토큰이 주어지면 예외를 던진다.")
    void parseTokenWithInvalidToken() {
        assertThatThrownBy(
                () -> authenticationService.parseToken(INVALID_TOKEN)
        ).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("parseToken은 값이 null이거나 비어있는 토큰이 주어지면 예외를 던진다.")
    void parseTokenWithNullAndEmptyToken(){
        final String[] givenTokens = new String[]{"", " ", null};

        assertThatThrownBy(() -> authenticationService.parseToken(NULL_TOKEN));

        for (String token : givenTokens) {
            assertThrows(InvalidTokenException.class,() -> jwtUtil.decode(token));
        }
    }

    @Test
    @DisplayName("roles는 전달된 회원 식별자가 가지고 있는 권한을 리턴한다.")
    void roles() {
        assertThat(
                authenticationService.roles(USER_ID).stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
        ).isEqualTo(Arrays.asList("USER"));
    }
}
