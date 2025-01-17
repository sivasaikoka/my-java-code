package com.dipl.abha.m3.findpatient;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientFindPayload{
    public String requestId;
    public LocalDateTime timestamp;
    public Query query;
}