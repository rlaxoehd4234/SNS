package com.ffsns.sns.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "comment")
@Getter
@Setter
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "comment")
    private String comment;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    //등록 시간을 넣어주는 메서드

    @PrePersist
    void registeredAt(){
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt(){
        this.updatedAt = Timestamp.from(Instant.now());
    }


    public static CommentEntity of(PostEntity postEntity, UserEntity userEntity, String comment){
        CommentEntity entity = new CommentEntity();
        entity.setUserEntity(userEntity);
        entity.setPostEntity(postEntity);
        entity.setComment(comment);

        return entity;
    }
}
