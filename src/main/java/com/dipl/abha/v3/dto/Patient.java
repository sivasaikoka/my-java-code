package com.dipl.abha.v3.dto;

import java.util.List;

import com.dipl.abha.m2.discoverpayload.CareContext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
	
	private String id;
	private String referenceNumber;
	private String display;
	private List<CareContext> careContexts;
	private String hiType;
	private Integer count;

}
