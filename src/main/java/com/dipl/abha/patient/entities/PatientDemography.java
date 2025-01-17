package com.dipl.abha.patient.entities;

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

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the patient_demography database table.
 * 
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient_demography")
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
public class PatientDemography extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3675417703153271654L;
	

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "patient_id")
	private Long id;

	private Integer age;

	@Column(name = "blood_group")
	private Integer bloodGroup;

	@Column(name = "can_view_in_other_centers")
	private String canViewInOtherCenters;

	@Column(name = "casetype_id")
	private Long casetypeId;

	@Column(name = "center_id")
	private Long centerId;

	private String declared;

	@JsonProperty("dob")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;

	@Column(name = "education_id")
	private Long educationId;

	@Column(name = "employee_id")
	private Long employeeId;

	@Column(name = "famliy_size")
	private Integer famliySize;

	@Column(name = "gender_id")
	private Long genderId;

	@Column(name = "hospital_confirmation")
	private String hospitalConfirmation;

	@Column(name = "identification_card")
	private String identificationCard;

	@Column(name = "identification_type_id")
	private Long identificationTypeId;

	@Column(name = "incomegroup_id")
	private Long incomegroupId;

	@Column(name = "is_emergency")
	private String isEmergency;

	@Column(name = "is_telemedicine")
	private String isTelemedicine;

	private Long maritalstatus;

	@Column(name = "may_call_this_number")
	private String mayCallThisNumber;

	@Column(name = "may_leave_message")
	private String mayLeaveMessage;

	private String mlc;

	@Column(name = "mrn_no")
	private String mrnNo;

	private Long nationality;

	@Column(name = "nhif_cardno")
	private String nhifCardno;

	@Column(name = "occupation_id")
	private Long occupationId;

	@Column(name = "org_id")
	private Long orgId;

	@Column(name = "organ_donation_status")
	private String organDonationStatus;

	@Column(name = "patient_category_id")
	private Long patientCategoryId;

	@Column(name = "patient_first_name")
	private String patientFirstName;

	@Column(name = "patient_last_name")
	private String patientLastName;

	@Column(name = "patient_middle_name")
	private String patientMiddleName;

	@Column(name = "patient_photo")
	private byte[] patientPhoto;
	
	@Column(name = "patient_document")
	private String patientDocument;

	@Column(name = "patient_type")
	private Long patientType;

	@Column(name = "patientsource_id")
	private Long patientsourceId;

	private String patientsourcedtls;

	@Column(name = "payment_type_id")
	private Long paymentTypeId;

	@Column(name = "police_station")
	private String policeStation;

	@Column(name = "race_id")
	private Long raceId;

	@Column(name = "relationship_id")
	private Long relationshipId;

	@Column(name = "religion_id")
	private Long religionId;

	@Column(name = "salutation_id")
	private Long salutationId;

	@Column(name = "tariff_category")
	private Long tariffCategory;

	@Column(name = "transfer_flag")
	private String transferFlag;

	@Column(name = "treating_doctor_id")
	private String treatingDoctorId;

	private Long tribe;

	private String uhid;
	
	@Column(name = "national_health_id")
	private String nationalHealthId;
	
	@Column(name = "national_health_number")
	private String nationalHealthNumber;
	
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
	@Column(name = "national_health_response")
//	@JsonProperty("nationalHealthResponse")
	private JsonNode nationalHealthResponse;
	
	@Column(name = "is_abha_linked")
	private String isAbhaLinked;
	
	@Transient
	private PatientAddress patientAddress;
	
	@Transient
	private PatientGuarantorPool garAddress;
	
	@Transient
	private PatientAddress secPatAddress;
	
	@Transient
	private PatientGuarantorPool secGarAddress;
	
	@Transient
	private PatientGuarantor guarator;
	
	@Transient
	private PatientGuarantor secGuarator;
	
	@Transient
	private List<PatientCreditDetail> creditDetails;
	
}