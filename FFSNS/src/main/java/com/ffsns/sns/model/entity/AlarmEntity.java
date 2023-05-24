package com.ffsns.sns.model.entity;

import com.ffsns.sns.model.AlarmArgs;
import com.ffsns.sns.model.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "alarm")
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonBinaryType.class)
@SQLDelete(sql = "UPDATE alarm SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class AlarmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // 알람을 받은 사람에 대한 정보
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private AlarmArgs args;
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


    public static AlarmEntity of(UserEntity userEntity, AlarmType alarmType, AlarmArgs args){
        AlarmEntity entity = new AlarmEntity();
        entity.setUserEntity(userEntity);
        entity.setAlarmType(alarmType);
        entity.setArgs(args);

        return entity;
    }
}
