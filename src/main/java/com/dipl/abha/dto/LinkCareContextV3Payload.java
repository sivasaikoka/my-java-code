package com.dipl.abha.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkCareContextV3Payload {
	public String requestId;
	public String abhaNumber;
	public String abhaAddress;
	public List<PatientLinkCareContext> patient;
	public String token;

}
