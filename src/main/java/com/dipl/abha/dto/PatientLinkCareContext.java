package com.dipl.abha.dto;

import java.util.List;

import com.dipl.abha.m2.addcarecontextpayload.CareContext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientLinkCareContext {
	public String referenceNumber;
	public String display;
	public List<CareContext> careContexts;
	public String hiType;
	public Integer count;
}
