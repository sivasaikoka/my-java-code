package com.dipl.abha.m2.userauthoninitpayload;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auth{
    public String transactionId;
    public String mode;
    public Meta meta;
}

