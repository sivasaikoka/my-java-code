package com.dipl.abha.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.client.RestTemplate;

import com.dipl.abha.controllers.KeysController;
import com.dipl.abha.dto.AccessToken;
import com.dipl.abha.dto.Resp;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.entities.HIUDecryptedDocument;
import com.dipl.abha.entities.HIURequest;
import com.dipl.abha.entities.HIURequestStatus;
import com.dipl.abha.entities.HIUResponse;
import com.dipl.abha.entities.HIUResponseDocument;
import com.dipl.abha.m2.requestypayload.Consent;
import com.dipl.abha.m2.requestypayload.DhPublicKey;
import com.dipl.abha.m2.requestypayload.HiRequest;
import com.dipl.abha.m2.requestypayload.KeyMaterial;
import com.dipl.abha.m2.requestypayload.RequestPayload;
import com.dipl.abha.m3.consentHiuOnNotify.Acknowledgement;
import com.dipl.abha.m3.consentHiuOnNotify.ConsentsHiuOnNotify;
import com.dipl.abha.m3.consentOnFetch.ConsentOnFetch;
import com.dipl.abha.m3.consentRequestInit.ConsentRequestV3Init;
import com.dipl.abha.m3.consentRequestOnInit.ConsentArtefact;
import com.dipl.abha.repositories.HIUDecryptedDocumentRepository;
import com.dipl.abha.repositories.HIURequestRepository;
import com.dipl.abha.repositories.HIUResponseRepository;
import com.dipl.abha.util.ConstantUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ABHAM3Service {

	@Value("${CLIENTSECRET}")
	private String clientSecret;
	@Value("${CLIENTID}")
	private String clientId;
	@Value("${GETSESSION}")
	private String getsession;
	@Value("${CERT}")
	private String cert;
	@Value("${CONSENT_MANAGER_ID}")
	private String cmId;
	@Value("${DATA_PUSH_URL}")
	private String dataPushUrl;
	@Autowired
	private ObjectMapper objMapper;
	@Autowired
	private RestTemplate template;
	@Value("${CMSESSIONS}")
	private String cmsessions;
	@Autowired
	private HIURequestRepository hiuRequestRepository;
	@Autowired
	private HIUResponseRepository responseRepository;
	@Autowired
	private HIUDecryptedDocumentRepository decryptedDocumentRepository;
	@Autowired
	private KeysController keys;
	@Autowired
	private Base64Genarate base64Generator;

	private static final Logger LOGGER = LoggerFactory.getLogger(ABHAM3Service.class);

	public ResponseEntity<ResponseBean> callingNdhmApi(String payload, String url, String apiType, String hiuId,
			String requestId) {
		ResponseBean bean = new ResponseBean();
		ResponseEntity<String> postForEntity = null;
		try {
			if (base64Generator.generateToken() == null) {
				bean.setData(null);
				bean.setMessage(ConstantUtil.ABHA_SEVER_ISSUE_ERROR_MESSAGE);
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
			}
			AccessToken convertValue = base64Generator.generateToken();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + convertValue.getAccessToken());
			headers.set("X-CM-ID", cmId);

			if ("HEALTH-INFORMATION-REQUEST".equals(apiType)) {
				headers.set("X-HIU-ID", hiuId);
				headers.set("TIMESTAMP", ConstantUtil.utcTimeStamp());
				headers.set("REQUEST-ID", requestId);
			}
			LOGGER.info(
					"ABDM API CALLING method ===========> {}, ============> {}, ===========> {},==================> {}",
					url, apiType, headers, payload);
			HttpEntity<String> entity = new HttpEntity<>(payload, headers);
			postForEntity = template.postForEntity(url, entity, String.class);
			bean.setData(null);
			bean.setStatus(postForEntity.getStatusCode());
			LOGGER.info("restCallApi() method response " + postForEntity.getBody());
		} catch (HttpClientErrorException exception) {
			LOGGER.error("restCallApi() method error HttpClientErrorException: " + exception.getResponseBodyAsString());
			bean.setData(null);
			bean.setMessage(exception.getResponseBodyAsString());
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		} catch (Exception exception) {
			LOGGER.error("restCallApi() method error: " + exception);
			bean.setData(null);
			bean.setMessage(exception.getMessage());
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
	}

	public ResponseEntity<ResponseBean> callingNdhmApiv3(Object payload, String url, String apiType) {
		ResponseBean bean = new ResponseBean();
		ResponseEntity<String> postForEntity = null;
		try {
			if (base64Generator.generateToken() == null) {
				bean.setData(null);
				bean.setMessage(ConstantUtil.ABHA_SEVER_ISSUE_ERROR_MESSAGE);
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
			}
			String finalConsentRequest = objMapper.writeValueAsString(payload);
			AccessToken convertValue = base64Generator.generateToken();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + convertValue.getAccessToken());
			headers.set("X-CM-ID", cmId);
			if ("CONSENT-REQUEST-INIT-V3".equals(apiType) || "CONSENT-STATUS-V3".equals(apiType)
					|| "CONSENT-FETCH-V3".equals(apiType)) {
				ConsentRequestV3Init obj = (ConsentRequestV3Init) payload;
				headers.set("X-HIU-ID", obj.getHiuId());
				headers.set("TIMESTAMP", ConstantUtil.utcTimeStamp());
				headers.set("REQUEST-ID", obj.getRequestId());
			} else if ("CONSENT-HIU-ON-NOTIFY".equals(apiType)) {
				ConsentsHiuOnNotify obj = (ConsentsHiuOnNotify) payload;
				headers.set("TIMESTAMP",  ConstantUtil.utcTimeStamp());
				headers.set("REQUEST-ID", obj.getRequestId());
				headers.set("X-HIU-ID", obj.getHiuId());
			}

//			LOGGER.info("restCallApi() method {}, {}", apiType, finalConsentRequest);
			LOGGER.info(
					"ABDM API CALLING method ===========> {}, ============> {}, ===========> {},==================> {}",
					url, apiType, headers, finalConsentRequest);
			HttpEntity<String> entity = new HttpEntity<>(finalConsentRequest, headers);
			postForEntity = template.postForEntity(url, entity, String.class);
			LOGGER.info("POST FOR ENTITY IS ===========> {}", postForEntity);
			bean.setData(null);
			bean.setStatus(postForEntity.getStatusCode());
			LOGGER.info("restCallApi() method response ", postForEntity.getBody());
		} catch (HttpClientErrorException exception) {
			LOGGER.error("restCallApi() method error HttpClientErrorException: {} :{}", apiType,
					exception.getResponseBodyAsString());
			bean.setData(null);
			bean.setMessage(exception.getResponseBodyAsString());
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		} catch (Exception exception) {
			LOGGER.error("restCallApi() method error: {} : {}", apiType, exception);
			bean.setData(null);
			bean.setMessage(exception.getMessage());
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
	}

	public HIURequest insertIntoHiuRequest(String requestId, String txnId, String request, Integer requestTypeId,
			Long benId, String abhaNo, String errorResponse, Long doctorId)
			throws JsonMappingException, JsonProcessingException {
		HIURequest hiuRequest = new HIURequest();
		JsonNode json = null;
		JsonNode errorResponseJson = null;
		try {
			hiuRequest.setRequestCode(requestId);
			hiuRequest.setTxnCode(txnId);
			if (request != null && !request.isEmpty()) {
				json = objMapper.readTree(request);
			}
			hiuRequest.setRequestJson(json);
			hiuRequest.setHiuRequestTypeId(requestTypeId);
			hiuRequest.setBeneficiaryId(benId);
			hiuRequest.setAbhaNumber(abhaNo);
			if (doctorId != null) {
				hiuRequest.setCreatedBy(doctorId);
			} else {
				hiuRequest.setCreatedBy((long) 78987);
			}
			if (errorResponse != null && !errorResponse.isEmpty()) {
				errorResponseJson = objMapper.readTree(errorResponse);
			}
			hiuRequest.setErrorResponse(errorResponseJson);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hiuRequestRepository.save(hiuRequest);
	}

	public HIUResponse insertIntoHiuResponse(String requestId, String txnId, String response, String consentRequestCode,
			String artefactId, int requestTypeId) throws JsonMappingException, JsonProcessingException {
		HIUResponse hiuResponse = new HIUResponse();
		JsonNode json = null;
		try {
			hiuResponse.setRequestCode(requestId);
			hiuResponse.setTxnCode(txnId);
			if (response != null && !response.isEmpty()) {
				json = objMapper.readTree(response);
			}
			hiuResponse.setResponseJson(json);
			hiuResponse.setConsentRequestCode(consentRequestCode);
			hiuResponse.setConsentArtefactCode(artefactId);
			hiuResponse.setHiuRequestTypeId(requestTypeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseRepository.save(hiuResponse);
	}

	public HIUResponse constructHiuResponseObject(String requestId, String txnId, String response,
			String consentRequestCode, String artefactId, int requestTypeId)
			throws JsonMappingException, JsonProcessingException {
		HIUResponse hiuResponse = new HIUResponse();
		JsonNode json = null;
		try {
			hiuResponse.setRequestCode(requestId);
			hiuResponse.setTxnCode(txnId);
			if (response != null && !response.isEmpty()) {
				json = objMapper.readTree(response);
			}
			hiuResponse.setResponseJson(json);
			hiuResponse.setConsentRequestCode(consentRequestCode);
			hiuResponse.setConsentArtefactCode(artefactId);
			hiuResponse.setHiuRequestTypeId(requestTypeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hiuResponse;
	}

	public HIURequestStatus consuturctHiuRequestStatus(String requestId, String consentRequestCod,
			String consentArtifactCode, String statusType) {
		HIURequestStatus hiuRequest = new HIURequestStatus();
		hiuRequest.setRequestCode(requestId);
		hiuRequest.setConsentRequestCode(consentRequestCod);
		hiuRequest.setStatusType(statusType);
		hiuRequest.setCosentArtifactCode(consentArtifactCode);
		return hiuRequest;
	}

	public HIUDecryptedDocument insertIntoHiuDecryptedDocument(String requestId, String txnId, String careContext,
			String artefactId, String decryptDocument, boolean valid)
			throws JsonMappingException, JsonProcessingException {
		HIUDecryptedDocument hiudecrpy = new HIUDecryptedDocument();
		JsonNode json = null;
		try {
			hiudecrpy.setTxnCode(txnId);
			hiudecrpy.setCareContextReference(careContext);
			hiudecrpy.setConsentArtefactCode(artefactId);
			hiudecrpy.setConsentRequestCode(requestId);
			if (decryptDocument != null && !decryptDocument.isEmpty()) {
				json = objMapper.readTree(decryptDocument);
			}
			hiudecrpy.setDecryptedDocument(json);
			hiudecrpy.setValidationSuccessful(valid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptedDocumentRepository.save(hiudecrpy);
	}

	public HIUResponseDocument insertIntoHiuResponseDocument(String txnId, String consentRequestId, String artefactId,
			String careContext, String entries, boolean decryptdone)
			throws JsonMappingException, JsonProcessingException {
		HIUResponseDocument hiuresponseDoc = new HIUResponseDocument();
		hiuresponseDoc.setTxnCode(txnId);
		hiuresponseDoc.setConsentArtefactCode(artefactId);
		hiuresponseDoc.setConsentRequestCode(consentRequestId);
		hiuresponseDoc.setCareContext(careContext);
		hiuresponseDoc.setEncryptedEntries(entries);
		hiuresponseDoc.setDecryptionDone(decryptdone);
		return hiuresponseDoc;
	}

	public RequestPayload constructRequestPayload(ConsentOnFetch consentOnFetch, LocalDateTime dataEraseAt,
			HttpServletRequest httpRequest) {
		RequestPayload requestPayload = null;
		try {
			com.dipl.abha.encryptdecrypclasses.KeyMaterial generate = keys.generate();
			com.dipl.abha.m3.consentOnFetch.DateRange onFetchDateRange = consentOnFetch.getConsent().getConsentDetail()
					.getPermission().getDateRange();
			requestPayload = new RequestPayload();
			requestPayload.setRequestId(UUID.randomUUID().toString());
			requestPayload.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
			HiRequest hiRequest = new HiRequest();
			Consent consent = new Consent();
			consent.setId(consentOnFetch.getConsent().getConsentDetail().getConsentId());
			hiRequest.setConsent(consent);
			com.dipl.abha.m2.notifypayload.DateRange dateRange = new com.dipl.abha.m2.notifypayload.DateRange();
			dateRange.setFrom(onFetchDateRange.getFrom());
			dateRange.setMyto(onFetchDateRange.getMyto());
			hiRequest.setDateRange(dateRange);
			String baseUrl = httpRequest.getScheme() + "s://" + httpRequest.getServerName()
					+ "/abha/v1.0/health-information/transfer";
			hiRequest.setDataPushUrl(baseUrl);
			KeyMaterial keyMaterial = new KeyMaterial();
			keyMaterial.setCryptoAlg("ECDH");
			keyMaterial.setCurve("Curve25519");
			DhPublicKey dhPublicKey = new DhPublicKey();
			dhPublicKey.setExpiry(dataEraseAt);
			requestPayload.setPrivateKey(generate.getPrivateKey());
			dhPublicKey.setParameters("Curve25519/32byte random key");
			dhPublicKey.setKeyValue(generate.getPublicKey());
			keyMaterial.setDhPublicKey(dhPublicKey);
			keyMaterial.setNonce(generate.getNonce());
			hiRequest.setKeyMaterial(keyMaterial);
			requestPayload.setHiRequest(hiRequest);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestPayload;
	}

	public ConsentsHiuOnNotify constructConsentHiuOnNotifyPaylod(List<ConsentArtefact> consentArtefacts,
			String requestId, String status, String hiuId) {
		ConsentsHiuOnNotify consentsHiuOnNotify = new ConsentsHiuOnNotify();
		List<Acknowledgement> ackList = new ArrayList<Acknowledgement>();
		consentsHiuOnNotify.setStatusForRef(status);
		consentArtefacts.forEach(s -> {
			Acknowledgement acknowledgement = new Acknowledgement();
			acknowledgement.setConsentId(s.getId());
			acknowledgement.setStatus("OK");
			ackList.add(acknowledgement);
		});
		consentsHiuOnNotify.setAcknowledgement(ackList);
		consentsHiuOnNotify.setRequestId(UUID.randomUUID().toString());
		consentsHiuOnNotify.setTimestamp(ConstantUtil.utcTimeStamp());
		consentsHiuOnNotify.setError(null);
		consentsHiuOnNotify.setHiuId(hiuId);
		Resp resp = new Resp();
		resp.setRequestId(requestId);
//		consentsHiuOnNotify.setResp(resp);
		consentsHiuOnNotify.setResponse(resp);
		return consentsHiuOnNotify;
	}

}