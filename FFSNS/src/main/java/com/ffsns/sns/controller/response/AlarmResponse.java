package com.ffsns.sns.controller.response;

import com.ffsns.sns.model.Alarm;
import com.ffsns.sns.model.AlarmArgs;
import com.ffsns.sns.model.AlarmType;
import com.ffsns.sns.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;

@Data
@Getter
@AllArgsConstructor
public class AlarmResponse {
    private Integer id;
    // 알람을 받은 사람에 대한 정보
    private AlarmType alarmType;
    private AlarmArgs args;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm){
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getRegisteredAt(),
                alarm.getUpdatedAt(),
                alarm.getDeletedAt()
        );

    }
}
