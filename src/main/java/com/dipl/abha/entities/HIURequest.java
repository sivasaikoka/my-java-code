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
@Table(name = "hiu_request")
@NoArgsConstructor
@AllArgsConstructor
public class HIURequest {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "request_code")
	private String requestCode;

	@Column(name = "txn_code")
	private String txnCode;

	@Column(name = "request_json")
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode requestJson;

	@Column(name = "hiu_request_type_id")
	private Integer hiuRequestTypeId;

	@Column(name = "beneficiary_id")
	private Long beneficiaryId;

	@Column(name = "abha_no")
	private String abhaNumber;
	
	@Column(name = "created_by")
	private Long createdBy;
	
	@Column(name = "error")
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
	private JsonNode errorResponse;
	

}
