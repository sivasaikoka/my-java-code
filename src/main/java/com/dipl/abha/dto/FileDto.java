package com.dipl.abha.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {

	@JsonProperty("anm_name")
	private String anmName;

	@JsonProperty("patient_name")
	private String patientName;

	@JsonProperty("created_on")
	private Date createdOn;

	@JsonProperty("file_path")
	private String filePath;

	@JsonProperty("name")
	private String fileName;

	@JsonProperty("incident_no")
	private Long incidentNo;
}
