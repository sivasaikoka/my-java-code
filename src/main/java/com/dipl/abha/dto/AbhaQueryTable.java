package com.dipl.abha.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AbhaQueryTable {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("tenant_id")
	private int tenantId;

	@JsonProperty("query_type")
	private String queryType;

	@JsonProperty("query_description")
	private String queryDescription;

	@JsonProperty("query")
	private String query;

	@JsonProperty("created_on")
	private LocalDateTime createdOn;
}
