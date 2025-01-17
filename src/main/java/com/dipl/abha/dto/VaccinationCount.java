package com.dipl.abha.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VaccinationCount {
	
	private Integer vaccineId;
	
	private Long vaccineCount;

	public VaccinationCount(Integer vaccineId, Long vaccineCount) {
		super();
		this.vaccineId = vaccineId;
		this.vaccineCount = vaccineCount;
	}
}
