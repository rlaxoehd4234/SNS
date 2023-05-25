package com.ffsns.sns.repository;

import com.ffsns.sns.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {
    private final RedisTemplate<String, User> userRedisTemplate;
    private final Duration USER_CACHE_TTL = Duration.ofDays(3);


    public void setUser(User user){
        String key = user.getUsername();
        log.info("Set User to {} : {}",key,user);
        userRedisTemplate.opsForValue().set(getKey(key),user, USER_CACHE_TTL);
    }

    public Optional<User> getUser(String userName){
        String key = getKey(userName);
        User user = userRedisTemplate.opsForValue().get(key);
        log.info("Get data from redis {} : {}", key, user);

        return Optional.ofNullable(user);
    }

    public String getKey(String userName){
        return "USER:" + userName;
    }

}
