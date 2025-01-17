package com.dipl.abha.entities;

import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "care_context_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareContextLogs {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	@JsonProperty("id")
	private Long id;
	
	
	@Column(name = "patient_interaction_id")
	@JsonProperty("patient_interaction_id")
	private String patietInterationId;
	
	
	@Column(name = "beneficiary_id")
	@JsonProperty("beneficiary_id")
	private Long beneficiaryId;
	

	@Column(name = "display")
	@JsonProperty("display")
	private String careContextDisplay;
	
	@Column(name = "direct_auth_status")
	@JsonProperty("direct_auth_status")
	private String directAuthStatus;

	
	@Column(name = "created_on")
	@JsonProperty("created_on")
	private LocalDateTime createdOn = LocalDateTime.now();
	
	
}
