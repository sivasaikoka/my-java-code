package com.dipl.abha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotVerifyMobileOtp {
	private int dayOfBirth;
    private String firstName;
    private String gender;
    private String lastName;
    private String middleName;
    private String monthOfBirth;
    private String name;
    private int otp;
    private String status;
    private String txnId;
    private int yearOfBirth;
}
