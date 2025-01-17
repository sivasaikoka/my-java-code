package com.dipl.abha.uhi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.others.PayloadException;
import com.dipl.abha.others.PayloadValidator;
import com.dipl.abha.uhi.dto.SearchByDoctorSpeciality;
import com.dipl.abha.uhi.service.EUAService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EUAController {
	
	
	@Autowired
	private EUAService euaService;

	@Autowired
	private PayloadValidator validator;
	
	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping("selectDoctor")
	public ResponseEntity<?> select(@RequestBody String request, BindingResult bindResult,
			HttpServletRequest httpServletRequest) throws PayloadException, Exception {
		log.info("request at ##EUA select# start: {}==========>", request);
		SearchByDoctorSpeciality doctorSpeciality = objectMapper.readValue(request, SearchByDoctorSpeciality.class);
		validator.validateFields(doctorSpeciality);
		return euaService.selectDoctor(request, httpServletRequest);
	}
	
	
	
	@PostMapping("initDoctor")
	public ResponseEntity<?> init(@RequestBody String request, BindingResult bindResult,
			HttpServletRequest httpServletRequest) throws PayloadException, Exception {
		log.info("request at ##EUA INIT# start: {}==========>", request);
		SearchByDoctorSpeciality doctorSpeciality = objectMapper.readValue(request, SearchByDoctorSpeciality.class);
		validator.validateFields(doctorSpeciality);
		return euaService.initDoctor(request, httpServletRequest);
	}
	

	@PostMapping("confirmDoctor")
	public ResponseEntity<?> confirm(@RequestBody String request, BindingResult bindResult,
			HttpServletRequest httpServletRequest) throws PayloadException, Exception {
		log.info("request at ##EUA CONFIRM# start: {}==========>", request);
		SearchByDoctorSpeciality doctorSpeciality = objectMapper.readValue(request, SearchByDoctorSpeciality.class);
		validator.validateFields(doctorSpeciality);
		return euaService.confirmDoctor(request, httpServletRequest);
	}
	

	@PostMapping("cancelDoctor")
	public ResponseEntity<?> cancel(@RequestBody String request, BindingResult bindResult,
			HttpServletRequest httpServletRequest) throws PayloadException, Exception {
		log.info("request at ##EUA CANCEL# start: {}==========>", request);
		SearchByDoctorSpeciality doctorSpeciality = objectMapper.readValue(request, SearchByDoctorSpeciality.class);
		validator.validateFields(doctorSpeciality);
		return euaService.cancelDoctor(request, httpServletRequest);
	}
	
	
	@PostMapping("statusDoctor")
	public ResponseEntity<?> status(@RequestBody String request, BindingResult bindResult,
			HttpServletRequest httpServletRequest) throws PayloadException, Exception {
		log.info("request at ##EUA STATUS# start: {}==========>", request);
		SearchByDoctorSpeciality doctorSpeciality = objectMapper.readValue(request, SearchByDoctorSpeciality.class);
		validator.validateFields(doctorSpeciality);
		return euaService.statusDoctor(request, httpServletRequest);
	}

	

}
