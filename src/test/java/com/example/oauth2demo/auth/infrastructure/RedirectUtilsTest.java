package com.example.oauth2demo.auth.infrastructure;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RedirectUtilsTest {

    private static final String BASE_URI = "/";
    private static final String REDIRECT_URI_ATTRIBUTE = "redirect_uri";
    private static final String TEST_REDIRECT_URI = "/login/success";

    @Test
    @DisplayName("세션이 null일때 /반환")
    public void testGetRedirectUri_WithNullSession_ShouldReturnBaseUri() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        when(mockRequest.getSession(false)).thenReturn(null);

        String result = RedirectUtils.getRedirectUri(mockRequest);

        assertEquals(BASE_URI, result);
    }

    @Test
    @DisplayName("속성이 없을 때 /반환")
    public void testGetRedirectUri_WithSessionButNoAttribute_ShouldReturnBaseUri() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute(REDIRECT_URI_ATTRIBUTE)).thenReturn(null);

        String result = RedirectUtils.getRedirectUri(mockRequest);

        assertEquals(BASE_URI, result);
    }

    @Test
    @DisplayName("세션, 속성이 있으면 리다이렉션 uri 반환")
    public void testGetRedirectUri_WithSessionAndAttribute_ShouldReturnAttributeValue() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpSession mockSession = Mockito.mock(HttpSession.class);

        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockSession.getAttribute(REDIRECT_URI_ATTRIBUTE)).thenReturn(TEST_REDIRECT_URI);

        String result = RedirectUtils.getRedirectUri(mockRequest);

        assertEquals(TEST_REDIRECT_URI, result);
    }
}