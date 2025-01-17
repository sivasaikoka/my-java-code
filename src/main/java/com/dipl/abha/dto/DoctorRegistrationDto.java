package com.dipl.abha.dto;

import org.springframework.beans.factory.annotation.Value;

public interface DoctorRegistrationDto {

	@Value("#{target.id}")
	Long getId();

	@Value("#{target.doctor_name}")
	String getDoctorName();

	@Value("#{target.mobile}")
	String getMobile();

	@Value("#{target.email}")
	String getEmail();

	@Value("#{target.years_experience}")
	Integer getExperience();

}
