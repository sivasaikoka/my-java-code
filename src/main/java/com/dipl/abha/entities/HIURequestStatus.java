package com.dipl.abha.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "hiu_request_status")
@NoArgsConstructor
@AllArgsConstructor
public class HIURequestStatus {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "request_code")
	private String requestCode;

	@Column(name = "consent_request_code")
	private String consentRequestCode;

	@Column(name = "status_type")
	private String statusType;
	
	@Column(name = "created_on", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdOn;
	
//	@Column(name = "created_by")
//	private Long createdBy;

	@Column(name = "is_deleted")
	private Boolean isDeleted = false;
	
	@Column(name="consent_artefact_code")
	private String cosentArtifactCode;

}