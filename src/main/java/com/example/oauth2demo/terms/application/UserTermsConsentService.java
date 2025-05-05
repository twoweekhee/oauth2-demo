package com.example.oauth2demo.terms.application;

import org.springframework.stereotype.Service;

import com.example.oauth2demo.terms.domain.UserTermsConsent;
import com.example.oauth2demo.terms.infrastructure.UserTermsConsentJpaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTermsConsentService {

	private final UserTermsConsentJpaRepository userTermsConsentJpaRepository;

	@Transactional
	public UserTermsConsent save(UserTermsConsent userTermsConsent) {
		return userTermsConsentJpaRepository.save(userTermsConsent);
	}
}
