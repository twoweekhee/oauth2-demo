package com.example.oauth2demo.user.dto;

import com.example.oauth2demo.user.domain.User;

public record CreateUserResponse(
	Long id,
	String nickname
) {
	public static CreateUserResponse from(User user) {
		return new CreateUserResponse(user.getId(), user.getNickname());
	}
}
