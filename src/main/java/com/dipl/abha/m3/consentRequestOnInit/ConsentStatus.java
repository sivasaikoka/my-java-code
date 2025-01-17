package com.dipl.abha.m3.consentRequestOnInit;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsentStatus{
    public String requestId;
    public LocalDateTime timestamp;
    public String consentRequestId;
}