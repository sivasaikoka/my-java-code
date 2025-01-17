package com.dipl.abha.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clinical_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class ClinicalDetails extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "case_id")
	private Long caseId;

	@Column(name = "provisional_diagnosis_id")
	private Integer provisionalDiagnosisId;

	@Column(name = "onset_date")
	private Date onsetDate;
	
	@Column(name = "opd_ipd")
	private String opdIpd;
	
	@Column(name = "advised_lab")
	private Boolean advisedLab;
	
	@Column(name = "first_visit_date")
	private Date firstVisitDate;
	
	@Column(name = "visit_type_id")
	private Long visitType;
	
	@Column(name = "source_id")
	private Integer sourceId;
	
	@Column(name = "survey_id")
	private Integer surveyId;

	@Column(name = "platform_id")
	private Integer platformId;
	

}