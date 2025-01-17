package com.dipl.abha.m3.onfindpatient;

import java.time.LocalDateTime;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PatientOnFindPayload{
    public String requestId;
    public LocalDateTime timestamp;
    public Patient patient;
    public Error error;
    public Resp resp;
}