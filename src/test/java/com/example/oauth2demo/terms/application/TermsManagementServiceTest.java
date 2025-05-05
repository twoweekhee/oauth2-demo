package com.example.oauth2demo.terms.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.oauth2demo.terms.domain.Terms;
import com.example.oauth2demo.terms.domain.TermsVersion;
import com.example.oauth2demo.terms.domain.UserTermsConsent;
import com.example.oauth2demo.terms.dto.TermsResponse;
import com.example.oauth2demo.terms.dto.UserTermsConsentRequest;
import com.example.oauth2demo.user.domain.User;
import com.example.oauth2demo.user.dto.CreateUserRequest;
import com.example.oauth2demo.util.TermsTestUtils;

@ExtendWith(MockitoExtension.class)
class TermsManagementServiceTest {

	@Mock
	private TermsService termsService;

	@Mock
	private TermsVersionService termsVersionService;

	@Mock
	private UserTermsConsentService userTermsConsentService;

	@InjectMocks
	private TermsManagementService termsManagementService;

	private User user;
	private UserTermsConsentRequest termsConsentRequest;
	private CreateUserRequest createUserRequest;

	@BeforeEach
	void setUp() {
		user = User.builder()
			.id(1L)
			.nickname("지적인 도마뱀").build();

		termsConsentRequest = new UserTermsConsentRequest(
			3L,
			"MARKETING",
			true
		);

		createUserRequest = new CreateUserRequest(
			List.of(termsConsentRequest),
			"지적인 도마뱀"
		);
	}

	@Test
	@DisplayName("최신 약관 동의 목록 조회")
	void testGetLatestTermsConsentList() {
		List<Terms> termsList = TermsTestUtils.createAllTermsList();

		TermsVersion serviceTermVersion = TermsTestUtils.createServiceTermsVersion();
		TermsVersion privacyTermVersion = TermsTestUtils.createPrivacyTermsVersion();
		TermsVersion marketingTermVersion = TermsTestUtils.createMarketingTermsVersion();

		when(termsService.findAll()).thenReturn(termsList);
		when(termsVersionService.getLatestTermsVersion(eq(1L), any(LocalDateTime.class))).thenReturn(serviceTermVersion);
		when(termsVersionService.getLatestTermsVersion(eq(2L), any(LocalDateTime.class))).thenReturn(privacyTermVersion);
		when(termsVersionService.getLatestTermsVersion(eq(3L), any(LocalDateTime.class))).thenReturn(marketingTermVersion);

		List<TermsResponse> result = termsManagementService.getLatestTermsConsentList();

		assertThat(result).hasSize(3);

		assertThat(result.get(0).title()).isEqualTo(termsList.get(0).getTitle());
		assertThat(result.get(0).code()).isEqualTo(termsList.get(0).getCode());
		assertThat(result.get(0).content()).isEqualTo(serviceTermVersion.getContent());

		assertThat(result.get(1).title()).isEqualTo(termsList.get(1).getTitle());
		assertThat(result.get(1).code()).isEqualTo(termsList.get(1).getCode());
		assertThat(result.get(1).content()).isEqualTo(privacyTermVersion.getContent());

		assertThat(result.get(2).title()).isEqualTo(termsList.get(2).getTitle());
		assertThat(result.get(2).code()).isEqualTo(termsList.get(2).getCode());
		assertThat(result.get(2).content()).isEqualTo(marketingTermVersion.getContent());

		verify(termsService, times(1)).findAll();
		verify(termsVersionService, times(1)).getLatestTermsVersion(eq(1L), any(LocalDateTime.class));
		verify(termsVersionService, times(1)).getLatestTermsVersion(eq(2L), any(LocalDateTime.class));
		verify(termsVersionService, times(1)).getLatestTermsVersion(eq(3L), any(LocalDateTime.class));
	}


	@Test
	void createUserTermsConsent_ShouldSaveAllTermsConsents() {

		Terms terms = TermsTestUtils.createMarketingTerms();
		TermsVersion marketingTermsVersion = TermsTestUtils.createMarketingTermsVersion();

		when(termsService.findByCode("MARKETING")).thenReturn(terms);
		when(termsVersionService.findById(3L)).thenReturn(marketingTermsVersion);

		termsManagementService.createUserTermsConsent(user, createUserRequest);

		verify(termsService).findByCode("MARKETING");
		verify(termsVersionService).findById(3L);
		verify(userTermsConsentService).save(any(UserTermsConsent.class));
	}
}