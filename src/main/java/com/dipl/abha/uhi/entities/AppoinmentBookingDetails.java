package com.dipl.abha.uhi.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="appoinments_booking_details")
@Entity
public class AppoinmentBookingDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@Column(name="transaction_id")
	private String transactionId;
	
	@Column(name="order_id")
	private String orderId;
	
	@Column(name="doctor_hpr_id")
	private String doctorHprId;
	
	@Column(name="patient_abha_id")
	private String patientAbhaId;
	
	@Column(name="consulation_status")
	private String consulationStatus;
	
	@Column(name="cancel_by")
	private String cancelBy;
	
	@Column(name="created_on")
	private Date created_on;

}
