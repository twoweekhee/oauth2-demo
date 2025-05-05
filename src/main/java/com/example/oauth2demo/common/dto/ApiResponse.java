package com.example.oauth2demo.common.dto;

public record ApiResponse<T>(boolean success, T data, String message) {

	public static final String DEFAULT_SUCCESS_MESSAGE = "Request succeeded.";

	public static <T> ApiResponse<T> successResponse(T data, String message) {
		return new ApiResponse<>(true, data, message);
	}

	public static <T> ApiResponse<T> successResponse(T data) {
		return new ApiResponse<>(true, data, DEFAULT_SUCCESS_MESSAGE);
	}

	public static <T> ApiResponse<T> successResponse() {
		return new ApiResponse<>(true, null, DEFAULT_SUCCESS_MESSAGE);
	}

	public static <T> ApiResponse<T> failureResponse(String message) {
		return new ApiResponse<>(false, null, message);
	}
}
