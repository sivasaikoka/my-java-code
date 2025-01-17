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
@Table(name = "sample_result")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class SampleResult implements Serializable {
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	@Column(name = "sample_collection_id")
	private Long sampleCollectionId;
	

	@Column(name = "test_config_id")
	private Integer testConfigId;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "test_conducted_date")
	private Date testConductedDate;
	
	@Column(name = "result_name")
	private String resultName;

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
	private TestConfig testConfig;

}