package com.dipl.abha.patient.entities;

import java.io.Serializable;
import java.math.BigDecimal;

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
 * The persistent class for the patient_credit_details database table.
 * 
 */
@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient_credit_details")
public class PatientCreditDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ptcrdtl_id")
	private Long id;

	@Column(name = "cmpschmember_id")
	private Long cmpschmemberId;

	@Column(name = "companyscheme_id")
	private Long companyschemeId;

	@Column(name = "credit_company_id")
	private Long creditCompanyId;

	@Column(name = "is_primary")
	private String isPrimary;

	@Column(name = "\"limit\"")
	private BigDecimal limit;

	@Column(name = "org_id")
	private Long orgId;

	@Column(name = "patient_id")
	private Long patientId;

	@Column(name = "transfer_flag")
	private String transferFlag;

}