package com.dipl.abha.m2.patientstatusonnotifypayload;

import java.time.LocalDateTime;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientStatuOnNotify{
    public String requestId;
    public LocalDateTime timestamp;
    public Acknowledgment acknowledgment;
    public Error error;
    public Resp resp;
}