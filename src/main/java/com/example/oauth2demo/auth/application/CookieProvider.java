package com.example.oauth2demo.auth.application;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;

@Service
public class CookieProvider {

	private static final String ACCESS_TOKEN_NAME = "access_token";
	private static final String REFRESH_TOKEN_NAME = "refresh_token";

	public Cookie generateAccessTokenCookie(String token, int expiresMS) {
		return createCookie(ACCESS_TOKEN_NAME, token, expiresMS);
	}

	public Cookie generateRefreshTokenCookie(String token, int expiresMS) {
		return createCookie(REFRESH_TOKEN_NAME, token, expiresMS);
	}

	public Cookie generateDeletedAccessTokenCookie() {

		return createCookie(ACCESS_TOKEN_NAME, "", 0);
	}

	public Cookie generateDeletedRefreshTokenCookie() {

		return createCookie(REFRESH_TOKEN_NAME, "", 0);
	}

	private Cookie createCookie(String name, String token, int expiresMS) {
		Cookie cookie = new Cookie(name, token);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(expiresMS / 1000);

		return cookie;
	}
}
