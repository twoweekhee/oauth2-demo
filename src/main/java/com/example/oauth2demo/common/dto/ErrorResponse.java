package com.example.oauth2demo.common.dto;

import com.example.oauth2demo.common.exception.ErrorCode;

public record ErrorResponse(boolean success, int status, String error, String message) {
	public ErrorResponse(ErrorCode errorCode) {
		this(false, errorCode.getHttpStatus().value(), errorCode.name(), errorCode.getMessage());
	}

	public ErrorResponse(ErrorCode errorCode, String customMessage) {
		this(false, errorCode.getHttpStatus().value(), errorCode.name(), customMessage);
	}
}
