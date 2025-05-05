package com.example.oauth2demo.auth.application;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.example.oauth2demo.auth.dto.AuthTokens;
import com.example.oauth2demo.auth.dto.CustomOAuth2User;
import com.example.oauth2demo.auth.infrastructure.KeyManager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenProvider {

	private final KeyManager keyManager;
	private final RefreshTokenService refreshTokenService;

	protected static final int ACCESS_EXPIRY_MS = 30 * 60 * 1000;
	protected static final int REFRESH_EXPIRY_MS = 2 * 24 * 60 * 60 * 1000;
	private static final String ISSUER = "read-up";

	public AuthTokens generateTokens(Authentication authentication) {

		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		String accessToken = generateAccessToken(oAuth2User);
		String refreshToken = generateRefreshToken();

		refreshTokenService.storeRefreshToken(oAuth2User.getName(), refreshToken, REFRESH_EXPIRY_MS);

		return AuthTokens.of(accessToken, refreshToken);
	}

	private String generateAccessToken(CustomOAuth2User oAuth2User) {

		Date now = new Date();
		Date expires = new Date(now.getTime() + ACCESS_EXPIRY_MS);

		Key key = keyManager.getAccessTokenKey();

		List<String> roles = oAuth2User.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.toList();

		return Jwts.builder()
			.subject(oAuth2User.getName())
			.issuer(ISSUER)
			.issuedAt(now)
			.expiration(expires)
			.signWith(key)
			.claim("roles", roles)
			.compact();
	}

	private String generateRefreshToken() {

		Date now = new Date();
		Date expires = new Date(now.getTime() + REFRESH_EXPIRY_MS);

		Key key = keyManager.getRefreshTokenKey();

		return Jwts.builder()
			.expiration(expires)
			.signWith(key)
			.compact();
	}

	public boolean validateAccessToken(String accessToken) {
		try {
			Jwts.parser()
				.verifyWith(keyManager.getAccessTokenKey())
				.build()
				.parseSignedClaims(accessToken);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean validateRefreshToken(String refreshToken) {
		try {
			Jwts.parser()
				.verifyWith(keyManager.getRefreshTokenKey())
				.build()
				.parseSignedClaims(refreshToken);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getUserIdFromToken(String accessToken) {
		Jws<Claims> claimsJws = Jwts.parser()
			.verifyWith(keyManager.getAccessTokenKey())
			.build()
			.parseSignedClaims(accessToken);
		Claims claims = claimsJws.getPayload();
		return Long.parseLong(claims.getSubject());
	}
}
