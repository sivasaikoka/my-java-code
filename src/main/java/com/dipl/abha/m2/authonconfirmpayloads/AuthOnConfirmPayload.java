package com.dipl.abha.m2.authonconfirmpayloads;

import java.time.LocalDateTime;

import com.dipl.abha.dto.Resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthOnConfirmPayload{
    public String requestId;
    public LocalDateTime timestamp;
    public Auth auth;
    public com.dipl.abha.dto.Error error;
    public Resp resp;
}

