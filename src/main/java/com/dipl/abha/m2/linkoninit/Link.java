package com.dipl.abha.m2.linkoninit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link{
    public String referenceNumber;
    public String authenticationType;
    public Meta meta;
}

