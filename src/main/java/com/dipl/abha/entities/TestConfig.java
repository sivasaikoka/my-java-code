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
@Table(name = "test_config")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class TestConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	@Column(name = "test_type_id")
	private Integer testTypeId;

	@Column(name = "sample_type_id")
	private Integer sampleTypeId;

	@Column(name = "result_type")
	private String resultType;

	@Column(name = "component_name")
	private String componentName;

	@Column(name = "gender")
	private String gender;

	@Column(name = "age")
	private String age;

	@Column(name = "from_age")
	private Integer fromAge;

	@Column(name = "to_age")
	private Integer toAge;

	@Column(name = "units")
	private String units;

	@Column(name = "normal_value")
	private String normalValue;

	@Column(name = "min_value")
	private String minValue;

	@Column(name = "max_value")
	private String maxValue;

	@Column(name = "normal_ref_range")
	private String normalRefRange;

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
	private String testTypeName;
	
	@Transient
	private String sampleTypeName;

}
