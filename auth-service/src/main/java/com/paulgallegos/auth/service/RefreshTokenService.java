package com.paulgallegos.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final String PREFIX = "refresh_token:";
    private static final long REFRESH_TOKEN_DAYS = 7;

    private final StringRedisTemplate redisTemplate;

    public void save(String userId, String refreshToken){
        redisTemplate.opsForValue()
                .set(PREFIX + userId, refreshToken, REFRESH_TOKEN_DAYS, TimeUnit.DAYS);
    }

    public boolean validate(String userId, String refreshToken){
        String stored = redisTemplate.opsForValue().get(PREFIX + userId);
        return refreshToken.equals(stored);
    }

    public void delete(String userId){
        redisTemplate.delete(PREFIX + userId);
    }
}
