package com.dipl.abha.m2.datapushpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KeyMaterialPayload{
    public String cryptoAlg;
    public String curve;
    public DhPublicKey dhPublicKey;
    public String nonce;
}

