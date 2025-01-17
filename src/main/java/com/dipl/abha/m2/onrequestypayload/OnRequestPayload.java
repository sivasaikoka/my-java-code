package com.dipl.abha.m2.onrequestypayload;

import java.time.LocalDateTime;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnRequestPayload{
    public String requestId;
    public LocalDateTime timestamp;
    public HiRequest hiRequest;
    public Resp resp;
    public Resp response;
    public Error error;
}