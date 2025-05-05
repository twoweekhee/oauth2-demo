package com.example.oauth2demo.auth.infrastructure;

import static com.example.oauth2demo.common.exception.ErrorCode.*;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.oauth2demo.auth.dto.GoogleOAuth2UserInfo;
import com.example.oauth2demo.auth.dto.KakaoOAuth2UserInfo;
import com.example.oauth2demo.auth.dto.NaverOAuth2UserInfo;
import com.example.oauth2demo.auth.dto.OAuth2UserInfo;
import com.example.oauth2demo.common.exception.ServiceException;

@Component
public class OAuth2UserInfoFactory {

	public OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {

		switch (registrationId) {
			case "google":
				return new GoogleOAuth2UserInfo(attributes);
			case "naver":
				return new NaverOAuth2UserInfo(attributes);
			case "kakao":
				return new KakaoOAuth2UserInfo(attributes);
		}
		throw new ServiceException(PROVIDER_NOT_FOUND, "Unknown registration id : " + registrationId);
	}
}
