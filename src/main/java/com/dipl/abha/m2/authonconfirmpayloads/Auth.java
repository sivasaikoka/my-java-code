package com.dipl.abha.m2.authonconfirmpayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auth{
    public String accessToken;
    public Validity validity;
    public Patient patient;
}

