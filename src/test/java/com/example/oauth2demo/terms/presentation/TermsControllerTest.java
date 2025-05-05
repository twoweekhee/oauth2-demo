package com.example.oauth2demo.terms.presentation;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.oauth2demo.AbstractWebMvcTest;
import com.example.oauth2demo.terms.application.TermsManagementService;
import com.example.oauth2demo.terms.dto.TermsResponse;

@AutoConfigureRestDocs
@WebMvcTest(TermsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(RestDocumentationExtension.class)
class TermsControllerTest extends AbstractWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private TermsManagementService termsManagementService;

	@Test
	@DisplayName("약관 리스트 가져오기")
	void testGetTermsList() throws Exception {

		final String uri = "/public/terms";

		TermsResponse terms1 = new TermsResponse(1L, "SERVICE", "서비스 이용약관", "제 1조");
		TermsResponse terms2 = new TermsResponse(2L, "PERSONAL", "개인정보 처리방침", "제 1조");
		List<TermsResponse> termsList = Arrays.asList(terms1, terms2);

		when(termsManagementService.getLatestTermsConsentList()).thenReturn(termsList);

		mockMvc.perform(get(uri)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data").isArray())
				.andExpect(jsonPath("$.data.length()").value(2))
				.andExpect(jsonPath("$.message").value("Request succeeded."))
				.andExpect(jsonPath("$.data[0].termsVersionId").value(1L))
				.andExpect(jsonPath("$.data[0].code").value("SERVICE"))
				.andExpect(jsonPath("$.data[0].title").value("서비스 이용약관"))
				.andExpect(jsonPath("$.data[0].content").value("제 1조"))
				.andExpect(jsonPath("$.data[1].termsVersionId").value(2L))
				.andExpect(jsonPath("$.data[1].code").value("PERSONAL"))
				.andExpect(jsonPath("$.data[1].title").value("개인정보 처리방침"))
				.andExpect(jsonPath("$.data[1].content").value("제 1조"))
				.andDo(document("get-terms-list",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					responseFields(
						fieldWithPath("success").description("API 요청 성공 여부"),
						fieldWithPath("data").description("약관 목록 데이터"),
						fieldWithPath("message").description("Request succeeded."),
						fieldWithPath("data[].termsVersionId").description("약관 버전 ID"),
						fieldWithPath("data[].code").description("약관 code"),
						fieldWithPath("data[].title").description("약관 제목"),
						fieldWithPath("data[].content").description("약관 내용")
					)
		));

		verify(termsManagementService, times(1)).getLatestTermsConsentList();
	}
}