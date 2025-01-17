package com.dipl.abha.m3.consentOnFetch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Consent{
    public String status;
    public ConsentDetail consentDetail;
    public String signature;
}

