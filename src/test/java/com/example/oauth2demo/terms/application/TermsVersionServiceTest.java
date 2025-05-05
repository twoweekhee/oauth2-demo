package com.example.oauth2demo.terms.application;

import static com.example.oauth2demo.common.exception.ErrorCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.oauth2demo.common.exception.ServiceException;
import com.example.oauth2demo.terms.domain.TermsVersion;
import com.example.oauth2demo.terms.infrastructure.TermsVersionJpaRepository;
import com.example.oauth2demo.util.TermsTestUtils;

@ExtendWith(MockitoExtension.class)
class TermsVersionServiceTest {

	@Mock
	TermsVersionJpaRepository termsVersionJpaRepository;

	@InjectMocks
	TermsVersionService termsVersionService;

	@Test
	@DisplayName("약관 ID로 최신 버전 조회 성공")
	void getLatestTermsVersion_Success() {
		LocalDateTime fixedDateTime = LocalDateTime.of(2025, 4, 22, 10, 0);
		Long termsId = 1L;

		TermsVersion serviceTermsVersion = TermsTestUtils.createServiceTermsVersion();

		when(termsVersionJpaRepository.findFirstByTermsIdAndEffectiveDateLessThanEqualOrderByVersionDesc(
			eq(termsId), any(LocalDateTime.class)))
			.thenReturn(Optional.ofNullable(serviceTermsVersion));

		TermsVersion result = termsVersionService.getLatestTermsVersion(termsId, fixedDateTime);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getTerms()).isEqualTo(serviceTermsVersion.getTerms());
		assertThat(result.getVersion()).isEqualTo(serviceTermsVersion.getVersion());
		assertThat(result.getContent()).isEqualTo(serviceTermsVersion.getContent());

		verify(termsVersionJpaRepository, times(1)).findFirstByTermsIdAndEffectiveDateLessThanEqualOrderByVersionDesc(
			eq(termsId), any(LocalDateTime.class));
	}

	@Test
	@DisplayName("존재하지 않는 약관 ID로 조회 시 예외 발생")
	void getLatestTermsVersion_NotFound_ThrowsException() {
		LocalDateTime fixedDateTime = LocalDateTime.of(2025, 4, 22, 10, 0);
		Long nonExistentTermsId = 999L;

		when(termsVersionJpaRepository.findFirstByTermsIdAndEffectiveDateLessThanEqualOrderByVersionDesc(
			eq(nonExistentTermsId), any(LocalDateTime.class)))
			.thenReturn(Optional.empty());

		assertThatThrownBy(() -> termsVersionService.getLatestTermsVersion(nonExistentTermsId, fixedDateTime))
			.isInstanceOf(ServiceException.class)
			.hasFieldOrPropertyWithValue("errorCode", TERMS_VERSION_NOT_FOUND);

		verify(termsVersionJpaRepository, times(1)).findFirstByTermsIdAndEffectiveDateLessThanEqualOrderByVersionDesc(
			eq(nonExistentTermsId), any(LocalDateTime.class));
	}

	@Test
	void testFindById_WhenTermsVersionExists_ShouldReturnTermsVersion() {
		Long validId = 1L;
		TermsVersion expectedTermsVersion = TermsTestUtils.createServiceTermsVersion();

		when(termsVersionJpaRepository.findById(validId))
			.thenReturn(Optional.of(expectedTermsVersion));

		TermsVersion foundTermsVersion = termsVersionService.findById(validId);

		assertThat(foundTermsVersion).isNotNull();
		assertThat(foundTermsVersion.getId()).isEqualTo(validId);
		assertThat(foundTermsVersion.getVersion()).isEqualTo("1.0");
		assertThat(foundTermsVersion.getContent()).isEqualTo("서비스 이용약관 내용");

		verify(termsVersionJpaRepository, times(1)).findById(validId);
	}

	@Test
	@DisplayName("id로 약관 찾기 에러")
	void testFindById_WhenTermsVersionNotFound_ShouldThrowServiceException() {
		Long nonExistentId = 999L;

		when(termsVersionJpaRepository.findById(nonExistentId))
			.thenReturn(Optional.empty());

		assertThatThrownBy(() -> termsVersionService.findById(nonExistentId))
			.isInstanceOf(ServiceException.class)
			.hasMessageContaining("약관의 버전이 존재하지 않습니다.");

		verify(termsVersionJpaRepository, times(1)).findById(nonExistentId);
	}
}