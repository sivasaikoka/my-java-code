package com.dipl.abha.m3.consentOnFetch;

import java.time.LocalDateTime;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsentOnFetch{
    public String requestId;
    public LocalDateTime timestamp;
    public Consent consent;
    public Error error;
    public Resp resp;
}