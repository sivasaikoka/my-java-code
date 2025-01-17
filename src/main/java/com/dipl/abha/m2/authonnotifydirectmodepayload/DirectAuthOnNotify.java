package com.dipl.abha.m2.authonnotifydirectmodepayload;

import java.time.LocalDateTime;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DirectAuthOnNotify{
    public String requestId;
    public LocalDateTime timestamp;
    public Acknowledgement acknowledgement;
    public Error error;
    public Resp resp;
}