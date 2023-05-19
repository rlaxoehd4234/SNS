package com.ffsns.sns.controller;

import com.ffsns.sns.exception.ErrorCode;
import com.ffsns.sns.exception.SnsApplicationException;
import com.ffsns.sns.fixture.PostEntityFixture;
import com.ffsns.sns.fixture.UserEntityFixture;
import com.ffsns.sns.model.entity.PostEntity;
import com.ffsns.sns.model.entity.UserEntity;
import com.ffsns.sns.repository.PostEntityRepository;
import com.ffsns.sns.repository.UserEntityRepository;
import com.ffsns.sns.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {


    @Autowired
    private PostService postService;

    @MockBean
    private UserEntityRepository userEntityRepository;
    @MockBean
    private PostEntityRepository postEntityRepository;
    @Test
    void 포스트_작성이_성공한_경우(){
        String title = "title";
        String body = "body";
        String userName ="kimtaedong";

        //mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));


        Assertions.assertDoesNotThrow( () -> postService.create(title, body, userName));


    }


    @Test
    void 포스트_작성시_로그인한_유저가_아닌경우(){

        String title = "title";
        String body = "body";
        String userName = "kimtaedong";

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        SnsApplicationException e = Assertions.
                assertThrows(SnsApplicationException.class, () -> postService.create(title, body, userName));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());

    }

    @Test
    void 포스트_수정에_성공(){
        String title = "1111";
        String body = "1111";
        String userName ="kimtaedong";
        Integer postId = 1;



        PostEntity post = PostEntityFixture.get(userName ,postId,1);
        UserEntity user = post.getUserEntity();
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(user));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postEntityRepository.saveAndFlush(any())).thenReturn(post);


        Assertions.assertDoesNotThrow( () -> postService.modify(title, body, userName, postId));

    }

    @Test
    void 포스트_수정시_게시물이없는경우(){
        String title = "1111";
        String body = "1111";
        String userName ="kimtaedong";
        Integer postId = 1;


        PostEntity post = PostEntityFixture.get(userName ,postId,1);
        UserEntity user = post.getUserEntity();
        when(userEntityRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
    }
    @Test
    void 포스트_수정시_권한이없는경우(){
        String title = "1111";
        String body = "1111";
        String userName ="kimtaedong";
        Integer postId = 1;


        PostEntity post = PostEntityFixture.get(userName ,postId,1);
        UserEntity userEntity = UserEntityFixture.get("jjabtaedong","123", 2);

        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(post));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(title, body, userName, postId));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }
}
