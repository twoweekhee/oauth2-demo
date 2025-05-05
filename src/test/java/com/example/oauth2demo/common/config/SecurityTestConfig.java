package com.example.oauth2demo.common.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.oauth2demo.auth.application.LoginAuthFilter;
import com.example.oauth2demo.auth.application.LogoutAuthFilter;

@TestConfiguration
public class SecurityTestConfig {

	@Bean
	public LoginAuthFilter loginAuthFilter() {
		return Mockito.mock(LoginAuthFilter.class);
	}

	@Bean
	public LogoutAuthFilter logoutAuthFilter() {
		return Mockito.mock(LogoutAuthFilter.class);
	}
}
