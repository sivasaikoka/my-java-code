package com.dipl.abha.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Guarator{
    public String address;
    public String guarantorName;
    public String transferFlag;
    public int guarantorRelationshipid;
    public Long id;
    public int status;
}

