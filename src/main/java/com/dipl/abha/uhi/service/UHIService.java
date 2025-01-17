package com.dipl.abha.uhi.service;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.repositories.AbdmRequstMappingRepository;
import com.dipl.abha.service.AllAbdmCentralDbSave;
import com.dipl.abha.uhi.dto.ResponseDto;
import com.dipl.abha.uhi.dto.SearchByDoctorSpeciality;
import com.dipl.abha.util.ConstantUtil;
import com.dipl.abha.util.Crypt;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UHIService {

	@Autowired
	private RestTemplate template;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private Crypt crypt;

	@Value("${SEARCH}")
	private String search;

	@Value("${SElECT}")
	private String select;

	@Value("${UHI_INIT}")
	private String init;

	@Value("${UHI_CONFIRM}")
	private String confirm;

	@Value("${UHI_CANCEL}")
	private String cancel;

	@Value("${UHI_STATUS}")
	private String status;

	@Value("${EUA_SUBSCRIBER_ID}")
	private String subscriberId;

	@Value("${EUA_PUBLIC_KEY_ID}")
	private String pubKeyId;

	@Autowired
	private AbdmRequstMappingRepository abdmRequstMappingRepository;

	@Autowired
	private AllAbdmCentralDbSave abdmCentralDbSave;

	public ResponseEntity<?> searchByDoctor(String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
			SearchByDoctorSpeciality doctorSpeciality = objectMapper.readValue(request, SearchByDoctorSpeciality.class);
			if(doctorSpeciality.getContext().getTransaction_id() == null) {
				doctorSpeciality.getContext().setTransaction_id(String.valueOf(UUID.randomUUID()));
			}
			if(doctorSpeciality.getContext().getMessage_id() == null) {
				doctorSpeciality.getContext().setMessage_id(String.valueOf(UUID.randomUUID()));
			}
			abdmCentralDbSave.saveEuaRequest("/search", doctorSpeciality.getContext().getTransaction_id(), null, null, request);
			abdmCentralDbSave.saveAbhaRequestMapping(doctorSpeciality.getContext().getTransaction_id(),
					Integer.parseInt(TenantContext.getCurrentTenant()), httpServletRequest.getRequestURI(), 1, request);
			System.out.println("jsonString===========" + request.toString());
			return this.checkRestCallSuccessOrNot(this
					.restCallApi(crypt.generateAuthorizationParams(request, subscriberId, pubKeyId), request, search));

		} catch (Exception e) {
			log.error("EUA SEARCH method error: " + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}
	}

	public ResponseEntity<?> checkRestCallSuccessOrNot(Object responseObject) {
		ResponseBean bean = new ResponseBean();
		try {
			log.info("===========ENTERING INTO THE REST CALL SUCESS OR NOT==================");
			JSONObject jsonObject = new JSONObject(responseObject);
			System.out.println("jsonData===========" + jsonObject);
			if (jsonObject.getInt("statusCodeValue") == 200) {
				bean.setMessage(null);
				bean.setStatus(HttpStatus.OK);
				bean.setMessage(ConstantUtil.SUCCESS);
			} else {
				String jsonMessagedata = jsonObject.getJSONObject("body").getString("data").toString();
				ResponseDto dto = objectMapper.readValue(jsonMessagedata, ResponseDto.class);
				log.info("dto =====> {}",  dto);
				switch (jsonObject.getInt("statusCodeValue")) {

				case 400:
					bean.setMessage(null);
					bean.setStatus(HttpStatus.BAD_REQUEST);
					bean.setMessage(dto.getError().getMessage());
					break;

				case 500:
					bean.setMessage(null);
					bean.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
					bean.setMessage(jsonMessagedata);
					break;

				default:
					break;
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(bean, bean.getStatus());

	}

	public ResponseEntity<?> restCallApi(String generateAuthorizationParams, Object commonObject, String url) {
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		HttpEntity<String> entity = null;
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<String> postForEntity = null;
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);
			if (generateAuthorizationParams != null) {
				headers.set(ConstantUtil.AUTHORIZATION, generateAuthorizationParams);
				System.out.println("header====>" + generateAuthorizationParams);
			}
			entity = new HttpEntity<>(commonObject.toString(), headers);
			System.out.println(entity);
			postForEntity = template.postForEntity(url, entity, String.class);
			bean.setMessage(postForEntity.getBody());
			bean.setStatus(postForEntity.getStatusCode());
//			
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			bean.setData(e.getResponseBodyAsString());
			bean.setStatus(e.getStatusCode());
		} catch (HttpServerErrorException e) {
			e.printStackTrace();
			bean.setData(e.getResponseBodyAsString());
			bean.setStatus(e.getStatusCode());
		} catch (RestClientException e) {
			e.printStackTrace();
			bean.setData("An error occurred while calling the API: " + e.getMessage());
			bean.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return new ResponseEntity<>(bean, bean.getStatus());
	}

}
