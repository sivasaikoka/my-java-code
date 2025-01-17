package com.dipl.abha.m2.onnofitypayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Acknowledgement{
    public String status;
    public String consentId;
}