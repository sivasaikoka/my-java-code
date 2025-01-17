package com.dipl.abha.dto;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CallBackDTO {
	
	private Long id;

	@JsonProperty("requestId") 
	private String requestid;

//	@Type(type = "com.dipl.core.util.JsonNodeUserType")
	private HashMap<String, Object> request;
	
//	@Type(type = "com.dipl.core.util.JsonNodeUserType")
	private HashMap<String, Object> response;
		
	private String abha_no;
	
	private String txnId;
}
