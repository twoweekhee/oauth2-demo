package com.example.oauth2demo.terms.application;

import static com.example.oauth2demo.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.oauth2demo.common.exception.ServiceException;
import com.example.oauth2demo.terms.domain.Terms;
import com.example.oauth2demo.terms.infrastructure.TermsJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TermsService {

	private final TermsJpaRepository termsJpaRepository;

	@Transactional(readOnly = true)
	public List<Terms> findAll() {
		List<Terms> termsList = termsJpaRepository.findAll();

		if (termsList.isEmpty()) {
			throw new ServiceException(TERMS_NOT_FOUND);
		}

		return termsList;
	}

	@Transactional(readOnly = true)
	public Terms findByCode(String code) {
		return termsJpaRepository.findByCode(code).orElseThrow(() -> new ServiceException(TERMS_NOT_FOUND));
	}
}
