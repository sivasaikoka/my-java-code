package com.dipl.abha.m3.consentRequestInit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Frequency{
    public String unit;
    public int value;
    public int repeats;
}

