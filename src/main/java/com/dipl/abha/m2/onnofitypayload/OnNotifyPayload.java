package com.dipl.abha.m2.onnofitypayload;

import java.time.LocalDateTime;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class OnNotifyPayload{
    public String requestId;
    public LocalDateTime timestamp;
    public Acknowledgement acknowledgement;
//    public List<Acknowledgement> acknowledgement;
    public Resp resp;
    public Error error;
    
    public Resp response;
}