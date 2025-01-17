package com.dipl.abha.patient.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateLinkTokenPayload {
	private String requestId;
	public String abhaNumber;
	public String abhaAddress;
	public String name;
	public String gender;
	public int yearOfBirth;
}
