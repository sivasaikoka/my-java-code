package com.dipl.abha.m2.authnotifydirectmodepayload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Validity{
    public String purpose;
    public Requester requester;
    public LocalDateTime expiry;
    public int limit;
}

