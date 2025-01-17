package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BiometricPaylod {
private String aadhaarNumber;
private String fingerPrintAuthPidl;
private String mobileNumber;
}
