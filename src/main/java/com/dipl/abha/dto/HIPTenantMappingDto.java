package com.dipl.abha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HIPTenantMappingDto {

	@JsonProperty("tenant_id")
	private Long tenantId;

	@JsonProperty("ndhm_client_id")
	private String ndhmClientId;

	@JsonProperty("ndhm_client_secrete_key")
	private String ndhmClientSecrete;

	@JsonProperty("is_server_access")
	private Boolean serverAccess;

	@JsonProperty("url_path")
	private String urlPath;
}
