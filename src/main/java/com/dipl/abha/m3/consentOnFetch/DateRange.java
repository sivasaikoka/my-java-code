package com.dipl.abha.m3.consentOnFetch;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateRange{
    public LocalDateTime from;
    @JsonProperty("to") 
    public LocalDateTime myto;
}



