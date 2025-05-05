package com.example.oauth2demo.terms.dto;

import com.example.oauth2demo.terms.domain.Terms;
import com.example.oauth2demo.terms.domain.TermsVersion;

public record TermsResponse(
	Long termsVersionId,
	String code,
	String title,
	String content
) {

	public static TermsResponse from(Terms terms, TermsVersion termsVersion) {
		return new TermsResponse(termsVersion.getId(), terms.getCode(), terms.getTitle(), termsVersion.getContent());
	}
}
