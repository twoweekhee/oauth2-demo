package com.example.oauth2demo.user.application;

import org.springframework.stereotype.Service;

import com.example.oauth2demo.user.domain.User;
import com.example.oauth2demo.user.infrastructure.UserJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserJpaRepository userJpaRepository;

	public User save(String nickname) {
		return userJpaRepository.save(User.builder().nickname(nickname).build());
	}
}
