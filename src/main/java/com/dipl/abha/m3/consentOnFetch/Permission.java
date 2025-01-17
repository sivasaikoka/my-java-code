package com.dipl.abha.m3.consentOnFetch;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission{
    public String accessMode;
    public DateRange dateRange;
    public LocalDateTime dataEraseAt;
    public Frequency frequency;
}

