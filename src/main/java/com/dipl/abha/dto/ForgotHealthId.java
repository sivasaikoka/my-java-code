package com.dipl.abha.dto;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotHealthId {
	
	    private String healthId;
	    private String healthIdNumber;
	    private JsonNode jwtResponse;
	
}
