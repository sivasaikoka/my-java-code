package com.dipl.abha.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResultSetDto {

	@JsonProperty("patient_referencenumber")
	private String patientReferenceNumber;

	@JsonProperty("patient_display")
	private String patientDisplay;

	@JsonProperty("patient_id")
	private String patientId;

	@JsonProperty("carecontexts_referencenumber")
	private String careContextReferenceNumber;

	@JsonProperty("result_id")
	private String resultId;

	@JsonProperty("carecontexts_display")
	private String careContextDisplay;

	@JsonProperty("report_type")
	private String reportType;

	@JsonProperty("consultation_date")
	private LocalDateTime consulstionDate;
	
	@JsonProperty("doctor")
	private String doctor;
	
	@JsonProperty("file_path")
	private String filePath;
	
	@JsonProperty("chief_complaints")
	private String chiefComplaints;
	
	@JsonProperty("meds")
	private String meds;
	
	@JsonProperty("med_directions")
	private String medDirections;
	
	@JsonProperty("visit_date")
	private String visitDate;
	
	@JsonProperty("follow_up_date")
	private String followUpDate;
	
	@JsonProperty("visit_start_time")
	private String visitStartTime;
	
	@JsonProperty("visit_end_time")
	private String visitEndTime;
	
	@JsonProperty("vital_history")
	private String vitalHistory;
	
	@JsonProperty("investigation")
	private String investigationAdvice;
	
	@JsonProperty("clinic_name")
	private String clinicName;
	
}
