package com.example.oauth2demo.user.dto;

import java.util.List;

import com.example.oauth2demo.terms.dto.UserTermsConsentRequest;

public record CreateUserRequest(
	List<UserTermsConsentRequest> termsConsentRequestList,
	String nickname
) {
}
