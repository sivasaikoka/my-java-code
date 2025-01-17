package com.dipl.abha.uhi.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HprDoctorMappingDto {
	
	
	@JsonProperty("language_id")
	@Column(name="language_id")
	private Integer language_id;
	
	@JsonProperty("language_name")
	@Column(name="language_name")
	private String language_name;

	
	@JsonProperty("doctor_id")
	@Column(name="doctor_id")
	private Long doctorId;
	
	@JsonProperty("doctor_name")
	@Column(name="doctor_name")
	private String doctorName;
	
	@JsonProperty("hpr_id")
	@Column(name="hpr_id")
	private String hprId;
	
	@JsonProperty("birth_date")
	@Column(name="birth_date")
	private Date birthDate;
	
	@JsonProperty("age")
	@Column(name="age")
	private Integer age;
	
	@JsonProperty("from_date")
	@Column(name="from_date")
	private Date fromDate;
	
	@JsonProperty("to_date")
	@Column(name="to_date")
	private Date toDate;
	
	
	@JsonProperty("gender")
	@Column(name="gender")
	private String gender;
	
	@JsonProperty("mobile")
	@Column(name="mobile")
	private String mobile;

	@JsonProperty("mci_number")
	@Column(name="mci_number")
	private String mci_number;
	
	@JsonProperty("qualification_id")
	@Column(name="qualification_id")
	private Integer qualificationId;
	
	@JsonProperty("qualification")
	@Column(name="qualification")
	private String qualification;
	
	@JsonProperty("specialization_id")
	@Column(name="specialization_id")
	private Integer specializationId;
	
	@JsonProperty("department_name")
	@Column(name="department_name")
	private String departmentName;
	
	
	@JsonProperty("years_experience")
	@Column(name="years_experience")
	private Integer yearsExperience;
	
	@JsonProperty("audio_consultation_price")
	@Column(name="audio_consultation_price")
	private Long audioConsultationPrice;
	
	@JsonProperty("video_consultation_price")
	@Column(name="video_consultation_price")
	private Long videoConsultationPrice;
	
	
	
	@JsonProperty("state_id")
	@Column(name="state_id")
	private Integer stateId;
	
	@JsonProperty("state_name")
	@Column(name="state_name")
	private String stateName;
	
	@JsonProperty("district_id")
	@Column(name="district_id")
	private Integer districtId;
	
	@JsonProperty("district_name")
	@Column(name="district_name")
	private String districtName;
	
	@JsonProperty("document_path")
	@Column(name="document_path")
	private String documentPath;
	
	@JsonProperty("hospital_id")
	@Column(name="hospital_id")
	private Integer hospitalId;
	
	@JsonProperty("hospital_name")
	@Column(name="hospital_name")
	private String hospitalName;
	
	
	@JsonProperty("country")
	@Column(name="country")
	private String country;
	
	@JsonProperty("country_code")
	@Column(name="country_code")
	private String countryCode;
	
	@JsonProperty("city_id")
	@Column(name="city_id")
	private Integer cityId;
	
	@JsonProperty("city_name")
	@Column(name="city_name")
	private String cityName;
	
	@JsonProperty("city_code")
	@Column(name="city_code")
	private String cityCode;
	
	@JsonProperty("hfr_id")
	@Column(name="hfr_id")
	private String hfrId;
	
	@JsonProperty("hspa_long_desc")
	@Column(name="hspa_long_desc")
	private String hspaLongDesc;
	
	@JsonProperty("hspa_name")
	@Column(name="hspa_name")
	private String hspaName;
	
}
