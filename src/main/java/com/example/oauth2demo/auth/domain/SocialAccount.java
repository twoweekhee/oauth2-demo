package com.example.oauth2demo.auth.domain;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.example.oauth2demo.common.entity.BaseEntity;
import com.example.oauth2demo.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "social_account")
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE social_account SET deleted_at = NOW() WHERE id = ?")
public class SocialAccount extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
	private User user;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "provider", nullable = false)
	private String provider;

	@Column(name = "provider_uid", nullable = false)
	private String providerUid;

	public void updateUserFromSocialAccount(User user) {
		this.user = user;
	}
}
