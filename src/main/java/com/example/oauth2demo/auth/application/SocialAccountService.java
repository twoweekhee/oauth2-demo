package com.example.oauth2demo.auth.application;

import static com.example.oauth2demo.common.exception.ErrorCode.*;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.oauth2demo.auth.domain.SocialAccount;
import com.example.oauth2demo.auth.dto.CreateSocialAccountRequest;
import com.example.oauth2demo.auth.dto.CustomOAuth2User;
import com.example.oauth2demo.auth.infrastructure.SocialAccountJpaRepository;
import com.example.oauth2demo.common.exception.ServiceException;
import com.example.oauth2demo.user.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SocialAccountService {

	private final SocialAccountJpaRepository socialAccountJpaRepository;

	public SocialAccount save(CreateSocialAccountRequest createSocialAccountRequest) {
		return socialAccountJpaRepository.save(toEntity(createSocialAccountRequest));
	}

	public SocialAccount findById(Long id) {
		return socialAccountJpaRepository.findById(id).orElseThrow(() -> new ServiceException(SOCIAL_ACCOUNT_NOT_FOUND));
	}

	public void updateUser(Long socialAccountId, User user) {
		SocialAccount savedSocialAccount = findById(socialAccountId);
		savedSocialAccount.updateUserFromSocialAccount(user);
	}

	public boolean isNewUser(Authentication authentication) {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		return findById(Long.parseLong(oAuth2User.getName())).getUser() == null;
	}

	private SocialAccount toEntity(CreateSocialAccountRequest createSocialAccountRequest) {
		return SocialAccount.builder()
			.email(createSocialAccountRequest.email())
			.provider(createSocialAccountRequest.provider())
			.providerUid(createSocialAccountRequest.providerUid())
			.build();
	}
}
