package com.dipl.abha.m2.userauthoninitpayload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meta{
    public String hint;
    public LocalDateTime expiry;
}

