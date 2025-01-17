package com.dipl.abha.m2.phrnotifypayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareContext{
    public String patientReference;
    public String careContextReference;
}

