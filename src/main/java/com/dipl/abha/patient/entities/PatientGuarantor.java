package com.dipl.abha.patient.entities;

import java.io.Serializable;

import javax.persistence.Basic;
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
 * The persistent class for the patient_guarantor database table.
 * 
 */
@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient_guarantor")
public class PatientGuarantor extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "guarantor_id")
	private Long id;

	private String address;

	@Column(name = "guarantor_name")
	private String guarantorName;

	@Column(name = "guarantor_relationshipid")
	private Long guarantorRelationshipid;

	@Column(name = "transfer_flag")
	private String transferFlag;

//	@ManyToMany(mappedBy = "patientGuarantor")
//	private List<PatientDemography> patientDemography;

}