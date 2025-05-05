package com.example.oauth2demo.common.exception;

public class ControllerException extends ApplicationException {
	public ControllerException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ControllerException(ErrorCode errorCode, String customMessage) {
		super(errorCode, customMessage);
	}
}
