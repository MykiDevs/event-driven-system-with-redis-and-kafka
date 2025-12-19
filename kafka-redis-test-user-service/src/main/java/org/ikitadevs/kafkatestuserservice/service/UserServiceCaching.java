package org.ikitadevs.kafkatestuserservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.quota.ClientQuotaAlteration;
import org.ikitadevs.kafkatestuserservice.models.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceCaching {
    private final RedisTemplate<String, User> redisTemplate;
    private static String CACHE_PREFIX = "user:";
    private static final long DEFAULT_TIMEOUT = 30;
    private final ObjectMapper objectMapper;

    public User getById(Long id) {
        User objFromCache = redisTemplate.opsForValue().getAndExpire(CACHE_PREFIX + id, DEFAULT_TIMEOUT, TimeUnit.MINUTES);
        if (objFromCache == null) return null;
        try {
            log.info("objFromCache is" + objectMapper.writeValueAsString(objFromCache));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return objFromCache;
    }

    public void put(User user) {
        put(user, DEFAULT_TIMEOUT, TimeUnit.MINUTES);
    }

    public void put(User user, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(CACHE_PREFIX + user.getId(), user, timeout, timeUnit);
        log.info("Putted user with: {}", user.toString());
    }
    public void delete(Long id) {
        redisTemplate.delete(CACHE_PREFIX + id);
    }


}
