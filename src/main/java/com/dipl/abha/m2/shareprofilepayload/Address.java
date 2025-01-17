package com.dipl.abha.m2.shareprofilepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address{
    public String line;
    public String district;
    public String state;
    public String pincode;
}

