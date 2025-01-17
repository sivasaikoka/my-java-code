package com.dipl.abha.m2.requestypayload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class DhPublicKey{
    public LocalDateTime expiry;
    public String parameters;
    public String keyValue;
}