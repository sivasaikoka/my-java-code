package com.dipl.abha.m2.datapushpayload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataPushPayload{
    public int pageNumber;
    public int pageCount;
    public String transactionId;
    public List<DataPushEntry> entries;
    public KeyMaterialPayload keyMaterial;
}

