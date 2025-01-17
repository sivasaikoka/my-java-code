package com.dipl.abha.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientAddress{
    public String address1;
    public String address2;
    public Object areaId;
    public Object cityId;
    public Object countryId;
    public Object districtId;
    public String email;
    public Long id;
    public Object mobile1;
    public Object mobile2;
    public Object officePhone;
    public String password;
    public String sendEmail;
    public String sendSms;
    public Object stateId;
    public String transferFlag;
    public String fax;
    public String webaddress;
    public Object zip;
    public int addressTypeId;
    public Object patientId;
    public String primaryAddress;
    public int status;
    public String homePhone;
}

