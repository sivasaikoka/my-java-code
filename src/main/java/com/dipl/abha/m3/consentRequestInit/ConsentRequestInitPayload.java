package com.dipl.abha.m3.consentRequestInit;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConsentRequestInitPayload{
    public String requestId;
    public LocalDateTime timestamp;
    public Consent consent;
}