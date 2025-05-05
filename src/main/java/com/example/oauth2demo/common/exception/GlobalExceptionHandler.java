package com.example.oauth2demo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.oauth2demo.common.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ControllerException.class)
	public ResponseEntity<ErrorResponse> handleControllerException(ControllerException ex) {
		return buildErrorResponse(ex, ex.getErrorCode(), "ControllerException");
	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ErrorResponse> handleServiceException(ServiceException ex) {
		return buildErrorResponse(ex, ex.getErrorCode(), "ServiceException");
	}

	@ExceptionHandler(RepositoryException.class)
	public ResponseEntity<ErrorResponse> handleRepositoryException(RepositoryException ex) {
		return buildErrorResponse(ex, ex.getErrorCode(), "RepositoryException");
	}

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
		return buildErrorResponse(ex, ex.getErrorCode(), "DomainException");
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
		return buildErrorResponse(ex, ex.getErrorCode(), "FeignException");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		log.error("[Unexpected Exception] {} - {}", ex, ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, ErrorCode errorCode, String exceptionType) {
		log.error("[{}] {} - {}", exceptionType, errorCode, ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(errorCode, ex.getMessage());
		return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
	}

}
