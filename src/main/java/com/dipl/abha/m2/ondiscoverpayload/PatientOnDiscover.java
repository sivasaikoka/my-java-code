package com.dipl.abha.m2.ondiscoverpayload;

import java.util.List;

import com.dipl.abha.m2.discoverpayload.CareContext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientOnDiscover {
	
    public String referenceNumber;
    public String display;
    public List<CareContext> careContexts;
    public List<String> matchedBy;
}
