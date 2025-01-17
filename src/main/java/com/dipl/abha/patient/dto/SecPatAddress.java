package com.dipl.abha.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecPatAddress{
    public String address1;
    public String address2;
    public Object areaId;
    public Object cityId;
    public Object countryId;
    public Object districtId;
    public String email;
    public String fax;
    public String homePhone;
    public Long id;
    public String mobile1;
    public String mobile2;
    public String officePhone;
    public String password;
    public String sendSms;
    public Object stateId;
    public String transferFlag;
    public String webaddress;
    public String addressTypeId;
    public Object patientId;
    public String primaryAddress;
    public int status;
    public String sendEmail;
}

