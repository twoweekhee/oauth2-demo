package com.example.oauth2demo.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락되었습니다."),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

	// Client
	API_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "API 서버 오류가 발생했습니다."),

	// NationalLibraryOfKorea
	EXTERNAL_BOOK_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "책을 찾을 수 없습니다."),

	// Book
	DUPLICATE_BOOK(HttpStatus.CONFLICT, "이미 존재하는 책입니다."),
	BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "책을 찾을 수 없습니다."),

	// Chapter
	INVALID_CHAPTER_NUMBER(HttpStatus.BAD_REQUEST, "잘못된 챕터 번호입니다."),
	NOT_FOUND_CHAPTER(HttpStatus.NOT_FOUND, "챕터를 찾지 못했습니다."),

	// Auth
	PROPERTY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 프로퍼티를 찾지 못했습니다."),
	PROVIDER_NOT_FOUND(HttpStatus.NOT_FOUND, "oauth 프로바이더를 찾지 못했습니다."),
	UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "로그인되지 않은 사용자입니다."),

	// Account
	SOCIAL_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "Social account를 찾을 수 없습니다."),

	// Book Registration
	DUPLICATE_BOOK_REGISTRATION(HttpStatus.CONFLICT, "이미 존재하는 책 등록 요청입니다."),

	// Terms
	TERMS_NOT_FOUND(HttpStatus.NOT_FOUND, "약관이 존재하지 않습니다."),

	// TermsVersion
	TERMS_VERSION_NOT_FOUND(HttpStatus.NOT_FOUND, "약관의 버전이 존재하지 않습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
