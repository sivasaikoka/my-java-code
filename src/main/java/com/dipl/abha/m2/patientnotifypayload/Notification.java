package com.dipl.abha.m2.patientnotifypayload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification{
    public String phoneNo;
    public Hip hip;
}

