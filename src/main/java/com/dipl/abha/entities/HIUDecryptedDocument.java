package com.dipl.abha.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "hiu_decrypted_document")
@NoArgsConstructor
@AllArgsConstructor
public class HIUDecryptedDocument {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "txn_code")
	private String txnCode;

	@Column(name = "consent_request_code")
	private String consentRequestCode;

	@Column(name = "consent_artefact_code")
	private String consentArtefactCode;

	@Column(name = "care_context_reference")
	private String careContextReference;

	@Column(name = "decrypted_document")
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode decryptedDocument;

	@Column(name = "validation_successful")
	private boolean validationSuccessful;
	
	@Column(name = "hip_id")
	@JsonProperty("hip_id")
	private String hipId;
	
	@Column(name = "hip_name")
	@JsonProperty("hip_name")
	private String hipName;
	
	@Transient
	private String simplifiedBundleJson;

}