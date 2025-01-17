package com.dipl.abha.m2.addcarecontextpayload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCareContextPayload{
    public String requestId;
    public LocalDateTime timestamp;
    public Link link;
}

