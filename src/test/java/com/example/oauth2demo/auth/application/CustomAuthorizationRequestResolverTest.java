package com.example.oauth2demo.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import com.example.oauth2demo.util.ClientRegistrationTestUtils;

class CustomAuthorizationRequestResolverTest {

	private CustomAuthorizationRequestResolver resolver;

	@BeforeEach
	void setup() {
		ClientRegistrationRepository clientRegistrationRepository = mock(ClientRegistrationRepository.class);
		ClientRegistration googleClientRegistration = ClientRegistrationTestUtils.createGoogleClientRegistration();
		ClientRegistration kakaoClientRegistration = ClientRegistrationTestUtils.createKakaoClientRegistration();
		ClientRegistration naverClientRegistration = ClientRegistrationTestUtils.createNaverClientRegistration();

		when(clientRegistrationRepository.findByRegistrationId("google")).thenReturn(googleClientRegistration);
		when(clientRegistrationRepository.findByRegistrationId("kakao")).thenReturn(kakaoClientRegistration);
		when(clientRegistrationRepository.findByRegistrationId("naver")).thenReturn(naverClientRegistration);

		this.resolver = new CustomAuthorizationRequestResolver(clientRegistrationRepository);
	}

	@ParameterizedTest
	@ValueSource(strings = {"google", "kakao", "naver"})
	@DisplayName("요청 리졸버 정상 작동")
	void test_resolve_google_success(String provider) throws OAuth2AuthenticationException {
		//given
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/api/public/oauth2/" + provider);
		request.setServletPath("/api/public/oauth2/" + provider);
		request.setParameter("redirect", "http://frontend.com/after-login");

		System.out.println("URI: " + request.getRequestURI());
		System.out.println("contextPath: " + request.getContextPath());
		System.out.println("full path: " + request.getRequestURL());

		//when
		OAuth2AuthorizationRequest result = resolver.resolve(request);

		//then
		assertThat(result).isNotNull();
		assertEquals("http://frontend.com/after-login",
			request.getSession().getAttribute("redirect"));
	}

	@ParameterizedTest
	@ValueSource(strings = {"google", "kakao", "naver"})
	@DisplayName("요청 리졸버(+registrationId) 정상 작동")
	void test_resolve_with_registrationId_google_success(String provider) throws OAuth2AuthenticationException {
		//given
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("redirect", "http://frontend.com/after-login");

		//when
		OAuth2AuthorizationRequest result = resolver.resolve(request, provider);

		//then
		assertThat(result).isNotNull();
		assertEquals("http://frontend.com/after-login",
			request.getSession().getAttribute("redirect"));
	}
}