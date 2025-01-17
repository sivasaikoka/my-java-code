package com.dipl.abha.m2.requestypayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyMaterial{
    public String cryptoAlg;
    public String curve;
    public DhPublicKey dhPublicKey;
    public String nonce;
}