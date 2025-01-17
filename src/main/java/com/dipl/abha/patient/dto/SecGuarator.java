package com.dipl.abha.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecGuarator{
    public String address;
    public String guarantorName;
    public int guarantorRelationshipid;
    public Long id;
    public int status;
    public String transferFlag;
}

