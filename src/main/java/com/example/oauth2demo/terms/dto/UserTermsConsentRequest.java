package com.example.oauth2demo.terms.dto;

public record UserTermsConsentRequest(
	Long termsVersionId,
	String code,
	boolean isConsent
) {
}
