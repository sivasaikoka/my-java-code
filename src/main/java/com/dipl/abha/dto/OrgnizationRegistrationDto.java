package com.dipl.abha.dto;



import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrgnizationRegistrationDto {
    
	@JsonProperty("orgnization_name")
	private String orgnizationName;
	@JsonProperty("primary_contact_no")
	private String orgnizationContactNo;
	@JsonProperty("email")
	private String orgnizationEmail;

}
