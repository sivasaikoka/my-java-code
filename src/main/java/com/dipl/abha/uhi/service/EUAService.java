package com.dipl.abha.uhi.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.service.AllAbdmCentralDbSave;
import com.dipl.abha.uhi.dto.RequestDto;
import com.dipl.abha.util.ConstantUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EUAService {
	
	@Autowired
	private AllAbdmCentralDbSave abdmCentralDbSave;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Autowired
	private UHIService service;
	
	@Value("${EUA_SUBSCRIBER_ID}")
	private String subscriberId;

	@Value("${EUA_PUBLIC_KEY_ID}")
	private String pubKeyId;
	
	
	public ResponseEntity<?> selectDoctor(String request, HttpServletRequest httpServletRequest) {
			
			ResponseBean bean = new ResponseBean();
			try {
				log.info("======== ENTERING INTO THE EUA SELECT ======================>{}",request);
				RequestDto selectDto = objectMapper.readValue(request, RequestDto.class);
				abdmCentralDbSave.saveEuaRequest("/select", selectDto.getContext().getTransaction_id(), null, null, request);
				log.info("=====REST CALLING THE HSPA SEARCH ======================>{}",request);
				return service.checkRestCallSuccessOrNot(service
						.restCallApi(null, request, selectDto.getContext().getProvider_uri()+ "/hspa/select"));

			} catch (Exception e) {
				log.error("EUA select method error: " + e);
				bean.setStatus(HttpStatus.EXPECTATION_FAILED);
				bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
				bean.setData(e.getMessage());
				e.printStackTrace();
				return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
			}
			
	}


	public ResponseEntity<?> initDoctor(String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
			RequestDto selectDto = objectMapper.readValue(request, RequestDto.class);
			abdmCentralDbSave.saveEuaRequest("/init", selectDto.getContext().getTransaction_id(), null, null, request);
			log.info("request==========={}", request.toString());
			return service.checkRestCallSuccessOrNot(service
					.restCallApi(null, request, selectDto.getContext().getProvider_uri()+ "/hspa/init"));
		} catch (Exception e) {
			log.error("EUA init method error: " + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}


	public ResponseEntity<?> confirmDoctor(String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
			RequestDto selectDto = objectMapper.readValue(request, RequestDto.class);
			abdmCentralDbSave.saveEuaRequest("/init", selectDto.getContext().getTransaction_id(), null, null, request);
			log.info("request==========={}",request.toString());
			return service.checkRestCallSuccessOrNot(service
					.restCallApi(null, request, selectDto.getContext().getProvider_uri()+ "/hspa/confirm"));

		} catch (Exception e) {
			log.error("EUA confirm method error: " + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}


	public ResponseEntity<?> cancelDoctor(String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
			RequestDto selectDto = objectMapper.readValue(request, RequestDto.class);
			abdmCentralDbSave.saveEuaRequest("/init", selectDto.getContext().getTransaction_id(), null, null, request);
//			abdmCentralDbSave.saveAbhaRequestMapping(selectDto.getContext().getTransaction_id(),
//					Integer.parseInt(TenantContext.getCurrentTenant()), httpServletRequest.getRequestURI(), 1, request);
			String jsonString = objectMapper.writeValueAsString(request).replace(" ", "");

			System.out.println("jsonString===========" + jsonString);
			System.out.println("jsonString===========" + request.toString());
			return service.checkRestCallSuccessOrNot(service
					.restCallApi(null, request, selectDto.getContext().getProvider_uri()+ "/hspa/cancel"));

		} catch (Exception e) {
			log.error("EUA cancel method error: " + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}


	public ResponseEntity<?> statusDoctor(String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
			RequestDto selectDto = objectMapper.readValue(request, RequestDto.class);
			abdmCentralDbSave.saveEuaRequest("/init", selectDto.getContext().getTransaction_id(), null, null, request);
//			abdmCentralDbSave.saveAbhaRequestMapping(selectDto.getContext().getTransaction_id(),
//					Integer.parseInt(TenantContext.getCurrentTenant()), httpServletRequest.getRequestURI(), 1, request);
			String jsonString = objectMapper.writeValueAsString(request).replace(" ", "");

			System.out.println("jsonString===========" + jsonString);
			System.out.println("jsonString===========" + request.toString());
			return service.checkRestCallSuccessOrNot(service
					.restCallApi(null, request, selectDto.getContext().getProvider_uri()+ "/hspa/status"));

		} catch (Exception e) {
			log.error("EUA init method error: " + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

}
