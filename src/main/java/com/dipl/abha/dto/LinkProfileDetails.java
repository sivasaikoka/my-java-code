package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkProfileDetails{
    public String name;
    public String yearOfBirth;
    public String dayOfBirth;
    public String monthOfBirth;
    public String gender;
    public String address;
    public String stateName;
    public String districtName;
    public String transactionId;
}