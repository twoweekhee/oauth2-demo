package com.example.oauth2demo.common.exception;

public class DomainException extends ApplicationException {
	public DomainException(ErrorCode errorCode) {
		super(errorCode);
	}

	public DomainException(ErrorCode errorCode, String customMessage) {
		super(errorCode, customMessage);
	}
}
