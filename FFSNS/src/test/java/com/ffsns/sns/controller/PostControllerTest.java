package com.ffsns.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ffsns.sns.controller.request.PostCreateRequest;
import com.ffsns.sns.controller.request.PostModifyRequest;
import com.ffsns.sns.controller.request.UserJoinRequest;
import com.ffsns.sns.exception.ErrorCode;
import com.ffsns.sns.exception.SnsApplicationException;
import com.ffsns.sns.fixture.PostEntityFixture;
import com.ffsns.sns.model.Post;
import com.ffsns.sns.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    @WithMockUser // 유저를 보내는 방법
    void 포스트_작성() throws Exception{

        String title = "kimtaedong";
        String body = "1111";

        mockMvc.perform(post(("/api/v1/posts"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithAnonymousUser // 익명의 유저를 보내는 방법
    void 포스트_작성_로그인_하지않은_경우() throws Exception{

        String title = "kimtaedong";
        String body = "1111";


        mockMvc.perform(post(("/api/v1/posts"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser
    void 포스트수정() throws Exception {
        String title = "123123";
        String body = "123123";

        when(postService.modify(eq(title), eq(body), any(), any())).thenReturn(Post.fromEntity(PostEntityFixture.get("userName",1,1)));

        mockMvc.perform(put(("/api/v1/posts/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
        ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 포스트_수정시_로그인하지_않은_유저인_경우() throws Exception {
        String title = "123";
        String body = "123123";

        mockMvc.perform(put(("/api/v1/posts/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title,body)))
        ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트_수정시_자신이_작성한_글이_아닐_경우() throws Exception {
        String title = "123";
        String body = "123123";

        //mocking
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title),eq(body),any(),eq(1));

        mockMvc.perform(put(("/api/v1/posts/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트_수정시_존재하지_않는_글일_경우() throws Exception {
        String title ="123";
        String body = "1234";

        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title),eq(body),any(),eq(1));

        mockMvc.perform(put(("/api/v1/posts/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }
}
