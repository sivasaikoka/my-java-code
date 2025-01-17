package com.dipl.abha.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Table(name = "hip_notify_logs")
@Data
public class HIPNotifyLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "request_code")
	@JsonProperty("requestId") 
	private String requestCode;

	@Column(name = "hip_request_type_id")
	private Integer hipRequestTypeId;

	@Column(name = "consent_request_code")
	private String consentRequestCode;

	@Column(name = "txn_code")
	private String txnCode;

	@Column(name = "abha_no")
	private String abhaNo;

	@Column(name = "status")
	private String status;
}
