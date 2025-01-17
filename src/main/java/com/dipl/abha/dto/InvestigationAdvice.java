package com.dipl.abha.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestigationAdvice {
	private ArrayList<Master> master;
    private String otherInvestigation;
}
