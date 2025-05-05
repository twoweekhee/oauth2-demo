package com.example.oauth2demo.auth.dto;

public record CreateSocialAccountRequest(
	String email,
	String provider,
	String providerUid
) {
	public static CreateSocialAccountRequest of(String email, String provider, String providerUid) {
		return new CreateSocialAccountRequest(
			email,
			provider,
			providerUid
		);
	}
}
