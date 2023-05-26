package com.ffsns.sns.service;

import com.ffsns.sns.exception.ErrorCode;
import com.ffsns.sns.exception.SnsApplicationException;
import com.ffsns.sns.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {


    private final static Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final static String ALARM_NAME = "alarm";
    private final EmitterRepository emitterRepository;
    public SseEmitter connectAlarm(Integer userId){
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, sseEmitter);
        // 브라우저 하나당 인스턴스가 하나씩 생기는 것이다.

        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        try{
            sseEmitter.send(SseEmitter.event().id("").name(ALARM_NAME).data("connectCompleted"));
        }catch (IOException e){
            throw new SnsApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);

        }
        return sseEmitter;

    }

    public void send(Integer alarmId,Integer userId){
        emitterRepository.get(userId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(alarmId.toString()).name(ALARM_NAME).data("new Alarm"));
            }catch (IOException e){
                emitterRepository.delete(userId);
                throw new SnsApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);

            }
        }, () -> log.info("No emitter founded"));
    }
}
