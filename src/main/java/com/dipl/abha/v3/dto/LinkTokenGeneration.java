package com.dipl.abha.v3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkTokenGeneration {

	
	private Long abhaNumber;
	
	private String abhaAddress;
	
	private String name;
	
	private String gender;
	
	private Integer yearOfBirth;
	
	
}
