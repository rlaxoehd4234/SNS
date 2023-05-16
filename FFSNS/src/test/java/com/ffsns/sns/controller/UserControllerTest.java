package com.ffsns.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffsns.sns.controller.request.UserJoinRequest;
import com.ffsns.sns.controller.request.UserLoginRequest;
import com.ffsns.sns.exception.SnsApplicationException;
import com.ffsns.sns.model.User;
import com.ffsns.sns.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void 회원가입() throws Exception {

        String username ="kimtaedong";
        String password = "1111";

        when(userService.join()).thenReturn(mock(User.class));


        mockMvc.perform(post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username , password)))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 이미회원가입된_userName으로_가입하는경우_에러반() throws Exception{

        String username ="kimtaedong";
        String password = "1111";

        when(userService.join()).thenThrow(new SnsApplicationException());


        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(username, password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 로그인() throws Exception {

        String username ="kimtaedong";
        String password = "1111";

        when(userService.login()).thenReturn("test_token");


        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username , password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인시_회원가입하지않은_userName_으로_접근하는_경우_에러반환() throws Exception {

        String username ="kimtaedong";
        String password = "1111";

        when(userService.login()).thenThrow(new SnsApplicationException());


        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username , password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 로그인시_틀린_password_으로_접근하는_경우_에러반환() throws Exception {

        String username ="kimtaedong";
        String password = "1111";

        when(userService.login()).thenThrow(new SnsApplicationException());


        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(username , password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
