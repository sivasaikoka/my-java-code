package com.dipl.abha.m3.consentOnFetch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Frequency{
    public String unit;
    public int value;
    public int repeats;
}

