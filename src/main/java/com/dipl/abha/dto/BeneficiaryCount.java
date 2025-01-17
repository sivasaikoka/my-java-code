package com.dipl.abha.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BeneficiaryCount {

	private Long beneficiaryCount;
	
	private Double totalAmount;

	public BeneficiaryCount(Long beneficiaryCount, Double totalAmount) {
		super();
		this.beneficiaryCount = beneficiaryCount;
		this.totalAmount = totalAmount;
	}
}
