package com.example.oauth2demo.terms.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.oauth2demo.terms.domain.Terms;
import com.example.oauth2demo.terms.domain.TermsVersion;
import com.example.oauth2demo.terms.domain.UserTermsConsent;
import com.example.oauth2demo.terms.dto.TermsResponse;
import com.example.oauth2demo.terms.dto.UserTermsConsentRequest;
import com.example.oauth2demo.user.domain.User;
import com.example.oauth2demo.user.dto.CreateUserRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermsManagementService {

	private final TermsService termsService;
	private final TermsVersionService termsVersionService;
	private final UserTermsConsentService userTermsConsentService;

	@Transactional(readOnly = true)
	public List<TermsResponse> getLatestTermsConsentList() {
		return termsService.findAll().stream().map(terms -> {
			TermsVersion termsVersion = termsVersionService.getLatestTermsVersion(terms.getId(), LocalDateTime.now());
			return TermsResponse.from(terms, termsVersion);
		}).toList();
	}

	@Transactional
	public void createUserTermsConsent(User user, CreateUserRequest createUserRequest) {
		createUserRequest.termsConsentRequestList().forEach(termsConsentRequest -> {
			userTermsConsentService.save(toEntity(user, termsConsentRequest));
		});
	}

	private UserTermsConsent toEntity(User user, UserTermsConsentRequest userTermsConsentRequest) {
		Terms terms = termsService.findByCode(userTermsConsentRequest.code());
		TermsVersion termsVersion = termsVersionService.findById(userTermsConsentRequest.termsVersionId());
		return UserTermsConsent.builder()
			.user(user)
			.isConsent(userTermsConsentRequest.isConsent())
			.termsVersion(termsVersion)
			.terms(terms)
			.build();
	}
}
