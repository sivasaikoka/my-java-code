package com.dipl.abha.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sample_collection")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class SampleCollection implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	@Column(name = "campaign_id")
	private Long campaignId;

	@Column(name = "incident_no")
	private Long incidentNo;

	@Column(name = "disease_id")
	private Integer diseaseId;

	@Column(name = "case_id")
	private Long caseId;

	@Column(name = "sample_type_id")
	private Long sampleTypeId;

	@Column(name = "specimen_code")
	private String specimenCode;

	@Column(name = "test_type_id")
	private Integer testTypeId;

	@Column(name = "visit_type_id")
	private Long visitTypeId;

	@Column(name = "source_id")
	private Integer sourceId;

	@Column(name = "platform_id")
	private Integer platformId;

	@Column(name = "sample_collected_date")
	private Date sampleCollectedDate;

	@Column(name = "self_conducted_test")
	private Boolean selfConductedTest;

	@Column(name = "lab_id")
	private Long labId;

	@Column(name = "uploaded_test_result")
	private Boolean uploadedTestResult;

	@Column(name = "required_facility_allocation")
	private Boolean requiredFacilityAllocation;

	@Column(name = "updated_on")
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date updatedOn;

	@Column(name = "created_on", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdOn;

	@Column(name = "updated_by")
	private Long updatedBy;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "is_deleted")
	@Builder.Default
	private Boolean isDeleted = false;

	@Transient
	private List<SampleResult> sampleCollectionResult;
	
	@Transient
	private String sampleTypeName;
	
	@Transient
	private String testTypeName;
	

}