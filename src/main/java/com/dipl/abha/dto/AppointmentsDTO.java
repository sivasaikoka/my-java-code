package com.dipl.abha.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Value;

public interface AppointmentsDTO {

	@Value("#{target.id}")
	Long getId();
	@Value("#{target.first_name}")
	String getFirstName();
	@Value("#{target.last_name}")
	String getLastName();
	@Value("#{target.email}")
	public String getEmail();
	@Value("#{target.mobile}")
	Long getMobile();
	@Value("#{target.appt_date}")
	LocalDateTime getApptDate();
	@Value("#{target.start_time}")
	LocalTime getStartTime();

}
