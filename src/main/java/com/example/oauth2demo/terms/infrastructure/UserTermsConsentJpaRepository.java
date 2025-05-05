package com.example.oauth2demo.terms.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oauth2demo.terms.domain.UserTermsConsent;

public interface UserTermsConsentJpaRepository extends JpaRepository<UserTermsConsent, Long> {
}
