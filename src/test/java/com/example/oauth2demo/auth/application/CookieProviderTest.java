package com.example.oauth2demo.auth.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.Cookie;

class CookieProviderTest {

	private final CookieProvider cookieProvider = new CookieProvider();

	@Test
	@DisplayName("액세스 토큰 쿠키 생성하기")
	void testGenerateAccessTokenCookie() {
		String accessToken = "sampleAccessToken";
		int expiresMS = 30 * 60 * 1000;

		Cookie accessTokenCookie = cookieProvider.generateAccessTokenCookie(accessToken, expiresMS);

		assertEquals("access_token", accessTokenCookie.getName());
		assertEquals(accessToken, accessTokenCookie.getValue());
		assertEquals("/", accessTokenCookie.getPath());
		assertTrue(accessTokenCookie.isHttpOnly());
		assertEquals(expiresMS / 1000, accessTokenCookie.getMaxAge());
	}

	@Test
	@DisplayName("리프레쉬 토큰 쿠키 생성하기")
	void testGenerateRefreshTokenCookie() {
		String refreshToken = "sampleRefreshToken";
		int expiresMS = 2 * 24 * 60 * 60 * 1000;

		Cookie refreshTokenCookie = cookieProvider.generateRefreshTokenCookie(refreshToken, expiresMS);

		assertEquals("refresh_token", refreshTokenCookie.getName());
		assertEquals(refreshToken, refreshTokenCookie.getValue());
		assertEquals("/", refreshTokenCookie.getPath());
		assertTrue(refreshTokenCookie.isHttpOnly());
		assertEquals(expiresMS / 1000, refreshTokenCookie.getMaxAge());
	}

	@Test
	@DisplayName("액세스 삭제 쿠키 생성하기")
	void testGenerateDeletedAccessTokenCookie() {
		Cookie refreshTokenCookie = cookieProvider.generateDeletedAccessTokenCookie();

		assertEquals("access_token", refreshTokenCookie.getName());
		assertEquals("", refreshTokenCookie.getValue());
		assertEquals("/", refreshTokenCookie.getPath());
		assertTrue(refreshTokenCookie.isHttpOnly());
		assertEquals(0, refreshTokenCookie.getMaxAge());
	}

	@Test
	@DisplayName("리프레쉬 삭제 쿠키 생성하기")
	void testGenerateDeletedRefreshTokenCookie() {
		Cookie refreshTokenCookie = cookieProvider.generateDeletedRefreshTokenCookie();

		assertEquals("refresh_token", refreshTokenCookie.getName());
		assertEquals("", refreshTokenCookie.getValue());
		assertEquals("/", refreshTokenCookie.getPath());
		assertTrue(refreshTokenCookie.isHttpOnly());
		assertEquals(0, refreshTokenCookie.getMaxAge());
	}
}