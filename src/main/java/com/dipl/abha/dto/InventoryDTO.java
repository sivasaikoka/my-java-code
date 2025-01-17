package com.dipl.abha.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventoryDTO {
	
	private Double stock;
	private String drugName;
	private String drugType;
	private Long agentId;
	private Long drugMappingId;
	private Long stateId;
	
	public InventoryDTO(Double stock, String drugName, String drugType, Long agentId, Long drugMappingId) {
		super();
		this.stock = stock;
		this.drugName = drugName;
		this.drugType = drugType;
		this.agentId = agentId;
		this.drugMappingId = drugMappingId;
	}
	
	public InventoryDTO(Double stock, String drugName, Long drugMappingId,Long stateId, String drugType) {
		super();
		this.stock = stock;
		this.drugName = drugName;
		this.drugMappingId = drugMappingId;
		this.stateId = stateId;
		this.drugType = drugType;
	}
}
