package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenanIdAndIsIntegratedModule {
	private String integrated_tenant_id;
	private Boolean is_integrated_module;
}