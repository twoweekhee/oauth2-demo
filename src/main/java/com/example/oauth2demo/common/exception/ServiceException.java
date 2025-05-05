package com.example.oauth2demo.common.exception;

public class ServiceException extends ApplicationException {
	public ServiceException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ServiceException(ErrorCode errorCode, String customMessage) {
		super(errorCode, customMessage);
	}
}
