package com.dipl.abha.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemographicResponse {
	private String healthId;
    private String healthIdNumber;
    private Object name;
    private String status;
    private ArrayList<String> authMethods;
    private JsonNode tags;
}
