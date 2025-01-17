package com.dipl.abha.patient.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the patient_guarantor_pool database table.
 * 
 */
@Entity
@Data
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient_guarantor_pool")
public class PatientGuarantorPool extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gp_id")
	private Long id;

	@Column(name = "guarantor_id")
	private Long guarantorId;

	@Column(name = "guarantor_type_id")
	private Integer guarantorTypeId;

	@Column(name = "patient_id")
	private Long patientId;

	@Column(name = "primary_guarantor")
	private String primaryGuarantor;

	@Column(name = "transfer_flag")
	private String transferFlag;
	
	@Column(name = "address_1")
	private String address1;

	@Column(name = "address_2")
	private String address2;

	@Column(name = "area_id")
	private Long areaId;

	@Column(name = "city_id")
	private Long cityId;

	@Column(name = "district_id")
	private Long districtId;

	@Column(name = "state_id")
	private Long stateId;

	@Column(name = "country_id")
	private Long countryId;

	@Column(name = "email")
	private String email;

	@Column(name = "fax")
	private String fax;

	@Column(name = "home_phone")
	private String homePhone;

	@Column(name = "mobile1")
	private String mobile1;

	@Column(name = "mobile2")
	private String mobile2;

	@Column(name = "office_phone")
	private String officePhone;

	@Column(name = "password")
	private String password;

	@Column(name = "send_email")
	private String sendEmail;

	@Column(name = "send_sms")
	private String sendSms;

	@Column(name = "webaddress")
	private String webaddress;

	@Column(name = "zip")
	private String zip;


}