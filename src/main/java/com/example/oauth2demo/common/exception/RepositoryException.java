package com.example.oauth2demo.common.exception;

public class RepositoryException extends ApplicationException {
	public RepositoryException(ErrorCode errorCode) {
		super(errorCode);
	}

	public RepositoryException(ErrorCode errorCode, String customMessage) {
		super(errorCode, customMessage);
	}
}
