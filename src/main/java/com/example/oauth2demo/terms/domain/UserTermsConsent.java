package com.example.oauth2demo.terms.domain;

import com.example.oauth2demo.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_terms_consent")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTermsConsent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "terms_id", nullable = false)
	private Terms terms;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "terms_version_id", nullable = false)
	private TermsVersion termsVersion;

	@Column(name = "is_consent", nullable = false)
	private Boolean isConsent;
}
