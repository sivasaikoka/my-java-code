package com.dipl.abha.m2.patientstatusnotifypayload;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientStatusNotifyPayload{
    public String requestId;
    public LocalDate timestamp;
    public Notification notification;
}