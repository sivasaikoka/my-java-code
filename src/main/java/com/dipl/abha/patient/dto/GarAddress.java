package com.dipl.abha.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GarAddress{
    public String address1;
    public String address2;
    public Object areaId;
    public Object cityId;
    public Object countryId;
    public Object districtId;
    public String email;
    public String homePhone;
    public String mobile1;
    public String mobile2;
    public String officePhone;
    public String password;
    public String sendEmail;
    public String sendSms;
    public String webaddress;
    public Object stateId;
    public String transferFlag;
    public Object zip;
    public Object patientId;
    public int status;
    public String fax;
    public int id;
    public Object guarantorId;
    public int guarantorTypeId;
    public String primaryGuarantor;
}

