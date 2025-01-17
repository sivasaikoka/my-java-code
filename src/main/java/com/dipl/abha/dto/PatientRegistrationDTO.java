package com.dipl.abha.dto;

public interface PatientRegistrationDTO {

	Long getPatient_id();

	String getMrn_no();

	String getMobile_no();

	int getGender_id();

	Integer getYear_of_birth();

	String getNational_health_id();

	String getNational_health_number();
	
	String getFirst_name();
	
	String getMiddle_name();
	
	String getLast_name();
	
	Integer getIs_abha_Linked();
	
	Long getReferral_id();
	

}
