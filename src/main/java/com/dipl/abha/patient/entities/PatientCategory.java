package com.dipl.abha.patient.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the patient_category database table.
 * 
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient_category")
public class PatientCategory extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "patient_category_id")
	private Long id;

	private String filecharges;

	@Column(name = "filecharges_amount")
	private BigDecimal filechargesAmount;

	@Column(name = "org_id")
	private Long orgId;

	@Column(name = "patient_category_desc")
	private String patientCategoryDesc;

	@Column(name = "patient_category_type")
	private long patientCategoryType;

//	@ManyToOne
//	@JoinColumn(referencedColumnName = "pt_id" , name = "paymenttype_id")
	@Column(name="paymenttype_id")
	private Integer paymenttypeId;

	@Column(name = "registration_fees")
	private String registrationFees;

	@Column(name = "registration_fees_amount") 
	private BigDecimal registrationFeesAmount;

	@Column(name = "transfer_flag")
	private String transferFlag;

	private LocalDate validupto;

}