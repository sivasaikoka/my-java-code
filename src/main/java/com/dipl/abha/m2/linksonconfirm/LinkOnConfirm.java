package com.dipl.abha.m2.linksonconfirm;

import java.time.LocalDateTime;
import java.util.List;

import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.Resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkOnConfirm{
    public String requestId;
    public LocalDateTime timestamp;
    public List<Patient> patient;
    public Error error;
    public Resp resp;
    public Resp response;
}