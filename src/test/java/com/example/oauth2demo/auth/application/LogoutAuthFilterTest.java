package com.example.oauth2demo.auth.application;

import com.example.oauth2demo.common.exception.ServiceException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutAuthFilterTest {

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private CookieProvider cookieProvider;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private LogoutAuthFilter logoutAuthFilter;

    @Spy
    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = spy(new MockHttpServletRequest());
        response = new MockHttpServletResponse();

        Cookie[] cookies = new Cookie[] { new Cookie("access_token", "test-token"), new Cookie("refresh_token", "test-token") };
        lenient().when(request.getCookies()).thenReturn(cookies);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        OAuth2AuthenticationToken mockAuth = mock(OAuth2AuthenticationToken.class);
        securityContext.setAuthentication(mockAuth);
        SecurityContextHolder.setContext(securityContext);

        Cookie deletedAccessCookie = new Cookie("access_token", "");
        deletedAccessCookie.setMaxAge(0);

        Cookie deletedRefreshCookie = new Cookie("refresh_token", "");
        deletedRefreshCookie.setMaxAge(0);

        lenient().when(cookieProvider.generateDeletedAccessTokenCookie()).thenReturn(deletedAccessCookie);
        lenient().when(cookieProvider.generateDeletedRefreshTokenCookie()).thenReturn(deletedRefreshCookie);
    }

    @Test
    @DisplayName("로그아웃 url 가 아닌 경우")
    void shouldPassThroughNonLogoutRequests() throws ServletException, IOException {
        request.setServletPath("/api/some-other-endpoint");
        request.setMethod("GET");

        logoutAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(refreshTokenService);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("로그아웃 url 가 맞는 경우")
    void shouldProcessLogoutRequestAndClearContext() throws ServletException, IOException {
        request.setServletPath("/api/public/logout");
        request.setMethod("GET");

        logoutAuthFilter.doFilterInternal(request, response, filterChain);

        verify(cookieProvider, times(1)).generateDeletedAccessTokenCookie();
        verify(cookieProvider, times(1)).generateDeletedRefreshTokenCookie();
        verifyNoInteractions(filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        assertEquals(2, response.getCookies().length);
    }

    @Test
    @DisplayName("리프레쉬 토큰 삭제 성공")
    void shouldRemoveRefreshTokenWhenPresent() throws ServletException, IOException {
        request.setServletPath("/api/public/logout");
        request.setMethod("GET");

        logoutAuthFilter.doFilterInternal(request, response, filterChain);

        verify(refreshTokenService).removeRefreshToken("test-token");
        verify(cookieProvider).generateDeletedAccessTokenCookie();
        verify(cookieProvider).generateDeletedRefreshTokenCookie();
    }

    @Test
    @DisplayName("쿠키 없는 사용자 에러")
    void shouldHandleEmptyRefreshToken() {
        MockHttpServletRequest testRequest = new MockHttpServletRequest();
        testRequest.setServletPath("/api/public/logout");
        testRequest.setMethod("GET");

        Exception exception = assertThrows(ServiceException.class, () -> logoutAuthFilter.doFilterInternal(testRequest, response, filterChain));

        assertEquals("로그인되지 않은 사용자입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("로그아웃 후 리다이렉트 성공")
    void shouldRedirectAfterLogout() throws ServletException, IOException {
        request.setServletPath("/api/public/logout");
        request.setMethod("GET");

        String redirectUri = "/landing-page";
        request.getSession().setAttribute("redirect_uri", redirectUri);

        logoutAuthFilter.doFilterInternal(request, response, filterChain);

        assertEquals(redirectUri, response.getRedirectedUrl());
    }
}