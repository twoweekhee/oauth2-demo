package com.example.oauth2demo.terms.application;

import static com.example.oauth2demo.common.exception.ErrorCode.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.oauth2demo.common.exception.ServiceException;
import com.example.oauth2demo.terms.domain.TermsVersion;
import com.example.oauth2demo.terms.infrastructure.TermsVersionJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermsVersionService {

	private final TermsVersionJpaRepository termsVersionJpaRepository;

	@Transactional(readOnly = true)
	public TermsVersion getLatestTermsVersion(Long termsId, LocalDateTime inquiryDate) {
		return termsVersionJpaRepository.findFirstByTermsIdAndEffectiveDateLessThanEqualOrderByVersionDesc(termsId, inquiryDate)
			.orElseThrow(() -> new ServiceException(TERMS_VERSION_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public TermsVersion findById(Long id) {
		return termsVersionJpaRepository.findById(id).orElseThrow(() -> new ServiceException(TERMS_VERSION_NOT_FOUND));
	}
}
