package com.dipl.abha.m2.onshareprofilepayload;

import java.time.LocalDateTime;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnShareProfilePayload{
    public String requestId;
    public LocalDateTime timestamp;
    public ShareAcknowledgement acknowledgement;
    public Error error;
    public Resp resp;
}