package com.ffsns.sns.model;


import com.ffsns.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Alarm {
    private Integer id;
    // 알람을 받은 사람에 대한 정보
    private User user;
    private AlarmType alarmType;
    private AlarmArgs args;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;


    public static Alarm fromEntity(AlarmEntity entity){
        return new Alarm(
                entity.getId(),
                User.fromEntity(entity.getUserEntity()),
                entity.getAlarmType(),
                entity.getArgs(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
