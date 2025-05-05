package com.example.oauth2demo.auth.application;

import static com.example.oauth2demo.auth.application.TokenProvider.*;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.oauth2demo.auth.dto.AuthTokens;
import com.example.oauth2demo.auth.infrastructure.RedirectUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final TokenProvider tokenProvider;
	private final CookieProvider cookieProvider;
	private final SocialAccountService socialAccountService;
	private static final String TERMS_REDIRECT = "/terms";

	@Override
	public void onAuthenticationSuccess(
		@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Authentication authentication) throws
		IOException {

		SecurityContextHolder.getContext().setAuthentication(authentication);

		AuthTokens authTokens = tokenProvider.generateTokens(authentication);

		Cookie accessCookie =
			cookieProvider.generateAccessTokenCookie(authTokens.accessToken(), ACCESS_EXPIRY_MS);

		Cookie refreshCookie =
			cookieProvider.generateRefreshTokenCookie(authTokens.refreshToken(), REFRESH_EXPIRY_MS);

		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);

		clearAuthenticationAttributes(request);

		if (socialAccountService.isNewUser(authentication)) {
			getRedirectStrategy().sendRedirect(request, response, TERMS_REDIRECT);
		} else {
			getRedirectStrategy().sendRedirect(request, response, RedirectUtils.getRedirectUri(request));
		}
	}
}
