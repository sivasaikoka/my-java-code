package com.dipl.abha.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class HIPMapping {

	private Long id;
	
	@JsonProperty("hfr_id")
	private String hfrId;
	
	@JsonProperty("tenant_id")
	private Integer tenantId;
	
	@JsonProperty("api_url")
	private String apiUrl;
	
	@JsonProperty("created_on")
	private LocalDateTime createdOn;
}
