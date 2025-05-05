package com.example.oauth2demo.auth.application;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.RedirectStrategy;

import com.example.oauth2demo.auth.dto.AuthTokens;
import com.example.oauth2demo.auth.infrastructure.RedirectUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class OAuth2AuthenticationSuccessHandlerTest {

	@Mock
	private RedirectStrategy redirectStrategy;

	@Mock
	private TokenProvider tokenProvider;

	@Mock
	private CookieProvider cookieProvider;

	@Mock
	private HttpSession session;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private Authentication authentication;

	@Mock
	private SocialAccountService socialAccountService;

	@Mock
	private SecurityContext securityContext;

	@InjectMocks
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@BeforeEach
	void setUp() {
		oAuth2AuthenticationSuccessHandler.setRedirectStrategy(redirectStrategy);
	}

	@Test
	@DisplayName("새로운 사용자 인증 성공 시 약관 페이지로 리다이렉트")
	void testOnAuthenticationSuccess_NewUser() throws IOException {
		try (MockedStatic<SecurityContextHolder> securityContextHolderMock = mockStatic(SecurityContextHolder.class);
			 MockedStatic<RedirectUtils> redirectUtilsMock = mockStatic(RedirectUtils.class)) {

			securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);

			AuthTokens authTokens = AuthTokens.of("accessToken", "refreshToken");
			when(tokenProvider.generateTokens(authentication)).thenReturn(authTokens);

			Cookie accessTokenCookie = new Cookie("accessToken", authTokens.accessToken());
			Cookie refreshTokenCookie = new Cookie("refreshToken", authTokens.refreshToken());

			when(cookieProvider.generateAccessTokenCookie(authTokens.accessToken(), TokenProvider.ACCESS_EXPIRY_MS))
				.thenReturn(accessTokenCookie);
			when(cookieProvider.generateRefreshTokenCookie(authTokens.refreshToken(), TokenProvider.REFRESH_EXPIRY_MS))
				.thenReturn(refreshTokenCookie);

			when(socialAccountService.isNewUser(authentication)).thenReturn(true);

			oAuth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

			verify(securityContext).setAuthentication(authentication);
			verify(response).addCookie(accessTokenCookie);
			verify(response).addCookie(refreshTokenCookie);
			verify(redirectStrategy).sendRedirect(request, response, "/terms");
		}
	}

	@Test
	@DisplayName("기존 사용자 인증 성공 시 쿠키와 함께 리다이렉트 - 세션 null")
	void testHandleAuthenticationSuccess_session_null() throws IOException {
		when(request.getSession(false)).thenReturn(null);

		AuthTokens authTokens = AuthTokens.of("accessToken", "refreshToken");
		when(tokenProvider.generateTokens(authentication)).thenReturn(authTokens);

		Cookie accessTokenCookie = new Cookie("accessToken", authTokens.accessToken());
		Cookie refreshTokenCookie = new Cookie("refreshToken", authTokens.refreshToken());

		doReturn(accessTokenCookie).when(cookieProvider).generateAccessTokenCookie(authTokens.accessToken(), TokenProvider.ACCESS_EXPIRY_MS);
		doReturn(refreshTokenCookie).when(cookieProvider).generateRefreshTokenCookie(authTokens.refreshToken(), TokenProvider.REFRESH_EXPIRY_MS);

		oAuth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

		verify(redirectStrategy).sendRedirect(request, response, "/");
	}

	@Test
	@DisplayName("기존 사용자 인증 성공 시 쿠키와 함께 리다이렉트 - URI null")
	void testHandleAuthenticationSuccess_redirect_null() throws IOException {
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("redirect_uri")).thenReturn(null);

		AuthTokens authTokens = AuthTokens.of("accessToken", "refreshToken");
		when(tokenProvider.generateTokens(authentication)).thenReturn(authTokens);

		Cookie accessTokenCookie = new Cookie("accessToken", authTokens.accessToken());
		Cookie refreshTokenCookie = new Cookie("refreshToken", authTokens.refreshToken());

		doReturn(accessTokenCookie).when(cookieProvider).generateAccessTokenCookie(authTokens.accessToken(), TokenProvider.ACCESS_EXPIRY_MS);
		doReturn(refreshTokenCookie).when(cookieProvider).generateRefreshTokenCookie(authTokens.refreshToken(), TokenProvider.REFRESH_EXPIRY_MS);

		oAuth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

		verify(redirectStrategy).sendRedirect(request, response, "/");
	}

	@Test
	@DisplayName("기존 사용자 인증 성공 시 쿠키와 함께 리다이렉트 URI not null")
	void testHandleAuthenticationSuccess_redirect_notNull() throws IOException {
		String redirectUri = "/success";

		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute("redirect_uri")).thenReturn(redirectUri);

		AuthTokens authTokens = AuthTokens.of("accessToken", "refreshToken");
		when(tokenProvider.generateTokens(authentication)).thenReturn(authTokens);

		Cookie accessTokenCookie = new Cookie("accessToken", authTokens.accessToken());
		Cookie refreshTokenCookie = new Cookie("refreshToken", authTokens.refreshToken());

		doReturn(accessTokenCookie).when(cookieProvider).generateAccessTokenCookie(authTokens.accessToken(), TokenProvider.ACCESS_EXPIRY_MS);
		doReturn(refreshTokenCookie).when(cookieProvider).generateRefreshTokenCookie(authTokens.refreshToken(), TokenProvider.REFRESH_EXPIRY_MS);

		oAuth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
		verify(redirectStrategy).sendRedirect(request, response, "/success");
		verify(response).addCookie(accessTokenCookie);
		verify(response).addCookie(refreshTokenCookie);
	}
}