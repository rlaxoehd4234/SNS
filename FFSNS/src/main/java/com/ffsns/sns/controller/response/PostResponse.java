package com.ffsns.sns.controller.response;

import com.ffsns.sns.model.Post;
import com.ffsns.sns.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Integer id;

    private String title;
    private String body;

    private Timestamp registeredAt;

    private UserResponse user;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static PostResponse fromPost(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getRegisteredAt(),
                UserResponse.fromUser(post.getUser()),
                post.getUpdatedAt(),
                post.getDeletedAt()
        );
    }

}
