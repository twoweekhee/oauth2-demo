package com.example.oauth2demo.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2demo.common.dto.ApiResponse;
import com.example.oauth2demo.common.exception.ControllerException;
import com.example.oauth2demo.common.exception.ErrorCode;
import com.example.oauth2demo.common.exception.RepositoryException;
import com.example.oauth2demo.common.exception.ServiceException;

@RestController
public class TestCommonController {

	@GetMapping("/test/controller")
	public void throwControllerException() {
		throw new ControllerException(ErrorCode.INVALID_REQUEST, "Controller exception occurred");
	}

	@GetMapping("/test/service")
	public void throwServiceException() {
		throw new ServiceException(ErrorCode.RESOURCE_NOT_FOUND, "Service exception occurred");
	}

	@GetMapping("/test/repository")
	public void throwRepositoryException() {
		throw new RepositoryException(ErrorCode.INTERNAL_SERVER_ERROR, "Repository exception occurred");
	}

	@GetMapping("/test/success")
	public ApiResponse<String> getSuccessResponse() {
		return ApiResponse.successResponse("Success Response Data", "Successful response.");
	}
}
