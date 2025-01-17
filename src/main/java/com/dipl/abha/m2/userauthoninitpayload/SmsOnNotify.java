package com.dipl.abha.m2.userauthoninitpayload;

import java.time.LocalDateTime;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsOnNotify{
    public String requestId;
    public LocalDateTime timestamp;
    public String status;
    public Error error;
    public Resp resp;
}