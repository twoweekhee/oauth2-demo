package com.example.oauth2demo.auth.dto;

import lombok.Builder;

@Builder
public record AuthTokens(
	String accessToken,
	String refreshToken
) {
	public static AuthTokens of(String accessToken, String refreshToken) {
		return new AuthTokens(accessToken, refreshToken);
	}
}
