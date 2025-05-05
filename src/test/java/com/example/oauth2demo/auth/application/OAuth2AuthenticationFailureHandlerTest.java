package com.example.oauth2demo.auth.application;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class OAuth2AuthenticationFailureHandlerTest {

	@Mock
	private RedirectStrategy redirectStrategy;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@InjectMocks
	private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

	@BeforeEach
	void setUp() {
		oAuth2AuthenticationFailureHandler.setRedirectStrategy(redirectStrategy);
	}

	@Test
	@DisplayName("인증 실패시 에러 페이지로 리다이렉트")
	void test_onAuthenticationFailure_redirects_to_error_page() throws IOException {
		AuthenticationException exception = mock(AuthenticationException.class);
		when(exception.getMessage()).thenReturn("Authentication failed");

		oAuth2AuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);

		verify(redirectStrategy).sendRedirect(request, response, "/login?error=true");
	}
}