package com.dipl.abha.v3.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkingCareContext {
	private String linktoken;
	private Long abhaNumber;
	private String abhaAddress;
	private ArrayList<Patient> patient;

}
