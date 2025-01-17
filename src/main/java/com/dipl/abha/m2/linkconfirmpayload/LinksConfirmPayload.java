package com.dipl.abha.m2.linkconfirmpayload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinksConfirmPayload{
    public String requestId;
    public LocalDateTime timestamp;
    public Confirmation confirmation;
}