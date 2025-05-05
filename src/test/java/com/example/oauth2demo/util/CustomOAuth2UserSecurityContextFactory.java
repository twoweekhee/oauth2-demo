package com.example.oauth2demo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.example.oauth2demo.auth.dto.CustomOAuth2User;

public class CustomOAuth2UserSecurityContextFactory implements WithSecurityContextFactory<WithCustomOAuth2User> {

	@Override
	public SecurityContext createSecurityContext(WithCustomOAuth2User annotation) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		Map<String, Object> attributes = new HashMap<>();
		attributes.put("id", annotation.id());
		attributes.put("email", annotation.email());

		List<GrantedAuthority> authorities = List.of(
			new SimpleGrantedAuthority("ROLE_USER")
		);

		CustomOAuth2User principal = new CustomOAuth2User(
			annotation.id(),
			annotation.email(),
			attributes,
			authorities
		);

		OAuth2AuthenticationToken auth = new OAuth2AuthenticationToken(
			principal,
			authorities,
			"google"
		);

		context.setAuthentication(auth);
		return context;
	}
}