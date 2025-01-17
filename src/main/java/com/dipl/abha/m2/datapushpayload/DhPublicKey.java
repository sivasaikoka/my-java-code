package com.dipl.abha.m2.datapushpayload;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DhPublicKey{
    public LocalDateTime expiry;
    public String parameters;
    public String keyValue;
}

