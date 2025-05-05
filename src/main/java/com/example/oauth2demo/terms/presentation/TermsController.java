package com.example.oauth2demo.terms.presentation;

import static com.example.oauth2demo.common.dto.ApiResponse.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2demo.common.dto.ApiResponse;
import com.example.oauth2demo.terms.application.TermsManagementService;
import com.example.oauth2demo.terms.dto.TermsResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TermsController {

	private final TermsManagementService termsManagementService;

	@GetMapping("/public/terms")
	public ApiResponse<List<TermsResponse>> getTermsList() {
		return successResponse(termsManagementService.getLatestTermsConsentList());
	}
}
