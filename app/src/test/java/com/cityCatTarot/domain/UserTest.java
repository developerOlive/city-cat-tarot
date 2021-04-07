package com.cityCatTarot.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("changeWith는 수정할 회원정보가 주어지면, 회원정보를 변경한다.")
    void changeWith() {
        User user = User.builder()
                        .email("user@email.coz")
                        .nickName("userNick")
                        .password("userPw")
                        .build();

        user.changeWith(User.builder()
                .nickName("TEST")
                .password("TEST")
                .build());

        assertThat(user.getNickName()).isEqualTo("TEST");
        assertThat(user.getNickName()).isNotEqualTo("xxx");
        assertThat(user.getPassword()).isEqualTo("TEST");
        assertThat(user.getPassword()).isNotEqualTo("xxx");
    }

    @Test
    @DisplayName("changePassword는 주어진 패스워드를 암호화 한다.")
    void changePassword() {
        User user = User.builder()
                .email("user@email.coz")
                .nickName("userNick")
                .password("userPw")
                .build();

        user.changePassword("TEST", passwordEncoder);

        assertThat(user.getPassword()).isNotEmpty();
        assertThat(user.getPassword()).isNotEqualTo("TEST");
    }

    @Test
    @DisplayName("destroy는 주어진 회원을 삭제한다.")
    void destroy() {
        User user = User.builder()
                .email("user@email.coz")
                .nickName("userNick")
                .password("userPw")
                .build();

        assertThat(user.isDeleted()).isFalse();

        user.destroy();
        assertThat(user.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("authenticate는 회원이 삭제되지 않았고, 비밀번호가 일치하면 true를 반환한다.")
    void authenticateWithDeletedUser() {
        User user = User.builder()
                .email("user@email.coz")
                .nickName("userNick")
                .password("userPw")
                .deleted(false)
                .build();

        user.changePassword("userPw", passwordEncoder);

        System.out.println("궁금해요 : " + user.authenticate("test", passwordEncoder));
        assertThat(user.authenticate("userPw", passwordEncoder)).isTrue();
        assertThat(user.authenticate("xxx", passwordEncoder)).isFalse();
    }


    @Test
    @DisplayName("authenticate는 회원이 삭제되면, 비밀번호가 일치해도 false 반환한다.")
    void authenticate() {
        User user = User.builder()
                .email("user@email.coz")
                .nickName("userNick")
                .password("userPw")
                .deleted(true)
                .build();

        user.changePassword("userPw", passwordEncoder);

        assertThat(user.authenticate("userPw", passwordEncoder)).isFalse();
    }
}
