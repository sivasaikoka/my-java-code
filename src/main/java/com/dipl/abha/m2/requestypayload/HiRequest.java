package com.dipl.abha.m2.requestypayload;

import com.dipl.abha.m2.notifypayload.DateRange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HiRequest{
    public Consent consent;
    public DateRange dateRange;
    public String dataPushUrl;
    public KeyMaterial keyMaterial;
}