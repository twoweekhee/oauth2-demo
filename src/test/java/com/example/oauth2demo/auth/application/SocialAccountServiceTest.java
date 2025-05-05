package com.example.oauth2demo.auth.application;

import static com.example.oauth2demo.common.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.example.oauth2demo.auth.domain.SocialAccount;
import com.example.oauth2demo.auth.dto.CreateSocialAccountRequest;
import com.example.oauth2demo.auth.dto.CustomOAuth2User;
import com.example.oauth2demo.auth.infrastructure.SocialAccountJpaRepository;
import com.example.oauth2demo.common.exception.ServiceException;
import com.example.oauth2demo.user.domain.User;

@ExtendWith(MockitoExtension.class)
class SocialAccountServiceTest {

	@Mock
	private SocialAccountJpaRepository socialAccountJpaRepository;

	@InjectMocks
	private SocialAccountService socialAccountService;

	private SocialAccount socialAccount;

	@BeforeEach
	void setUp() {
		socialAccount = spy(SocialAccount.builder()
			.id(1L)
			.email("readup@readup.com")
			.provider("google")
			.providerUid("readup")
			.build());

		lenient().when(socialAccountJpaRepository.save(any(SocialAccount.class))).thenReturn(socialAccount);
		lenient().when(socialAccountJpaRepository.findById(1L)).thenReturn(Optional.ofNullable(socialAccount));
	}

	@Test
	@DisplayName(value = "social account 저장하기")
	void saveTest_success() {

		CreateSocialAccountRequest createSocialAccountRequest = CreateSocialAccountRequest.of(
			"readup@readup.com", "google", "readup"
		);

		SocialAccount result = socialAccountService.save(createSocialAccountRequest);
		assertNotNull(result);
		assertEquals(socialAccount.getProvider(), result.getProvider());
		assertEquals(socialAccount.getProviderUid(), result.getProviderUid());
		assertEquals(socialAccount.getEmail(), result.getEmail());
		verify(socialAccountJpaRepository, times(1)).save(any(SocialAccount.class));
	}

	@Test
	@DisplayName("id로 계정 찾기 성공")
	void findByIdTest() {
		when(socialAccountJpaRepository.findById(1L)).thenReturn(Optional.ofNullable(socialAccount));

		SocialAccount result = socialAccountService.findById(1L);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		verify(socialAccountJpaRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("id로 계정 찾기 실패")
	void findByIdExceptionTest() {
		when(socialAccountJpaRepository.findById(1L)).thenReturn(Optional.empty());

		ServiceException exception = assertThrows(ServiceException.class, () -> {
			socialAccountService.findById(1L);
		});

		assertEquals(SOCIAL_ACCOUNT_NOT_FOUND, exception.getErrorCode());
		verify(socialAccountJpaRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("social Account에 user 업데이트")
	void updateUser_ShouldUpdateUserWithSocialAccountInfo() {
		User user = User.builder()
			.id(2L)
			.nickname("read-up")
			.build();

		socialAccountService.updateUser(socialAccount.getId(), user);

		verify(socialAccount, times(1)).updateUserFromSocialAccount(user);
	}

	@Test
	@DisplayName("회원가입 시 회원가입 true")
	void isNewUser_ShouldReturnTrue_WhenUserDoesNotExist() {
		Long socialAccountId = 1L;
		String socialAccountIdStr = socialAccountId.toString();

		CustomOAuth2User oAuth2User = mock(CustomOAuth2User.class);
		when(oAuth2User.getName()).thenReturn(socialAccountIdStr);

		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(oAuth2User);

		when(socialAccountJpaRepository.findById(socialAccountId)).thenReturn(java.util.Optional.of(socialAccount));

		boolean result = socialAccountService.isNewUser(authentication);

		assertTrue(result);
		verify(authentication, times(1)).getPrincipal();
		verify(oAuth2User, times(1)).getName();
		verify(socialAccountJpaRepository, times(1)).findById(socialAccountId);
	}

	@Test
	@DisplayName("로그인 시 회원가입 false")
	void isNewUser_ShouldReturnFalse_WhenUserExists() {
		Long socialAccountId = 1L;
		String socialAccountIdStr = socialAccountId.toString();

		CustomOAuth2User oAuth2User = mock(CustomOAuth2User.class);
		when(oAuth2User.getName()).thenReturn(socialAccountIdStr);

		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(oAuth2User);
		when(socialAccountJpaRepository.findById(socialAccountId)).thenReturn(java.util.Optional.of(socialAccount));

		User user = User.builder()
			.id(2L)
			.nickname("read-up")
			.build();

		socialAccount.updateUserFromSocialAccount(user);

		boolean result = socialAccountService.isNewUser(authentication);

		assertFalse(result);
		verify(authentication, times(1)).getPrincipal();
		verify(oAuth2User, times(1)).getName();
		verify(socialAccountJpaRepository, times(1)).findById(socialAccountId);
	}
}