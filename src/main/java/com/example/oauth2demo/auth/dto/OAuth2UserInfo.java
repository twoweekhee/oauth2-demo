package com.example.oauth2demo.auth.dto;

import java.util.Map;

public interface OAuth2UserInfo {
	Map<String, Object> attributes();

	String getId();

	String getName();

	String getEmail();
}
