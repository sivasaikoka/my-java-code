package com.dipl.abha.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "hip_link_token_entity")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HipLinkTokenEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(name = "hip_id")
	private String hipId;

	@Column(name = "patient_id")
	private Long patientId;

	@Column(name = "abha_address")
	private String abhaAddress;

	@Column(name = "link_token")
	private String linkToken;

	@Column(name = "link_token_created_on")
	private LocalDateTime linkTokenCreatedOn;

	@Column(name = "link_token_expires_on")
	private LocalDateTime linkTokenExpiresOn;

}
