package com.cityCatTarot.controllers;

import com.cityCatTarot.application.UserService;
import com.cityCatTarot.domain.User;
import com.cityCatTarot.dto.UserModificationData;
import com.cityCatTarot.dto.UserRegistrationData;
import com.cityCatTarot.errors.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @BeforeEach
    void setUp() {

        given(userService.registerUser(any(UserRegistrationData.class)))
                .will(invocation -> {
                    UserRegistrationData registrationData = invocation.getArgument(0);
                    return User.builder()
                            .id(1L)
                            .email(registrationData.getEmail())
                            .nickName(registrationData.getNickName())
                            .build();
                });

        given(userService.updateUser(eq(1L), any(UserModificationData.class)))
                .will(invocation -> {
                    Long id = invocation.getArgument(0);
                    UserModificationData modificationData =
                            invocation.getArgument(1);
                    return User.builder()
                            .id(id)
                            .email("tester@example.com")
                            .nickName(modificationData.getNickName())
                            .build();
                });

        given(userService.updateUser(eq(999L), any(UserModificationData.class)))
                .willThrow(new UserNotFoundException(99L));


        given(userService.deleteUser(100L))
                .willThrow(new UserNotFoundException(100L));
    }

    @Test
    @DisplayName("POST 요청은 새로운 회원을 추가하면 201 코드와 생성된 회원을 응답한다.")
    void registerUserWithValidAttributes() throws Exception {
        mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tester@example.com\"," +
                                "\"nickName\":\"Tester\",\"password\":\"test\"}")
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        containsString("\"id\":1")
                ))
                .andExpect(content().string(
                        containsString("\"email\":\"tester@example.com\"")
                ))
                .andExpect(content().string(
                        containsString("\"nickName\":\"Tester\"")
                ));

        verify(userService).registerUser(any(UserRegistrationData.class));
    }

    @Test
    @DisplayName("PATCH 요청은 등록된 회원의 id가 주어지면 200 코드와 수정된 회원을 응답한다.")
    void updateUserWithValidAttributes() throws Exception {
        mockMvc.perform(
                patch("/patch-userInfo/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickName\":\"revisedNickName\",\"password\":\"revisedPassword\"}")
        )
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1")
                ))
                .andExpect(content().string(
                        containsString("\"nickName\":\"revisedNickName\"")
                ));

        verify(userService).updateUser(eq(1L), any(UserModificationData.class));
    }

    @Test
    @DisplayName("PATCH 요청은 등록되지 않은 회원의 id가 주어지면 404 코드를 응답한다.")
    void updateUserWithNotExistingId() throws Exception {
        mockMvc.perform(
                patch("/patch-userInfo/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickName\":\"revisedNickName\",\"password\":\"revisedPassword\"}")
        )
                .andExpect(status().isNotFound());

        verify(userService).updateUser(eq(99L), any(UserModificationData.class));
    }

    @Test
    @DisplayName("DELETE 요청은 등록된 회원의 id가 주어지면 그 id에 해당하는 회원을 삭제한다.")
    void destroyWithExistingId() throws Exception {
        mockMvc.perform(delete("/delete-user/{id}",1L))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("DELETE 요청은 등록되지 않은 회원의 id가 주어지면 404 코드를 응답한다.")
    void destroyWithNotExistingId() throws Exception {
        mockMvc.perform(delete("/delete-user/{id}",100L))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(100L);
    }
}
