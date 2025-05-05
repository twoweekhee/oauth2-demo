package com.example.oauth2demo.auth.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class RedirectUtils {

	private static final String BASE_URI = "/";
	private static final String REDIRECT_URI_ATTRIBUTE = "redirect_uri";

	private RedirectUtils() {}

	public static String getRedirectUri(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute(REDIRECT_URI_ATTRIBUTE) != null) {
			return (String) session.getAttribute(REDIRECT_URI_ATTRIBUTE);
		}
		return BASE_URI;
	}
}
