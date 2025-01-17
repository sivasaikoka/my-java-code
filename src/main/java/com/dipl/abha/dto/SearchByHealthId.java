package com.dipl.abha.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchByHealthId {
	
	private String healthId;
	private String healthIdNumber;
	private Object name;
	private ArrayList<String> authMethods;
	private JsonNode tags;
private ArrayList<String> blockedAuthMethods;
private String status;
private String verificationStatus;
}
