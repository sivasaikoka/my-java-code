package com.dipl.abha.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.dto.DecryptionRequest;
import com.dipl.abha.dto.DecryptionResponse;
import com.dipl.abha.dto.HIPTenantMappingDto;
import com.dipl.abha.dto.ProcessAndDecryptDTO;
import com.dipl.abha.entities.HIUDecryptedDocument;
import com.dipl.abha.entities.HIURequest;
import com.dipl.abha.entities.HIURequestStatus;
import com.dipl.abha.entities.HIUResponse;
import com.dipl.abha.entities.HIUResponseDocument;
import com.dipl.abha.m2.datapushpayload.DataPushEntry;
import com.dipl.abha.m2.datapushpayload.DataPushPayload;
import com.dipl.abha.m2.requestypayload.RequestPayload;
import com.dipl.abha.m3.cmOnRequest.CmOnRequest;
import com.dipl.abha.m3.consentHiuNotify.ConsentHiuNotify;
import com.dipl.abha.m3.consentOnFetch.ConsentOnFetch;
import com.dipl.abha.m3.consentOnStatus.ConsentOnStatus;
import com.dipl.abha.m3.consentRequestOnInit.ConsentArtefact;
import com.dipl.abha.m3.consentRequestOnInit.ConsentRequestOnInit;
import com.dipl.abha.m3.onfindpatient.PatientOnFindPayload;
import com.dipl.abha.repositories.HIUDecryptedDocumentRepository;
import com.dipl.abha.repositories.HIURequestRepository;
import com.dipl.abha.repositories.HIURequestStatusRepository;
import com.dipl.abha.repositories.HIUResponseDocumentRepository;
import com.dipl.abha.repositories.HIUResponseRepository;
import com.dipl.abha.service.ABHAM2Service;
import com.dipl.abha.service.ABHAM3Service;
import com.dipl.abha.service.AllAbdmCentralDbSave;
import com.dipl.abha.service.WebHookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = { "gateway/v0.5", "v0.5", "v1.0", "gateway/v1.0" })
public class NDHMM3CallBackController {

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private ABHAM3Service abham3Service;

	@Autowired
	private HIUResponseRepository responseRepository;

	@Autowired
	private HIURequestRepository hiuRequestRepo;

	@Autowired
	private NDHMM3Controller ndhmM3Controller;

	@Autowired
	private HIURequestStatusRepository hiuRequestStatusRepository;

	@Autowired
	private HIUResponseDocumentRepository hiuResponseDocument;

	@Autowired
	private HIUDecryptedDocumentRepository hiuDecryptedDocRepo;

	@Autowired
	private DecryptionController decryptController;
	
	@Autowired
	private WebHookService webHookService;
	

	@Value("${VMED_URL}")
	private String vmedUrl;
	
	@Value("${HEALTH-INFORMATION-NOTIFY}")
	private String healthInformationNotifyUrl;

	@Autowired
	private AllAbdmCentralDbSave abdmCentralDbSave;

	@Autowired
	private NDHMM2CallBackController ndhmM2CallBackController;

	private static final Logger LOGGER = LoggerFactory.getLogger(NDHMM3CallBackController.class);

