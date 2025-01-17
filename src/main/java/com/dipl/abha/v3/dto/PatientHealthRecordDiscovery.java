package com.dipl.abha.v3.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientHealthRecordDiscovery {

	private String hipId;
	private ArrayList<UnverifiedIdentifier> unverifiedIdentifiers;
	private String authToken;
	private String hiuId;

}
