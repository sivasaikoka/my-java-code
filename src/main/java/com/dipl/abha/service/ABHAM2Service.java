package com.dipl.abha.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.dipl.abha.dto.AbhaQueryTable;
import com.dipl.abha.dto.AccessToken;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.entities.ABDMRequestMapping;
import com.dipl.abha.repositories.AbdmRequstMappingRepository;
import com.dipl.abha.util.ConstantUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ABHAM2Service {

	@Value("${CONSENT_MANAGER_ID}")
	private String cmId;
	@Autowired
	private com.dipl.abha.repositories.PatientRegistrationRepository patientRegistrationRepository;
	@Autowired
	private RestTemplate template;

	@Autowired
	private Base64Genarate base64Genrator;

	@Autowired
	private AbdmRequstMappingRepository abdmRequestMapping;

	@Value("${VMED_URL}")
	private String vmedUrl;

	@Autowired
	private ObjectMapper objMapper;

	private static final Logger LOGGER = LoggerFactory.getLogger(ABHAM2Service.class);

	public ResponseEntity<ResponseBean> restCallApi(String payload, String url, String apiType, String requestId,
			String hipId, String linkToken) {
		ResponseBean bean = new ResponseBean();
		ResponseEntity<String> postForEntity = null;
		try {
			if (base64Genrator.generateToken() == null) {
				bean.setData(null);
				bean.setMessage(ConstantUtil.ABHA_SEVER_ISSUE_ERROR_MESSAGE);
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
			}
			AccessToken convertValue = base64Genrator.generateToken();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + convertValue.getAccessToken());
			headers.set("X-CM-ID", cmId);
			headers.set("Accept", "*/*");
			String requestIdNew = UUID.randomUUID().toString();
			if ("V3-LINK-TOKEN".equals(apiType)) {
				headers.set("REQUEST-ID", requestId);
				headers.set("TIMESTAMP", ConstantUtil.utcTimeStamp());
				headers.set("X-HIP-ID", hipId);
			}
			if ("LINK-CARE-CONTEXT-V3".equals(apiType)) {
				headers.set("TIMESTAMP", ConstantUtil.utcTimeStamp());
				headers.set("x-hip-id", hipId);
				headers.set("x-link-token", linkToken);
				headers.set("request-id", requestId);
			}
			if ("ON-DISCOVER-V3".equals(apiType) || "LINKS-ON-CONFIRM-V3".equals(apiType) || "ON-NOTIFY".equals(apiType)
					|| "HEALTH-INFORMATION-NOTIFY".equals(apiType)
					|| "HEALTH-INFORMATION-ON-REQUEST-V3".equals(apiType)) {
				headers.set("TIMESTAMP", ConstantUtil.utcTimeStamp());
				headers.set("REQUEST-ID", requestIdNew);
			}

			if ("LINKS-ON-INIT-V3".equals(apiType)) {
				headers.set("X-HIU-ID", linkToken);
				headers.set("TIMESTAMP", ConstantUtil.utcTimeStamp());
				headers.set("REQUEST-ID", requestId);

			}

			LOGGER.info("restCallApi() method " + apiType + " " + payload);
			LOGGER.info("ABDM API CALLING =========>{},========> {},========> {}, =======>{}", url, apiType, payload,
					headers);
			HttpEntity<String> entity = new HttpEntity<>(payload, headers);
			postForEntity = template.postForEntity(url, entity, String.class);
			bean.setData(null);
			bean.setStatus(postForEntity.getStatusCode());
			LOGGER.info("restCallApi() method response " + postForEntity.getBody());
		} catch (HttpClientErrorException exception) {
			LOGGER.error("restCallApi() method error HttpClientErrorException: " + exception.getResponseBodyAsString());
			exception.printStackTrace();
			bean.setData(null);
			bean.setMessage(exception.getResponseBodyAsString());
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		} catch (HttpServerErrorException exception) {
			LOGGER.error("restCallApi() method error HttpServerErrorException: " + exception.getResponseBodyAsString());
			exception.printStackTrace();
			bean.setData(null);
			bean.setMessage(exception.getResponseBodyAsString());
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		} catch (Exception exception) {
			LOGGER.error("restCallApi() method error: " + exception);
			exception.printStackTrace();
			bean.setData(null);
			bean.setMessage(exception.getMessage());
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
	}

	public ResponseBean vmedRestCall(String payload, String url, String apiType) {
		ResponseBean bean = new ResponseBean();
		ResponseEntity<String> postForEntity = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			LOGGER.info("restCallApi() method " + apiType + " " + payload);
			HttpEntity<String> entity = new HttpEntity<>(payload, headers);
			postForEntity = template.postForEntity(url, entity, String.class);
			if (postForEntity != null && postForEntity.getBody() != null) {
				ResponseBean beanNew = objMapper.readValue(postForEntity.getBody(), ResponseBean.class);
				LOGGER.info("REST CALL RESPONSE=========>" + beanNew);
				if (beanNew != null) {
					bean.setData(beanNew.getData());
					bean.setStatus(beanNew.getStatus());
					bean.setMessage(beanNew.getMessage());
				} else {
					bean.setData(null);
					bean.setStatus(HttpStatus.OK);
					bean.setMessage("failed");
				}
			} else {
				bean.setData(null);
				bean.setStatus(HttpStatus.OK);
				bean.setMessage("failed");
			}
			LOGGER.info("restCallApi() method response " + postForEntity.getBody());
		} catch (HttpClientErrorException exception) {
			LOGGER.error("restCallApi() method error HttpClientErrorException: " + exception.getResponseBodyAsString());
			bean.setData(null);
			bean.setMessage(exception.getResponseBodyAsString());
		} catch (Exception exception) {
			LOGGER.error("restCallApi() method error: " + exception);
			bean.setData(null);
			bean.setMessage(exception.getMessage());
		}
		return bean;
	}

	public Map<String, Object> getPatientDetailsByIdOrMRNNumber(String mrnNumber) {
		if (mrnNumber != null && !mrnNumber.isEmpty()) {
			Map<String, Object> pat = new HashMap<>(patientRegistrationRepository.findByPatientId2(-1L, mrnNumber));
			Long patientId = Long.valueOf(pat.get("patient_id").toString());
			return pat;
		}
		return null;
	}

	public Boolean saveVmedRequest(String requestId, String payload, String endPoint, String consentId, String txnId) {
		boolean flag = false;
		try {
			LOGGER.info("Calling VMED URL");
			ABDMRequestMapping abdmRequest = null;
			if (!requestId.isEmpty()) {
				abdmRequest = abdmRequestMapping.findByRequestIdLimit1(requestId);
				if (abdmRequest != null) {
					HttpEntity<String> entity = null;
					entity = new HttpEntity<>(payload);
					template.postForEntity(abdmRequest.getUrl() + "/" + endPoint, entity, String.class);
					flag = true;
				}
			} else if (!consentId.isEmpty()) {
				abdmRequest = abdmRequestMapping.findByConsentIdLimit1(consentId);
				if (abdmRequest != null) {
					HttpEntity<String> entity = null;
					entity = new HttpEntity<>(payload);
					template.postForEntity(abdmRequest.getUrl() + "/" + endPoint, entity, String.class);
					flag = true;
				}
			} else if (!txnId.isEmpty()) {
				abdmRequest = abdmRequestMapping.findByTxnIdLimit1(txnId);
				if (abdmRequest != null) {
					HttpEntity<String> entity = null;
					entity = new HttpEntity<>(payload);
					template.postForEntity(abdmRequest.getUrl() + "/" + endPoint, entity, String.class);
					flag = true;
				}
			} else {
				HttpEntity<String> entity = null;
				entity = new HttpEntity<>(payload);
				template.postForEntity(vmedUrl + endPoint, entity, String.class);
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public String streamAndReturnQuery(String apiType, List<AbhaQueryTable> abhaQueriesList) {
		Optional<String> findFirst = abhaQueriesList.stream().filter(s -> s.getQueryType().equals(apiType))
				.map(p -> p.getQuery()).findFirst();
		String query = "";
		if (findFirst.isPresent()) {
			query = findFirst.get();
		}
		return query;
	}
}
