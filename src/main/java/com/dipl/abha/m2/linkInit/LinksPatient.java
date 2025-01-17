package com.dipl.abha.m2.linkInit;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinksPatient{
    public String id;
    public String referenceNumber;
    public List<CareContext> careContexts;
}

