package com.dipl.abha.m2.userauthinitpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Query{
    public String id;
    public String purpose;
    public String authMode;
    public Requester requester;
}

