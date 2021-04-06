package com.cityCatTarot.application;

import com.cityCatTarot.domain.Role;
import com.cityCatTarot.domain.RoleRepository;
import com.cityCatTarot.domain.User;
import com.cityCatTarot.domain.UserRepository;
import com.cityCatTarot.errors.EncoderFailException;
import com.cityCatTarot.errors.LoginFailWithNotFoundEmailException;
import com.cityCatTarot.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 인증을 담당합니다.
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 JwtUtil jwtUtil,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 전달된 로그인 정보로 인증한 토큰을 반환합니다.
     *
     * @param email    회원 이메일
     * @param password 회원 비밀번호
     * @return 인증 토큰
     */
    public String login(String email, String password) {

        User user = userRepository.findByEmailForLogin(email)
                .orElseThrow(() -> new LoginFailWithNotFoundEmailException(email));

        if ((user.authenticate(password, passwordEncoder)) == false) {
            throw new EncoderFailException(email);
        }

        return jwtUtil.encode(user.getId());
    }

    /**
     * 전달된 토큰을 복호화하여 회원 식별자를 리턴합니다.
     * @param accessToken 인증 토큰
     * @return 회원 식별자
     */
    public Long parseToken(String accessToken) {
        Claims claims = jwtUtil.decode(accessToken);
        return claims.get("user_id", Long.class);
    }

    /**
     * 전달된 회원 식별자가 가지고 있는 권한을 리턴합니다.
     *
     * @param userId 회원 식별자
     * @return 회원 권한 목록
     */
    public List<Role> roles(Long userId) {
        return roleRepository.findAllByUserId(userId);
    }
}
