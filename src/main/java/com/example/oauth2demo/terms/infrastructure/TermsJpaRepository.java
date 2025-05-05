package com.example.oauth2demo.terms.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oauth2demo.terms.domain.Terms;

public interface TermsJpaRepository extends JpaRepository<Terms, Long> {
	Optional<Terms> findByCode(String code);
}
