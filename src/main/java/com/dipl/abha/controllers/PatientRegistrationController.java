//package com.dipl.abha.controllers;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.dipl.abha.dto.ResponseBean;
//import com.dipl.abha.service.PatientRegistrationService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/registration")
//@Slf4j
//public class PatientRegistrationController {
//
//	@Autowired
//	private PatientRegistrationService patientRegistrationService;
//	@Autowired
//	private PayloadValidator validator;
//	@Autowired
//	private ObjectMapper mapper;
//
//
//	@PostMapping(value = "/")
//	@ApiOperation(value = "Admit Patient Registration Details", response = PatientRegistrationDto.class)
//	public ResponseEntity<ResponseBean> admitPatient(@RequestParam(value = "registration") String registration,
//			@RequestParam(value = "profilePic", required = false) MultipartFile profilePic)
//			throws JsonMappingException, JsonProcessingException {
//		ResponseBean bean = new ResponseBean();
//		log.debug("user registration apis request payload  : {}", registration);
//		PatientRegistrationDto patientRegistrationPayload = mapper.readValue(registration,
//				PatientRegistrationDto.class);
//		if (patientRegistrationPayload.getIsEmergency().equalsIgnoreCase("N")) {
//			if (profilePic != null)
//				ExtensionUtil.validateDocument(profilePic);
//			validator.validateFields(patientRegistrationPayload);
//		}
//		String id = patientRegistrationService.admitPatient(patientRegistrationPayload, profilePic);
//		ApiResponse response = new ApiResponse(patientRegistrationPayload.getId(), id);
//		return ResponseEntity.ok(response);
//	}
//
//}
