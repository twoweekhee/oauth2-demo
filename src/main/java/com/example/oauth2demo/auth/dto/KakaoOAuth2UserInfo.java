package com.example.oauth2demo.auth.dto;

import static com.example.oauth2demo.common.exception.ErrorCode.*;

import java.util.Map;

import com.example.oauth2demo.common.exception.ServiceException;

public record KakaoOAuth2UserInfo(
	Map<String, Object> attributes
) implements OAuth2UserInfo {

	@Override
	public String getId() {
		return attributes.get("id").toString();
	}

	@Override
	public String getName() {

		return (String)getProperties().get("nickname");
	}

	@Override
	public String getEmail() {
		Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
		if (kakaoAccount == null) {
			return null;
		}
		return (String)kakaoAccount.get("email");
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getProperties() {
		Object properties = attributes.get("properties");
		if (properties instanceof Map) {
			return (Map<String, Object>)properties;
		}
		throw new ServiceException(PROPERTY_NOT_FOUND, "KakaoOAuth2UserInfo : No properties found");
	}
}
