package com.dipl.abha.m2.authnotifydirectmodepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address{
    public String line;
    public String district;
    public String state;
    public Object pincode;
}

