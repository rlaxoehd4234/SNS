package com.ffsns.sns.model;

import com.ffsns.sns.model.entity.CommentEntity;
import com.ffsns.sns.model.entity.PostEntity;
import com.ffsns.sns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Comment {

    private Integer id;

    private String comment;

    private String userName;

    private Integer postId;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;



    public static Comment formEntity(CommentEntity entity){
        return new Comment(
               entity.getId(),
               entity.getComment(),
               entity.getUserEntity().getUserName(),
               entity.getPostEntity().getId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );

    }
}
