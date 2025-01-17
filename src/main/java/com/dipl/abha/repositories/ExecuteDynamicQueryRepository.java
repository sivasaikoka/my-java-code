package com.dipl.abha.repositories;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.dipl.abha.dto.NotifyResultSetDto;
import com.dipl.abha.dto.PatientIdDto;
import com.dipl.abha.dto.RequestResultSetDto;
import com.dipl.abha.dto.RequestResultSetDtoNew;
import com.dipl.abha.uhi.dto.DoctorDetailsSlotsDto;
import com.dipl.abha.uhi.dto.HprDoctorMappingDto;

@Repository
public class ExecuteDynamicQueryRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<RequestResultSetDto> executeDynamicQueryForRequest(String dynamicQuery) {
		try {
			Query query = entityManager.createNativeQuery(dynamicQuery, "RequestResultSetDtoMapping");
			return query.getResultList();
		} catch (EmptyResultDataAccessException eRDE) {
			return Collections.emptyList();
		}
	}
	
	public List<RequestResultSetDtoNew> executeDynamicQueryForRequestNew(String dynamicQuery) {
		try {
			Query query = entityManager.createNativeQuery(dynamicQuery, "RequestResultSetDtoNewMapping");
			return query.getResultList();
		} catch (EmptyResultDataAccessException eRDE) {
			return Collections.emptyList();
		}
	}


	public List<NotifyResultSetDto> executeDynamicQueryForNotify(String dynamicQuery) {
		try {
			Query query = entityManager.createNativeQuery(dynamicQuery, "NotifyResultSetDtoMapping");
			return query.getResultList();
		} catch (EmptyResultDataAccessException eRDE) {
			return Collections.emptyList();
		}

	}

	public List<NotifyResultSetDto> executeDynamicQueryForAbhaRegistration(String dynamicQuery) {
		try {
			Query query = entityManager.createNativeQuery(dynamicQuery, "AbhaRegistrationDtoMapping");
			return query.getResultList();
		} catch (EmptyResultDataAccessException eRDE) {
			return Collections.emptyList();
		}
	}
	
	public PatientIdDto executeDynamicQueryForPatientId(String dynamicQuery) {
		try {
			Query query = entityManager.createNativeQuery(dynamicQuery, "PatientIdDtoMapping");
			return (PatientIdDto) query.getSingleResult();
		} catch (EmptyResultDataAccessException eRDE) {
			return null;
		}
	}

	public void executeDynamicQuery(String dynamicQuery) {
//		EntityTransaction transaction = entityManager.getTransaction();
//	    transaction.begin();
//		Query query = entityManager.createNativeQuery(dynamicQuery,"AbhaRegistrationDtoMapping");
//		query.executeUpdate();
//	    transaction.commit();
		String dynamicQuery1 = "update beneficiary_medical_record set is_care_context_linked = true where patient_interaction_id = :interactionId";
		Query query = entityManager.createNativeQuery(dynamicQuery1, "AbhaRegistrationDtoMapping");
		query.setParameter("interactionId", "14244");

		int updatedRows = query.executeUpdate();
		System.out.println("Number of rows updated: " + updatedRows);
	}

	public List<HprDoctorMappingDto> excuteDynamicQueryForHprIdDetails(String dynamicQuery) {
		try {
			System.out.println("dynamicQuery " +dynamicQuery);
			Query query = entityManager.createNativeQuery(dynamicQuery,"HprDoctorMappingDetailsDto");
//			System.out.println(" query.getResultList();=---------"+ query.getResultList().toString());
			return query.getResultList();
		} catch (EmptyResultDataAccessException eRDE) {
			return Collections.emptyList();
		}

	}

	public List<DoctorDetailsSlotsDto> excuteDynamicQueryForDoctorDetails(String dynamicQuery) {
		try {
			System.out.println("dynamicQuery " +dynamicQuery);
			Query query = entityManager.createNativeQuery(dynamicQuery,"DoctorDetailsMappingSlotsDto");
//			System.out.println(" query.getResultList();=---------"+ query.getResultList().toString());
			return query.getResultList();
		} catch (EmptyResultDataAccessException eRDE) {
			return Collections.emptyList();
		}
	
	

	}
	}
