package com.dipl.abha.entities;

import java.io.Serializable;

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
@Table(name = "master_facility")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class MasterFacility extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	@Column(name = "facility_name")
	private String facilityName;

	@Column(name = "facility_code")
	private String facilityCode;

	@Column(name = "hfr_code")
	private String hfrCode;

	@Column(name = "village_id")
	private Integer villageId;

	@Column(name = "block_id")
	private Integer blockId;

	@Column(name = "tehsil_id")
	private Integer tehsilId;

	@Column(name = "district_id")
	private Integer districtId;

	@Column(name = "facilitytype_id")
	private Long facilitytypeId;

	@Column(name = "facility_classification")
	private String facilityClassification;

	@Column(name = "facility_category")
	private String facilityCategory;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "hims_code")
	private Long himsCode;

	@Column(name = "hwc_status")
	private String hwcStatus;

	@Column(name = "fru_status")
	private String fruStatus;

}
