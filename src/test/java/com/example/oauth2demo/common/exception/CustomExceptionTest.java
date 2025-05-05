package com.example.oauth2demo.common.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomExceptionTest {

	@Test
	@DisplayName("컨트롤러 예외 생성 및 메시지 검증")
	void testControllerException() {
		ControllerException ex = new ControllerException(ErrorCode.INVALID_REQUEST, "Test controller error");
		assertEquals(ErrorCode.INVALID_REQUEST, ex.getErrorCode());
		assertEquals("Test controller error", ex.getMessage());
	}

	@Test
	@DisplayName("서비스 예외 생성 및 메시지 검증")
	void testServiceException() {
		ServiceException ex = new ServiceException(ErrorCode.RESOURCE_NOT_FOUND, "Test service error");
		assertEquals(ErrorCode.RESOURCE_NOT_FOUND, ex.getErrorCode());
		assertEquals("Test service error", ex.getMessage());
	}

	@Test
	@DisplayName("레포지토리 예외 생성 및 메시지 검증")
	void testRepositoryException() {
		RepositoryException ex = new RepositoryException(ErrorCode.INTERNAL_SERVER_ERROR, "Test repository error");
		assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, ex.getErrorCode());
		assertEquals("Test repository error", ex.getMessage());
	}
}
