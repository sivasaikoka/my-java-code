package com.dipl.abha.uhi.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDetailsSlotsDto {
	
	@Column(name="doctor_name")
	@JsonProperty("doctor_name")
	private String doctorName;
	
	@JsonProperty("doctor_id")
	@Column(name="doctor_id")
	private Long doctorId;
	
	@JsonProperty("from_date")
	@Column(name="from_date")
	private Date fromDate;
	
	@JsonProperty("to_date")
	@Column(name="to_date")
	private Date toDate;
	
	@JsonProperty("available_slot_id")
	@Column(name="available_slot_id")
	private String availableSlotId;
	
	@JsonProperty("time_slot")
	@Column(name="time_slot")
	private String timeSlot; 
	
	@JsonProperty("hpr_id")
	@Column(name="hpr_id")
	private String hprId;
	
}
