package com.dipl.abha.m2.patientstatusnotifypayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification{
    public String status;
    public Patient patient;
}

