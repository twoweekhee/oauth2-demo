package com.example.oauth2demo.util;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

public class ClientRegistrationTestUtils {

	public static ClientRegistration createGoogleClientRegistration() {

		return ClientRegistration.withRegistrationId("google")
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
			.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
			.tokenUri("https://oauth2.googleapis.com/token")
			.userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.userNameAttributeName("sub")
			.clientName("google")
			.build();
	}

	public static ClientRegistration createKakaoClientRegistration() {

		return ClientRegistration.withRegistrationId("kakao")
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
			.authorizationUri("https://kauth.kakao.com/oauth/authorize")
			.tokenUri("https://kauth.kakao.com/oauth/token")
			.userInfoUri("https://kapi.kakao.com/v2/user/me")
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.userNameAttributeName("id")
			.clientName("Kakao")
			.build();
	}

	public static ClientRegistration createNaverClientRegistration() {

		return ClientRegistration.withRegistrationId("naver")
			.clientId("clientId")
			.clientSecret("clientSecret")
			.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
			.authorizationUri("https://nid.naver.com/oauth2.0/authorize")
			.tokenUri("https://nid.naver.com/oauth2.0/token")
			.userInfoUri("https://openapi.naver.com/v1/nid/me")
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.userNameAttributeName("response")
			.clientName("Naver")
			.build();
	}
}
