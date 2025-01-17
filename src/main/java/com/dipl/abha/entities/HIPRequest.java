package com.dipl.abha.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Entity
@Table(name = "hip_request")
@Data
public class HIPRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "request_code")
	@JsonProperty("requestId") 
	private String requestCode;

	@Column(name = "hip_request_type_id")
	private Integer hipRequestTypeId;

	@Column(name = "request_json")
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode requestJson;

	@Column(name = "abha_no")
	private String abhaNo;

	@Column(name = "create_on")
	private LocalDateTime createdOn;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "consent_artefact_code")
	private String consentArtefactCode;

	@Column(name = "txn_code")
	private String txnCode;

	@Column(name = "interaction_id")
	private String interactionId;

	@Column(name = "error_response_json")
	private String errorResponseJson;

}
