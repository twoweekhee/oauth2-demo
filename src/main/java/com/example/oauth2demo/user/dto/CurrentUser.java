package com.example.oauth2demo.user.dto;

import com.example.oauth2demo.user.domain.User;

public record CurrentUser(
	Long id,
	String nickname
) {
	public static CurrentUser from(User user) {
		return new CurrentUser(user.getId(), user.getNickname());
	}
}
