package com.dipl.abha.v3.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthRecordDiscover {
	
	private String transactionId;
	private ArrayList<Patient> patient;
	private ArrayList<String> matchedBy;
	private Response response;

}
