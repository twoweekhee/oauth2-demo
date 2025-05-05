package com.example.oauth2demo.auth.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.oauth2demo.auth.dto.AuthTokens;
import com.example.oauth2demo.auth.dto.CustomOAuth2User;
import com.example.oauth2demo.auth.infrastructure.KeyManager;

import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

	@Mock
	RefreshTokenService refreshTokenService;

	@Mock
	KeyManager keyManager;

	@InjectMocks
	private TokenProvider tokenProvider;

	@BeforeEach
	void setUp() {
		String sampleKey = "12345678901234567890123456789012";
		SecretKey key = Keys.hmacShaKeyFor(sampleKey.getBytes(StandardCharsets.UTF_8));

		lenient().when(keyManager.getAccessTokenKey()).thenReturn(key);
		lenient().when(keyManager.getRefreshTokenKey()).thenReturn(key);
	}

	@Test
	@DisplayName("토큰Dto 생성하기")
	void testGenerateTokens() {
		CustomOAuth2User user = mock(CustomOAuth2User.class);
		when(user.getName()).thenReturn("readup");
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
		doReturn(authorities).when(user).getAuthorities();

		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(user);

		AuthTokens authTokens = tokenProvider.generateTokens(authentication);

		assertNotNull(authTokens);
		assertNotNull(authTokens.accessToken());
		assertNotNull(authTokens.refreshToken());

		verify(refreshTokenService, times(1)).storeRefreshToken("readup", authTokens.refreshToken(), tokenProvider.REFRESH_EXPIRY_MS);
	}

	@Test
	@DisplayName("액세스 토큰 검증하기 - 성공")
	void testValidateAccessToken_success() {
		CustomOAuth2User user = mock(CustomOAuth2User.class);
		when(user.getName()).thenReturn("readup");
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
		doReturn(authorities).when(user).getAuthorities();

		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(user);

		AuthTokens authTokens = tokenProvider.generateTokens(authentication);
		String accessToken = authTokens.accessToken();

		boolean isValid = tokenProvider.validateAccessToken(accessToken);
		assertTrue(isValid);
	}

	@Test
	@DisplayName("액세스 토큰 검증하기 - 실패")
	void testValidateAccessToken_failure() {
		boolean isValid = tokenProvider.validateAccessToken("invalidToken");
		assertFalse(isValid);
	}

	@Test
	@DisplayName("리프레쉬 토큰 검증하기 - 성공")
	void testValidateRefreshToken_success() {
		CustomOAuth2User user = mock(CustomOAuth2User.class);
		when(user.getName()).thenReturn("readup");
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority);
		doReturn(authorities).when(user).getAuthorities();

		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(user);

		AuthTokens authTokens = tokenProvider.generateTokens(authentication);
		String refreshToken = authTokens.refreshToken();

		boolean isValid = tokenProvider.validateRefreshToken(refreshToken);
		assertTrue(isValid);
	}

	@Test
	@DisplayName("리프레쉬 토큰 검증하기 - 실패")
	void testValidateRefreshToken_failure() {
		boolean isValid = tokenProvider.validateRefreshToken("invalidToken");
		assertFalse(isValid);
	}
}