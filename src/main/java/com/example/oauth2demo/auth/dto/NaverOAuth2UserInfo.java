package com.example.oauth2demo.auth.dto;

import static com.example.oauth2demo.common.exception.ErrorCode.*;

import java.util.Map;

import com.example.oauth2demo.common.exception.ServiceException;

public record NaverOAuth2UserInfo(
	Map<String, Object> attributes
) implements OAuth2UserInfo {

	@Override
	public String getId() {

		return (String)getResponse().get("id");
	}

	@Override
	public String getName() {

		return (String)getResponse().get("name");
	}

	@Override
	public String getEmail() {

		return (String)getResponse().get("email");
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getResponse() {
		Object response = attributes.get("response");
		if (response instanceof Map) {
			return (Map<String, Object>)response;
		}
		throw new ServiceException(PROPERTY_NOT_FOUND, "NaverOAuth2UserInfo : No response found");
	}
}
