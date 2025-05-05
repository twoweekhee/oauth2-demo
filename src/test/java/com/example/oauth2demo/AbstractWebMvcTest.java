package com.example.oauth2demo;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;

import com.example.oauth2demo.common.config.SecurityTestConfig;

@AutoConfigureRestDocs
@Import(SecurityTestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class AbstractWebMvcTest {
}
