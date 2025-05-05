package com.example.oauth2demo.user.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oauth2demo.user.domain.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
