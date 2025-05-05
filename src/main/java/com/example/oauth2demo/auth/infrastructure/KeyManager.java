package com.example.oauth2demo.auth.infrastructure;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "spring.security.oauth2.keys")
public class KeyManager {

	private final SecretKey accessTokenKey;
	private final SecretKey refreshTokenKey;

	@ConstructorBinding
	public KeyManager(String accessTokenKeyString, String refreshTokenKeyString) {
		this.accessTokenKey = Keys.hmacShaKeyFor(accessTokenKeyString.getBytes(StandardCharsets.UTF_8));
		this.refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenKeyString.getBytes(StandardCharsets.UTF_8));
	}
}
