package com.example.oauth2demo.auth.application;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.oauth2demo.auth.domain.SocialAccount;
import com.example.oauth2demo.auth.dto.CreateSocialAccountRequest;
import com.example.oauth2demo.auth.dto.CustomOAuth2User;
import com.example.oauth2demo.auth.dto.OAuth2UserInfo;
import com.example.oauth2demo.auth.infrastructure.OAuth2UserInfoFactory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final SocialAccountService socialAccountService;
	private final OAuth2UserInfoFactory oAuth2UserInfoFactory;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = loadUserFromSuper(oAuth2UserRequest);

		return createOAuth2User(oAuth2UserRequest, oAuth2User);
	}

	protected OAuth2User loadUserFromSuper(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		return super.loadUser(oAuth2UserRequest);
	}

	private OAuth2User createOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
		String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
		OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo(registrationId,
			oAuth2User.getAttributes());

		SocialAccount socialAccount = socialAccountService.save(
			CreateSocialAccountRequest.of(oAuth2UserInfo.getEmail(), registrationId, oAuth2UserInfo.getId()));

		return CustomOAuth2User.from(socialAccount, oAuth2User.getAttributes());
	}
}
