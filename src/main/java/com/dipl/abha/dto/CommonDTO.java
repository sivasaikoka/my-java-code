package com.dipl.abha.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonDTO {
	
	private Long id;
	private Boolean isActive;
	private Boolean accountActivated;
	private Boolean liveRecord;
	
	public CommonDTO(Long id, Boolean isActive, Boolean accountActivated, Boolean liveRecord) {
		super();
		this.id = id;
		this.isActive = isActive;
		this.accountActivated = accountActivated;
		this.liveRecord = liveRecord;
	}
}
