package com.example.oauth2demo.auth.infrastructure;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class KeyManagerTest {

    private static final String TEST_ACCESS_TOKEN_KEY = "testAccessTokenKey123456789012345678901234567890";
    private static final String TEST_REFRESH_TOKEN_KEY = "testRefreshTokenKey123456789012345678901234567890";

    @Test
    @DisplayName("키 매니저 초기화")
    void testKeyManagerInitialization() {
        KeyManager keyManager = new KeyManager(TEST_ACCESS_TOKEN_KEY, TEST_REFRESH_TOKEN_KEY);

        assertNotNull(keyManager.getAccessTokenKey());
        assertNotNull(keyManager.getRefreshTokenKey());
    }

    @Test
    @DisplayName("액세스 토큰 키 가져오기")
    void testAccessTokenKeyIsCorrect() {var expectedKey = Keys.hmacShaKeyFor(TEST_ACCESS_TOKEN_KEY.getBytes(StandardCharsets.UTF_8));
        KeyManager keyManager = new KeyManager(TEST_ACCESS_TOKEN_KEY, TEST_REFRESH_TOKEN_KEY);

        assertEquals(expectedKey.getAlgorithm(), keyManager.getAccessTokenKey().getAlgorithm());
        assertArrayEquals(expectedKey.getEncoded(), keyManager.getAccessTokenKey().getEncoded());
    }

    @Test
    @DisplayName("리프레쉬 토큰 키 가져오기")
    void testRefreshTokenKeyIsCorrect() {var expectedKey = Keys.hmacShaKeyFor(TEST_REFRESH_TOKEN_KEY.getBytes(StandardCharsets.UTF_8));
        KeyManager keyManager = new KeyManager(TEST_ACCESS_TOKEN_KEY, TEST_REFRESH_TOKEN_KEY);

        assertEquals(expectedKey.getAlgorithm(), keyManager.getRefreshTokenKey().getAlgorithm());
        assertArrayEquals(expectedKey.getEncoded(), keyManager.getRefreshTokenKey().getEncoded());
    }
}