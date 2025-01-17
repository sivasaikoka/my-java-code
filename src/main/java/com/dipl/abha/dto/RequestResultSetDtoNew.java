package com.dipl.abha.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResultSetDtoNew {

	@JsonProperty("patient_id")
	private String patientId;

	@JsonProperty("patient_display")
	private String patientDisplay;

	@JsonProperty("patient_referencenumber")
	private String patentReferenceNumber;

	@JsonProperty("doctor")
	private String doctor;

	@JsonProperty("carecontexts_referencenumber")
	private String careContextReferenceNumber;

	@JsonProperty("file_path")
	private String filePath;

	@JsonProperty("carecontexts_display")
	private String carContextDisplay;

	@JsonProperty("report_type")
	private String reportType;

	@JsonProperty("consultation_date")
	private LocalDateTime consulstionDate;

	@JsonProperty("clinic_name")
	private String clinicName;
	
	@JsonProperty("result_id")
	private String resultId;
}
