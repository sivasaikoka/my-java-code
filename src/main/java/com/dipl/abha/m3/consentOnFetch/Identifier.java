package com.dipl.abha.m3.consentOnFetch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Identifier{
    public String type;
    public String value;
    public String system;
}

