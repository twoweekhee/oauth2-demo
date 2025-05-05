package com.example.oauth2demo.auth.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "#authentication.principal instanceof T(com.example.oauth2demo.auth.dto.CustomOAuth2User) ? #authentication.principal.getName() : null")
public @interface CurrentSocialAccountId {
}