	@PostMapping(value = "patients/on-find")
	public void onFindPatient(@RequestBody String request) {
		LOGGER.info("REQUEST AT ##PATIENT/ON-FIND# START:" + request);
		try {
			PatientOnFindPayload patientOnFind = objMapper.readValue(request, PatientOnFindPayload.class);
			abdmCentralDbSave.updateCallBackInCentralDb(Integer.parseInt(TenantContext.getCurrentTenant()), request, "",
					patientOnFind.getResp().getRequestId(), null);
			HIPTenantMappingDto hipTenantMappingDto = ndhmM2CallBackController
					.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
			if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
					&& !hipTenantMappingDto.getServerAccess()) {
				ndhmM2CallBackController.restCallTenantUrlForCallBacks(
						hipTenantMappingDto.getUrlPath() + "/abha/v0.5/patients/on-find", request, "ON-FIND", null);
			} else {
				this.commanHiuResponseSave(patientOnFind.getResp().getRequestId(), null, request, null, null, 10,
						"PATIENT/ON-FIND");
			}

			LOGGER.info("REQUEST AT ##PATIENT/ON-FIND## END:");
		} catch (Exception e) {
			LOGGER.error("ERROR IN ##PATIENT/ON-FIND##");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@PostMapping(value = "consent-requests/on-init")
	public void consentRequestOnInit(@RequestBody String request) {
		LOGGER.info("REQUEST AT ##CONSENT-REQUESTS/ON-INIT# START:" + request);
		String requestId = "";
		String consentRequestId = "";
		try {
			ConsentRequestOnInit consentRequestOnInit = objMapper.readValue(request, ConsentRequestOnInit.class);
			if (consentRequestOnInit != null) {
				if (consentRequestOnInit.getResp() != null) {
					requestId = consentRequestOnInit.getResp().getRequestId();
				}
				if (consentRequestOnInit.getConsentRequest() != null) {
					consentRequestId = consentRequestOnInit.getConsentRequest().getId();
				}
			}

			abdmCentralDbSave.updateCallBackInCentralDb(Integer.parseInt(TenantContext.getCurrentTenant()), request,
					consentRequestId, consentRequestOnInit.getResp().getRequestId(), null);
			HIPTenantMappingDto hipTenantMappingDto = ndhmM2CallBackController
					.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
			if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
					&& !hipTenantMappingDto.getServerAccess()) {
				ndhmM2CallBackController.restCallTenantUrlForCallBacks(
						hipTenantMappingDto.getUrlPath() + "/abha/v0.5/consent-requests/on-init", request,
						"CONSENT-REQUESTS/ON-INIT", null);
			} else {
				this.commanHiuResponseSave(requestId, null, request, consentRequestId, null, 2,
						"CONSENT-REQUESTS/ON-INIT");
			}
			LOGGER.info("REQUEST AT ##CONSENT-REQUESTS/ON-INIT## END:");
		} catch (Exception e) {
			LOGGER.error("ERROR IN ##CONSENT-REQUESTS/ON-INIT##");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@PostMapping(value = "consent-requests/on-status")
	public void consentOnStatus(@RequestBody String request) {
		LOGGER.info("request at ##consent-requests/on-status# start:" + request);
		try {
			ConsentOnStatus consentOnStatus = objMapper.readValue(request, ConsentOnStatus.class);
			if (consentOnStatus != null) {
				if (consentOnStatus.getResp() != null) {
//					String requestId = consentOnStatus.getResp().getRequestId();
				}
			}
//			com.dipl.core.entities.CallBackApiResponse callBack = webHookService.findAndUpdateRecord(requestId, "CONSENT-STATUS", request, "");
//			if (Objects.nonNull(callBack)) {
//				LOGGER.info("request at ##consent-requests/on-status## successfull:");
//			} else {
//				LOGGER.info("request at ##consent-requests/on-status## did not save in DB:");
//			}
//			LOGGER.info("request at ##consent-requests/on-status## end:");
//			;
		} catch (Exception e) {
			LOGGER.error("request at ##consent-requests/on-status## end:");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@PostMapping(value = "consents/hiu/notify")
	public void consenHiuNotify(@RequestBody String request, HttpServletRequest httpServletRequest) {
		LOGGER.info("REQUEST AT ##CONSENTS/HIU/NOTIFY# START: {}, HIU ID: {}", request,
				httpServletRequest.getHeader("X-HIU-ID"));
		try {
			HIPTenantMappingDto hipTenantMappingDto = ndhmM2CallBackController
					.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
			if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
					&& !hipTenantMappingDto.getServerAccess()) {
				ndhmM2CallBackController.restCallTenantUrlForCallBacks(
						hipTenantMappingDto.getUrlPath() + "/abha/v0.5/consents/hiu/notify", request,
						"CONSENTS/HIU/NOTIFY", httpServletRequest.getHeader("X-HIU-ID"));
				return;
			}
			String requestId = "";
			String concentId = "";
			String status = "";
			List<HIURequestStatus> hiuRequestStatus = new ArrayList<>();
			List<HIUResponse> hiuResponseList = new ArrayList<>();

			ConsentHiuNotify consentOnStatus = objMapper.readValue(request, ConsentHiuNotify.class);
			if (consentOnStatus != null) {
				if (consentOnStatus.getRequestId() != null) {
					requestId = consentOnStatus.getRequestId();
					concentId = consentOnStatus.getNotification().getConsentRequestId();
					status = consentOnStatus.getNotification().getStatus();
				}
			}
			for (ConsentArtefact ca : consentOnStatus.getNotification().getConsentArtefacts()) {
				if ("GRANTED".equals(status)) {
					abdmCentralDbSave.processPayloadForHIUNotify(consentOnStatus, 1, httpServletRequest);
				}
				if (concentId.isEmpty()) {
					concentId = responseRepository.getConsentRequestIdByConsentArtifactCode(ca.getId());
				}
				hiuResponseList.add(
						abham3Service.constructHiuResponseObject(requestId, "", request, concentId, ca.getId(), 3));
			}

			hiuRequestStatus.add(abham3Service.consuturctHiuRequestStatus(requestId, concentId, "", status));
			responseRepository.saveAll(hiuResponseList);
			hiuRequestStatusRepository.saveAll(hiuRequestStatus);
			ndhmM3Controller.consentOnNotify(abham3Service.constructConsentHiuOnNotifyPaylod(
					consentOnStatus.getNotification().getConsentArtefacts(), requestId, status,
					httpServletRequest.getHeader("X-HIU-ID")), httpServletRequest);
			LOGGER.info("REQUEST AT ##CONSENTS/HIU/NOTIFY# END:");
		} catch (Exception e) {
			LOGGER.error("ERROR IN ##CONSENTS/HIU/NOTIFY##");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void successOrFailureLog(HIUResponse hiuResponse, String apiUrl) {
		if (Objects.nonNull(hiuResponse)) {
			LOGGER.info("REQUEST AT ## " + apiUrl + " ## SUCCESSFULL AND SAVED IN DB");
		} else {
			LOGGER.info("REQUEST AT ## " + apiUrl + " ## DID NOT SAVE IN DB:");
		}
	}

	@PostMapping(value = "consents/on-fetch")
	public void consentOnFetch(@RequestBody String request, HttpServletRequest httpRequest) {
		LOGGER.info("REQUEST AT ##CONSENTS/ON-FETCH# START:" + request);
		String requestId = "";
		String consentArtifactId = "";
		LocalDateTime dataEraseAt = null;
		try {
			HIPTenantMappingDto hipTenantMappingDto = ndhmM2CallBackController
					.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
			if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
					&& !hipTenantMappingDto.getServerAccess()) {
				ndhmM2CallBackController.restCallTenantUrlForCallBacks(
						hipTenantMappingDto.getUrlPath() + "/abha/v0.5/consents/on-fetch", request, "CONSENTS/ON-FETCH",
						httpRequest.getHeader("X-HIU-ID"));
				return;
			}
			ConsentOnFetch consentOnFetch = objMapper.readValue(request, ConsentOnFetch.class);
			if (consentOnFetch != null) {
				if (consentOnFetch.getResp() != null) {
					requestId = consentOnFetch.getResp().getRequestId();
					consentArtifactId = consentOnFetch.getConsent().getConsentDetail().getConsentId();
				}
				abdmCentralDbSave.updateCallBackInCentralDb(Integer.parseInt(TenantContext.getCurrentTenant()), request,
						"", consentOnFetch.getResp().getRequestId(), "");
				if (consentOnFetch.getError() == null || consentOnFetch.getError().equals("null")) {
					if (consentOnFetch.getConsent() != null
							&& consentOnFetch.getConsent().getStatus().equals("GRANTED")) {
						if (consentOnFetch.getConsent() != null
								&& consentOnFetch.getConsent().getConsentDetail() != null
								&& consentOnFetch.getConsent().getConsentDetail().getPermission() != null
								&& consentOnFetch.getConsent().getConsentDetail().getPermission()
										.getDataEraseAt() != null) {
							dataEraseAt = consentOnFetch.getConsent().getConsentDetail().getPermission()
									.getDataEraseAt();
						}
						RequestPayload requestPayload = abham3Service.constructRequestPayload(consentOnFetch,
								dataEraseAt, httpRequest);
						if (requestPayload != null) {
							ndhmM3Controller.cmRequest(requestPayload, httpRequest);
						}
					} else {
						LOGGER.info("CONSENT IS " + consentOnFetch.getConsent().getStatus());
					}
				} else {
					LOGGER.error("ERROR IN CONSENTS ON FETCH");
				}
				String data = responseRepository.getConsentRequestIdByConsentArtifactCode(consentArtifactId);
				if (!data.isEmpty()) {
					this.commanHiuResponseSave(requestId, null, request, data, consentArtifactId, 6,
							"CONSENTS/HIU/NOTIFY");
				} else {
					LOGGER.info("#NO CONSENT REQUESTID WITH CONSENT-ARTIFACTID ## " + consentArtifactId);
				}
//				}
			}

			LOGGER.info("REQUEST AT ##CONSENTS/ON-FETCH# END:");
		} catch (Exception e) {
			LOGGER.error("ERROR IN ##CONSENTS/ON-FETCH##");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@PostMapping(value = "health-information/hiu/on-request")
	public void cmOnRequest(@RequestBody String request, HttpServletRequest httpRequest) {
		LOGGER.info("request at ##health-information/hiu/on-request# start:" + request);
		String requestId = "";
		String transactionId = "";
		try {
			HIPTenantMappingDto hipTenantMappingDto = ndhmM2CallBackController
					.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
			if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
					&& !hipTenantMappingDto.getServerAccess()) {
				ndhmM2CallBackController.restCallTenantUrlForCallBacks(
						hipTenantMappingDto.getUrlPath() + "/abha/v0.5/health-information/hiu/on-request", request,
						"/ABHA/V0.5/HEALTH-INFORMATION/HIU/ON-REQUEST", httpRequest.getHeader("X-HIU-ID"));
				return;
			}

			CmOnRequest consentOnRequest = objMapper.readValue(request, CmOnRequest.class);
			if (consentOnRequest != null && consentOnRequest.getError() == null) {
				transactionId = consentOnRequest.getHiRequest().getTransactionId();
				if (consentOnRequest.getResp() != null) {
					requestId = consentOnRequest.getResp().getRequestId();
				}
			}
			abdmCentralDbSave.updateCallBackInCentralDb(Integer.parseInt(TenantContext.getCurrentTenant()), request, "",
					consentOnRequest.getResp().getRequestId(), transactionId);
			Optional<HIURequest> hiuRequest = hiuRequestRepo.findByRequestCode(requestId);
			if (hiuRequest.isPresent()) {
				RequestPayload requestPayload = objMapper.readValue(hiuRequest.get().getRequestJson().toString(),
						RequestPayload.class);
				if (requestPayload != null && requestPayload.getHiRequest() != null
						&& requestPayload.getHiRequest().getConsent() != null) {
					String consentArtifactId = requestPayload.getHiRequest().getConsent().getId();
					if (consentArtifactId != null) {
						List<HIUResponse> hiuResponseList = responseRepository
								.findByConsentArtefactCode(consentArtifactId);
						if (!hiuResponseList.isEmpty()) {
							List<HIUResponse> finalList = hiuResponseList.stream()
									.filter(s -> s.getConsentRequestCode() != null).collect(Collectors.toList());
							String consentRequestId = finalList.get(0).getConsentRequestCode();
							this.commanHiuResponseSave(requestId, transactionId, request, consentRequestId,
									consentArtifactId, 8, "HEALTH-INFORMATION/HIU/ON-REQUEST");
						}
					}
				}
			}
			LOGGER.info("REQUEST AT ##HEALTH-INFORMATION/HIU/ON-REQUEST## END:");
		} catch (Exception e) {
			LOGGER.error("REQUEST AT ##HEALTH-INFORMATION/HIU/ON-REQUEST## END:");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void commanHiuResponseSave(String requestId, String transactionId, String request, String consentRequestId,
			String consentArtifactId, int apiTypeId, String apiType) {
		HIUResponse hiuResponse;
		try {
			hiuResponse = abham3Service.insertIntoHiuResponse(requestId, transactionId, request, consentRequestId,
					consentArtifactId, apiTypeId);
			this.successOrFailureLog(hiuResponse, apiType);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	}

	@PostMapping(value = "health-information/transfer")
	public void healthInfoTransfer(@RequestBody String request, HttpServletRequest httpServletRequest) {
		LOGGER.info("request at ##/health-information/transfer# start:");
		String transactionId = "";
		try {
			DataPushPayload dataTransfer = objMapper.readValue(request, DataPushPayload.class);

			HIPTenantMappingDto hipTenantMappingDto = ndhmM2CallBackController
					.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
			if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
					&& !hipTenantMappingDto.getServerAccess()) {
				ndhmM2CallBackController.restCallTenantUrlForCallBacks(
						hipTenantMappingDto.getUrlPath() + "/abha/v0.5/health-information/transfer", request,
						"/ABHA/V0.5/HEALTH-INFORMATION/TRANSFER", httpServletRequest.getHeader("X-HIU-ID"));
				return;
			}

			if (Objects.nonNull(dataTransfer)) {
				if (Objects.nonNull(dataTransfer.getTransactionId())) {
					transactionId = dataTransfer.getTransactionId();
				}
//				abdmCentralDbSave.processPayload(request, 1, httpServletRequest);
				List<HIUResponse> consentIdAndArtifactId = responseRepository.findByTransactionId(transactionId);
				if (!consentIdAndArtifactId.isEmpty()) {
					this.commanHiuResponseSave(UUID.randomUUID().toString(), transactionId, request,
							consentIdAndArtifactId.get(0).getConsentRequestCode(),
							consentIdAndArtifactId.get(0).getConsentArtefactCode(), 11, "HEALTH-INFORMATION/TRANSFER");
					ProcessAndDecryptDTO processDecrypt = new ProcessAndDecryptDTO(consentIdAndArtifactId,
							dataTransfer.getEntries(), transactionId);
//					String processDecryptString = objMapper.writeValueAsString(processDecrypt);

					this.decryptDocumentAndStoreInDd(processDecrypt, httpServletRequest);
				}
			}
			LOGGER.info("request at ##health-information/transfer## end:");
		} catch (Exception e) {
			LOGGER.error("request at ##health-information/transfer## end:");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void decryptDocumentAndStoreInDd(ProcessAndDecryptDTO processDecrypt,
			HttpServletRequest httpServletRequest) {
		try {
			List<HIUResponseDocument> hiuResponseDocList = new ArrayList<>();
			List<HIUDecryptedDocument> hiuDecryptedDocuments = new ArrayList<>();
			for (DataPushEntry s : processDecrypt.getDataEntries()) {
				try {
					hiuResponseDocList
							.add(abham3Service.insertIntoHiuResponseDocument(processDecrypt.getTransactionId(),
									processDecrypt.getConsentIdAndArtifactId().get(0).getConsentRequestCode(),
									processDecrypt.getConsentIdAndArtifactId().get(0).getConsentArtefactCode(),
									s.getCareContextReference(), s.getContent(), false));

				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			List<HIUResponseDocument> hiuRespDocSave = hiuResponseDocument.saveAll(hiuResponseDocList);

			String hiuId = hiuDecryptedDocRepo.getHIUIdByConsentRequestId(
					processDecrypt.getConsentIdAndArtifactId().get(0).getConsentRequestCode());

			List<Map<String, String>> keyMap = hiuDecryptedDocRepo.getKeysByTxnId(processDecrypt.getTransactionId());
			Set<String> uniqueArtifactIds = hiuRespDocSave.stream()
					.filter(s -> s.getConsentArtefactCode() != null && !s.getConsentArtefactCode().isEmpty())
					.map(p -> p.getConsentArtefactCode()).distinct().collect(Collectors.toSet());
			List<Map<String, String>> hipCodeAndHipName = hiuDecryptedDocRepo
					.getHipCodeAndHipNameByConsentArtifactIds(uniqueArtifactIds);
			if (!keyMap.isEmpty()) {
				for (HIUResponseDocument s : hiuRespDocSave) {
					Optional<Map<String, String>> findAny = keyMap.stream()
							.filter(p -> p.get("txn_code").equals(s.getTxnCode())).findAny();
					if (findAny.isPresent()) {
						Map<String, String> foundKeys = findAny.get();
						DecryptionRequest decryptRequest = new DecryptionRequest();
						decryptRequest.setSenderPublicKey(foundKeys.get("sender_public_key"));
						decryptRequest.setSenderNonce(foundKeys.get("sender_nonce"));
						decryptRequest.setReceiverPrivateKey(foundKeys.get("receiver_private_Key"));
						decryptRequest.setReceiverNonce(foundKeys.get("receiver_nonce"));
						decryptRequest.setEncryptedData(s.getEncryptedEntries());
						HIUDecryptedDocument hiuDecryptedDocument = new HIUDecryptedDocument();
						hiuDecryptedDocument.setCareContextReference(s.getCareContext());
						hiuDecryptedDocument.setConsentArtefactCode(s.getConsentArtefactCode());
						hiuDecryptedDocument.setConsentRequestCode(s.getConsentRequestCode());
						hiuDecryptedDocument.setTxnCode(s.getTxnCode());
						Optional<Map<String, String>> findFirst = hipCodeAndHipName.stream().filter(
								p -> p.get("consent_artefact_code").toString().equals(s.getConsentArtefactCode()))
								.findFirst();
						if (findFirst.isPresent()) {
							hiuDecryptedDocument.setHipId(findFirst.get().get("hip_id"));
							hiuDecryptedDocument.setHipName(findFirst.get().get("hip_name"));
						}

						try {
							DecryptionResponse decrypt = decryptController.decrypt(decryptRequest);
							if (decrypt != null) {
								JsonNode jsonNode = objMapper.readTree(decrypt.getDecryptedData());
								hiuDecryptedDocument.setDecryptedDocument(jsonNode);
								hiuDecryptedDocument.setValidationSuccessful(true);
								s.setDecryptionDone(true);
							} else {
								hiuDecryptedDocument.setDecryptedDocument(null);
								hiuDecryptedDocument.setValidationSuccessful(false);
								s.setDecryptionDone(false);
							}
						} catch (Exception e) {
							hiuDecryptedDocument.setDecryptedDocument(null);
							hiuDecryptedDocument.setValidationSuccessful(false);
							e.printStackTrace();
							s.setDecryptionDone(false);
						}
						hiuDecryptedDocuments.add(hiuDecryptedDocument);
					}
				} 
				hiuResponseDocument.saveAll(hiuResponseDocList);
				List<HIUDecryptedDocument> saveAll = hiuDecryptedDocRepo.saveAll(hiuDecryptedDocuments);
				LOGGER.info("HIU NOTIFY STARTED");
				if (saveAll != null && !saveAll.isEmpty()) {
					List<String> careContexts = hiuRespDocSave.stream()
							.filter(s -> s.getCareContext() != null && !s.getCareContext().isEmpty())
							.map(p -> p.getCareContext()).distinct().collect(Collectors.toList());
					String transferFileNotifyRequest = objMapper.writeValueAsString(
							webHookService.constructNotifyHipAfterPushPayload(saveAll.get(0).getConsentArtefactCode(),
									saveAll.get(0).getTxnCode(), careContexts, saveAll.get(0).getHipId(), hiuId, "HIU",
									"RECEIVED","RECEIVED REDORDS"));

					webHookService.saveAndReturnResponse(transferFileNotifyRequest, healthInformationNotifyUrl,
							"HEALTH-INFORMATION-NOTIFY", UUID.randomUUID().toString(), saveAll.get(0).getTxnCode(), 40,
							null, saveAll.get(0).getConsentArtefactCode(), null, null);

				}
				LOGGER.info("HIU NOTIFY ENDED");
			}
		} catch (Exception e) {
			LOGGER.error("request at ##health-information/transfer## end:");
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

}