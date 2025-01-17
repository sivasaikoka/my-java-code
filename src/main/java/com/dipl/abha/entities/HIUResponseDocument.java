package com.dipl.abha.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "hiu_response_document")
@NoArgsConstructor
@AllArgsConstructor
public class HIUResponseDocument {
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

	@Column(name = "care_context")
	private String careContext;

	@Column(name = "encrypted_entries")
	private String encryptedEntries;

	@Column(name = "decryption_done")
	private boolean decryptionDone;

}