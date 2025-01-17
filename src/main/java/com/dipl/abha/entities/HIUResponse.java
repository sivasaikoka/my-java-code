package com.dipl.abha.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "hiu_response")
@NoArgsConstructor
@AllArgsConstructor
public class HIUResponse {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "request_code")
	private String requestCode;

	@Column(name = "txn_code")
	private String txnCode;

	@Column(name = "consent_request_code")
	private String consentRequestCode;

	@Column(name = "consent_artefact_code")
	private String consentArtefactCode;

	@Column(name = "response_json")
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode responseJson;
	
	@Column(name = "hiu_request_type_id")
	private Integer hiuRequestTypeId;

}