package com.example.oauth2demo.auth.application;

import com.example.oauth2demo.auth.domain.SocialAccount;
import com.example.oauth2demo.auth.dto.AuthTokens;
import com.example.oauth2demo.auth.dto.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.example.oauth2demo.auth.dto.UserRole.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginAuthFilterTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private CookieProvider cookieProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private SocialAccountService socialAccountService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private LoginAuthFilter loginAuthFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private SocialAccount mockSocialAccount;
    private CustomOAuth2User mockOAuth2User;

    protected static final int ACCESS_EXPIRY_MS = 30 * 60 * 1000;
    protected static final int REFRESH_EXPIRY_MS = 2 * 24 * 60 * 60 * 1000;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();

        mockSocialAccount = SocialAccount.builder().id(1L).provider("google").build();
        mockOAuth2User = mock(CustomOAuth2User.class);
        lenient().when(mockOAuth2User.authorities()).thenReturn(List.of(new SimpleGrantedAuthority(ROLE_USER.name())));
    }

    @Test
    @DisplayName("특정 url 에서 스킵하기")
    void shouldSkipFilterForExcludedPaths() throws ServletException, IOException {
        request.setRequestURI("/api/docs/swagger");

        loginAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(tokenProvider);
    }

    @Test
    @DisplayName("액세스 토큰이 유효 시 로그인")
    void shouldProcessValidAccessToken() throws ServletException, IOException {
        String accessToken = "valid.access.token";
        Cookie accessTokenCookie = new Cookie("access_token", accessToken);
        request.setCookies(accessTokenCookie);

        when(tokenProvider.validateAccessToken(accessToken)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(accessToken)).thenReturn(1L);
        when(socialAccountService.findById(1L)).thenReturn(mockSocialAccount);

        loginAuthFilter.doFilterInternal(request, response, filterChain);

        verify(tokenProvider).validateAccessToken(accessToken);
        verify(tokenProvider).getUserIdFromToken(accessToken);
        verify(socialAccountService).findById(1L);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertInstanceOf(OAuth2AuthenticationToken.class, SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("액세스 토큰 만료, 리프레쉬 토큰 유표 시 로그인")
    void shouldRefreshTokenWhenAccessTokenIsInvalidButRefreshTokenIsValid() throws ServletException, IOException {
        String invalidAccessToken = "invalid.access.token";
        String validRefreshToken = "valid.refresh.token";
        Cookie accessTokenCookie = new Cookie("access_token", invalidAccessToken);
        Cookie refreshTokenCookie = new Cookie("refresh_token", validRefreshToken);
        request.setCookies(accessTokenCookie, refreshTokenCookie);

        when(tokenProvider.validateAccessToken(invalidAccessToken)).thenReturn(false);
        when(tokenProvider.validateRefreshToken(validRefreshToken)).thenReturn(true);
        when(refreshTokenService.getUserIdByRefreshToken(validRefreshToken)).thenReturn("1");
        when(socialAccountService.findById(1L)).thenReturn(mockSocialAccount);

        AuthTokens newTokens = AuthTokens.of("new.access.token", "new.refresh.token");
        when(tokenProvider.generateTokens(any(OAuth2AuthenticationToken.class))).thenReturn(newTokens);

        Cookie newAccessCookie = new Cookie("access_token", "new.access.token");
        Cookie newRefreshCookie = new Cookie("refresh_token", "new.refresh.token");
        when(cookieProvider.generateAccessTokenCookie(eq("new.access.token"), anyInt())).thenReturn(newAccessCookie);
        when(cookieProvider.generateRefreshTokenCookie(eq("new.refresh.token"),  anyInt())).thenReturn(newRefreshCookie);


        loginAuthFilter.doFilterInternal(request, response, filterChain);

        verify(tokenProvider).validateAccessToken(invalidAccessToken);
        verify(tokenProvider).validateRefreshToken(validRefreshToken);
        verify(refreshTokenService).getUserIdByRefreshToken(validRefreshToken);
        verify(tokenProvider).generateTokens(any(OAuth2AuthenticationToken.class));
        verify(cookieProvider).generateAccessTokenCookie("new.access.token", ACCESS_EXPIRY_MS);
        verify(cookieProvider).generateRefreshTokenCookie("new.refresh.token", REFRESH_EXPIRY_MS);

        assertEquals(2, response.getCookies().length);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("토큰 없을 시")
    void shouldContinueFilterChainWhenNoTokensPresent() throws ServletException, IOException {

        loginAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("유효 토큰 없을 시")
    void shouldContinueFilterChainWhenBothTokensAreInvalid() throws ServletException, IOException {
        String invalidAccessToken = "invalid.access.token";
        String invalidRefreshToken = "invalid.refresh.token";
        Cookie accessTokenCookie = new Cookie("access_token", invalidAccessToken);
        Cookie refreshTokenCookie = new Cookie("refresh_token", invalidRefreshToken);
        request.setCookies(accessTokenCookie, refreshTokenCookie);

        when(tokenProvider.validateAccessToken(invalidAccessToken)).thenReturn(false);
        when(tokenProvider.validateRefreshToken(invalidRefreshToken)).thenReturn(false);

        loginAuthFilter.doFilterInternal(request, response, filterChain);

        verify(tokenProvider).validateAccessToken(invalidAccessToken);
        verify(tokenProvider).validateRefreshToken(invalidRefreshToken);
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}