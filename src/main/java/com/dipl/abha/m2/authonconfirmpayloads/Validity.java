package com.dipl.abha.m2.authonconfirmpayloads;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Validity{
    public String purpose;
    public Requester requester;
    public LocalDateTime expiry;
    public String limit;
}