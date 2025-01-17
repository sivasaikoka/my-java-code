package com.dipl.abha.m2.addcarecontextpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link{
    public String accessToken;
    public Patient patient;
}

