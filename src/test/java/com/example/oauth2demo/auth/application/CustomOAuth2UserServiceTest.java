package com.example.oauth2demo.auth.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.oauth2demo.auth.domain.SocialAccount;
import com.example.oauth2demo.auth.dto.CreateSocialAccountRequest;
import com.example.oauth2demo.auth.dto.CustomOAuth2User;
import com.example.oauth2demo.auth.dto.OAuth2UserInfo;
import com.example.oauth2demo.auth.infrastructure.OAuth2UserInfoFactory;
import com.example.oauth2demo.util.ClientRegistrationTestUtils;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

	@Mock
	private SocialAccountService socialAccountService;
	@Mock
	private OAuth2UserInfoFactory oAuth2UserInfoFactory;

	@Spy
	@InjectMocks
	private CustomOAuth2UserService customOAuth2UserService;

	@Test
	@DisplayName("CustomOAuth2User 반환")
	void test_loadUser_success() throws OAuth2AuthenticationException {
		//given
		OAuth2UserRequest oAuth2UserRequest = mock(OAuth2UserRequest.class);

		when(oAuth2UserRequest.getClientRegistration()).thenReturn(ClientRegistrationTestUtils.createGoogleClientRegistration());

		OAuth2User fakeOAuth2User = mock(OAuth2User.class);
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("id", "readup");
		attributes.put("email", "readup@readup.com");

		when(fakeOAuth2User.getAttributes()).thenReturn(attributes);
		doReturn(fakeOAuth2User).when(customOAuth2UserService).loadUserFromSuper(any(OAuth2UserRequest.class));

		OAuth2UserInfo fakeOAuth2UserInfo = mock(OAuth2UserInfo.class);

		when(fakeOAuth2UserInfo.getEmail()).thenReturn("readup@readup.com");
		when(fakeOAuth2UserInfo.getId()).thenReturn("readup");
		when(oAuth2UserInfoFactory.getOAuth2UserInfo("google", attributes)).thenReturn(fakeOAuth2UserInfo);

		SocialAccount fakeSocialAccount = SocialAccount.builder()
			.id(1L)
			.email("readup@readup.com")
			.provider("google")
			.build();
		when(socialAccountService.save(any(CreateSocialAccountRequest.class))).thenReturn(fakeSocialAccount);

		//when
		OAuth2User result = customOAuth2UserService.loadUser(oAuth2UserRequest);

		//then
		assertTrue(result instanceof CustomOAuth2User, "CustomOAuth2User 가 반환");

		CustomOAuth2User customOAuth2User = (CustomOAuth2User)result;

		assertEquals("1", customOAuth2User.getName());
		assertEquals(attributes, customOAuth2User.getAttributes());

		verify(socialAccountService).save(any(CreateSocialAccountRequest.class));
	}
}