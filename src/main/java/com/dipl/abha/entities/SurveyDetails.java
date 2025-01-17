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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "survey_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class SurveyDetails extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	@Column(name = "campaign_id")
	private Long campaignId;

	@Column(name = "case_Id")
	private Long caseId;

	@Column(name = "incident_no")
	private Long incidentNo;

	@Column(name = "question_id")
	private Long questionId;

	@Column(name = "answer")
	private String answer;

	@Column(name = "from_date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date fromDate;

	@Column(name = "other_symptoms")
	private String otherSymptoms;

	@Column(name = "comments")
	private String comments;


}
