package com.example.oauth2demo.auth.application;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RedisTemplate<String, String> redisTemplate;

	private static final String TOKEN_PREFIX = "REFRESH_TOKEN::";

	public void storeRefreshToken(String userId, String refreshToken, long expiresMS) {
		String oldToken = getRefreshToken(userId);
		if (oldToken != null) {
			redisTemplate.delete(TOKEN_PREFIX + oldToken);
		}

		redisTemplate.opsForValue().set(
			TOKEN_PREFIX + userId,
			refreshToken,
			expiresMS,
			TimeUnit.MILLISECONDS
		);

		redisTemplate.opsForValue().set(
			TOKEN_PREFIX + refreshToken,
			userId,
			expiresMS,
			TimeUnit.MILLISECONDS
		);
	}

	public String getUserIdByRefreshToken(String refreshToken) {
		return redisTemplate.opsForValue().get(TOKEN_PREFIX + refreshToken);
	}

	public Long getUserIdByRefreshTokenAsLong(String refreshToken) {
		String userId = getUserIdByRefreshToken(refreshToken);
		return userId != null ? Long.parseLong(userId) : null;
	}

	public String getRefreshToken(String userId) {
		return redisTemplate.opsForValue().get(TOKEN_PREFIX + userId);
	}

	public void deleteRefreshToken(String userId) {
		String token = getRefreshToken(userId);
		if (token != null) {
			redisTemplate.delete(TOKEN_PREFIX + token);
		}
		redisTemplate.delete(TOKEN_PREFIX + userId);
	}

	public void removeRefreshToken(String refreshToken) {
		String userId = getUserIdByRefreshToken(refreshToken);
		if (userId != null) {
			redisTemplate.delete(TOKEN_PREFIX + userId);
		}
		redisTemplate.delete(TOKEN_PREFIX + refreshToken);
	}
}
