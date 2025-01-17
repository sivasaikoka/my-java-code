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
@Table(name = "lab_registration")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false)
public class LabRegistration extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	 @Column(name = "lab_code")
	 private String labCode;
	 
	 @Column(name = "lab_name")
	 private String labName;
	 
	 @Column(name = "district_id")
	 private Integer districtId;
	 
	 @Column(name = "latitude")
	 private String latitude;
	 
	 @Column(name = "longitude")
	 private String longitude;
	 
	 @Column(name = "is_public")
	 private Boolean isPublic;
	 
	 @Column(name = "block_id")
	 private Integer blockId;
	 
	 @Column(name = "daily_test_capacity")
	 private Long dailyTestCapacity;
	 
	 @Column(name = "address")
	 private String address;
	 
	 @Column(name = "owner_name")
	 private String ownerName;
	 
	 @Column(name = "mobile")
	 private String mobile;
	 
	 @Column(name = "landline")
	 private String landLine;
	 
	 @Column(name = "email_id")
	 private String emailId;
	 
	 @Column(name = "state_id")
	 private Integer stateId;
	 
	 @Column(name = "lab_type_id")
	 private Integer labTypeId;
	 
	 @Column(name = "hfr_id")
	 private String hfrId;
	 
	 @Column(name = "hpr_id")
	 private Long hprId;
	 
	 @Column(name= "nabl_icmr_reg")
	 private Boolean nablIcmrReg;
	 
	 @Column(name = "empanelment_number")
	 private String empanelmentNumber;
	 
	 @Column(name = "cmo_reg")
	 private Boolean cmoReg;
	 
	 @Column(name = "cmo_reg_no")
	 private String cmoRegNo;
	 
	 @Column(name = "location_type")
	 private String locationType;
	
}