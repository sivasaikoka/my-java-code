package com.dipl.abha.m2.phrnotifypayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification{
    public Patient patient;
    public CareContext careContext;
    public List<String> hiTypes;
    public String date;
    public Hip hip;
}

