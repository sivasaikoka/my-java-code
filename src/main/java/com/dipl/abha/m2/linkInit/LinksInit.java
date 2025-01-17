package com.dipl.abha.m2.linkInit;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinksInit{
    public String requestId;
    public LocalDateTime timestamp;
    public String transactionId;
    public LinksPatient patient;
}