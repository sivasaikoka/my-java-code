package com.dipl.abha.uhi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tags {
	
	@JsonProperty("@abdm/gov/in/education")
	private String education;
	
	@JsonProperty("@abdm/gov/in/experience")
    private String experience;
	
	@JsonProperty("@abdm/gov/in/languages")
    private String languages;
	
	@JsonProperty("@abdm/gov/in/hpr_id")
    private String hprid;
	
	@JsonProperty("@abdm/gov/in/hfr_id")
    private String hfrId;
	
	@JsonProperty("@abdm/gov/in/groupConsultation")
    private String groupConsultation;

	@JsonProperty("@abdm/gov.in/cancelledby")
	 private String cancelledby;
	
		@JsonProperty("@abdm/gov.in/teleconsultation/uri")
	    private String teleconsultationuri;


}

