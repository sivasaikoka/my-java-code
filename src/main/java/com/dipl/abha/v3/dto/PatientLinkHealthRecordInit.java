package com.dipl.abha.v3.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientLinkHealthRecordInit {

	private String transactionId;
	private ArrayList<Patient> patient;

}
