package com.dipl.abha.controllers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.dto.AbhaQueryTable;
import com.dipl.abha.dto.AbhaRegistrationDTO;
import com.dipl.abha.dto.AccessToken;
import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.HIPTenantMappingDto;
import com.dipl.abha.dto.LinkCareContextV3Payload;
import com.dipl.abha.dto.NotifyResultSetDto;
import com.dipl.abha.dto.ONGenerateLinkToken;
import com.dipl.abha.dto.PatientIdDto;
import com.dipl.abha.dto.PatientRegistrationDTO;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.dto.TenanIdAndIsIntegratedModule;
import com.dipl.abha.entities.CareContextLogs;
import com.dipl.abha.entities.HIPCallback;
import com.dipl.abha.entities.HIPRequest;
import com.dipl.abha.entities.HipLinkTokenEntity;
import com.dipl.abha.m2.addcarecontextpayload.AddCareContextPayload;
import com.dipl.abha.m2.addcarecontextpayload.Link;
import com.dipl.abha.m2.authnotifydirectmodepayload.DirectModeAuth;
import com.dipl.abha.m2.authonconfirmpayloads.AuthOnConfirmPayload;
import com.dipl.abha.m2.authonnotifydirectmodepayload.DirectAuthOnNotify;
import com.dipl.abha.m2.discoverpayload.DiscoverPayload;
import com.dipl.abha.m2.discoverpayload.VerifiedIdentifiers;
import com.dipl.abha.m2.discoverpayload.patient;
import com.dipl.abha.m2.linkInit.LinksInit;
import com.dipl.abha.m2.linkInit.LinksPatient;
import com.dipl.abha.m2.linkconfirmpayload.LinksConfirmPayload;
import com.dipl.abha.m2.linkoninit.LinksOnInit;
import com.dipl.abha.m2.linksonconfirm.LinkOnConfirm;
import com.dipl.abha.m2.notifypayload.Hip;
import com.dipl.abha.m2.notifypayload.NotifyPayload;
import com.dipl.abha.m2.onaddcarecontextpayload.OnAddCareContext;
import com.dipl.abha.m2.onfetchpayload.OnFetchPayload;
import com.dipl.abha.m2.onnofitypayload.OnNotifyPayload;
import com.dipl.abha.m2.onrequestypayload.OnRequestPayload;
import com.dipl.abha.m2.patientstatusnotifypayload.PatientStatusNotifyPayload;
import com.dipl.abha.m2.patientstatusonnotifypayload.PatientStatuOnNotify;
import com.dipl.abha.m2.requestypayload.HiRequest;
import com.dipl.abha.m2.requestypayload.RequestPayload;
import com.dipl.abha.m2.shareprofilepayload.ProfileSharePayload;
import com.dipl.abha.m2.userauthoninitpayload.AuthOnInitPayload;
import com.dipl.abha.m2.userauthoninitpayload.SmsOnNotify;
import com.dipl.abha.repositories.CareContextLogsRepository;
import com.dipl.abha.repositories.HIPCallbackRepository;
import com.dipl.abha.repositories.HIPRequestRepository;
import com.dipl.abha.repositories.HipLinkTokenRepository;
import com.dipl.abha.repositories.PatientRegistrationRepository;
import com.dipl.abha.service.ABHAM2Service;
import com.dipl.abha.service.AllAbdmCentralDbSave;
import com.dipl.abha.service.ExtractResultSetService;
import com.dipl.abha.service.WebHookService;
import com.dipl.abha.util.ConstantUtil;
import com.dipl.abha.util.JdbcTemplateHelper;
import com.dipl.abha.v3.dto.CareContextOnDiscoverV3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Abhinay
 *
 */
@RestController
@Slf4j
@RequestMapping(value = { "v0.5", "v1.0", "api/v3", "gateway/v0.5", "gateway/v1.0", "gateway/api/v3" })
public class NDHMM2CallBackController {

	@Value("${VALIDATEOTP}")
	private String validateOtp;
	@Value("${GENERATEOTP}")
	private String generateOtp;
	@Value("${VMED_GET_DATA_NOTIFY}")
	private String vmedGetCareContexts;
	@Value("${PATIENT-STATUS-NOTIFY}")
	private String patientStatusNotify;
	@Value("${HIMS_TENANT_IDS}")
	private Set<Integer> himsTenantIds;
	@Autowired
	private NDHMM2Controller ndhmController;
	@Autowired
	private ObjectMapper objMapper;
	@Autowired
	private WebHookService webHookService;
	@Autowired
	private HIPCallbackRepository hipCallBackRepository;
	@Autowired
	private PatientRegistrationRepository patientRegRepository;
	@Autowired
	private RestTemplate template;
	@Autowired
	private CareContextLogsRepository careContextLogsRepository;
	@Autowired
	private HIPRequestRepository hipRequestRepo;
	@Autowired
	private ABHAM2Service abhaM2Service;
	@Autowired
	private AllAbdmCentralDbSave allAbdmCentralDbSave;
	@Autowired
	private JdbcTemplateHelper jdbcTemplateHelper;
	@Autowired
	private ExtractResultSetService extractResultSetService;
	@Autowired
	private HipLinkTokenRepository hipLinkTokenRepository;

	@Autowired
	private JdbcTemplateHelper jdbcTemplate;

	// STEP 1.1 CALL BACK FOR STEP 1 IN NDHMM2CONTROLLER
	// HIP INITITATED LINKING
	@PostMapping(value = "users/auth/on-fetch-modes")
	private void usersAuthonfetchmodes(@RequestBody String onFetchModes, HttpServletRequest servletRequest) {
		log.info("request at ##users/auth/on-fetch-modes# start: {}==========>", onFetchModes);
		try {
			log.info(onFetchModes);
			OnFetchPayload onFetchClass = objMapper.readValue(onFetchModes, OnFetchPayload.class);
			allAbdmCentralDbSave.updateCallBackInCentralDb(Integer.parseInt(TenantContext.getCurrentTenant()),
					onFetchModes, null, onFetchClass.getResp().getRequestId(), null);
			this.saveOrResestCall("/abha/v0.5/users/auth/on-fetch-modes", onFetchModes, "ON-FETCH-MODES",
					onFetchClass.getResp().getRequestId(), null, onFetchModes, null, null, 2, null,
					this.ifError(onFetchClass.getError()));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception occured :{}==========>", e);
			log.error(e.getMessage());
			log.info("ON-FETCH CALLED UNABLE TO SAVE IN DB:{}==========>" + e.getLocalizedMessage());
		}
	}

