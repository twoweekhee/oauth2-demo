package com.example.oauth2demo.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomOAuth2UserSecurityContextFactory.class)
public @interface WithCustomOAuth2User {
	long id() default 1L;
	String email() default "test@example.com";
}
