package com.dipl.abha.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "case_registration")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class CaseRegistrationEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	@Column(name = "case_no")
	private String caseNo;

	@Column(name = "abha_no")
	private String abhaNo;
	
	@Column(name = "abha_address")
	private String abhaAddress;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "dob")
	private LocalDate dateOfBirth;

	@Column(name = "sex")
	private String sex;
	
	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "identification_type_id")
	private Integer identificationTypeId;

	@Column(name = "identification_type_no")
	private String identificationTypeNo;

	@Column(name = "family_id")
	private String familyId;

	@Column(name = "family_head")
	private Boolean familyHead;
	
	@Column(name = "relation_id")
	private Integer relationId;

	@Column(name = "case_type_id")
	private Integer caseTypeId;

	@Column(name = "source_id")
	private Integer sourceId;

	@Column(name = "platform_id")
	private Integer platformId;

	@Column(name = "state_id")
	private Integer stateId;

	@Column(name = "division_id")
	private Integer divisionId;

	@Column(name = "district_id")
	private Integer districtId;

	@Column(name = "tehsil_id")
	private Integer tehsilId;

	@Column(name = "block_id")
	private Integer blockId;
	
	@Column(name = "facility_id")
	private Integer facilityId;
	
	@Column(name = "village_id")
	private Integer villageId;

	@Column(name = "village_tola")
	private String villageTola;

	@Column(name = "house_no")
	private String houseNo;

	@Column(name = "address")
	private String address;

	@Column(name = "landmark")
	private String landmark;

	@Column(name = "pincode")
	private Integer pincode;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "latitude")
	private Double latitude;
	
	
	@Transient
	@JsonProperty(value =  "clinicDetails")
	private ClinicalDetails clinicDetails;
	
	@Transient
	@JsonProperty(value =  "sampleCollection")
	private List<SampleCollection> sampleCollection;

	@Transient
	private String stateName;
	
	@Transient
	private String divisionName;
	
	@Transient
	private String districtName;
	
	@Transient
	private String tehsilName;
	
	@Transient
	private String blockName;
	
	@Transient
	private String villageName;

}
