package com.dipl.abha.m2.authnotifydirectmodepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Auth{
    public String transactionId;
    public String status;
    public String accessToken;
    public Validity validity;
    public Patient patient;
}

