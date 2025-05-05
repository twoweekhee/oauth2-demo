package com.example.oauth2demo.terms.infrastructure;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oauth2demo.terms.domain.TermsVersion;

public interface TermsVersionJpaRepository extends JpaRepository<TermsVersion, Long> {

	Optional<TermsVersion> findFirstByTermsIdAndEffectiveDateLessThanEqualOrderByVersionDesc(Long termsId, LocalDateTime inquiryDate);
}
