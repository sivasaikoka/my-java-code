package com.dipl.abha.uhi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchTypeDto {
	
	private String speciality;
	
	private String doctorName;
	
	private String hospitalName;
	
	private String hprId;
	
	private String toDate;
	
	private String fromDate;
	

}
