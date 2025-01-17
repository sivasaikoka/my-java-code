package com.dipl.abha.m2.linkconfirmpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Confirmation{
    public String linkRefNumber;
    public String token;
}

