package com.dipl.abha.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.dto.AbhaRegistrationDTO;
import com.dipl.abha.dto.AccountProfile;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.service.AllAbdmCentralDbSave;
import com.dipl.abha.util.ConstantUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/central")
@Slf4j
@CrossOrigin
public class CentralDbSaveController {

	@Autowired
	private AllAbdmCentralDbSave abdmCentralDbSave;

	@Autowired
	private ObjectMapper mapper;


	@PostMapping("/save-in-central-db")
	private ResponseEntity<?> saveCentralDbSave(@RequestBody String profile) {
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		bean.setStatus(HttpStatus.OK);
		try {
			if (profile == null || profile.isEmpty()) {
				bean.setData(null);
				bean.setStatus(HttpStatus.BAD_REQUEST);
				bean.setMessage("Invalid Payload");
				return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
			}
			this.processDataAndStoreInCentralDb(mapper.readValue(profile, AccountProfile.class));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("===================ERROR IN USER AUTH CONFIRM=================" + e.getMessage());
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
		}
		return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
	}

	@PostMapping("/save-abha-registration")
	private ResponseEntity<?> saveAbhaRegistration(@RequestBody AbhaRegistrationDTO profile) {
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		bean.setMessage("Saved in central DB..");
		bean.setStatus(HttpStatus.OK);
		try {
			this.processDataAndStoreInCentralDbNew(mapper.readValue(profile.getAbha_profile(), AccountProfile.class),
					profile.getPatient_id());
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error in ABDM Central Save" + e.getMessage());
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
		}
		return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
	}

	@GetMapping("update-patient-id/{patientId}/{abhaNo}/{abhaAddress}")
	private ResponseEntity<?> updatePatientId(@PathVariable Long patientId, @PathVariable String abhaNo,
			@PathVariable String abhaAddress) {
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		bean.setStatus(HttpStatus.OK);
		Integer tenantId = Integer.parseInt(TenantContext.getCurrentTenant());
		abdmCentralDbSave.updatePatientId(tenantId, abhaNo, patientId);
		return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
	}

	@GetMapping("save-hip-mapping/{hipCode}")
	private ResponseEntity<?> insertHIPMapping(@PathVariable String hipCode) {
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		bean.setStatus(HttpStatus.OK);
		bean.setMessage("HIP MAPPING SAVED SUCCESSFULLY");
		Integer tenantId = Integer.parseInt(TenantContext.getCurrentTenant());
		abdmCentralDbSave.insertHipTenantMapping(tenantId, hipCode);
		return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
	}

	private void processDataAndStoreInCentralDb(AccountProfile profile)
			throws NumberFormatException, JsonProcessingException {
		abdmCentralDbSave.saveAbhaRegistration(Integer.parseInt(TenantContext.getCurrentTenant()), "ABHA Registration",
				profile.getHealthIdNumber(), profile.getHealthId(), mapper.writeValueAsString(profile), 1L,
				profile.getGender(), Integer.parseInt(profile.getYearOfBirth()), profile.getMobile(), 1,
				profile.getName());

	}

	private void processDataAndStoreInCentralDbNew(AccountProfile profile, Long patientId)
			throws NumberFormatException, JsonProcessingException {
		abdmCentralDbSave.saveAbhaRegistration(Integer.parseInt(TenantContext.getCurrentTenant()), "ABHA Registration",
				profile.getHealthIdNumber(), profile.getHealthId(), mapper.writeValueAsString(profile), patientId,
				profile.getGender(), Integer.parseInt(profile.getYearOfBirth()), profile.getMobile(), 1,
				profile.getName());

	}

}
