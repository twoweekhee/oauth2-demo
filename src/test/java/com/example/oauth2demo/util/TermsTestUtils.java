package com.example.oauth2demo.util;

import java.util.Arrays;
import java.util.List;

import com.example.oauth2demo.terms.domain.Terms;
import com.example.oauth2demo.terms.domain.TermsVersion;
import com.example.oauth2demo.terms.dto.UserTermsConsentRequest;

public class TermsTestUtils {

	public static Terms createServiceTerms() {
		return Terms.builder()
			.id(1L)
			.code("SERVICE")
			.title("서비스 이용약관")
			.build();
	}

	public static Terms createPrivacyTerms() {
		return Terms.builder()
			.id(2L)
			.code("PRIVACY")
			.title("개인정보 처리방침")
			.build();
	}

	public static Terms createMarketingTerms() {
		return Terms.builder()
			.id(3L)
			.code("MARKETING")
			.title("마케팅 정보 수신동의")
			.build();
	}

	public static List<Terms> createAllTermsList() {
		return Arrays.asList(
			createServiceTerms(),
			createPrivacyTerms(),
			createMarketingTerms()
		);
	}

	public static TermsVersion createServiceTermsVersion() {
		return TermsVersion.builder()
			.id(1L)
			.terms(createServiceTerms())
			.version("1.0")
			.content("서비스 이용약관 내용")
			.build();
	}
	public static TermsVersion createPrivacyTermsVersion() {
		return TermsVersion.builder()
			.id(2L)
			.terms(createPrivacyTerms())
			.version("2.1")
			.content("개인정보 처리방침 내용")
			.build();
	}

	public static TermsVersion createMarketingTermsVersion() {
		return TermsVersion.builder()
			.id(3L)
			.terms(createMarketingTerms())
			.version("1.5")
			.content("마케팅 정보 수신동의 내용")
			.build();

	}

	public static UserTermsConsentRequest createUserServiceTermsConsentRequest() {
		return new UserTermsConsentRequest(1L, "SERVICE", true);
	}

	public static UserTermsConsentRequest createUserMarketingTermsConsentRequest() {
		return new UserTermsConsentRequest(3L, "MARKETING", true);
	}

	public static List<UserTermsConsentRequest> createUserTermsConsentList() {
		return Arrays.asList(createUserServiceTermsConsentRequest(), createUserMarketingTermsConsentRequest());
	}
}
