package com.dipl.abha.m3.findpatient;

import com.dipl.abha.m2.userauthinitpayload.Requester;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Query{
    public Patient patient;
    public Requester requester;
}