	public void saveOrResestCall(String url, String payload, String apiType, String requestId, String txnId,
			String response, String consentRequestCode, String patientRefNo, Integer requestTypeId, String abhano,
			String errorMessage) throws JsonMappingException, JsonProcessingException {
		HIPTenantMappingDto hipTenantMappingDto = this.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
		if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
				&& !hipTenantMappingDto.getServerAccess()) {
			this.restCallTenantUrlForCallBacks(hipTenantMappingDto.getUrlPath() + url, payload, apiType, null);
		} else {
			webHookService.insertIntoHipCallBack(requestId, txnId, response, consentRequestCode, patientRefNo,
					requestTypeId, abhano, errorMessage);
		}
	}

	public void restCallTenantUrlForCallBacks(String url, String payload, String apiType, String hipOrHIuId) {
		try {
			HttpHeaders headers = new HttpHeaders();
			if (hipOrHIuId != null && !hipOrHIuId.isEmpty()) {
				headers.set(hipOrHIuId, hipOrHIuId);
			}
			headers.setContentType(MediaType.APPLICATION_JSON);
			log.info("restCallApi() method " + apiType + " " + payload);
			log.info("ABDM API CALLING =========>{},========> {},========> {}, =======>{}", url, apiType, payload,
					headers);
			HttpEntity<String> entity = new HttpEntity<>(payload, headers);
			template.postForEntity(url, entity, String.class);
		} catch (HttpClientErrorException exception) {
			log.error("restCallApi() method error HttpClientErrorException: " + exception.getResponseBodyAsString());
			exception.printStackTrace();
		} catch (HttpServerErrorException exception) {
			log.error("restCallApi() method error HttpServerErrorException: " + exception.getResponseBodyAsString());
			exception.printStackTrace();
		} catch (Exception exception) {
			log.error("restCallApi() method error: " + exception);
			exception.printStackTrace();
		}
	}

	// STEP 2.2 CALL BACK FOR STEP 2 IN NDHMM2CONTROLLER
	@PostMapping(value = "users/auth/on-init")
	public void usersAuthOnInit(@RequestBody String onInitRequest) {
		log.info("request at ##users/auth/on-init# start: {}", onInitRequest);
		log.info(onInitRequest);
		try {
			String txnId = "";
			AuthOnInitPayload authOnInit = objMapper.readValue(onInitRequest, AuthOnInitPayload.class);
			if (authOnInit != null && authOnInit.getError() == null && authOnInit.getAuth() != null) {
				txnId = authOnInit.getAuth().getTransactionId();
			}
			allAbdmCentralDbSave.updateCallBackInCentralDb(Integer.parseInt(TenantContext.getCurrentTenant()),
					onInitRequest, null, authOnInit.getResp().getRequestId(), txnId);

			this.saveOrResestCall("/abha/v0.5/users/auth/on-init", onInitRequest, "ON-INIT",
					authOnInit.getResp().getRequestId(),
					authOnInit.getAuth() != null ? authOnInit.getAuth().getTransactionId() : null, onInitRequest, null,
					null, 4, null, this.ifError(authOnInit.getError()));

//			webHookService.insertIntoHipCallBack(authOnInit.getResp().getRequestId(),
//					authOnInit.getAuth() != null ? authOnInit.getAuth().getTransactionId() : null, onInitRequest, null,
//					null, 4, null, this.ifError(authOnInit.getError()));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception occured :{}", e);
			log.error(e.getMessage());
			log.info("ON-INIT CALLED, UNABLE TO SAVE IN DB:{}==========>", onInitRequest);
		}
	}

	// STEP 3.1 CALL BACK FOR STEP 3 IN NDHMM2CONTROLLER
	// HIP INITITATED LINKING
	@PostMapping(value = "users/auth/on-confirm")
	public void usersAuthOnConfirm(@RequestBody String onConfirmrequest) {
		log.info("request at ##usersauthonconfirm# start:" + onConfirmrequest);
		log.info(onConfirmrequest);
		try {
			AuthOnConfirmPayload authOnConfirm = objMapper.readValue(onConfirmrequest, AuthOnConfirmPayload.class);
			allAbdmCentralDbSave.updateCallBackInCentralDb(Integer.parseInt(TenantContext.getCurrentTenant()),
					onConfirmrequest, null, authOnConfirm.getResp().getRequestId(), null);

			this.saveOrResestCall("/abha/v0.5/users/auth/on-confirm", onConfirmrequest, "ON-CONFIRM",
					authOnConfirm.getResp().getRequestId(), null, onConfirmrequest, null, null, 4, null,
					this.ifError(authOnConfirm.getError()));

//			webHookService.insertIntoHipCallBack(authOnConfirm.getResp().getRequestId(), null, onConfirmrequest, null,
//					null, 4, null, this.ifError(authOnConfirm.getError()));
			log.info("request at ##users/auth/on-confirm# end:");
		} catch (Exception e) {
			log.error("Exception occured :{}", e);
			log.error(e.getMessage());
			log.info("ON-INIT CALLED, UNABLE TO SAVE IN DB:{}==========>" + onConfirmrequest);
		}
		log.info("request at ##usersauthonconfirm# end:");
	}

	// STEP 4.1 CALL BACK FOR STEP 3 IN NDHMM2CONTROLLER
	// HIP INITITATED LINKING
	// DIRECT AUTH ONE OF THE DEMOGRAPHY MODE
	@PostMapping(value = "users/auth/notify")
	public void usersAuthNotify(@RequestBody String request, HttpServletRequest httpServletRequest) {
		log.info("request at ##usersauthnotify# start:" + request);
		log.info("{}==> " + request);
		HIPTenantMappingDto hipTenantMappingDto = this.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
		if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
				&& !hipTenantMappingDto.getServerAccess()) {
			this.restCallTenantUrlForCallBacks(hipTenantMappingDto.getUrlPath() + "/abha/v0.5/users/auth/notify",
					request, "DIRECT AUTH NOTIFY", httpServletRequest.getHeader("X-HIP-ID"));
			return;
		}

		String requestId = "";
		String healthId = "";
		Error error = null;
		DirectAuthOnNotify onNofity = null;
		DirectModeAuth directModeAuth = new DirectModeAuth();
		String status = "";
		String accessToken = "";
		String transactionId = "";
		String patientId = "";
		List<String> consultationIds = new ArrayList<>();
		List<com.dipl.abha.m2.addcarecontextpayload.CareContext> latestCareContet = new ArrayList<>();
		List<NotifyResultSetDto> careContextList = new ArrayList<>();
		try {
			directModeAuth = objMapper.readValue(request, DirectModeAuth.class);
			if (directModeAuth != null) {
				requestId = directModeAuth.getRequestId();
				if (directModeAuth.getAuth().getPatient() != null) {
					healthId = directModeAuth.getAuth().getPatient().getId();
				}
				if (directModeAuth.getAuth() != null) {
					status = directModeAuth.getAuth().getStatus();
					transactionId = directModeAuth.getAuth().getTransactionId();
					accessToken = directModeAuth.getAuth().getAccessToken();
				}

				webHookService.insertIntoHipCallBack(requestId, transactionId, request, null, null, 15, healthId, null);
				if (!transactionId.isEmpty()) {
					Map<String, Object> initResponse = hipCallBackRepository
							.findFirstByTxnCodeNoAndHipRequestTypeIdOrderByIdDesc(transactionId, 4);
					if (initResponse != null && !initResponse.isEmpty() && initResponse.get("response_json") != null) {
						AuthOnInitPayload onInitPayload = objMapper
								.readValue(initResponse.get("response_json").toString(), AuthOnInitPayload.class);
						if (onInitPayload != null && onInitPayload.getAuth() != null
								&& onInitPayload.getAuth().getMeta() != null
								&& onInitPayload.getAuth().getMeta().getExpiry() != null) {
							LocalDateTime expiryDate = onInitPayload.getAuth().getMeta().getExpiry().plusHours(5)
									.plusMinutes(30);
							consultationIds.add(initResponse.get("interaction_id").toString());
							List<AbhaQueryTable> requestQueries = jdbcTemplateHelper
									.getResults("select * from public.abha_query_table where  tenant_id = "
											+ TenantContext.getCurrentTenant()
											+ " and query_type in ('GET-CONSULTATION-CARE-CONTEXTS-FOR-DIRECT-AUTH',"
											+ "'GET-LAB-CARE-CONTEXTS-FOR-DIRECT-AUTH','GET-RADIOLOGY-CARE-CONTEXTS-FOR-DIRECT-AUTH',"
											+ "'GET-DISCHARGE-SUMMARY-CARE-CONTEXTS-FOR-DIRECT-AUTH',"
											+ "'GET-PATIENT-ID')", AbhaQueryTable.class);
							if (requestQueries != null && !requestQueries.isEmpty()) {
								PatientIdDto patienIdDto = extractResultSetService
										.executePatientIdQuery(webHookService.returnFinalQueryForDirectAuth(
												this.streamAndReturnQuery("GET-PATIENT-ID", requestQueries),
												webHookService.returnCareContextsIdsAsString(consultationIds)));
								if (patienIdDto != null) {
									patientId = patienIdDto.getPatientId();
								}
								if (himsTenantIds.contains(Integer.parseInt(TenantContext.getCurrentTenant()))) {
									if (isNumeric(initResponse.get("interaction_id").toString())) {
										careContextList.addAll(extractResultSetService
												.executeDynamicQueryFromDBForNotify(webHookService
														.returnFinalQueryForDirectAuth(this.streamAndReturnQuery(
																"GET-CONSULTATION-CARE-CONTEXTS-FOR-DIRECT-AUTH",
																requestQueries),
																webHookService.returnCareContextsIdsAsString(
																		consultationIds))));
									} else {
										if (initResponse.get("interaction_id").toString().contains("LR")
												|| initResponse.get("interaction_id").toString().contains("LBILL")
												|| initResponse.get("interaction_id").toString()
														.contains("RETAILLABBILL")
												|| initResponse.get("interaction_id").toString().contains("LAB")
												|| initResponse.get("interaction_id").toString()
														.contains("RETAILLABBILL")) {
											careContextList
													.addAll(extractResultSetService.executeDynamicQueryFromDBForNotify(
															webHookService.returnFinalQueryForDirectAuth(
																	this.streamAndReturnQuery(
																			"GET-LAB-CARE-CONTEXTS-FOR-DIRECT-AUTH",
																			requestQueries),
																	webHookService.returnCareContextsIdsAsString(
																			consultationIds))));
										} else if (initResponse.get("interaction_id").toString().startsWith("RR")) {
											careContextList.addAll(extractResultSetService
													.executeDynamicQueryFromDBForNotify(webHookService
															.returnFinalQueryForDirectAuth(this.streamAndReturnQuery(
																	"GET-RADIOLOGY-CARE-CONTEXTS-FOR-DIRECT-AUTH",
																	requestQueries),
																	webHookService.returnCareContextsIdsAsString(
																			consultationIds))));
										} else if (initResponse.get("interaction_id").toString().startsWith("DI")) {
											careContextList.addAll(extractResultSetService
													.executeDynamicQueryFromDBForNotify(webHookService
															.returnFinalQueryForDirectAuth(this.streamAndReturnQuery(
																	"GET-DISCHARGE-SUMMARY-CARE-CONTEXTS-FOR-DIRECT-AUTH",
																	requestQueries),
																	webHookService.returnCareContextsIdsAsString(
																			consultationIds))));
										}
									}
								} else {
									if (isNumeric(initResponse.get("interaction_id").toString())) {
										careContextList.addAll(extractResultSetService
												.executeDynamicQueryFromDBForNotify(webHookService
														.returnFinalQueryForDirectAuth(this.streamAndReturnQuery(
																"GET-CONSULTATION-CARE-CONTEXTS-FOR-DIRECT-AUTH",
																requestQueries),
																webHookService.returnCareContextsIdsAsString(
																		consultationIds))));
									} else if (initResponse.get("interaction_id").toString().contains("LR")
											|| initResponse.get("interaction_id").toString().contains("LBILL")
											|| initResponse.get("interaction_id").toString().contains("RETAILLABBILL")
											|| initResponse.get("interaction_id").toString().contains("LAB")
											|| initResponse.get("interaction_id").toString()
													.contains("RETAILLABBILL")) {
										careContextList
												.addAll(extractResultSetService.executeDynamicQueryFromDBForNotify(
														webHookService.returnFinalQueryForDirectAuth(
																this.streamAndReturnQuery(
																		"GET-LAB-CARE-CONTEXTS-FOR-DIRECT-AUTH",
																		requestQueries),
																webHookService.returnCareContextsIdsAsString(
																		consultationIds))));
									}
								}
								if (expiryDate.isAfter(LocalDateTime.now())) {
									if (!careContextList.isEmpty()) {
										if (status.equals("GRANTED")) {
											onNofity = webHookService.constructDirectAuthOnNotify(requestId, error);
											ndhmController.directUserAuthOnNofity(onNofity);
											AddCareContextPayload addCareContext = new AddCareContextPayload();
											addCareContext.setRequestId(UUID.randomUUID().toString());
											addCareContext.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
											careContextList.forEach(s -> {
												com.dipl.abha.m2.addcarecontextpayload.CareContext newCareContext = new com.dipl.abha.m2.addcarecontextpayload.CareContext();
												newCareContext.setReferenceNumber(s.getCareContextRef());
												newCareContext.setDisplay(s.getCareContextDisplay());
												latestCareContet.add(newCareContext);
											});
											com.dipl.abha.m2.addcarecontextpayload.Patient patient = new com.dipl.abha.m2.addcarecontextpayload.Patient();
											patient.setCareContexts(latestCareContet);
											patient.setReferenceNumber(
													careContextList.get(0).getPatientReferenceNumber());
											patient.setDisplay(careContextList.get(0).getPatientDisplay());
											Link link = new Link();
											link.setAccessToken(accessToken);
											link.setPatient(patient);
											addCareContext.setLink(link);
											Optional<String> s = Optional
													.of(initResponse.get("interaction_id").toString());
											ndhmController
													.addContext(addCareContext, s, Optional.empty(),
															this.directAuthStatus(directModeAuth.getTimestamp()
																	.plusHours(5).plusMinutes(30).toString(), status),
															httpServletRequest);

										} else {
											if (initResponse.get("interaction_id") != null) {
												CareContextLogs careContextLog = new CareContextLogs();
												careContextLog.setBeneficiaryId(Long.parseLong(patientId));
												careContextLog.setPatietInterationId(
														initResponse.get("interaction_id").toString());
												careContextLog.setDirectAuthStatus(
														this.directAuthStatus(directModeAuth.getTimestamp().plusHours(5)
																.plusMinutes(30).toString(), status));
												careContextLogsRepository.save(careContextLog);
												onNofity = webHookService.constructDirectAuthOnNotify(requestId, error);
												ndhmController.directUserAuthOnNofity(onNofity);
											}
										}
									} else {
										onNofity = webHookService.constructDirectAuthOnNotify(requestId,
												new Error(1001, "CARE CONTEXT ALREADY LINKED USING OTHER AUTH MODES"));
										ndhmController.directUserAuthOnNofity(onNofity);

									}
								} else {
									CareContextLogs careContextLog = new CareContextLogs();
									careContextLog.setBeneficiaryId(Long.parseLong(patientId));
									careContextLog.setPatietInterationId(initResponse.get("interaction_id").toString());
									careContextLog.setDirectAuthStatus(this.directAuthStatus(
											directModeAuth.getTimestamp().plusHours(5).plusMinutes(30).toString(),
											status));
									careContextLogsRepository.save(careContextLog);
									onNofity = webHookService.constructDirectAuthOnNotify(requestId,
											new Error(1017, "Invalid TransactionId or TransactionId Expired"));
									ndhmController.directUserAuthOnNofity(onNofity);
								}
							} else {
								log.info("NO QUERIS RETURENED from DB to get care context records for TENANT ID===>{}",
										TenantContext.getCurrentTenant());
							}
						}
					} else {
						log.info("COULD NOT DETERMINE ON-INIT BASED ON TRANSACTION ID...");
					}
				} else {
					log.info("TRANSACTION ID IS NOT SAVE WHEN ON-INIT SAVED");
				}
			}
		} catch (Exception e) {
			log.error("EXCEPTION IN USER AUTH NOTIFY");
			e.printStackTrace();
		}
	}

	public String streamAndReturnQuery(String apiType, List<AbhaQueryTable> abhaQueriesList) {
		Optional<String> findFirst = abhaQueriesList.stream().filter(s -> s.getQueryType().equals(apiType))
				.map(p -> p.getQuery()).findFirst();
		if (findFirst.isPresent()) {
			return findFirst.get();
		}
		return "";
	}

	// STEP 5.1 CALL BACK FOR STEP 5 IN NDHMM2CONTROLLER
	// HIP INITITATED LINKING
	@PostMapping(value = "links/link/on-add-contexts")
	public void linksLinkOnAddContexts(@RequestBody String onAddContextrequest) {
		log.info("request at ##links/link/on-add-contexts# start:" + onAddContextrequest);
		log.info("{}==> " + onAddContextrequest);
		try {
			OnAddCareContext onAddCareContext = objMapper.readValue(onAddContextrequest, OnAddCareContext.class);

			HIPTenantMappingDto hipTenantMappingDto = this.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
			if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
					&& !hipTenantMappingDto.getServerAccess()) {
				this.restCallTenantUrlForCallBacks(
						hipTenantMappingDto.getUrlPath() + "/abha/v0.5/links/link/on-add-contexts", onAddContextrequest,
						"ON-ADD-CARE-CONTEXT", null);
				return;
			}

			List<CareContextLogs> contextLogs = new ArrayList<>();
			String requestId = "";
			requestId = onAddCareContext.getResp().getRequestId();
			webHookService.insertIntoHipCallBack(requestId, null, onAddContextrequest, null, null, 8, null,
					this.ifError(onAddCareContext.getError()));
			if (onAddCareContext.getError() == null
					&& "SUCCESS".equalsIgnoreCase(onAddCareContext.getAcknowledgement().getStatus())) {
				HIPRequest hipRequest = hipRequestRepo.findByRequestCodeAndHipRequestTypeId(requestId, 7);
				HIPRequest hipRequestNew = hipRequestRepo.findByRequestCodeAndHipRequestTypeId(requestId, 35);
				if (hipRequest != null) {
					AddCareContextPayload addCareContext = objMapper.readValue(hipRequest.getRequestJson().toString(),
							AddCareContextPayload.class);
					if (addCareContext != null) {
						List<com.dipl.abha.m2.addcarecontextpayload.CareContext> careContexts = addCareContext.getLink()
								.getPatient().getCareContexts();
						if (!careContexts.isEmpty()) {
							List<String> interactionsId = new ArrayList<>();
							interactionsId = careContexts.stream().filter(s -> s.getReferenceNumber() != null)
									.map(p -> p.getReferenceNumber()).collect(Collectors.toList());
							String patieneref = addCareContext.getLink().getPatient().getReferenceNumber();
							for (String s : interactionsId) {
								CareContextLogs careContextLog = new CareContextLogs();
								careContextLog.setBeneficiaryId(
										(patieneref != null && !patieneref.isEmpty()) ? Long.parseLong(patieneref)
												: null);
								careContextLog.setPatietInterationId(s);
								careContextLog.setDirectAuthStatus(hipRequest.getConsentArtefactCode() != null
										? hipRequest.getConsentArtefactCode()
										: null);
								contextLogs.add(careContextLog);
							}
							careContextLogsRepository.saveAll(contextLogs);
						}
					} else {
						log.info("Could not find add-care-context in linksLinkOnAddContexts");
					}
				} else if (hipRequestNew != null) {
					LinkCareContextV3Payload addCareContext = objMapper
							.readValue(hipRequestNew.getRequestJson().toString(), LinkCareContextV3Payload.class);
					if (addCareContext != null) {
						List<com.dipl.abha.m2.addcarecontextpayload.CareContext> careContexts = addCareContext
								.getPatient().get(0).getCareContexts();
						if (!careContexts.isEmpty()) {
							List<String> interactionsId = new ArrayList<>();
							interactionsId = careContexts.stream().filter(s -> s.getReferenceNumber() != null)
									.map(p -> p.getReferenceNumber()).collect(Collectors.toList());
							String patieneref = addCareContext.getPatient().get(0).getReferenceNumber();
							for (String s : interactionsId) {
								CareContextLogs careContextLog = new CareContextLogs();
								careContextLog.setBeneficiaryId(
										(patieneref != null && !patieneref.isEmpty()) ? Long.parseLong(patieneref)
												: null);
								careContextLog.setPatietInterationId(s);
								careContextLog.setDirectAuthStatus(null);
								contextLogs.add(careContextLog);
							}
							careContextLogsRepository.saveAll(contextLogs);
						}
					}
				} else {
					log.info("Could not find add-care-context in linksLinkOnAddContexts");
				}
			} else {
				log.info("Error in on-add-care-context" + onAddCareContext.getError());
			}
		} catch (Exception e) {
			log.error("Exception occured :{}", e);
			e.printStackTrace();
			log.error(e.getMessage());
			log.info("ON-ADD-CARE-CONTEXT CALLED, UNABLE TO SAVE IN DB:{}==========>" + onAddContextrequest);
		}
		log.info("request at ##links/link/on-add-contexts# end:");
	}

	// STEP 1
	// USER INITIATED LINKING
	@PostMapping(value = "care-contexts/discover")
	public void hipDiscovery(@RequestBody String request, HttpServletRequest httpServletRequest) {
		log.info("request at ##carecontextsdiscovery# start:" + request);
		log.info("{}==> " + request);
		HIPTenantMappingDto hipTenantMappingDto = this.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
		if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
				&& !hipTenantMappingDto.getServerAccess()) {
			this.restCallTenantUrlForCallBacks(hipTenantMappingDto.getUrlPath() + "/abha/v0.5/care-contexts/discover",
					request, "CARE-CONTEXT-DISCOVER", httpServletRequest.getHeader("X-HIP-ID"));
			return;
		}
		String requestId = "";
		String transactionId = "";
		String mobileNo = "";
		String healthNumber = "";
		String ndhmHealthId = "";
		patient patient = null;
		String healthId = "";
		List<String> matchedBy = new ArrayList<String>();
		Error error = null;
		String patientRef = "";
		String patientDisplay = "";
		List<AbhaRegistrationDTO> findByMobile = new ArrayList<>();
		AbhaRegistrationDTO patientDetails = null;
		try {
			DiscoverPayload discoverPayload = objMapper.readValue(request, DiscoverPayload.class);
			requestId = discoverPayload.getRequestId();
			transactionId = discoverPayload.getTransactionId();
			healthId = discoverPayload.getPatient().getId();
			webHookService.insertIntoHipCallBack(requestId, transactionId, request, null, null, 9, healthId, null);
			List<VerifiedIdentifiers> verifiedIdentifiers = null;
			int genderId = 0;
			if (discoverPayload != null) {
				patient = discoverPayload.getPatient();
				healthId = patient.getId();
				verifiedIdentifiers = patient.getVerifiedIdentifiers();
				if (patient.getGender() == 'M') {
					genderId = 1;
				} else if (patient.getGender() == 'F') {
					genderId = 2;
				}
			}
			Optional<VerifiedIdentifiers> verified = Optional.empty();
			if (verifiedIdentifiers != null) {
				verified = verifiedIdentifiers.stream().filter(s -> s.getType().equals("MOBILE")).findFirst();
				if (verified.isPresent()) {
					mobileNo = verified.get().getValue() != null ? verified.get().getValue() : mobileNo;
				}
				verified = verifiedIdentifiers.stream().filter(s -> s.getType().equals("NDHM_HEALTH_NUMBER"))
						.findFirst();
				if (verified.isPresent()) {
					healthNumber = verified.get().getValue() != null ? verified.get().getValue() : healthNumber;
				}
				verified = verifiedIdentifiers.stream().filter(s -> s.getType().equals("HEALTH_ID")).findFirst();
				if (verified.isPresent()) {
					ndhmHealthId = verified.get().getValue() != null ? verified.get().getValue() : ndhmHealthId;
				}
			}
			String query = "";
			String newMobile = mobileNo.length() > 10 ? mobileNo.substring(4, mobileNo.length()) : mobileNo;
			if (!ndhmHealthId.isEmpty() && !healthNumber.isEmpty()) {
				query = "select * from public.abha_registration where tenant_id ='" + TenantContext.getCurrentTenant()
						+ "' and abha_address = '" + ndhmHealthId + "' and abha_no = '" + healthNumber
						+ "' order by id desc limit 1";
				matchedBy.add("NDHM_HEALTH_NUMBER");
				matchedBy.add("HEALTH_ID");
			} else if (!ndhmHealthId.isEmpty()) {
				query = "select * from public.abha_registration where tenant_id ='" + TenantContext.getCurrentTenant()
						+ "' and abha_address = '" + ndhmHealthId + "' order by id desc limit 1";
				matchedBy.add("HEALTH_ID");
			} else if (!healthNumber.isEmpty()) {
				query = "select * from public.abha_registration where tenant_id ='" + TenantContext.getCurrentTenant()
						+ "' and abha_no = '" + healthNumber + "' order by id desc limit 1";
				matchedBy.add("NDHM_HEALTH_NUMBER");
			}
			Object object = jdbcTemplateHelper.getAbhaRegistration(query);
			if (object != null) {
				patientDetails = (AbhaRegistrationDTO) object;
				matchedBy = webHookService.searchByManyMatches(patientDetails, newMobile, patient.getGender(),
						patient.getYearOfBirth());
			} else {
				if (!mobileNo.isEmpty()) {
					query = "select * from public.abha_registration ar where ar.mobile_no = '" + newMobile
							+ "' and ar.tenant_id ='" + TenantContext.getCurrentTenant() + "' order by id desc limit 1";
					List<AbhaRegistrationDTO> abhaRegistration = jdbcTemplateHelper.getResults(query,
							AbhaRegistrationDTO.class);
					if (abhaRegistration != null && !abhaRegistration.isEmpty()) {
						if (abhaRegistration.size() > 1) {
							Map<Long, List<String>> map = new HashMap<>();
							for (AbhaRegistrationDTO s : abhaRegistration) {
								map.put(s.getPatient_id(), webHookService.searchByManyMatches(s, newMobile,
										patient.getGender(), patient.getYearOfBirth()));
							}
							Long benWithManyMatches = map.entrySet().stream()
									.max(Comparator.comparingInt(entry -> entry.getValue().size()))
									.map(Map.Entry::getKey).orElse(null);
							matchedBy = map.entrySet().stream()
									.max(Comparator.comparingInt(entry -> entry.getValue().size()))
									.map(Map.Entry::getValue).orElse(null);
							if (benWithManyMatches != null) {
								Optional<AbhaRegistrationDTO> benRegistrationOptional = findByMobile.stream()
										.filter(s -> s.getPatient_id().equals(benWithManyMatches)).findFirst();
								if (benRegistrationOptional.isPresent()) {
									patientDetails = benRegistrationOptional.get();
								}
							}
						} else {
							patientDetails = abhaRegistration.get(0);
							matchedBy = webHookService.searchByManyMatches(patientDetails, mobileNo,
									patient.getGender(), patient.getYearOfBirth());
						}
					} else {
						log.info("NO patient Found with Mobile No...");
					}
				}
			}

			if (patientDetails != null) {
				patientRef = patientDetails.getPatient_id() + "";
				if (patientDetails.getFullName() != null && !patientDetails.getFullName().isEmpty()) {
					patientDisplay = patientDetails.getFullName();
				}
				if (patientDetails.getIs_abha_linked() != null
						&& (patientDetails.getIs_abha_linked() == 0 || patientDetails.getIs_abha_linked() == 2)) {
					error = new Error(1023, "PATIENT IS OUT OF ABHA");
				}
			} else {
				error = new Error(1023, "NO PATIENT FOUND");
			}

//			OnDiscoverPayload onDiscoverPayload = webHookService.findCareContexts(
//					patientDetails != null ? Optional.ofNullable(patientDetails) : Optional.empty(), requestId,
//					transactionId, error, matchedBy, patientRef, patientDisplay, healthNumber);
			CareContextOnDiscoverV3 onDiscoverPayload = webHookService.findCareContexts(
					patientDetails != null ? Optional.ofNullable(patientDetails) : Optional.empty(), requestId,
					transactionId, error, matchedBy, patientRef, patientDisplay, healthNumber);

			String onDiscoverPayloadString = objMapper.writeValueAsString(onDiscoverPayload);
			log.info(onDiscoverPayloadString);

			ndhmController.careContextOnDiscoverV3(onDiscoverPayload);

//			ndhmController.onHIPDiscovery(onDiscoverPayload);
		} catch (Exception e) {
			log.error("Exception occured :{}", e);
			log.error(e.getMessage());
			log.info("DISCOVER CALLED BY NDHM, UNABLE TO SAVE IN DB:{}==========>" + request);
		}
		log.info("request at ##carecontextsdiscovery# end:");
	}

	// USER INITITATED LINKING
	// STEP 2
	@PostMapping(value = "links/link/init")
	public void linkInit(@RequestBody String request, HttpServletRequest httpServletRequest) {
		String transactionId = "";
		HIPTenantMappingDto hipTenantMappingDto = this.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
		if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
				&& !hipTenantMappingDto.getServerAccess()) {
			this.restCallTenantUrlForCallBacks(hipTenantMappingDto.getUrlPath() + "/abha/v0.5/links/link/init", request,
					"CARE-CONTEXT-DISCOVER", httpServletRequest.getHeader("X-HIP-ID"));
			return;
		}
		try {
			log.info("request at ##Links Init# start: {} ===>", request);
			LinksInit linkInitPayload = objMapper.readValue(request, LinksInit.class);
			String requestId = linkInitPayload.getRequestId();
			transactionId = linkInitPayload.getTransactionId();
			String referenceNumber = linkInitPayload.getPatient().getReferenceNumber();
			LinksPatient patient = linkInitPayload.getPatient();
			String healthId = patient.getId();
			webHookService.insertIntoHipCallBack(requestId, transactionId, request, null, referenceNumber, 11, healthId,
					null);
			log.info("Links Init saved in db");
			if (!referenceNumber.isEmpty()) {
				String query = "select * from public.abha_registration where tenant_id ='"
						+ TenantContext.getCurrentTenant() + "' and patient_id = '" + referenceNumber + "' limit 1";
				List<AbhaRegistrationDTO> abhaRegistration = jdbcTemplateHelper.getResults(query,
						AbhaRegistrationDTO.class);
				if (abhaRegistration != null && !abhaRegistration.isEmpty()
						&& abhaRegistration.get(0).getMobile_no() != null) {
					String mobileNo = abhaRegistration.get(0).getMobile_no();
					if (!mobileNo.isEmpty()) {
						log.info("Mobile No of Patient ", mobileNo);
						hipCallBackRepository.updatePatientMobileNoInHIPCallBack(mobileNo, requestId);
						try {
							log.info("Mobile No of Patient ==> {}", generateOtp + "&mobile_no=" + mobileNo
									+ "&operation=ndhm-care-context-link&carecontexts=");
							template.getForObject(generateOtp + "&mobile_no=" + mobileNo
									+ "&operation=ndhm-care-context-link&carecontexts=", String.class);
						} catch (Exception e) {
							Error error = new Error(1231, "Dependent Serice Failure");
							LinksOnInit linksOnInit = webHookService.constructLinksOnInit(requestId, transactionId,
									referenceNumber, error);
//							ndhmController.linksOnInit(linksOnInit, transactionId);
							ndhmController.careContextV3LinksOnInit(linksOnInit);
							return;
						}
					}
				}
			}
			LinksOnInit linksOnInit = webHookService.constructLinksOnInit(requestId, transactionId, referenceNumber,
					null);
//			ndhmController.linksOnInit(linksOnInit, transactionId);
			ndhmController.careContextV3LinksOnInit(linksOnInit);
		} catch (Exception e) {
			log.error("Exception occured :{}", e);
			log.error(e.getMessage());
			log.info("LINKS INIT CALLED BY NDHM, UNABLE TO SAVE IN DB:{}==========>" + request);
			e.printStackTrace();
		}
	}

	// USER INITIATED LINKING
	// STEP 3
	@PostMapping(value = "links/link/confirm")
	public void linkConfirm(@RequestBody String request, HttpServletRequest httpServletRequest) {
		try {
			HIPTenantMappingDto hipTenantMappingDto = this.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
			if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
					&& !hipTenantMappingDto.getServerAccess()) {
				this.restCallTenantUrlForCallBacks(hipTenantMappingDto.getUrlPath() + "/abha/v0.5/links/link/confirm",
						request, "CARE-CONTEXT-LINKS-LINK", httpServletRequest.getHeader("X-HIP-ID"));
				return;
			}
			log.info("request at ##Links Confirm# start:" + request);
			log.info("{}==> " + request);
			List<CareContextLogs> careContextsList = new ArrayList<>();
			List<CareContextLogs> vmedCareContextsLogs = new ArrayList<>();
			Error error = null;
			String transactionId = "";
			LinksInit linkInit = null;
			Set<String> careContext = new HashSet<>();
			LinksConfirmPayload linkConfirmPayload = objMapper.readValue(request, LinksConfirmPayload.class);
			if (linkConfirmPayload != null) {
				String requestId = linkConfirmPayload.getRequestId();
				String patientRef = linkConfirmPayload.getConfirmation().getLinkRefNumber();
				HIPCallback callBack = hipCallBackRepository
						.findFirstByPatientRefNoAndHipRequestTypeIdOrderByIdDesc(patientRef, 11);
				if (callBack != null && callBack.getResponseJson() != null) {
					linkInit = objMapper.readValue(callBack.getResponseJson().toPrettyString(), LinksInit.class);
					if (linkInit != null) {
						transactionId = linkInit.getTransactionId();
						if (linkInit.getPatient() != null && linkInit.getPatient().getCareContexts() != null) {
							careContext = linkInit.getPatient().getCareContexts().stream()
									.filter(s -> s.getReferenceNumber() != null)
									.map(p -> p.getReferenceNumber().toString()).collect(Collectors.toSet());
						}
					}
					String mobileNo = callBack.getConsentRequestCode();
					String getOtp = linkConfirmPayload.getConfirmation().getToken();
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					HashMap<String, Integer> request1 = new HashMap<>();
					request1.put(mobileNo, Integer.parseInt(getOtp));
					HttpEntity<Object> requests = new HttpEntity<>(request1, headers);
					String response = template.postForObject(validateOtp, requests, String.class);
					System.out.println(response);
					if (response != null) {
						JSONObject json = new JSONObject(response);
						if (json.get("response").toString().equalsIgnoreCase("OTP verification Success")) {
							System.out.println("OTP VALIDTED SUCCESSFULLY");
							if (!careContext.isEmpty()) {
								for (String s : careContext) {
									CareContextLogs careContextLogs = new CareContextLogs();
									if (himsTenantIds.contains(Integer.parseInt(TenantContext.getCurrentTenant()))) {
										if (NDHMM2CallBackController.isNumeric(s)) {
											if (Long.parseLong(s) < 10000000000L) {
												CareContextLogs careContextLog = new CareContextLogs();
												careContextLog.setBeneficiaryId(Long.parseLong(patientRef));
												careContextLog.setPatietInterationId(s);
												vmedCareContextsLogs.add(careContextLog);
											} else {
												careContextLogs.setBeneficiaryId(Long.parseLong(patientRef));
												careContextLogs.setPatietInterationId(s);
												careContextsList.add(careContextLogs);
											}
										} else {
											careContextLogs.setBeneficiaryId(Long.parseLong(patientRef));
											careContextLogs.setPatietInterationId(s);
											careContextsList.add(careContextLogs);
										}
									} else {
										careContextLogs.setBeneficiaryId(Long.parseLong(patientRef));
										careContextLogs.setPatietInterationId(s);
										careContextsList.add(careContextLogs);
									}
								}
								Object tenantUrlIsIntegratedModule = jdbcTemplateHelper.getTenantUrlIsIntegratedModule(
										"select is_integrated_module, integrated_tenant_id  from orgnization_registration or2 \r\n"
												+ "			inner join integrated_tenant_mapping on tenant_id = or2.id where or2.id = '"
												+ TenantContext.getCurrentTenant() + "'");
								if (tenantUrlIsIntegratedModule != null) {
									TenanIdAndIsIntegratedModule tenantIdofIntegratedModule = (TenanIdAndIsIntegratedModule) tenantUrlIsIntegratedModule;
									if (tenantIdofIntegratedModule.getIs_integrated_module()) {
										if (patientRef != null && !patientRef.isEmpty()) {
											String query = "select * from public.abha_registration where tenant_id ='"
													+ TenantContext.getCurrentTenant() + "' and patient_id = '"
													+ patientRef + "' limit 1";
											List<AbhaRegistrationDTO> abhaRegistration = jdbcTemplateHelper
													.getResults(query, AbhaRegistrationDTO.class);
											if (abhaRegistration != null && !abhaRegistration.isEmpty()
													&& abhaRegistration.get(0) != null) {
												Object abhaRegistrationObj = jdbcTemplateHelper.getAbhaRegistration(
														"select * from public.abha_registration where tenant_id ='"
																+ tenantIdofIntegratedModule.getIntegrated_tenant_id()
																+ "' and abha_no = '"
																+ abhaRegistration.get(0).getAbha_no() + "' limit 1");
												if (abhaRegistrationObj != null) {
													List<AbhaQueryTable> notifyTenantQueries = jdbcTemplateHelper
															.getResults(
																	"select * from public.abha_query_table where  tenant_id = "
																			+ TenantContext.getCurrentTenant()
																			+ " and query_type in ('VMED-SAVE-CARE-CONTEXST-LOGS')",
																	AbhaQueryTable.class);
													if (!notifyTenantQueries.isEmpty()
															&& !vmedCareContextsLogs.isEmpty()) {
														Set<String> collect = vmedCareContextsLogs.stream()
																.map(m -> m.getPatietInterationId())
																.collect(Collectors.toSet());
														Map<String, Set<String>> payload = new HashMap<>();
														payload.put("interactionIds", collect);
														String url = abhaM2Service.streamAndReturnQuery(
																"VMED-SAVE-CARE-CONTEXST-LOGS", notifyTenantQueries);
														if (!url.isEmpty()) {
															abhaM2Service.vmedRestCall(
																	objMapper.writeValueAsString(payload), url,
																	"VMED-SAVE-CARE-CONTEXST-LOGS");
														}
													}
												}
											}
										}
									}
								}
								careContextLogsRepository.saveAll(careContextsList);
							}
						} else {
							error = new Error();
							error.setMessage("OTP does not matched");
							error.setStatus(1035);
						}
					}
				}
				HIPCallback insertIntoHipCallBack = webHookService.insertIntoHipCallBack(requestId, transactionId,
						request, null, null, 19, null, error != null ? error.toString() : null);
				if (insertIntoHipCallBack != null) {
					log.info("Links Confirm saved in db");
				}
				LinkOnConfirm linksOnInit = webHookService.constructLinksOnConfirmPayload(requestId, patientRef, error,
						careContext);
//				ndhmController.linksOnConfirm(linksOnInit);
				ndhmController.linksOnConfirmv3(linksOnInit);
			}
		} catch (Exception e) {
			log.info("LINKS CONFIRM CALLED BY NDHM, UNABLE TO SAVE IN DB:{}==========>" + request);
			log.error("Exception occured :{}", e);
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

//
//	// DATA TRANSFER INTIATES FROM HERE
//	// SETP 1 CM CALLS THIS API TO NOTIFY THE HIP
	@PostMapping(value = "consents/hip/notify")
	public void consentsHipNotify(@RequestBody String request, HttpServletRequest servletRequest) {
		log.info("request at ##NOTIFY CALLING# start:");
		log.info("{}==> " + request);
		List<String> careContextNo = new ArrayList<String>();
		try {
			HIPTenantMappingDto hipTenantMappingDto = this.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
			if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
					&& !hipTenantMappingDto.getServerAccess()) {
				this.restCallTenantUrlForCallBacks(hipTenantMappingDto.getUrlPath() + "/abha/v0.5/consents/hip/notify",
						request, "HIP-NOFITY", servletRequest.getHeader("X-HIP-ID"));
				return;
			}
			LocalDateTime fromDate = null;
			LocalDateTime toDate = null;
			String concentId = "";
			String requestId = "";
			String status = "";
			String healthId = "";
			Error error = null;
			OnNotifyPayload onNotifyPayload = null;
			List<String> consultationIds = new ArrayList<>();
			List<String> labResultIds = new ArrayList<>();
			List<String> radiologyResultIds = new ArrayList<>();
			List<String> dischargeSummaryIds = new ArrayList<>();
			List<NotifyResultSetDto> careContextRecords = new ArrayList<>();
			allAbdmCentralDbSave.processPayloadNotify(request, 2, servletRequest);
			NotifyPayload notify = objMapper.readValue(request, NotifyPayload.class);
			if (notify != null) {
				requestId = notify.getRequestId();
				concentId = notify.getNotification().getConsentId();
				if (!notify.getNotification().getStatus().equals("GRANTED")) {
					status = notify.getNotification().getStatus();
				} else {
					status = "OK";
					if (notify.getNotification() != null && notify.getNotification().getConsentDetail() != null
							&& notify.getNotification().getConsentDetail().getCareContexts() != null) {
						careContextNo = notify.getNotification().getConsentDetail().getCareContexts().stream()
								.filter(s -> s.getCareContextReference() != null).map(p -> p.getCareContextReference())
								.collect(Collectors.toList());
					}
					if (notify.getNotification().getConsentDetail().getPatient() != null
							&& notify.getNotification().getConsentDetail().getPatient().getId() != null) {
						String query = "select * from public.abha_registration where tenant_id ='"
								+ TenantContext.getCurrentTenant() + "' and abha_address = '"
								+ notify.getNotification().getConsentDetail().getPatient().getId() + "' limit 1";
						log.info("Executing Query :{}", query);
						Object object = jdbcTemplateHelper.getAbhaRegistration(query);
						AbhaRegistrationDTO abhaRegistration = object != null ? (AbhaRegistrationDTO) object : null;
						if (abhaRegistration != null) {
							healthId = abhaRegistration.getAbha_no();
							if (abhaRegistration.getIs_abha_linked() != null
									&& (abhaRegistration.getIs_abha_linked() == 0
											|| abhaRegistration.getIs_abha_linked() == 2)) {
								error = new Error(1013, "PATIENT UNLIKED OR DELETED FROM ABHA");
							} else if (notify.getNotification().getConsentDetail().getPermission() != null && notify
									.getNotification().getConsentDetail().getPermission().getDateRange() != null) {
								toDate = notify.getNotification().getConsentDetail().getPermission().getDateRange()
										.getMyto();
								fromDate = notify.getNotification().getConsentDetail().getPermission().getDateRange()
										.getFrom();
								if (notify.getNotification().getConsentDetail().getPermission().getDataEraseAt()
										.isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
									error = new Error(1012, "CONSENT ARTIFACT EXPIRED");
								}
							}
							List<AbhaQueryTable> notifyTenantQueries = jdbcTemplateHelper.getResults(
									"select * from public.abha_query_table where  tenant_id = "
											+ TenantContext.getCurrentTenant()
											+ " and query_type in ('CONSULTATION-WITH-DATE-RANGE-FOR-NOTIFY','LAB-REPORTS-WITH-DATE-RANGE-FOR-NOTIFY','RADIOLOGY-REPORTS-WITH-DATE-RANGE-FOR-NOTIFY','DISCHARGE-SUMMARY-WITH-DATE-RANGE-FOR-NOTIFY','VMED_GET_DATA_NOTIFY')",
									AbhaQueryTable.class);
							Set<Long> vmedConsultations = new HashSet<>();
							Object tenantUrlIsIntegratedModule = jdbcTemplateHelper.getTenantUrlIsIntegratedModule(
									"select is_integrated_module, integrated_tenant_id  from orgnization_registration or2 \r\n"
											+ "			inner join integrated_tenant_mapping on tenant_id = or2.id where or2.id = '"
											+ TenantContext.getCurrentTenant() + "'");
							careContextNo.forEach(s -> {
								if (NDHMM2CallBackController.isNumeric(s)) {
									if (himsTenantIds.contains(Integer.parseInt(TenantContext.getCurrentTenant()))) {
										TenanIdAndIsIntegratedModule isIntegratedModule = tenantUrlIsIntegratedModule != null
												? (TenanIdAndIsIntegratedModule) tenantUrlIsIntegratedModule
												: null;
										if (isIntegratedModule != null
												&& isIntegratedModule.getIs_integrated_module()) {
											if (Long.parseLong(s) < 10000000000L) {
												vmedConsultations.add(Long.parseLong(s));
											} else {
												consultationIds.add(s);
											}
										} else {
											consultationIds.add(s);
										}
									} else {
										consultationIds.add(s);
									}
								} else {
									if (s.contains("LBILL") || s.contains("RETAILLABBILL") || s.contains("LAB")
											|| s.contains("RETAILLABBILL")) {
										labResultIds.add(s);
									}
									if (s.startsWith("LR")) {
										labResultIds.add(s);
									} else if (s.startsWith("RR")) {
										radiologyResultIds.add(s);
									} else if (s.startsWith("DI")) {
										dischargeSummaryIds.add(s);
									}
								}
							});

							if (himsTenantIds.contains(Integer.parseInt(TenantContext.getCurrentTenant()))) {
								if (!vmedConsultations.isEmpty()) {
									Map<String, Object> requestPayload = new HashMap<>();
									requestPayload.put("interactionIds", vmedConsultations);
									requestPayload.put("fromDate", fromDate.toLocalDate());
									requestPayload.put("toDate", toDate.toLocalDate());
									ResponseBean resp = new ResponseBean();
									resp = abhaM2Service.vmedRestCall(objMapper.writeValueAsString(requestPayload),
											this.streamAndReturnQuery("VMED_GET_DATA_NOTIFY", notifyTenantQueries),
											"NOTIFY");
									if (resp != null) {
										if (resp.getMessage() != null && !resp.getMessage().isEmpty()
												&& "success".equals(resp.getMessage())) {
											TypeReference<List<NotifyResultSetDto>> listType = new TypeReference<List<NotifyResultSetDto>>() {
											};
											careContextRecords.addAll(objMapper
													.readValue(objMapper.writeValueAsString(resp.getData()), listType));
										}
									}
								}

								if (notify.getNotification().getConsentDetail().getHiTypes().contains("Prescription")
										|| notify.getNotification().getConsentDetail().getHiTypes()
												.contains("OpConsultation")) {
									if (!consultationIds.isEmpty()) {
										careContextRecords
												.addAll(this.returnCareContexts(notifyTenantQueries, consultationIds,
														fromDate, toDate, "CONSULTATION-WITH-DATE-RANGE-FOR-NOTIFY"));
									}
								}
								if (notify.getNotification().getConsentDetail().getHiTypes()
										.contains("DiagnosticReport")) {
									if (!labResultIds.isEmpty()) {
										careContextRecords
												.addAll(this.returnCareContexts(notifyTenantQueries, labResultIds,
														fromDate, toDate, "LAB-REPORTS-WITH-DATE-RANGE-FOR-NOTIFY"));

									}
									if (!radiologyResultIds.isEmpty()) {
										careContextRecords.addAll(this.returnCareContexts(notifyTenantQueries,
												radiologyResultIds, fromDate, toDate,
												"RADIOLOGY-REPORTS-WITH-DATE-RANGE-FOR-NOTIFY"));

									}
								}
								if (notify.getNotification().getConsentDetail().getHiTypes()
										.contains("DischargeSummary")) {
									if (!dischargeSummaryIds.isEmpty()) {
										careContextRecords.addAll(this.returnCareContexts(notifyTenantQueries,
												dischargeSummaryIds, fromDate, toDate.plusDays(1),
												"DISCHARGE-SUMMARY-WITH-DATE-RANGE-FOR-NOTIFY"));
									}
								}
							} else {
								if (notify.getNotification().getConsentDetail().getHiTypes().contains("Prescription")) {
									if (!consultationIds.isEmpty()) {
										careContextRecords
												.addAll(this.returnCareContexts(notifyTenantQueries, consultationIds,
														fromDate, toDate, "CONSULTATION-WITH-DATE-RANGE-FOR-NOTIFY"));
									}
								}
								if (notify.getNotification().getConsentDetail().getHiTypes()
										.contains("DiagnosticReport")) {
									if (!labResultIds.isEmpty()) {
										careContextRecords
												.addAll(this.returnCareContexts(notifyTenantQueries, labResultIds,
														fromDate, toDate, "LAB-REPORTS-WITH-DATE-RANGE-FOR-NOTIFY"));

									}
								}
							}
							if (careContextRecords.isEmpty()) {
								error = new Error(1012,
										"NO RECORDS FOUND AGAINST THE ABHA ADDRESS FOR GIVEN DATE RANGE");
								log.error("NO RECORDS FOUND AGAINST THE ABHA ADDRESS FOR GIVEN DATE RANGE {}", error);
							}
						} else {
							error = new Error(1013, "PATIENT NOT FOUND");
							log.error("PATIENT NOT FOUND {}", error);
						}
					}
				}
				onNotifyPayload = webHookService.constructOnNotifyPayload(concentId, requestId, status, error);
				webHookService.insertIntoHipCallBack(requestId, null, request, concentId, null, 17, healthId, null);
				webHookService.insertIntoHipNotifyLogs(requestId, notify.getNotification().getStatus(), null, concentId,
						healthId);
				ndhmController.onHIPNotify(onNotifyPayload);
			}
		} catch (Exception e) {
			log.error("ERROR at ##consentshipnotify#: {}  ============>", e.getLocalizedMessage());
			log.error("EXCEPTION OCCURED :{}  ============>", e);
			log.error(e.getMessage());
			log.error("ERROR CAUSE:  {} ============>", e.getCause());
			log.info("ON-INIT CALLED, UNABLE TO SAVE IN DB: {}==========>");
		}
	}

	public List<NotifyResultSetDto> returnCareContexts(List<AbhaQueryTable> notifyTenantQueries,
			List<String> consultationIds, LocalDateTime fromDate, LocalDateTime toDate, String queryType)
			throws JsonProcessingException {
		return extractResultSetService.executeDynamicQueryFromDBForNotify(
				webHookService.returnFinalQuery(this.streamAndReturnQuery(queryType, notifyTenantQueries),
						webHookService.returnCareContextsIdsAsString(consultationIds), fromDate.toLocalDate(),
						toDate.toLocalDate()));
	}

	public LocalDateTime convertUtcDateStringToIstDate(String dateString) {
		String date = dateString.replace(dateString.substring(dateString.lastIndexOf("."), dateString.length()), "");
		return LocalDateTime.parse(date);
	}

	public List<NotifyResultSetDto> returnCareContexts(List<AbhaQueryTable> notifyTenantQueries,
			List<String> consultationIds, String queryType) throws JsonProcessingException {
		return extractResultSetService.executeDynamicQueryFromDBForNotify(
				webHookService.returnFinalQueryForDirectAuth(this.streamAndReturnQuery(queryType, notifyTenantQueries),
						webHookService.returnCareContextsIdsAsString(consultationIds)));
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			Long.parseLong(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

//
//	// DATA TRANSFER STEP 2
//	// SETP 2 HERE WE GET EXCRYPTION KEYS AND DATA PUSH URL
	@PostMapping(value = "health-information/hip/request")
	public void getDataPushURL(@RequestBody String request, HttpServletRequest httpServletRequest) {
		log.info("{health-information/hip/request}==> " + request);
		HIPTenantMappingDto hipTenantMappingDto = this.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
		if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
				&& !hipTenantMappingDto.getServerAccess()) {
			this.restCallTenantUrlForCallBacks(
					hipTenantMappingDto.getUrlPath() + "/abha/v0.5/health-information/hip/request", request, "HIP-REQUEST",
					httpServletRequest.getHeader("X-HIP-ID"));
			return;
		}

		List<com.dipl.abha.m2.notifypayload.CareContext> careContext = new ArrayList<>();
		String purposeType = "";
		LocalDateTime fromDate = null;
		LocalDateTime toDate = null;
		LocalDateTime dataEraseAt = null;
		String consent = "";
		Error error = null;
		HIPRequest callBack = null;
		List<String> fileTypes = new ArrayList<>();
		try {
			log.info("request at ##REQUEST CALLED# start:");
			RequestPayload req = objMapper.readValue(request, RequestPayload.class);
			System.out.println("REQUEST OBJ MAPPER            " + objMapper.writeValueAsString(req));
			String transactionId = req.getTransactionId();
			String requestId = req.getRequestId();
			HiRequest hiRequest = req.getHiRequest();
			consent = hiRequest.getConsent().getId();
			String dhPublicKey = hiRequest.getKeyMaterial().getDhPublicKey().getKeyValue();
			String nonce = hiRequest.getKeyMaterial().getNonce();
			String dataPushUrl = hiRequest.getDataPushUrl();
			allAbdmCentralDbSave.processPayloadRequest(request, 2, httpServletRequest);
			if (!consent.isEmpty()) {
				callBack = hipRequestRepo.findFirstByConsentArtefactCodeAndHipRequestTypeIdOrderByCreatedOnDesc(consent,
						20);
				if (callBack != null && callBack.getRequestJson() != null) {
					OnRequestPayload onRequest = objMapper.readValue(callBack.getRequestJson().toPrettyString(),
							OnRequestPayload.class);
					if (onRequest != null && onRequest.getHiRequest() != null
							&& onRequest.getHiRequest().getSessionStatus() != null
							&& onRequest.getHiRequest().getSessionStatus().equals("ACKNOWLEDGED")) {
						log.debug("Concent already been acknowledged and records pushed");
						error = new Error(1061, "Concent already been acknowledged and records pushed");
					}
				}
				String hipId = "";
				HIPCallback notify = hipCallBackRepository
						.findFirstByConsentRequestCodeAndHipRequestTypeIdOrderByCreatedOnDesc(consent, 17);
				if (notify != null && notify.getResponseJson() != null) {
					NotifyPayload notifyPayload = objMapper.readValue(notify.getResponseJson().toPrettyString(),
							NotifyPayload.class);
					if (notifyPayload != null && notifyPayload.getNotification() != null) {
						if (notifyPayload.getNotification().getStatus().equalsIgnoreCase("GRANTED")) {
							if (notifyPayload.getNotification().getConsentDetail() != null) {
								fileTypes = notifyPayload.getNotification().getConsentDetail().getHiTypes();
								Hip hip = notifyPayload.getNotification().getConsentDetail().getHip();
								if (hip != null) {
									hipId = hip.getId();
								}
								careContext = notifyPayload.getNotification().getConsentDetail().getCareContexts();
								purposeType = notifyPayload.getNotification().getConsentDetail().getPurpose().getText();
								if (notifyPayload.getNotification().getConsentDetail().getPermission() != null
										&& notifyPayload.getNotification().getConsentDetail().getPermission()
												.getDataEraseAt() != null) {
									fromDate = notifyPayload.getNotification().getConsentDetail().getPermission()
											.getDateRange().getFrom();
									toDate = notifyPayload.getNotification().getConsentDetail().getPermission()
											.getDateRange().getMyto();
									dataEraseAt = notifyPayload.getNotification().getConsentDetail().getPermission()
											.getDataEraseAt();
									if (notifyPayload.getNotification().getConsentDetail().getPermission()
											.getDataEraseAt().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
										error = new Error(1061, "Consent artefact expired");
									}
								}
							}
						} else {
							error = new Error(1061, "Consent artefact " + notifyPayload.getNotification().getStatus());
						}
					}
				}
				String errorRequestId = "";
				Thread.sleep(10000);		
				log.info("Thread sleep for 10 secs");
				HIPCallback findByRequestCodeLimit1 = hipCallBackRepository.findByRequestCodeLimit1(requestId);
				if (findByRequestCodeLimit1 == null) {
					webHookService.insertIntoHipCallBack(requestId, transactionId, request, consent, null, 19, null,
							error != null ? error.toString() : null);
				} else {
					error = new Error(1064, "Request with this request id already exists (" + requestId + ")");
					errorRequestId = UUID.randomUUID().toString();
					webHookService.insertIntoHipCallBack(errorRequestId, transactionId, request, consent, null, 19,
							null, error.toString());
				}
				OnRequestPayload onRequestPayloadObj = webHookService.constructOnRequestPayload(requestId,
						transactionId, error);

				ndhmController.onHIPOnRequest(onRequestPayloadObj, dhPublicKey, nonce, transactionId, careContext,
						dataPushUrl, purposeType, consent, fromDate, toDate, httpServletRequest, fileTypes, hipId,
						dataEraseAt, requestId);
				log.info("request at ##health-informationhiprequest# end:");
			}
		} catch (Exception e) {
			log.error("error at ##health-informationhiprequest# end:");
			e.printStackTrace();
		}
	}

	// Delinking ABHA or Deleteing Abha or Reactivating Abha
	@PostMapping(value = "patients/status/notify")
	public void patientsStatusNotify(@RequestBody String request, HttpServletRequest httpServletRequest)
			throws Exception {
		log.info("request at ##patientsstatusnotify# start:" + request);
		log.info("{}==> " + request);
		Error error = null;
		try {
			HIPTenantMappingDto hipTenantMappingDto = this.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
			if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
					&& !hipTenantMappingDto.getServerAccess()) {
				this.restCallTenantUrlForCallBacks(hipTenantMappingDto.getUrlPath() + "/abha/v0.5/patients/status/notify",
						request, "PATIENT-STATUS-NOTIFY", httpServletRequest.getHeader("X-HIP-ID"));
				return;
			}
			PatientStatusNotifyPayload patientStatus = objMapper.readValue(request, PatientStatusNotifyPayload.class);
			String requestId = patientStatus.getRequestId();
			String healthId = patientStatus.getNotification().getPatient().getId();
			PatientRegistrationDTO findByHealthId = patientRegRepository.findByHealthId(healthId);
			if (findByHealthId != null) {
				error = new Error();
				error.setMessage("NO PATIEN FOUND WITH HEALT ID");
				error.setStatus(1000);
			} else {
				// 0 - deleted
				// 1 - linked
				// 2 - delinked
				if (patientStatus.getNotification().getStatus().equals("DELETED")) {
					patientRegRepository.updateAbhaLinkedStatus(0, healthId);
				} else if (patientStatus.getNotification().getStatus().equals("DEACTIVATED")) {
					patientRegRepository.updateAbhaLinkedStatus(2, healthId);
				} else if (patientStatus.getNotification().getStatus().equals("REACTIVATED")) {
					patientRegRepository.updateAbhaLinkedStatus(1, healthId);
				}
			}
			HIPCallback insertIntoHipCallBack = webHookService.insertIntoHipCallBack(requestId, null, request, null,
					null, 25, healthId, null);
			if (insertIntoHipCallBack != null) {
				log.info("request at ##patientsstatusnotify# successfull:");
			} else {
				log.info("request at ##patientsstatusnotify# did not save in DB:");
			}
			if (Objects.nonNull(insertIntoHipCallBack)) {
				log.info("request at ##patientsstatusnotify# successfull:");
				PatientStatuOnNotify patientOnNotify = webHookService.constructPatientStatusOnNotifyPayload(requestId,
						error);
				ndhmController.patientsStatusOnNotify(patientOnNotify);
			} else {
				log.info("request at ##patientsstatusnotify# did not save in DB:");
			}
			log.info("request at ##patientsstatusnotify# end:");
		} catch (Exception e) {
			;
			log.info("EXCEPTION IN USER AUTH NOTIFY");
		}
	}

