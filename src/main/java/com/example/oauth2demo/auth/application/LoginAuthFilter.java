package com.example.oauth2demo.auth.application;

import static com.example.oauth2demo.auth.application.TokenProvider.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.oauth2demo.auth.domain.SocialAccount;
import com.example.oauth2demo.auth.dto.AuthTokens;
import com.example.oauth2demo.auth.dto.CustomOAuth2User;
import com.example.oauth2demo.auth.infrastructure.RedirectUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginAuthFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProvider;
	private final CookieProvider cookieProvider;
	private final RefreshTokenService refreshTokenService;
	private final SocialAccountService socialAccountService;

	private static final Set<String> VALID_COOKIE_NAMES = Set.of("access_token", "refresh_token");
	private static final Set<String> EXCLUDED_PATHS = Set.of(
		"/api/docs",
		"/api/swagger-ui/",
		"/api/api-docs/",
		"/api/springdoc/",
		"/api/public"
	);

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		if (isSkipUri(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		AuthTokens tokens = extractTokenFromCookies(request.getCookies());
		String accessToken = tokens.accessToken();
		String refreshToken = tokens.refreshToken();
		boolean accessTokenIsValid = false;
		Long userId = null;

		if (StringUtils.hasText(accessToken)) {
			accessTokenIsValid = tokenProvider.validateAccessToken(accessToken);
			if (accessTokenIsValid) {
				userId = tokenProvider.getUserIdFromToken(accessToken);
			}
		}

		if (!accessTokenIsValid && StringUtils.hasText(refreshToken) && tokenProvider.validateRefreshToken(refreshToken)) {
			String tokenUserId = refreshTokenService.getUserIdByRefreshToken(refreshToken);

			if (tokenUserId != null) {
				userId = Long.parseLong(tokenUserId);

				OAuth2AuthenticationToken authentication = createAuthentication(userId);
				AuthTokens newTokens = tokenProvider.generateTokens(authentication);
				addTokenCookies(response, newTokens);

				accessTokenIsValid = true;
			}
		}

		if (accessTokenIsValid) {
			OAuth2AuthenticationToken authentication = createAuthentication(userId);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			response.sendRedirect(RedirectUtils.getRedirectUri(request));
		}

		filterChain.doFilter(request, response);
	}

	private void addTokenCookies(HttpServletResponse response, AuthTokens tokens) {
		Cookie accessCookie = cookieProvider.generateAccessTokenCookie(tokens.accessToken(), ACCESS_EXPIRY_MS);
		Cookie refreshCookie = cookieProvider.generateRefreshTokenCookie(tokens.refreshToken(), REFRESH_EXPIRY_MS);
		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);
	}


	private OAuth2AuthenticationToken createAuthentication(Long userId) {
		SocialAccount socialAccount = socialAccountService.findById(userId);
		CustomOAuth2User user = CustomOAuth2User.from(socialAccount, Map.of());

		return new OAuth2AuthenticationToken(
			user, user.authorities(), socialAccount.getProvider());
	}

	private AuthTokens extractTokenFromCookies(Cookie[] cookies) {

		if (cookies == null || cookies.length == 0) return AuthTokens.builder().build();

		Map<String, String> map = Arrays.stream(cookies)
			.filter(cookie -> VALID_COOKIE_NAMES.contains(cookie.getName()))
			.collect(Collectors.toMap(Cookie::getName, Cookie::getValue));

		return AuthTokens.of(map.get("access_token"), map.get("refresh_token"));
	}

	private boolean isSkipUri(HttpServletRequest request) {
		String path = request.getRequestURI();
		return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
	}
}
