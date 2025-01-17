package com.dipl.abha.m2.userauthinitpayload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthInitPayload{
    public String requestId;
    public LocalDateTime timestamp;
    public Query query;
}
