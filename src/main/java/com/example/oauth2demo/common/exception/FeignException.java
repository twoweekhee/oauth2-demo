package com.example.oauth2demo.common.exception;

public class FeignException extends ApplicationException {
	public FeignException(ErrorCode errorCode) {
		super(errorCode);
	}

	public FeignException(ErrorCode errorCode, String customMessage) {
		super(errorCode, customMessage);
	}
}
