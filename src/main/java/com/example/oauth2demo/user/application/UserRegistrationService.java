package com.example.oauth2demo.user.application;

import org.springframework.stereotype.Service;

import com.example.oauth2demo.auth.application.SocialAccountService;
import com.example.oauth2demo.auth.domain.SocialAccount;
import com.example.oauth2demo.terms.application.TermsManagementService;
import com.example.oauth2demo.user.domain.User;
import com.example.oauth2demo.user.dto.CreateUserRequest;
import com.example.oauth2demo.user.dto.CreateUserResponse;
import com.example.oauth2demo.user.dto.CurrentUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

	private final UserService userService;
	private final SocialAccountService socialAccountService;
	private final TermsManagementService termsManagementService;

	public CurrentUser getUserFromSocialAccount(Long socialAccountId) {
		SocialAccount socialAccount = socialAccountService.findById(socialAccountId);
		return CurrentUser.from(socialAccount.getUser());
	}

	public CreateUserResponse createUser(Long socialAccountId, CreateUserRequest createUserRequest) {
		User user = userService.save(createUserRequest.nickname());
		socialAccountService.updateUser(socialAccountId, user);
		termsManagementService.createUserTermsConsent(user, createUserRequest);

		return CreateUserResponse.from(user);
	}
}
