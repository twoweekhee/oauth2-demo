package com.example.oauth2demo.auth.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ValueOperations<String, String> valueOperations;

	@InjectMocks
	private RefreshTokenService refreshTokenService;

	private static final String TOKEN_PREFIX = "REFRESH_TOKEN::";
	private static final String USER_ID = "1";

	@BeforeEach
	void setUp() {
		lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
	}

	@Test
	@DisplayName("리프레쉬 토큰 저장하기 - 기존 토큰이 없는 경우")
	void testStoreRefreshTokenWithoutExistingToken() {
		String refreshToken = "sampleRefreshToken";
		long expiresMS = 2 * 24 * 60 * 60 * 1000;
		String userKey = TOKEN_PREFIX + USER_ID;
		String tokenKey = TOKEN_PREFIX + refreshToken;

		when(valueOperations.get(userKey)).thenReturn(null);

		refreshTokenService.storeRefreshToken(USER_ID, refreshToken, expiresMS);

		verify(valueOperations).get(userKey);
		verify(redisTemplate, never()).delete(anyString());
		verify(valueOperations).set(userKey, refreshToken, expiresMS, TimeUnit.MILLISECONDS);
		verify(valueOperations).set(tokenKey, USER_ID, expiresMS, TimeUnit.MILLISECONDS);
	}

	@Test
	@DisplayName("리프레쉬 토큰 저장하기 - 기존 토큰이 있는 경우")
	void testStoreRefreshTokenWithExistingToken() {
		String oldRefreshToken = "oldRefreshToken";
		String newRefreshToken = "newRefreshToken";
		long expiresMS = 2 * 24 * 60 * 60 * 1000;
		String userKey = TOKEN_PREFIX + USER_ID;
		String oldTokenKey = TOKEN_PREFIX + oldRefreshToken;
		String newTokenKey = TOKEN_PREFIX + newRefreshToken;

		when(valueOperations.get(userKey)).thenReturn(oldRefreshToken);

		refreshTokenService.storeRefreshToken(USER_ID, newRefreshToken, expiresMS);

		verify(valueOperations).get(userKey);
		verify(redisTemplate).delete(oldTokenKey);
		verify(valueOperations).set(userKey, newRefreshToken, expiresMS, TimeUnit.MILLISECONDS);
		verify(valueOperations).set(newTokenKey, USER_ID, expiresMS, TimeUnit.MILLISECONDS);
	}

	@Test
	@DisplayName("리프레쉬 토큰으로 유저 ID 조회하기")
	void testGetUSER_IDByRefreshToken() {
		String refreshToken = "sampleRefreshToken";
		String tokenKey = TOKEN_PREFIX + refreshToken;

		when(valueOperations.get(tokenKey)).thenReturn(USER_ID);

		String result = refreshTokenService.getUserIdByRefreshToken(refreshToken);

		assertEquals(USER_ID, result);
		verify(valueOperations).get(tokenKey);
	}

	@Test
	@DisplayName("리프레쉬 토큰으로 유저 ID Long 으로 조회하기")
	void testGetUSER_IDByRefreshTokenAsLong() {
		String refreshToken = "sampleRefreshToken";
		Long expectedId = 1L;
		String tokenKey = TOKEN_PREFIX + refreshToken;

		when(valueOperations.get(tokenKey)).thenReturn(USER_ID);

		Long result = refreshTokenService.getUserIdByRefreshTokenAsLong(refreshToken);

		assertEquals(expectedId, result);
		verify(valueOperations).get(tokenKey);
	}

	@Test
	@DisplayName("리프레쉬 토큰으로 유저 ID Long 으로 조회 시 null 인 경우")
	void testGetUSER_IDByRefreshTokenAsLongWithNullUSER_ID() {
		String refreshToken = "sampleRefreshToken";
		String tokenKey = TOKEN_PREFIX + refreshToken;

		when(valueOperations.get(tokenKey)).thenReturn(null);

		Long result = refreshTokenService.getUserIdByRefreshTokenAsLong(refreshToken);

		assertNull(result);
		verify(valueOperations).get(tokenKey);
	}

	@Test
	@DisplayName("리프레쉬 토큰 조회하기")
	void testGetRefreshToken() {
		String expectedKey = TOKEN_PREFIX + USER_ID;
		String expectedToken = "sampleRefreshToken";

		when(valueOperations.get(expectedKey)).thenReturn(expectedToken);

		String actualToken = refreshTokenService.getRefreshToken(USER_ID);

		assertEquals(expectedToken, actualToken);
		verify(valueOperations).get(expectedKey);
	}

	@Test
	@DisplayName("리프레쉬 토큰 삭제하기 - 토큰이 있는 경우")
	void testDeleteRefreshTokenWithExistingToken() {
		String refreshToken = "sampleRefreshToken";
		String userKey = TOKEN_PREFIX + USER_ID;
		String tokenKey = TOKEN_PREFIX + refreshToken;

		when(valueOperations.get(userKey)).thenReturn(refreshToken);

		refreshTokenService.deleteRefreshToken(USER_ID);

		verify(valueOperations).get(userKey);
		verify(redisTemplate).delete(tokenKey);
		verify(redisTemplate).delete(userKey);
	}

	@Test
	@DisplayName("리프레쉬 토큰 삭제하기 - 토큰이 없는 경우")
	void testDeleteRefreshTokenWithoutExistingToken() {
		String userKey = TOKEN_PREFIX + USER_ID;

		when(valueOperations.get(userKey)).thenReturn(null);

		refreshTokenService.deleteRefreshToken(USER_ID);

		verify(valueOperations).get(userKey);
		verify(redisTemplate, times(1)).delete(TOKEN_PREFIX + anyString());
		verify(redisTemplate).delete(userKey);
	}

	@Test
	@DisplayName("리프레쉬 토큰으로 삭제하기 - 유저 ID가 있는 경우")
	void testRemoveRefreshTokenWithExistingUSER_ID() {
		String refreshToken = "sampleRefreshToken";
		String tokenKey = TOKEN_PREFIX + refreshToken;
		String userKey = TOKEN_PREFIX + USER_ID;

		when(valueOperations.get(tokenKey)).thenReturn(USER_ID);

		refreshTokenService.removeRefreshToken(refreshToken);

		verify(valueOperations).get(tokenKey);
		verify(redisTemplate).delete(userKey);
		verify(redisTemplate).delete(tokenKey);
	}

	@Test
	@DisplayName("리프레쉬 토큰으로 삭제하기 - 유저 ID가 없는 경우")
	void testRemoveRefreshTokenWithoutExistingUSER_ID() {
		String refreshToken = "sampleRefreshToken";
		String tokenKey = TOKEN_PREFIX + refreshToken;

		when(valueOperations.get(tokenKey)).thenReturn(null);

		refreshTokenService.removeRefreshToken(refreshToken);

		verify(valueOperations).get(tokenKey);
		verify(redisTemplate, times(1)).delete(TOKEN_PREFIX + anyString());
		verify(redisTemplate).delete(tokenKey);
	}
}