//
//	// CALL BACK FOR PHR NOTIFICATION WHEN NEW RECORD IS UPDATED
	@PostMapping(value = "links/context/on-notify")
	public void phrAppNotification(@RequestBody String request) {
		log.info("request at ##PHR ON NOTIFY# start:" + request);
		String requestId = "";
		try {

			OnNotifyPayload linksOnNotifyPayload = objMapper.readValue(request, OnNotifyPayload.class);
			if (linksOnNotifyPayload != null) {
				requestId = linksOnNotifyPayload.getResp().getRequestId();
			}

//			HIPCallback insertIntoHipCallBack = webHookService.insertIntoHipCallBack(requestId, null, request, null,
//					null, 28, null, null);
			this.saveOrResestCall("/abha/v0.5/links/context/on-notify", request, "LINK CONTEXT ON NOTIFY", requestId, null,
					request, null, null, 28, null, null);
//			if (insertIntoHipCallBack != null) {
//				log.info("request at ##PHR ON NOTIF# successfull:");
//			} else {
//				log.info("request at ##PHR ON NOTIF# did not save in DB:");
//			}
			log.info("request at ##PHR ON NOTIF# end:");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("request at ##PHR ON NOTIF# end:");
			log.error(e.getMessage());
		}
	}

	// CALL BACK FOR MOBILE SMS
	@PostMapping(value = "patients/sms/on-notify")
	public void linksContextOnNotify(@RequestBody String request) throws Exception {
		log.info("request at ##linkscontexton-notify# start:" + request);
		try {
			SmsOnNotify smsOnNofityPayload = objMapper.readValue(request, SmsOnNotify.class);
			String requestId = smsOnNofityPayload.getResp().getRequestId();
//			webHookService.insertIntoHipCallBack(requestId, null, request, null, null, 24, null,
//					this.ifError(smsOnNofityPayload.getError()));
			this.saveOrResestCall("/abha/v0.5/links/context/on-notify", request, "PATIENT-SMS-ON-NOTIFY", requestId, null,
					request, null, null, 24, null, this.ifError(smsOnNofityPayload.getError()));
		} catch (Exception e) {
			log.error("Exception occured :{}", e);
			log.error(e.getMessage());
			log.info("patients/sms/on-notify CALLED, UNABLE TO SAVE IN DB:{}==========>");
		}
		log.info("request at ##linkscontexton-notify# end:");
	}

	// AUTO REGISTRATION ONCE THE USER SCANS THE QR CODE AT FACILITY OR LAB USER
	// WILL BE REGISTERED IN OUR IHAT PLATFORM
	// SHARE PROFILE FUNCTIONALITY
	@PostMapping(value = "patients/profile/share")
	public void sharePatienProfile(@RequestBody String request, HttpServletRequest httpServletRequest) {
		String integratedTenantId = "";
		String tenantId = "";
		try {
			ProfileSharePayload profileSharePayload = objMapper.readValue(request, ProfileSharePayload.class);
			tenantId = "'" + TenantContext.getCurrentTenant() + "'";
			if (profileSharePayload != null) {
				Object tenantUrlIsIntegratedModule = jdbcTemplateHelper.getTenantUrlIsIntegratedModule(
						"select is_integrated_module, integrated_tenant_id  from orgnization_registration or2 \r\n"
								+ "			inner join integrated_tenant_mapping on tenant_id = or2.id where or2.id = '"
								+ TenantContext.getCurrentTenant() + "'");
				if (tenantUrlIsIntegratedModule != null) {
					TenanIdAndIsIntegratedModule tenantIdofIntegratedModule = (TenanIdAndIsIntegratedModule) tenantUrlIsIntegratedModule;
					if (tenantIdofIntegratedModule != null && tenantIdofIntegratedModule.getIs_integrated_module()) {
						log.info("This Tenant is Integrated Module " + TenantContext.getCurrentTenant()
								+ " In Integrateion With " + tenantIdofIntegratedModule.getIntegrated_tenant_id());
						integratedTenantId = "'" + tenantIdofIntegratedModule.getIntegrated_tenant_id() + "'";
					} else {
						log.info("This Tenant Is Not Integrated Module " + TenantContext.getCurrentTenant());
					}
				}
				String query = "";
				if (!integratedTenantId.isEmpty()) {
					query = "select * from public.abha_query_table where  tenant_id in ( " + tenantId + ","
							+ integratedTenantId + ") and query_type in ('SHARE-PROFILE')";
				} else {
					query = "select * from public.abha_query_table where  tenant_id in ( " + tenantId
							+ ") and query_type in ('SHARE-PROFILE')";
				}

				if (!query.isEmpty()) {
					List<AbhaQueryTable> requestQueries = jdbcTemplateHelper.getResults(query, AbhaQueryTable.class);
					if (requestQueries != null && !requestQueries.isEmpty()) {
						requestQueries.forEach(s -> {
							HttpHeaders headers = new HttpHeaders();
							headers.setContentType(MediaType.APPLICATION_JSON);
							HttpEntity<String> entity = new HttpEntity<>(request, headers);
							template.postForEntity(s.getQuery(), entity, String.class);
							log.info("Calling  :  " + s.getQuery());
						});
					} else {
						log.info("Unable to share profile Share Rouing   :  ");
					}
				} else {
					log.info("Could not create a query to get SHARE-PROFILE");
				}

			}
		} catch (Exception e) {
			log.error("EXCEPTION IN SHARE PROFILE");
			e.printStackTrace();
		}
	}

	private String ifError(Error error) {
		String errorMessage = null;
		if (error != null) {
			try {
				errorMessage = objMapper.writeValueAsString(error);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		return errorMessage;
	}

	public String directAuthStatus(String directAuthTime, String status) {
		LocalDateTime localDateTime = LocalDateTime.parse(directAuthTime, DateTimeFormatter.ISO_DATE_TIME);
		String date = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH).format(localDateTime);
		String time = localDateTime.toString().substring(localDateTime.toString().indexOf('T') + 1,
				localDateTime.toString().indexOf('.'));
		String finalTime = status + " - " + date + " " + time;
		return finalTime;
	}

	@PostMapping(value = "/hip/token/on-generate-token")
	public void onLinkTokenV3Api(@RequestBody String onGenerateToken, HttpServletRequest httpServletRequest) {
		log.info("request at ##hip/token/on-generate-token# start:" + onGenerateToken);
		ResponseBean bean = new ResponseBean();
		String requestId = "";
		try {
			HIPTenantMappingDto hipTenantMappingDto = this.isServerAcessTrueOrfalse(TenantContext.getCurrentTenant());
			if (hipTenantMappingDto != null && hipTenantMappingDto.getServerAccess() != null
					&& !hipTenantMappingDto.getServerAccess()) {
				this.restCallTenantUrlForCallBacks(hipTenantMappingDto.getUrlPath() + "/abha/v0.5/hip/token/on-generate-token",
						onGenerateToken, "ON-GENERATE-TOKEN", httpServletRequest.getHeader("X-HIP-ID"));
				return;
			}
			
			
			bean.setStatus(HttpStatus.OK);
			ONGenerateLinkToken response = objMapper.readValue(onGenerateToken, ONGenerateLinkToken.class);
			if (response != null) {
				requestId = response.getResponse().getRequestId();
				if (response.getError() == null) {
					webHookService.insertIntoHipCallBack(requestId, null, onGenerateToken, null, null, 34, null, null);
					HIPRequest findByRequestIdAndAPIType = hipRequestRepo
							.findByRequestCodeAndHipRequestTypeId(requestId, 33);
					if (findByRequestIdAndAPIType != null && findByRequestIdAndAPIType.getRequestJson() != null) {
						String abhaAddress = findByRequestIdAndAPIType.getRequestJson().get("abhaAddress").toString();
						String trimmedAdha = abhaAddress.replace("\"", "");
						Object object = jdbcTemplateHelper
								.getAbhaRegistration("select * from public.abha_registration where tenant_id ='"
										+ TenantContext.getCurrentTenant() + "' and abha_address = '" + trimmedAdha
										+ "' order by id desc limit 1");
						if (object != null) {
							AbhaRegistrationDTO patientDetails = (AbhaRegistrationDTO) object;
							if (findByRequestIdAndAPIType != null) {
								HipLinkTokenEntity hipLinkTokenEntity = new HipLinkTokenEntity();
								hipLinkTokenEntity.setAbhaAddress(trimmedAdha);
								hipLinkTokenEntity.setHipId(findByRequestIdAndAPIType.getConsentArtefactCode());
								hipLinkTokenEntity
										.setLinkToken(response.getLinkToken() != null ? response.getLinkToken() : null);
								hipLinkTokenEntity.setLinkTokenCreatedOn(LocalDateTime.now());
								hipLinkTokenEntity.setLinkTokenExpiresOn(LocalDateTime.now().plusMonths(6));
								hipLinkTokenEntity.setPatientId(patientDetails.getPatient_id());
								hipLinkTokenRepository.save(hipLinkTokenEntity);
							}
						}
					}
				} else {
					webHookService.insertIntoHipCallBack(requestId, null, onGenerateToken, null, null, 21, null,
							response.getError().toString());
					log.info("Error in on-generate token");
				}
			}
			bean.setStatus(HttpStatus.OK);
			bean.setMessage("record save saccessfully");
		} catch (Exception e) {
			log.error("Exception occured :{}", e);
			log.error(e.getMessage());
			log.info("hip/token/on-generate-token, UNABLE TO SAVE IN DB:{}==========>" + onGenerateToken);
			bean.setData(null);
			bean.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class ValidateOtpPayload {
		private String number;
		private Integer OTP;
	}

	public HIPTenantMappingDto isServerAcessTrueOrfalse(String currentTenant) {
		HIPTenantMappingDto hipTenantMappingDto = null;
		try {
			hipTenantMappingDto = jdbcTemplate.queryForObject(
					"select id as tenant_id, ndhm_client_id, ndhm_client_secrete_key, is_server_access, url_path "
							+ "from orgnization_registration or1 where or1.id = ?",
					new Object[] { Long.parseLong(currentTenant) }, (rs, rowNum) -> {
						HIPTenantMappingDto dto = new HIPTenantMappingDto();
						dto.setTenantId(rs.getLong("tenant_id"));
						dto.setNdhmClientId(rs.getString("ndhm_client_id"));
						dto.setNdhmClientSecrete(rs.getString("ndhm_client_secrete_key"));
						dto.setServerAccess(rs.getBoolean("is_server_access"));
						dto.setUrlPath(rs.getString("url_path"));
						return dto;
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hipTenantMappingDto;
	}
}
