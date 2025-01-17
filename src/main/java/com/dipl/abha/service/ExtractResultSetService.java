package com.dipl.abha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dipl.abha.dto.NotifyResultSetDto;
import com.dipl.abha.dto.PatientIdDto;
import com.dipl.abha.dto.RequestResultSetDto;
import com.dipl.abha.dto.RequestResultSetDtoNew;
import com.dipl.abha.repositories.ExecuteDynamicQueryRepository;
import com.dipl.abha.uhi.dto.DoctorDetailsSlotsDto;
import com.dipl.abha.uhi.dto.HprDoctorMappingDto;

@Service
public class ExtractResultSetService {

	@Autowired
	private ExecuteDynamicQueryRepository customRepository;

	public List<RequestResultSetDto> executeDynamicQueryFromDBForRequest(String dynamicQuery) {
		return customRepository.executeDynamicQueryForRequest(dynamicQuery);
	}
	
	public List<RequestResultSetDtoNew> executeDynamicQueryFromDBForRequestNew(String dynamicQuery) {
		System.out.println(dynamicQuery);
		return customRepository.executeDynamicQueryForRequestNew(dynamicQuery);
	}

	public List<NotifyResultSetDto> executeDynamicQueryFromDBForNotify(String dynamicQuery) {
		return customRepository.executeDynamicQueryForNotify(dynamicQuery);
	}

	public void executeUpdate(String dynamicQuery) {
		customRepository.executeDynamicQuery(dynamicQuery);
	}

	public PatientIdDto executePatientIdQuery(String dynamicQuery) {
		return customRepository.executeDynamicQueryForPatientId(dynamicQuery);
	}
	
	public List<HprDoctorMappingDto> excuteDynamicQueryForHprIdDetails(String dynamicQuery) {
		return customRepository.excuteDynamicQueryForHprIdDetails(dynamicQuery);
	}

	public List<DoctorDetailsSlotsDto> excuteDynamicQueryForDoctorDetailsSlots(String dynamicQuery) {
		// TODO Auto-generated method stub
		return customRepository.excuteDynamicQueryForDoctorDetails(dynamicQuery);
	}
}
