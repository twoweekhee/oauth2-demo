package com.example.oauth2demo.auth.dto;

import java.util.Map;

public record GoogleOAuth2UserInfo(
	Map<String, Object> attributes
) implements OAuth2UserInfo {

	@Override
	public String getId() {
		return (String)attributes.get("sub");
	}

	@Override
	public String getName() {
		return (String)attributes.get("name");
	}

	@Override
	public String getEmail() {
		return (String)attributes.get("email");
	}
}

