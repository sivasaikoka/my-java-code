package com.dipl.abha.v3.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecordConfirm {
	
	 	private ArrayList<Patient> patient;
	    public Response response;

}
