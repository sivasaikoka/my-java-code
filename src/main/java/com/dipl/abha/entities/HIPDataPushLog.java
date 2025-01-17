package com.dipl.abha.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hip_datapush_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HIPDataPushLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "txn_code")
    private String txnCode;

    @Column(name = "data_push_fhir_bundle")
	@Type(type = "com.dipl.abha.util.JsonNodeUserType")
    private String dataPushFhirBundle;

    @Column(name = "enctyped_fhir_bundle")
    private String encryptedFhirBundle;

    @Column(name = "care_context_id")
    private String careContextId;

    @Column(name = "is_data_push_successful")
    private Boolean isDataPushSuccessFull;
}