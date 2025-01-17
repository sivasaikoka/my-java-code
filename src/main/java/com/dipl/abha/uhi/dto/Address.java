package com.dipl.abha.uhi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	
	 	private String door;
	 	
	 	private String name;
	    
	 	private String locality;
	    
	 	private String city;
	    
	 	private String state;
	    
	 	private String country;
	    
	 	private String area_code;
}
