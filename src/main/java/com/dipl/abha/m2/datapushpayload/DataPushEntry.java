package com.dipl.abha.m2.datapushpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataPushEntry{
    public String content;
    public String media;
    public String checksum;
    public String careContextReference;
}

