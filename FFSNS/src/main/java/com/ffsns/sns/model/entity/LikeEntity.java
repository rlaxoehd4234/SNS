package com.ffsns.sns.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "likes")
@Getter
@Setter
@SQLDelete(sql = "UPDATE likes SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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


    public static LikeEntity of(PostEntity postEntity, UserEntity userEntity){
        LikeEntity entity = new LikeEntity();
        entity.setUserEntity(userEntity);
        entity.setPostEntity(postEntity);

        return entity;
    }
}
