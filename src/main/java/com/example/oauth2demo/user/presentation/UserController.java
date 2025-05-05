package com.example.oauth2demo.user.presentation;

import static com.example.oauth2demo.common.dto.ApiResponse.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2demo.auth.annotaion.CurrentSocialAccountId;
import com.example.oauth2demo.common.dto.ApiResponse;
import com.example.oauth2demo.user.application.UserRegistrationService;
import com.example.oauth2demo.user.dto.CreateUserRequest;
import com.example.oauth2demo.user.dto.CreateUserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserRegistrationService userRegistrationService;

	@PostMapping("/private/users/signup")
	public ApiResponse<CreateUserResponse> createUser(@CurrentSocialAccountId Long socialAccountId, @RequestBody CreateUserRequest createUserRequest) {
		return successResponse(userRegistrationService.createUser(socialAccountId, createUserRequest));
	}
}
