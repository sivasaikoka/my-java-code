package com.dipl.abha.patient.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientDemographics{
    public int age;
    public String bloodGroup;
    public String canViewInOtherCenters;
    public Object casetypeId;
    public String declared;
    public String dob;
    public Object educationId;
    public Object employeeId;
    public int id;
    public int referral_id;
    public Object famliySize;
    public int genderId;
    public String hospitalConfirmation;
    public String mlc;
    public String identificationCard;
    public Object identificationTypeId;
    public Object incomegroupId;
    public String isEmergency;
    public String isTelemedicine;
    public int maritalstatus;
    public String mayCallThisNumber;
    public String mayLeaveMessage;
    public String mrnNo;
    public Object nationality;
    public Object nhifCardno;
    public Object raceId;
    public Object occupationId;
    public String organDonationStatus;
    public Object patientCategoryId;
    public String patientFirstName;
    public String patientLastName;
    public String patientMiddleName;
    public int patientType;
    public Object patientsourceId;
    public String patientsourcedtls;
    public int paymentTypeId;
    public String policeStation;
    public int relationshipId;
    public Object religionId;
    public Object salutationId;
    public int status;
    public String transferFlag;
    public long tariffCategory;
    public Object treatingDoctorId;
    public Object tribe;
    public Object uhid;
    public String patientDocument;
    public PatientAddress patientAddress;
    public GarAddress garAddress;
    public Guarator guarator;
    public SecPatAddress secPatAddress;
    public String nationalHealthId; 
    public String  nationalHealthNumber;
    public SecGarAddress secGarAddress;
    public SecGuarator secGuarator;
    public List<Object> creditDetails;
}

