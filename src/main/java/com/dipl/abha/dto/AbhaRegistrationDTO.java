package com.dipl.abha.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AbhaRegistrationDTO {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("tenant_id")
	private int tenant_id;

	@JsonProperty("api_type")
	private String api_type;

	@JsonProperty("abha_address")
	private String abha_address;

	@JsonProperty("abha_no")
	private String abha_no;

	@JsonProperty("abha_profile")
	private String abha_profile;

	@JsonProperty("patient_id")
	private Long patient_id;

	@JsonProperty("gender")
	private String gender;

	@JsonProperty("year_of_birth")
	private Long year_of_birth;

	@JsonProperty("mobile_no")
	private String mobile_no;

	@JsonProperty("abha_card_path")
	private String abha_card_path;

	@JsonProperty("is_abha_linked")
	private Integer is_abha_linked;
	
	@JsonProperty("created_on")
	private LocalDateTime created_on;
	
	@JsonProperty("mrn_no")
	private String mrnNo;
	
	@JsonProperty("full_name")
	private String fullName;
}
