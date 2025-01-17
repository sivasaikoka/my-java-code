package com.dipl.abha.m2.authonconfirmpayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Identifier{
    public String type;
    public String value;
}

