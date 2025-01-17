package com.dipl.abha.encryptdecrypclasses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptionRequest {

    private String receiverPublicKey;
    private String receiverNonce;
    private String senderPrivateKey;

    private String senderPublicKey;
    private String senderNonce;
    private String plainTextData;
}
