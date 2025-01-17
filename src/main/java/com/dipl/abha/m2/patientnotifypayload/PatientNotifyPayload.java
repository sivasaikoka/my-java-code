package com.dipl.abha.m2.patientnotifypayload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientNotifyPayload{
    public String requestId;
    public LocalDateTime timestamp;
    public Notification notification;
}