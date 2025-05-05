package com.example.oauth2demo.auth.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.oauth2demo.auth.dto.GoogleOAuth2UserInfo;
import com.example.oauth2demo.auth.dto.KakaoOAuth2UserInfo;
import com.example.oauth2demo.auth.dto.NaverOAuth2UserInfo;
import com.example.oauth2demo.auth.dto.OAuth2UserInfo;
import com.example.oauth2demo.common.exception.ServiceException;

class OAuth2UserInfoFactoryTest {

	private final OAuth2UserInfoFactory oAuth2UserInfoFactory = new OAuth2UserInfoFactory();

	@Test
	@DisplayName("구글 oauth2 유저 인포 반환하기")
	void test_google_oAuth2UserInfo_provider_success() {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("sub", "readup");
		attributes.put("email", "readup@readup.com");

		OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo("google", attributes);
		assertInstanceOf(GoogleOAuth2UserInfo.class, oAuth2UserInfo, "반환된 userInfo가 GoogleOAuth2UserInfo 여야 함.");
	}

	@Test
	@DisplayName("네이버 oauth2 유저 인포 반환하기")
	void test_naver_oAuth2UserInfo_provider_success() {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("response", new HashMap<String, Object>() {{
			put("id", "readup");
			put("email", "readup@readup.com");
		}});

		OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo("naver", attributes);
		assertInstanceOf(NaverOAuth2UserInfo.class, oAuth2UserInfo, "반환된 userInfo가 NaverOAuth2UserInfo 여야 함.");
	}

	@Test
	@DisplayName("카카오 oauth2 유저 인포 반환하기")
	void test_kakao_oAuth2UserInfo_provider_success() {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("id", "readup");
		attributes.put("kakao_account", "readup@readup.com");

		OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo("kakao", attributes);
		assertInstanceOf(KakaoOAuth2UserInfo.class, oAuth2UserInfo, "반환된 userInfo가 KakaoOAuth2UserInfo 여야 함.");
	}

	@Test
	@DisplayName("provider를 찾지 못하면 에러")
	void test_unknown_oAuth2UserInfo_provider_success() {
		Map<String, Object> attributes = new HashMap<>();

		Exception exception = assertThrows(ServiceException.class, () -> {
			oAuth2UserInfoFactory.getOAuth2UserInfo("unknown", attributes);
		});
		assertTrue(exception.getMessage().contains("Unknown registration id : unknown"));
	}
}