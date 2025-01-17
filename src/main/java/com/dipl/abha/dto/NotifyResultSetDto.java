package com.dipl.abha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotifyResultSetDto {
	
	@JsonProperty("patient_referencenumber")
	private String patientReferenceNumber;

	@JsonProperty("patient_display")
	private String patientDisplay;

	@JsonProperty("patient_id")
	private String patientId;

	@JsonProperty("result_no")
	private String resultNo;

	@JsonProperty("carecontexts_referencenumber")
	private String careContextRef;

	@JsonProperty("carecontexts_display")
	private String careContextDisplay;
}
