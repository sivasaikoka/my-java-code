package com.dipl.abha.uhi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.others.PayloadException;
import com.dipl.abha.others.PayloadValidator;
import com.dipl.abha.uhi.dto.SearchByDoctorSpeciality;
import com.dipl.abha.uhi.service.UHIService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("uhi")
@Slf4j
public class UHIController {

	@Autowired
	private UHIService uhiService;

	@Autowired
	private PayloadValidator validator;
	
	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping("/searchForDoctor")
	public ResponseEntity<?> searchByDoctor(@RequestBody String request, BindingResult bindResult,
			HttpServletRequest httpServletRequest) throws PayloadException, Exception {
		log.info("request at ##EUA SEARCH# start: {}==========>", request);
		SearchByDoctorSpeciality doctorSpeciality = objectMapper.readValue(request, SearchByDoctorSpeciality.class);
		validator.validateFields(doctorSpeciality);
		return uhiService.searchByDoctor(request, httpServletRequest);
	}
	

}
