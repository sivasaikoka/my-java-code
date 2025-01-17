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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hip_call_back")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HIPCallback {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "request_code")
	@JsonProperty("requestId") 
	private String requestCode;

	@Column(name = "hip_request_type_id")
	private Integer hipRequestTypeId;

	@Column(name = "response_json")
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode responseJson;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "consent_request_code")
	private String consentRequestCode;

	@Column(name = "txn_code")
	private String txnCode;

	@Column(name = "patient_ref_no")
	private String patientRefNo;

	@Column(name = "abha_no")
	private String abhaNo;
	
	@Column(name = "error_message")
	private String errorMessage;

}