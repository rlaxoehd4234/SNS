package com.ffsns.sns.model;

import com.ffsns.sns.model.entity.PostEntity;
import com.ffsns.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Post {
    private Integer id;

    private String title;
    private String body;

    private Timestamp registeredAt;

    private User user;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static Post fromEntity(PostEntity entity){
        return new Post(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                entity.getRegisteredAt(),
                User.fromEntity(entity.getUserEntity()),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
