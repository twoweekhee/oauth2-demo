package com.example.oauth2demo.auth.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.oauth2demo.auth.domain.SocialAccount;

@Repository
public interface SocialAccountJpaRepository extends JpaRepository<SocialAccount, Long> {
}
