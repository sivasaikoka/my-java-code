package com.dipl.abha.controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.dto.AbhaQueryTable;
import com.dipl.abha.dto.AbhaRegistrationDTO;
import com.dipl.abha.dto.AccountProfile;
import com.dipl.abha.dto.InvestigationAdvice;
import com.dipl.abha.dto.LabResultFetchDto;
import com.dipl.abha.dto.LinkCareContextV3Payload;
import com.dipl.abha.dto.Master;
import com.dipl.abha.dto.RequestResultSetDto;
import com.dipl.abha.dto.RequestResultSetDtoNew;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.dto.TenanIdAndIsIntegratedModule;
import com.dipl.abha.dto.VitalHistory;
import com.dipl.abha.encryptdecrypclasses.EncryptionRequest;
import com.dipl.abha.encryptdecrypclasses.EncryptionResponse;
import com.dipl.abha.encryptdecrypclasses.KeyMaterial;
import com.dipl.abha.entities.HIPDataPushLog;
import com.dipl.abha.entities.HIPRequest;
import com.dipl.abha.heartBeat.HeartBeat;
import com.dipl.abha.m2.addcarecontextpayload.AddCareContextPayload;
import com.dipl.abha.m2.afterpushnotifyhippayloads.NotifyHipAfterDataPush;
import com.dipl.abha.m2.authconfirmpayloads.AuthConfirmPayload;
import com.dipl.abha.m2.authonnotifydirectmodepayload.DirectAuthOnNotify;
import com.dipl.abha.m2.datapushpayload.DataPushPayload;
import com.dipl.abha.m2.initpayloads.FetchAndInitPayload;
import com.dipl.abha.m2.linkoninit.LinksOnInit;
import com.dipl.abha.m2.linksonconfirm.LinkOnConfirm;
import com.dipl.abha.m2.notifypayload.CareContext;
import com.dipl.abha.m2.ondiscoverpayload.OnDiscoverPayload;
import com.dipl.abha.m2.onnofitypayload.OnNotifyPayload;
import com.dipl.abha.m2.onrequestypayload.OnRequestPayload;
import com.dipl.abha.m2.onshareprofilepayload.OnShareProfilePayload;
import com.dipl.abha.m2.patientstatusonnotifypayload.PatientStatuOnNotify;
import com.dipl.abha.m2.phrnotifypayload.PHRNotifyPayload;
import com.dipl.abha.others.PayloadException;
import com.dipl.abha.others.PayloadValidator;
import com.dipl.abha.patient.dto.GenerateLinkTokenPayload;
import com.dipl.abha.repositories.HIPDataPushLogsRepository;
import com.dipl.abha.repositories.HIPRequestRepository;
import com.dipl.abha.repositories.HIUResponseRepository;
import com.dipl.abha.service.ABHAM2Service;
import com.dipl.abha.service.AllAbdmCentralDbSave;
import com.dipl.abha.service.ExtractResultSetService;
import com.dipl.abha.service.WebHookService;
import com.dipl.abha.util.BundleForLab;
import com.dipl.abha.util.ConstantUtil;
import com.dipl.abha.util.ExampleBundleForLab;
import com.dipl.abha.util.JdbcTemplateHelper;
import com.dipl.abha.util.SfhirB;
import com.dipl.abha.v3.dto.CareContextOnDiscoverV3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Abhinay
 *
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping(value = { "m2/v0.5", "m2/v3" })
public class NDHMM2Controller {

	// hip Initiated linking urls
	@Value("${CREATE-CALLBACK-BRIDGE}")
	private String callBackBridgeurl;
	// step 1
	@Value("${FETCH-USER-MODES}")
	private String fetchUserModes;
	// step 2
	@Value("${AUTH-INIT}")
	private String authInit;
	// step 3
	@Value("${AUTH-CONFIRM}")
	private String authConfirmUrl;
	// step 4
	@Value("${ADD-CONTEXT}")
	private String addCareContext;
	// User Initiated Linking urls
	// step 1
	@Value("${HIP-ON-DISCOVER}")
	private String onDiscoverURL;
	// step 2
	@Value("${LINKS-ON-INIT}")
	private String linksOnInitUrl;
	// step 3
	@Value("${LINKS-ON-CONFIRM}")
	private String linksONConfirm;
	// Document Sending urls
	// step 1
	@Value("${HIP-ON-NOTIFY}")
	private String hipOnNoify;

	@Value("${HIP-ON-NOTIFY-V3}")
	private String hipOnNoifyv3;
	// step 2
	@Value("${ON-HIP-REQUEST}")
	private String onRequestURL;

	@Value("${HEALTH-INFORMATION-ON-REQUEST}")
	private String healthInformationOnRequestURL;
	// step 3 url will come from request url
	// step 4
	@Value("${FILE-TRANSFER-NOTIFY}")
	private String fileTransferNotifyUrl;

	@Value("${HEALTH-INFORMATION-NOTIFY}")
	private String healthInformationNotifyUrl;

	// share user profile
	@Value("${ON-SHARE-PROFILE}")
	private String onShareProfileUrl;

	// url used for direct auth
	@Value("${USER-AUTH-ON-NOFITY}")
	private String userAuthOnNotify;

	// used for sending sms to link health records
	@Value("${PATIENT-SMS-NOTIFY}")
	private String patientSmsNotify;
	// Opt out of abha
	@Value("${PATIENT-STATUS-NOTIFY}")
	private String patientStatusNotify;

	@Value("${SEND_PHR_NOTIFICATION}")
	private String sendPhrNotification;
	// used for sending notification if lab record is added
	@Value("${PATIENT-STATUS-ON-NOTIFY}")
	private String patientStatusOnNotify;

	@Value("${CARE-CONTEXT-V3-ON-DISCOVER}")
	private String v3onDiscover;

	@Value("${CARE-CONTEXT-V3-LINKS-ON-INIT}")
	private String v3linksOnInitUrl;

	@Value("${CARE-CONTEXT-V3-LINKS-ON-CONFIRM}")
	private String v3linksONConfirm;

	@Value("${HIMS-JASPER-URL}")
	private String himsJasperUrl;

	@Value("${HIMS_TENANT_IDS}")
	private Set<Integer> himsTenantIds;

	@Autowired
	private AllAbdmCentralDbSave abdmCentralDbSave;

	@Autowired
	private ABHAM2Service abhaM2Service;

	@Autowired
	private KeysController keys;
	@Autowired
	private EncryptionController encrypController;
	@Autowired
	private ObjectMapper objMapper;
	@Autowired
	private WebHookService webHookService;
	@Autowired
	private PayloadValidator validator;
	@Autowired
	private HIPDataPushLogsRepository hipDataPushLogsRepository;
	@Autowired
	private HIPRequestRepository hipRequestRepo;
	@Autowired
	private JdbcTemplateHelper jdbcTemplateHelper;

	@Value("${VMED_GET_DATA_REQUEST}")
	private String vmedGetDataPushRecords;

	@Value("${GENERATE-LINK-TOKEN}")
	private String generateLinkTokenUrl;

	@Value("${LINK-CARE-CONTEXT-V3}")
	private String linkCareContextUrl;

	@Autowired
	private HIUResponseRepository hiuResponseRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	ExampleBundleForLab exampleBundleForLab;

	@Autowired
	private ExtractResultSetService extractResultSetService;

	@Autowired
	private BundleForLab bundleForLab;

	@Autowired
	private SfhirB sfhirB;

	private static final Logger LOGGER = LoggerFactory.getLogger(NDHMM2Controller.class);

	// https://dev.abdm.gov.in/devservice/v1/bridges
	// Creating a Bridge by giving company URL for CALLBACKS
	@PatchMapping("/company/bridges")
	@ApiOperation(value = "CREATING COMPANY URL")
	private ResponseEntity<?> creatingCompanyUrlForCallBacks(@RequestBody String payload) {
		log.info("authenticatingWithGateway() Authenticating with gateway : ");
		ResponseEntity<ResponseBean> auth = abhaM2Service.restCallApi(payload, "", "CREATED BRIDGE", "", "", "");
		if (auth.getStatusCode().compareTo(HttpStatus.ACCEPTED) == 0
				|| auth.getStatusCode().compareTo(HttpStatus.OK) == 0) {
			ResponseBean bean = new ResponseBean();
			bean.setData(null);
			bean.setMessage("Created a call back Bridge with url");
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
		} else {
			return new ResponseEntity<ResponseBean>(auth.getBody(), HttpStatus.BAD_REQUEST);
		}
	}

	// step 1
	@PostMapping("/users/auth/fetch-modes")
	@ApiOperation(value = "FETCHING ALL USER MODES")
	private ResponseEntity<?> fetchModes(@Valid @RequestBody FetchAndInitPayload fetchModesPayload,
			@RequestParam(value = "consultationId", required = false) Optional<String> consultationId,
			@RequestParam(value = "interactionId", required = false) Optional<String> interactionId,
			HttpServletRequest servletRequest) {
		log.info("================FETCH USER MODES STARTED=====================");
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		bean.setStatus(HttpStatus.OK);
		String careContexId = "";
		try {
			validator.validateFields(fetchModesPayload);
		} catch (PayloadException e) {
			bean = new ResponseBean(HttpStatus.BAD_REQUEST, e.getErrorMap().get("message").toString(), null, null);
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		}
		try {
			String fetchModes = objMapper.writeValueAsString(fetchModesPayload);
			abdmCentralDbSave.processPayload(fetchModes, 1, servletRequest);
			if (consultationId.isPresent()) {
				careContexId = consultationId.get();
			}
			if (interactionId.isPresent()) {
				careContexId = interactionId.get();
			}
			return webHookService.saveAndReturnResponse(fetchModes, fetchUserModes, "FETCH-MODES",
					fetchModesPayload.getRequestId(), null, 1, fetchModesPayload.getQuery().getId(), null, careContexId,
					null);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("=================EXCEPTION IN FETCH MODES================" + e.getMessage());
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		}
	}

	// step 2
	@PostMapping("/users/auth/init")
	@ApiOperation(value = "USER INIT")
	public ResponseEntity<?> userAuthInit(@Valid @RequestBody FetchAndInitPayload authInitPayload,
			@RequestParam(value = "consultationId", required = true) Optional<String> consultationId,
			@RequestParam(value = "interactionId", required = false) Optional<String> interactionId,
			HttpServletRequest servletRequest) {
		log.info("=========================INIT STARTED==============================");
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		bean.setStatus(HttpStatus.OK);
		String careContexId = "";
		try {
			validator.validateFields(authInitPayload);
		} catch (PayloadException e) {
			bean = new ResponseBean(HttpStatus.BAD_REQUEST, e.getErrorMap().get("message").toString(), null, null);
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		}
		try {
			String userauth = objMapper.writeValueAsString(authInitPayload);
			abdmCentralDbSave.processPayload(userauth, 2, servletRequest);
			if (consultationId.isPresent()) {
				careContexId = consultationId.get();
			}
			if (interactionId.isPresent()) {
				careContexId = interactionId.get();
			}
			return webHookService.saveAndReturnResponse(userauth, authInit, "AUTH-INIT", authInitPayload.getRequestId(),
					null, 3, authInitPayload.getQuery().getId(), null, careContexId, null);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("====================ERROR IN INIT================= " + e.getMessage());
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		}
	}

	// step 3
	/// users/auth/confirm
	@PostMapping("/users/auth/confirm")
	@ApiOperation(value = "User AuthConform")
	private ResponseEntity<?> userAuthConform(@Valid @RequestBody AuthConfirmPayload authConform,
			@RequestParam(value = "consultationId", required = true) Optional<String> consultationId,
			@RequestParam(value = "interactionId", required = false) Optional<String> interactionId,
			HttpServletRequest servletRequest) {
		log.info("==============AUTH CONFIRM STARTED==========");
		ResponseBean bean = null;
		String careContexId = "";
		try {
			validator.validateFields(authConform);
		} catch (PayloadException e) {
			bean = new ResponseBean(HttpStatus.BAD_REQUEST, e.getErrorMap().get("message").toString(), null, null);
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		}
		try {
			String authConfirm = objMapper.writeValueAsString(authConform);
			abdmCentralDbSave.processPayload(authConfirm, 3, servletRequest);
			if (consultationId.isPresent()) {
				careContexId = consultationId.get();
			}
			if (interactionId.isPresent()) {
				careContexId = interactionId.get();
			}
			return webHookService.saveAndReturnResponse(authConfirm, authConfirmUrl, "AUTH-CONFIRM",
					authConform.getRequestId(), authConform.getTransactionId(), 5, null, null, careContexId, null);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("===================ERROR IN USER AUTH CONFIRM=================" + e.getMessage());
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		}
	}

	// step 4
	// links/link/add-contexts
	@PostMapping("/links/link/add-contexts")
	@ApiOperation(value = "USER AUTH ADD CONTEXT")
	public ResponseEntity<?> addContext(@Valid @RequestBody AddCareContextPayload careContextPayload,
			@RequestParam(value = "consultationId", required = true) Optional<String> consultationId,
			@RequestParam(value = "interactionId", required = false) Optional<String> interactionId,
			String directAuthStatus, HttpServletRequest servletRequest) {
		ResponseBean bean = null;
		String careContexId = "";
		log.info("====================ADD CARE CONTEXT STARTED===================");
		try {
			validator.validateFields(careContextPayload);
		} catch (PayloadException e) {
			bean = new ResponseBean(HttpStatus.OK, e.getErrorMap().get("message").toString(), null, null);
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		}
		try {
			String careContext = objMapper.writeValueAsString(careContextPayload);
			abdmCentralDbSave.processPayload(careContext, 4, servletRequest);
			if (consultationId.isPresent()) {
				careContexId = consultationId.get();
			}
			if (interactionId.isPresent()) {
				careContexId = interactionId.get();
			}
			return webHookService.saveAndReturnResponse(careContext, addCareContext, "ADD-CONTEXT",
					careContextPayload.getRequestId(), null, 7, null, directAuthStatus, careContexId, null);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("===================ERROR IN ADD CARE CONTEXT=================" + e.getMessage());
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		}

	}

	// step 5
	// on nofity to gateway that HIP got a request and HIP noficed it, this is used
	// From NHA application
	@PostMapping("/consents/hip/on-notify")
	@ApiOperation(value = "USER ON-NOTIFY")
	public ResponseEntity<?> onHIPNotify(@RequestBody OnNotifyPayload onHipNotify) {
		LOGGER.info("======================ON NOTIFY STARTED=================");
		ResponseBean bean = null;
		try {
			String onNotify = objMapper.writeValueAsString(onHipNotify);
			return webHookService.saveAndReturnResponse(onNotify, hipOnNoifyv3, "ON-NOTIFY",
					onHipNotify.getResp().getRequestId(), null, 18, null,
					onHipNotify.getAcknowledgement().getConsentId(), null, null);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("===================ERROR IN ON NOFITY=================" + e.getMessage());
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// step 5 same as about
	// this is user asking hip to discover his records in hip
	@PostMapping("/care-contexts/on-discover")
	@ApiOperation(value = "USER AUTH CONFORM")
	public ResponseEntity<?> onHIPDiscovery(@RequestBody OnDiscoverPayload onDiscover)
			throws JsonMappingException, JsonProcessingException {
		log.info("=========================ON DISCOVER STARTED==============================");
		ResponseBean bean = null;
		try {
			String onDiscoverPayload = objMapper.writeValueAsString(onDiscover);
			return webHookService.saveAndReturnResponse(onDiscoverPayload, onDiscoverURL, "DISCOVER",
					onDiscover.getResp().getRequestId(), onDiscover.getTransactionId(), 10, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("===================ERROR IN ON DISCOVER=================" + e.getMessage());
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
		}

	}

	@PostMapping("/users/hip/on-request")
	@ApiOperation(value = "HIP ON REQUEST")
	public void onHIPOnRequest(@RequestBody OnRequestPayload onRequest, String ReceiverPubliKey, String nonce,
			String transactionId, List<CareContext> careContexts, String dataPushUrl, String purposeType,
			String concentId, LocalDateTime fromDate, LocalDateTime toDate, HttpServletRequest httpServletRequest,
			List<String> fileTypes, String hipId, LocalDateTime dataEraseAt, String errorRequestId) {
		log.info("ON REQUEST HIP");
		ResponseBean bean = new ResponseBean();
		bean.setData(null);
		bean.setStatus(HttpStatus.OK);
		List<String> consultationIds = new ArrayList<>();
		List<String> labResultIds = new ArrayList<>();
		List<String> radiologyResultIds = new ArrayList<>();
		List<Long> vmedConsultationIds = new ArrayList<>();
		List<String> dischargeSummaryIds = new ArrayList<>();
		List<RequestResultSetDto> careContextRecords = new ArrayList<>();
		List<RequestResultSetDtoNew> requestResultSetDtoNew = new ArrayList<>();
		ResponseEntity<ResponseBean> onRequestCallBackResponse = null;
		try {
			log.info("Care Contexts size {}: " + careContexts.size());

//			if (onRequest.getError() != null) {
//				if (onRequest.getError().getStatus() == 1064) {
//					onRequestCallBackResponse = webHookService.saveAndReturnResponse(onRequestPayload, onRequestURL,
//							"ON-REQUEST", errorRequestId, transactionId, 20, null, concentId, null, null);
//				} else {
//					onRequestCallBackResponse = webHookService.saveAndReturnResponse(onRequestPayload, onRequestURL,
//							"ON-REQUEST", errorRequestId, transactionId, 20, null, concentId, null, null);
//				}
//			} else {
//				onRequestCallBackResponse = webHookService.saveAndReturnResponse(onRequestPayload, onRequestURL,
//						"ON-REQUEST", errorRequestId, transactionId, 20, null, concentId, null, null);
//			}

			onRequest.setRequestId(null);
			onRequest.setTimestamp(null);
			String onRequestPayload = objMapper.writeValueAsString(onRequest);
			if (onRequest.getError() != null) {
				if (onRequest.getError().getStatus() == 1064) {
					onRequestCallBackResponse = webHookService.saveAndReturnResponse(onRequestPayload,
							healthInformationOnRequestURL, "HEALTH-INFORMATION-ON-REQUEST-V3", errorRequestId,
							transactionId, 20, null, concentId, null, null);
				} else {
					onRequestCallBackResponse = webHookService.saveAndReturnResponse(onRequestPayload,
							healthInformationOnRequestURL, "HEALTH-INFORMATION-ON-REQUEST-V3", errorRequestId,
							transactionId, 20, null, concentId, null, null);
				}
			} else {
				onRequestCallBackResponse = webHookService.saveAndReturnResponse(onRequestPayload,
						healthInformationOnRequestURL, "HEALTH-INFORMATION-ON-REQUEST-V3",
						onRequest.getResponse().getRequestId(), transactionId, 20, null, concentId, null, null);
			}
			if (onRequest.getError() == null && onRequestCallBackResponse != null
					&& onRequestCallBackResponse.getBody() != null
					&& onRequestCallBackResponse.getBody().getMessage() != null
					&& "HEALTH-INFORMATION-ON-REQUEST-V3 SUCCESSFUL AND SAVED IN DB"
							.equals(onRequestCallBackResponse.getBody().getMessage())) {
				List<AbhaQueryTable> requestQueries = jdbcTemplateHelper.getResults(
						"select * from public.abha_query_table where  tenant_id = " + TenantContext.getCurrentTenant()
								+ " and query_type in ('CONSULTATION-WITH-DATE-RANGE-FOR-REQUEST','LAB-REPORTS-WITH-DATE-RANGE-FOR-REQUEST','RADIOLOGY-REPORTS-WITH-DATE-RANGE-FOR-REQUEST','VMED_GET_DATA_REQUEST')",
						AbhaQueryTable.class);
				Object tenantUrlIsIntegratedModule = jdbcTemplateHelper.getTenantUrlIsIntegratedModule(
						"select is_integrated_module, integrated_tenant_id  from orgnization_registration or2 \r\n"
								+ "			inner join integrated_tenant_mapping on tenant_id = or2.id where or2.id = '"
								+ TenantContext.getCurrentTenant() + "'");
				String integratedTenantUrl = jdbcTemplateHelper
						.getTenantUrl("select or2.url_path from orgnization_registration or2 where or2.id = \r\n"
								+ "(select itm.integrated_tenant_id  from integrated_tenant_mapping itm where itm.tenant_id = '"
								+ TenantContext.getCurrentTenant() + "')");
				Set<Long> vmedConsultations = new HashSet<>();
				careContexts.forEach(s -> {
					if (NDHMM2CallBackController.isNumeric(s.getCareContextReference())) {
						if (himsTenantIds.contains(Integer.parseInt(TenantContext.getCurrentTenant()))) {
							TenanIdAndIsIntegratedModule isIntegratedModule = tenantUrlIsIntegratedModule != null
									? (TenanIdAndIsIntegratedModule) tenantUrlIsIntegratedModule
									: null;
							if (isIntegratedModule != null && isIntegratedModule.getIs_integrated_module()) {
								if (Long.parseLong(s.getCareContextReference()) < 10000000000L) {
									vmedConsultations.add(Long.parseLong(s.getCareContextReference()));
								} else {
									consultationIds.add(s.getCareContextReference());
								}
							} else {
								consultationIds.add(s.getCareContextReference());
							}
						} else {
							consultationIds.add(s.getCareContextReference());
						}
					} else {
						if (s.getCareContextReference().contains("LBILL")
								|| s.getCareContextReference().contains("RETAILLABBILL")
								|| s.getCareContextReference().contains("LAB")
								|| s.getCareContextReference().contains("RETAILLABBILL")) {
							labResultIds.add(s.getCareContextReference());
						}
						if (s.getCareContextReference().startsWith("LR")) {
							labResultIds.add(s.getCareContextReference());
						} else if (s.getCareContextReference().startsWith("RR")) {
							radiologyResultIds.add(s.getCareContextReference());
						} else if (s.getCareContextReference().startsWith("DI")) {
							dischargeSummaryIds.add(s.getCareContextReference());
						}
					}
				});

				if (himsTenantIds.contains(Integer.parseInt(TenantContext.getCurrentTenant()))) {
					if (fileTypes.contains("PRESCRIPTION")) {
						if (!vmedConsultationIds.isEmpty()) {
							Map<String, Object> requestPayload = new HashMap<>();
							requestPayload.put("interactionIds", vmedConsultationIds);
							requestPayload.put("fromDate", fromDate.toLocalDate());
							requestPayload.put("toDate", toDate.toLocalDate());
							ResponseBean resp = new ResponseBean();
							resp = abhaM2Service.vmedRestCall(objMapper.writeValueAsString(requestPayload),
									abhaM2Service.streamAndReturnQuery("VMED_GET_DATA_REQUEST", requestQueries),
									"HEALTH-INFORMATION-ON-REQUEST");
							if (resp != null) {
								if (resp.getMessage() != null && !resp.getMessage().isEmpty()
										&& "success".equals(resp.getMessage())) {
									TypeReference<List<RequestResultSetDto>> listType = new TypeReference<List<RequestResultSetDto>>() {
									};
									careContextRecords.addAll(objMapper
											.readValue(objMapper.writeValueAsString(resp.getData()), listType));

									if (!careContextRecords.isEmpty()) {
										for (RequestResultSetDto s : careContextRecords) {
											s.setCareContextDisplay(integratedTenantUrl + s.getCareContextDisplay());
										}
									}
								}
							}
						}
					}
					if ((fileTypes.contains("Prescription") || fileTypes.contains("OpConsultation"))
							&& !consultationIds.isEmpty()) {
						careContextRecords.addAll(this.returnRequestCareContexts(requestQueries, consultationIds,
								fromDate, toDate, "CONSULTATION-WITH-DATE-RANGE-FOR-REQUEST"));
					}

					if (fileTypes.contains("DiagnosticReport")) {
						if (!labResultIds.isEmpty()) {
							requestResultSetDtoNew.addAll(this.returnRequestCareContextsNew(requestQueries,
									labResultIds, fromDate, toDate, "LAB-REPORTS-WITH-DATE-RANGE-FOR-REQUEST"));
						}
						if (!radiologyResultIds.isEmpty()) {
							requestResultSetDtoNew
									.addAll(this.returnRequestCareContextsNew(requestQueries, radiologyResultIds,
											fromDate, toDate, "RADIOLOGY-REPORTS-WITH-DATE-RANGE-FOR-REQUEST"));
						}
					}
					if (fileTypes.contains("DischargeSummary")) {
						if (!dischargeSummaryIds.isEmpty()) {
							requestResultSetDtoNew.addAll(
									this.returnRequestCareContextsNew(requestQueries, dischargeSummaryIds, fromDate,
											toDate.plusDays(1), "DISCHARGE-SUMMARY-WITH-DATE-RANGE-FOR-REQUEST"));
						}
					}
				} else {
					if (fileTypes.contains("Prescription")) {
						if (!consultationIds.isEmpty()) {
							careContextRecords.addAll(this.returnRequestCareContexts(requestQueries, consultationIds,
									fromDate, toDate, "CONSULTATION-WITH-DATE-RANGE-FOR-REQUEST"));
						}
					}
					if (fileTypes.contains("DiagnosticReport")) {
						if (!labResultIds.isEmpty()) {
							requestResultSetDtoNew.addAll(this.returnRequestCareContextsNew(requestQueries,
									labResultIds, fromDate, toDate, "LAB-REPORTS-WITH-DATE-RANGE-FOR-REQUEST"));
						}
					}
				}

				if (!careContextRecords.isEmpty()) {
					this.pushRecordsProcess(careContextRecords, nonce, transactionId, careContexts, dataPushUrl,
							ReceiverPubliKey, dataEraseAt, concentId, hipId);
				}
				if (!requestResultSetDtoNew.isEmpty()) {
					this.pushRecordsProcessNew(requestResultSetDtoNew, nonce, transactionId, careContexts, dataPushUrl,
							ReceiverPubliKey, dataEraseAt, concentId, hipId, labResultIds);
				}
			} else {
				log.error("Error in HEALTH-INFORMATION-ON-REQUEST : {}", onRequestCallBackResponse);
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
		}
	}

	public List<RequestResultSetDto> returnRequestCareContexts(List<AbhaQueryTable> requestQueries,
			List<String> consultationIds, LocalDateTime fromDate, LocalDateTime toDate, String queryType)
			throws JsonProcessingException {
		return extractResultSetService.executeDynamicQueryFromDBForRequest(
				webHookService.returnFinalQuery(abhaM2Service.streamAndReturnQuery(queryType, requestQueries),
						webHookService.returnCareContextsIdsAsString(consultationIds), fromDate.toLocalDate(),
						toDate.toLocalDate()));
	}

	public List<RequestResultSetDtoNew> returnRequestCareContextsNew(List<AbhaQueryTable> requestQueries,
			List<String> consultationIds, LocalDateTime fromDate, LocalDateTime toDate, String queryType)
			throws JsonProcessingException {
		return extractResultSetService.executeDynamicQueryFromDBForRequestNew(
				webHookService.returnFinalQuery(abhaM2Service.streamAndReturnQuery(queryType, requestQueries),
						webHookService.returnCareContextsIdsAsString(consultationIds), fromDate.toLocalDate(),
						toDate.toLocalDate()));
	}

	private void pushRecordsProcess(List<RequestResultSetDto> careContextRecords, String nonce, String transactionId,
			List<CareContext> careContexts, String dataPushUrl, String ReceiverPubliKey, LocalDateTime dataEraseAt,
			String concentId, String hipId) throws Exception {
		int pageNO = 0;
		int pagecount = 0;
		NotifyHipAfterDataPush afterPush = new NotifyHipAfterDataPush();
		KeyMaterial keysMaterial = keys.generate();
		String patientId = careContexts.get(0).getPatientReference();
		String docRef = "";
		String patienName = "";
		String docName = "";
		String prescirptionPath = "";
		String ConsulstionDate = "";
		String fileType = "";
		String interactionId = "";
		String transferFileNotifyRequest = "";
		String resultId = "";
		String reportTypeNew = "";
		String medication = "";
		String medDirections = "";
		String chiefComplaints = "";
		String visitDate1 = "";
		String visitStartTime = "";
		String visitEndTime = "";
		String clinicName = "";
		String followUpDate = "";
		VitalHistory history = null;
		InvestigationAdvice investigationAdvice = null;
		String allinvestigationAdvice = "";
		List<HIPDataPushLog> hipDataPushLogs = new ArrayList<HIPDataPushLog>();
		List<HIPRequest> hipRequestList = new ArrayList<>();
		boolean filePushStatus = false;
		List<String> careContextString = new ArrayList<>();
		if (!careContextRecords.isEmpty()) {
			prescirptionPath = jdbcTemplateHelper
					.getTenantUrl("select url_path from public.orgnization_registration or2 where or2.id="
							+ TenantContext.getCurrentTenant());
			for (RequestResultSetDto s : careContextRecords) {
				String actualPrescriptionPath = "";
				if (himsTenantIds.contains(Integer.parseInt(TenantContext.getCurrentTenant()))) {
					if ("OPCONSULTATION".equals(s.getReportType())) {
						docRef = s.getCareContextReferenceNumber();
						patienName = s.getPatientDisplay();
						docName = s.getDoctor();
						ConsulstionDate = s.getConsulstionDate().toString();
						medication = s.getMeds();
						medDirections = s.getMedDirections();
						chiefComplaints = s.getChiefComplaints();
						fileType = "OP CONSULTATION REPORT";
						if (s.getPatientId() != null && s.getResultId() != null
								&& s.getCareContextReferenceNumber() != null) {
							actualPrescriptionPath = this.restCallJaserServiceForPerscription(s.getPatientId(),
									s.getResultId(), s.getCareContextReferenceNumber(), "", "", fileType);
						}
						interactionId = s.getCareContextReferenceNumber();
						reportTypeNew = "OP CONSULTATION (" + interactionId + ")";
						LOGGER.info("PRESCRIPTOINS FOUND");
					} else if ("LAB".equals(s.getReportType())) {
						docRef = s.getCareContextReferenceNumber();
						patienName = s.getPatientDisplay();
						docName = s.getDoctor();
						ConsulstionDate = s.getConsulstionDate().toString();
						interactionId = s.getCareContextReferenceNumber();
						fileType = "DIAGNOSTIC REPORTS";
						reportTypeNew = "DIAGNOSTIC REPORTS (LAB) (" + interactionId + ")";
						resultId = s.getResultId();
						if (!resultId.isEmpty()) {
							actualPrescriptionPath = this.restCallJaserServiceForPerscription("", "", "", resultId, "",
									reportTypeNew);
							LOGGER.info("LAB REPORTS FOUND");
						}
					} else if ("RADIOLOGY".equals(s.getReportType())) {
						docRef = s.getCareContextReferenceNumber();
						patienName = s.getPatientDisplay();
						docName = s.getDoctor();
						ConsulstionDate = s.getConsulstionDate().toString();
						interactionId = s.getCareContextReferenceNumber();
						fileType = "DIAGNOSTIC REPORTS";
						reportTypeNew = "DIAGNOSTIC REPORTS (RADIOLOGY)(" + interactionId + ")";
						resultId = s.getResultId();
						if (!resultId.isEmpty()) {
							actualPrescriptionPath = this.restCallJaserServiceForPerscription("", "", "", resultId, "",
									reportTypeNew);
							LOGGER.info("RADIOLOGY REPORTS FOUND");
						}
					}
				} else {
					if ("PRESCRIPTION".equals(s.getReportType())) {
						docRef = s.getCareContextReferenceNumber();
						patienName = s.getPatientDisplay();
						docName = s.getDoctor();
						ConsulstionDate = s.getConsulstionDate().toString();
						interactionId = s.getCareContextReferenceNumber();
						visitDate1 = s.getVisitDate();
						clinicName = s.getClinicName();
						visitStartTime = s.getVisitStartTime();
						followUpDate = s.getFollowUpDate();
						visitEndTime = s.getVisitEndTime();
						ObjectMapper objectMapper = new ObjectMapper();
						history = objectMapper.readValue(s.getVitalHistory(), VitalHistory.class);
						log.info("Vitals History   {}", history);
						fileType = "PRESCRIPTION";
						reportTypeNew = "PRESCRIPTION";
						medication = s.getMeds();
						medDirections = s.getMedDirections();
						chiefComplaints = s.getChiefComplaints();
						investigationAdvice = objectMapper.readValue(s.getInvestigationAdvice(),
								InvestigationAdvice.class);
						List<Master> masters = investigationAdvice.getMaster();
						allinvestigationAdvice = (investigationAdvice.getOtherInvestigation() != null
								&& !investigationAdvice.getOtherInvestigation().isEmpty())
										? masters.stream().map(Master::getInvestigation)
												.collect(Collectors.joining(","))
										: masters.stream().map(Master::getInvestigation)
												.collect(Collectors.joining(",")) + ","
												+ investigationAdvice.getOtherInvestigation();
						if (s.getFilePath() != null && !s.getFilePath().isEmpty()) {
							actualPrescriptionPath = prescirptionPath + s.getFilePath();
						}
					}
				}
				LOGGER.info("REPORT PATH:{}", actualPrescriptionPath);
				if (!prescirptionPath.equals(actualPrescriptionPath.trim()) && !actualPrescriptionPath.isEmpty()) {
					LOGGER.info("ENTERED INTO CONVERTING INTO BASE 64");
					byte[] fileByte = webHookService.convertUrlDocToBytes(actualPrescriptionPath);
					LOGGER.info("ENTERING INTO BUILDING FHIR BUNDLE START");

					String query = "select * from abha_registration ar where ar.tenant_id ='"
							+ TenantContext.getCurrentTenant() + "'and ar.patient_id ='" + patientId + "'";
					log.info("========query=========" + query);
					AccountProfile profile = null;
					List<AbhaRegistrationDTO> abhaRegistrationdto = jdbcTemplateHelper.getResults(query,
							AbhaRegistrationDTO.class);
					if (abhaRegistrationdto != null && !abhaRegistrationdto.isEmpty()) {
						log.info("===dto========{}", abhaRegistrationdto);
						String profileDetails = abhaRegistrationdto.get(0).getAbha_profile().toString();
						log.info("=====Abharegistrationdto======{}", abhaRegistrationdto.get(0).getAbha_profile());
						if (profileDetails != null) {
							profile = objMapper.readValue(profileDetails, AccountProfile.class);
						}
					}
					String fhirBundle = sfhirB.buildFhirBundle(docName, chiefComplaints, medication, medDirections,
							visitDate1, fileType, visitEndTime, visitStartTime, history, profile, fileByte,
							ConsulstionDate, allinvestigationAdvice, clinicName, followUpDate);
					byte[] dataBytes = fhirBundle.getBytes();
					byte[] mdHash = MessageDigest.getInstance("MD5").digest(dataBytes);
					String dataChecksum = new BigInteger(1, mdHash).toString(16);
					if (keysMaterial != null) {
						EncryptionRequest encryptPaylod = new EncryptionRequest();
						encryptPaylod.setPlainTextData(fhirBundle);
						encryptPaylod.setReceiverNonce(nonce);
						encryptPaylod.setReceiverPublicKey(ReceiverPubliKey);
						encryptPaylod.setSenderNonce(keysMaterial.getNonce());
						encryptPaylod.setSenderPrivateKey(keysMaterial.getPrivateKey());
						encryptPaylod.setSenderPublicKey(keysMaterial.getPublicKey());
						EncryptionResponse encryptResponse = encrypController.encrypt(encryptPaylod);
						pageNO++;
						DataPushPayload dataPushPayload = webHookService.constructDataPushPayload(transactionId,
								interactionId, encryptResponse.getEncryptedData(), encryptResponse.getKeyToShare(),
								keysMaterial.getNonce(), pageNO, pagecount, dataChecksum, dataEraseAt);
						String dataPushString = objMapper.writeValueAsString(dataPushPayload);
						log.info("==============DATA PUSH START===========");
						ResponseEntity<ResponseBean> saveAndReturnResponse = webHookService.saveAndReturnResponse(
								dataPushString, dataPushUrl, "DATA-PUSH", UUID.randomUUID().toString(), transactionId,
								30, "", concentId, interactionId, "");
						if (saveAndReturnResponse != null && saveAndReturnResponse.getBody() != null) {
							if (saveAndReturnResponse.getBody().getData() != null
									&& saveAndReturnResponse.getBody().getMessage() != null
									&& "DATA-PUSH SUCCESSFUL AND SAVED IN DB"
											.equals(saveAndReturnResponse.getBody().getMessage())) {
								hipRequestList.add((HIPRequest) saveAndReturnResponse.getBody().getData());
								filePushStatus = true;
								careContextString.add(interactionId);
							}
						}
						hipDataPushLogs.add(this.addDataPushLogs(transactionId, fhirBundle,
								encryptResponse.getEncryptedData(), interactionId, filePushStatus));
					}
				} else {
					log.error("No Precription Path for Care Context====> {}", docRef);
				}
			}
			afterPush = webHookService.constructNotifyHipAfterPushPayload(concentId, transactionId, careContextString,
					hipId, hipId, "HIP", "TRANSFERRED", "RECORDS TRANSFERRED SUCCESSFULLY");
			transferFileNotifyRequest = objMapper.writeValueAsString(afterPush);
			log.info("==============DATA PUSH END==============");
//			webHookService.saveAndReturnResponse(transferFileNotifyRequest, fileTransferNotifyUrl, "FILE-TRANSFER",
//					UUID.randomUUID().toString(), transactionId, 29, null, concentId, null, null);
			webHookService.saveAndReturnResponse(transferFileNotifyRequest, healthInformationNotifyUrl,
					"HEALTH-INFORMATION-NOTIFY", UUID.randomUUID().toString(), transactionId, 29, null, concentId, null,
					null);
			hipRequestRepo.saveAll(hipRequestList);
			hipDataPushLogsRepository.saveAll(hipDataPushLogs);
		} else {
			log.error("QUERY DID NOT RETURN ANY RESULTSET : {}", careContextRecords.size());
		}
	}

	private void pushRecordsProcessNew(List<RequestResultSetDtoNew> requestResultSetDtoNew, String nonce,
			String transactionId, List<CareContext> careContexts, String dataPushUrl, String ReceiverPubliKey,
			LocalDateTime dataEraseAt, String concentId, String hipId, List<String> labResultId) throws Exception {
		int pageNO = 0;
		int pagecount = 0;
		NotifyHipAfterDataPush afterPush = new NotifyHipAfterDataPush();
		KeyMaterial keysMaterial = keys.generate();
		String patientId = careContexts.get(0).getPatientReference();
		String docRef = "";
		String patienName = "";
		String docName = "";
		String prescirptionPath = "";
		String consulstionDate = "";
		String fileType = "";
		String interactionId = "";
		String transferFileNotifyRequest = "";
		String resultId = "";
		String reportTypeNew = "";
		String clinicName = "";
		List<HIPDataPushLog> hipDataPushLogs = new ArrayList<HIPDataPushLog>();
		List<HIPRequest> hipRequestList = new ArrayList<>();
		boolean filePushStatus = false;
		List<String> careContextString = new ArrayList<>();
		if (!requestResultSetDtoNew.isEmpty()) {
			prescirptionPath = jdbcTemplateHelper
					.getTenantUrl("select url_path from public.orgnization_registration or2 where or2.id="
							+ TenantContext.getCurrentTenant());
			for (RequestResultSetDtoNew s : requestResultSetDtoNew) {
				String actualPrescriptionPath = "";
				if (himsTenantIds.contains(Integer.parseInt(TenantContext.getCurrentTenant()))) {
					if ("LAB".equals(s.getReportType())) {
						docRef = s.getCareContextReferenceNumber();
						patienName = s.getPatientDisplay();
						docName = s.getDoctor();
						consulstionDate = s.getConsulstionDate().toString();
						interactionId = s.getCareContextReferenceNumber();
						clinicName = s.getClinicName();
						fileType = "LAB REPORT";
						reportTypeNew = "DIAGNOSTIC REPORTS (LAB) (" + interactionId + ")";
						resultId = s.getResultId();
						if (!resultId.isEmpty()) {
							actualPrescriptionPath = this.restCallJaserServiceForPerscription(null, null, null, resultId, null,
									reportTypeNew);
							LOGGER.info("LAB REPORTS FOUND");
						}
					} else if ("RADIOLOGY-REPORT".equals(s.getReportType())) {
						docRef = s.getCareContextReferenceNumber();
						patienName = s.getPatientDisplay();
						docName = s.getDoctor();
						consulstionDate = s.getConsulstionDate().toString();
						clinicName = s.getClinicName();
						interactionId = s.getCareContextReferenceNumber();
						fileType = "RADIOLOGY REPORT";
						reportTypeNew = "DIAGNOSTIC REPORTS (RADIOLOGY)(" + interactionId + ")";
						resultId = s.getResultId();
						if (!resultId.isEmpty()) {
							actualPrescriptionPath = this.restCallJaserServiceForPerscription(null, null, null, resultId, null,
									reportTypeNew);
							LOGGER.info("RADIOLOGY REPORTS FOUND");
						}
					} else if ("DISCHARGE SUMMARY".equals(s.getReportType())) {
						docRef = s.getCareContextReferenceNumber();
						patienName = s.getPatientDisplay();
						docName = s.getDoctor();
						clinicName = s.getClinicName();
						consulstionDate = s.getConsulstionDate().toString();
						interactionId = s.getCareContextReferenceNumber();
						fileType = "DISCHARGE SUMMARY";
						reportTypeNew = "DIAGNOSTIC REPORTS (DISCHARGE SUMMARY)(" + interactionId + ")";
						resultId = s.getResultId();
						if (!resultId.isEmpty()) {
							actualPrescriptionPath = this.restCallJaserServiceForPerscription(null, null, null, resultId, null,
									reportTypeNew);
							LOGGER.info("DISCHARGE SUMMARY FOUND");
						}
					}
				} else {
					if ("LAB REPORT".equals(s.getReportType())) {
						docRef = s.getCareContextReferenceNumber();
						patienName = s.getPatientDisplay();
						docName = s.getDoctor();
						clinicName = s.getClinicName();
						consulstionDate = s.getConsulstionDate().toString();
						interactionId = s.getCareContextReferenceNumber();
						fileType = "LAB REPORTS";
						reportTypeNew = "DIAGNOSTIC REPORTS (LAB) (" + interactionId + ")";
						resultId = s.getResultId();
						if (s.getFilePath() != null && !s.getFilePath().isEmpty()) {
							actualPrescriptionPath = prescirptionPath + s.getFilePath();
						}
					}
				}
				LOGGER.info("REPORT PATH:{}", actualPrescriptionPath);
				if (!prescirptionPath.equals(actualPrescriptionPath.trim()) && !actualPrescriptionPath.isEmpty()) {
					LOGGER.info("ENTERED INTO CONVERTING INTO BASE 64");
					byte[] fileByte = webHookService.convertUrlDocToBytes(actualPrescriptionPath);
					LOGGER.info("ENTERING INTO BUILDING FHIR BUNDLE START");
					String query = "select * from abha_registration ar where ar.tenant_id ='"
							+ TenantContext.getCurrentTenant() + "'and ar.patient_id ='" + patientId + "'";
					log.info("========query========={}", query);
					AccountProfile profile = null;
					List<AbhaRegistrationDTO> abhaRegistrationdto = jdbcTemplateHelper.getResults(query,
							AbhaRegistrationDTO.class);
					if (abhaRegistrationdto != null && !abhaRegistrationdto.isEmpty()) {
						log.info("===dto========{}", abhaRegistrationdto);
						String profileDetails = abhaRegistrationdto.get(0).getAbha_profile().toString();
						log.info("=====Abharegistrationdto======{}", abhaRegistrationdto.get(0).getAbha_profile());
						if (profileDetails != null) {
							profile = objMapper.readValue(profileDetails, AccountProfile.class);
						}
					}
					List<LabResultFetchDto> LabResultFetchDtoCs = hiuResponseRepository.findLabResults(docRef);
					LOGGER.info("=============> {}" + objMapper.writeValueAsString(LabResultFetchDtoCs));
					Map<String, List<LabResultFetchDto>> groupedByServiceId = LabResultFetchDtoCs.stream()
							.collect(Collectors.groupingBy(LabResultFetchDto::getServiceId));
					for (Map.Entry<String, List<LabResultFetchDto>> entry : groupedByServiceId.entrySet()) {
						String fhirBundle = bundleForLab.labBundle(fileByte, fileType, docRef, docName, consulstionDate,
								clinicName, profile, patienName, prescirptionPath, entry.getValue());
						LOGGER.info("ENTERING INTO BUILDING FHIR BUNDLE END  {}", fhirBundle);
						byte[] dataBytes = fhirBundle.getBytes();
						byte[] mdHash = MessageDigest.getInstance("MD5").digest(dataBytes);
						String dataChecksum = new BigInteger(1, mdHash).toString(16);
						if (keysMaterial != null) {
							EncryptionRequest encryptPaylod = new EncryptionRequest();
							encryptPaylod.setPlainTextData(fhirBundle);
							encryptPaylod.setReceiverNonce(nonce);
							encryptPaylod.setReceiverPublicKey(ReceiverPubliKey);
							encryptPaylod.setSenderNonce(keysMaterial.getNonce());
							encryptPaylod.setSenderPrivateKey(keysMaterial.getPrivateKey());
							encryptPaylod.setSenderPublicKey(keysMaterial.getPublicKey());
							EncryptionResponse encryptResponse = encrypController.encrypt(encryptPaylod);
							pageNO++;
							DataPushPayload dataPushPayload = webHookService.constructDataPushPayload(transactionId,
									interactionId, encryptResponse.getEncryptedData(), encryptResponse.getKeyToShare(),
									keysMaterial.getNonce(), pageNO, pagecount, dataChecksum, dataEraseAt);
							String dataPushString = objMapper.writeValueAsString(dataPushPayload);
							log.info("==============DATA PUSH START===========");
							LOGGER.info("DATA PUSH START==========>" + dataPushString);
							ResponseEntity<ResponseBean> saveAndReturnResponse = webHookService.saveAndReturnResponse(
									dataPushString, dataPushUrl, "DATA-PUSH", UUID.randomUUID().toString(),
									transactionId, 30, null, concentId, interactionId, null);
							if (saveAndReturnResponse != null && saveAndReturnResponse.getBody() != null) {
								if (saveAndReturnResponse.getBody().getData() != null
										&& saveAndReturnResponse.getBody().getMessage() != null
										&& "DATA-PUSH SUCCESSFUL AND SAVED IN DB"
												.equals(saveAndReturnResponse.getBody().getMessage())) {
									hipRequestList.add((HIPRequest) saveAndReturnResponse.getBody().getData());
									filePushStatus = true;
									careContextString.add(interactionId);
								}
							}
							hipDataPushLogs.add(this.addDataPushLogs(transactionId, fhirBundle,
									encryptResponse.getEncryptedData(), interactionId, filePushStatus));
						}
					}
				} else {
					log.error("No Precription Path for Care Context====> {}", docRef);
				}
				afterPush = webHookService.constructNotifyHipAfterPushPayload(concentId, transactionId,
						careContextString, hipId, hipId, "HIP", "TRANSFERRED", "RECORDS TRANSFERRED SUCCESSFULLY");
				transferFileNotifyRequest = objMapper.writeValueAsString(afterPush);
				log.info("==============DATA PUSH END==============");
				webHookService.saveAndReturnResponse(transferFileNotifyRequest, healthInformationNotifyUrl,
						"HEALTH-INFORMATION-NOTIFY", UUID.randomUUID().toString(), transactionId, 29, null, concentId,
						null, null);
				hipRequestRepo.saveAll(hipRequestList);
				hipDataPushLogsRepository.saveAll(hipDataPushLogs);

			}
		} else {
			log.error("QUERY DID NOT RETURN ANY RESULTSET :", requestResultSetDtoNew.size());
			LOGGER.info("QUERY DID NOT RETURN ANY RESULTSET :", requestResultSetDtoNew.size());
		}
	}

	private String restCallJaserServiceForPerscription(String patientId, String otTriageId, String dcid,
			String resultId, String jwtToken, String apiType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JSONObject json = new JSONObject();
		if (apiType.contains("OP CONSULTATION")) {
			json.put("id", 105);
			json.put("fileType", "pdf");
			json.put("patientId", patientId);
			json.put("opTriageId", otTriageId);
			json.put("dcId", dcid);
		} else if (apiType.contains("DIAGNOSTIC REPORTS (LAB)")) {
			json.put("id", 79);
			json.put("fileType", "pdf");
			json.put("resultId", resultId);
		} else if (apiType.contains("DIAGNOSTIC REPORTS (RADIOLOGY)")) {
			json.put("id", 32);
			json.put("fileType", "pdf");
			json.put("resultId", resultId);
		}
		HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);
		ResponseEntity<ResponseBean> postForEntity = restTemplate.postForEntity(himsJasperUrl, entity,
				ResponseBean.class);
		if (postForEntity != null && postForEntity.getBody().getData() != null) {
			ObjectMapper objMapper = new ObjectMapper();
			try {
				HashMap<?, ?> readValue = objMapper
						.readValue(objMapper.writeValueAsString(postForEntity.getBody().getData()), HashMap.class);
				if (readValue != null) {
					if (readValue.get("viewPath") != null) {
						return readValue.get("viewPath").toString();
					}
				}
			} catch (JsonMappingException e) {
				e.printStackTrace();
				return "";
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return "";
			}
		}
		return "";
	}

	private HIPDataPushLog addDataPushLogs(String transactionId, String fhirBundle, String encryptedData,
			String careContextId, Boolean filePushStatus) {
		HIPDataPushLog hipDataPushLogs = new HIPDataPushLog();
		hipDataPushLogs.setTxnCode(transactionId);
		hipDataPushLogs.setDataPushFhirBundle(fhirBundle);
		hipDataPushLogs.setEncryptedFhirBundle(encryptedData);
		hipDataPushLogs.setCareContextId(careContextId);
		hipDataPushLogs.setIsDataPushSuccessFull(filePushStatus);
		return hipDataPushLogs;

	}

	@PostMapping("patients/profile/on-share")
	@ApiOperation(value = "ON SHARE PROFILE")
	public ResponseEntity<?> onShareProfile(@RequestBody OnShareProfilePayload onShareString) {
		ResponseBean bean = null;
		try {
			String payLoad = objMapper.writeValueAsString(onShareString);
			log.info("===============ON SHARE PAYLOAD===============" + payLoad);
			return webHookService.saveAndReturnResponse(payLoad, onShareProfileUrl, "ON-SHARE-PROFILE",
					onShareString.getResp().getRequestId(), null, 22, null, null, null, null);
		} catch (Exception e) {
			log.error("Error in on Share");
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			e.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
		}
	}

	@PostMapping("links/link/on-init")
	@ApiOperation(value = "LINKS ON INIT")
	public ResponseEntity<?> linksOnInit(@RequestBody LinksOnInit linksOnInit, String transactionId) {
		ResponseBean bean = new ResponseBean();
		try {
			String payLoad = objMapper.writeValueAsString(linksOnInit);
			return webHookService.saveAndReturnResponse(payLoad, linksOnInitUrl, "LINKS-INIT",
					linksOnInit.getResp().getRequestId(), transactionId, 12, null, null, null, null);
		} catch (Exception e) {
			log.error("========================ERROR IN LINKS ON INIT========================");
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			e.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
		}
	}

	@PostMapping("links/link/on-confirm")
	@ApiOperation(value = "LINKS ON CONFIRM")
	public ResponseEntity<?> linksOnConfirm(@RequestBody LinkOnConfirm linksOnConfirmV) {
		ResponseBean bean = new ResponseBean();
		bean.setStatus(HttpStatus.OK);
		try {
			String payLoad = objMapper.writeValueAsString(linksOnConfirmV);
			return webHookService.saveAndReturnResponse(payLoad, linksONConfirm, "LINKS-CONFIRM",
					linksOnConfirmV.getResp().getRequestId(), null, 14, null, null, null, null);
		} catch (Exception e) {
			log.error("========================ERROR IN LINKS ON CONFIRM========================");
			e.printStackTrace();
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
		}
	}

	@PostMapping("users/auth/on-notify")
	@ApiOperation(value = "USER AUTH DIRECT ON NOFITY")
	public ResponseEntity<?> directUserAuthOnNofity(@RequestBody DirectAuthOnNotify directAuthOnNotify) {
		ResponseBean bean = new ResponseBean();
		try {
			bean.setStatus(HttpStatus.OK);
			String payLoad = objMapper.writeValueAsString(directAuthOnNotify);
			return webHookService.saveAndReturnResponse(payLoad, userAuthOnNotify, "DIRECT-AUTH",
					directAuthOnNotify.getResp().getRequestId(), null, 16, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("========================USERS AUTH ON NOTIFY========================");
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
		}
	}

	@PostMapping("patients/sms/notify2")
	@ApiOperation(value = "PATIENT SMS NOTIFY")
	public ResponseEntity<?> sendSmsNotification(@RequestBody String patientNotifyPayload,
			HttpServletRequest servletRequest) {
		JSONObject jsonObject = new JSONObject(patientNotifyPayload);
		abdmCentralDbSave.processPayload(patientNotifyPayload, 4, servletRequest);
		return webHookService.saveAndReturnResponse(patientNotifyPayload, patientSmsNotify, "SMS-NOTIFY",
				jsonObject.get("requestId").toString(), null, 23, null, null, null, null);
	}

	@PostMapping("health-information/notify")
	@ApiOperation(value = "NOTIFYING HIP AFTER FILE TRANSFER")
	public ResponseEntity<?> nofityingHipAfterFileTransfer(@RequestBody String fileTransferNotify,
			HttpServletRequest servletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
			JSONObject jsonObject = new JSONObject(fileTransferNotify);
			abdmCentralDbSave.processPayload(fileTransferNotify, 4, servletRequest);
			webHookService.saveAndReturnResponse(fileTransferNotify, fileTransferNotifyUrl, "FILE-TRANSFER",
					jsonObject.get("requestId").toString(), null, 0, null, null, null, null);
		} catch (Exception e) {
			log.error("========================ERROR IN FILE TRANSFER========================");
			bean.setData(null);
			bean.setMessage("EXCEPTION");
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			e.printStackTrace();
		}
		return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
	}

	@PostMapping(value = "patients/status/on-notify")
	public ResponseEntity<?> patientsStatusOnNotify(@RequestBody PatientStatuOnNotify request) throws Exception {
		ResponseBean bean = new ResponseBean();
		try {
			String payLoad = objMapper.writeValueAsString(request);
			webHookService.saveAndReturnResponse(payLoad, patientStatusOnNotify, "STATUS-NOTIFY",
					request.getResp().getRequestId(), null, 26, null, null, null, null);
		} catch (Exception e) {
			log.error("========================ERROR IN PATIENT ON NOTIFY========================");
			bean.setData(null);
			bean.setMessage("EXCEPTION");
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			e.printStackTrace();
		}
		return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
	}

	@GetMapping(value = "heartbeat")
	public ResponseEntity<?> heartBeat() {
		ResponseBean bean = new ResponseBean();
		HeartBeat heartBeat = new HeartBeat();
		heartBeat.setStatus("UP");
		heartBeat.setTimestamp(LocalDateTime.now());
		bean.setData(heartBeat);
		bean.setMessage("HIP STATUS");
		bean.setStatus(HttpStatus.OK);
		return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
	}

	@PostMapping(value = "links/context/notify")
	public ResponseEntity<?> sendPhrNotification(@RequestBody PHRNotifyPayload request,
			HttpServletRequest servletRequest) throws Exception {
		ResponseBean bean = new ResponseBean();
		try {
			String payload = objMapper.writeValueAsString(request);
			abdmCentralDbSave.processPayload(payload, 4, servletRequest);
			webHookService.saveAndReturnResponse(payload, sendPhrNotification, "PHR-NOTIFY", request.getRequestId(),
					null, 27, null, null, null, null);
		} catch (Exception e) {
			log.error("========================ERROR IN PHR NOTIFY========================");
			bean.setData(null);
			bean.setMessage("EXCEPTION");
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			e.printStackTrace();
		}
		return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
	}

	// GENERATE LINK TOKEN
	@PostMapping("/v3-generate-link-token")
	public ResponseEntity<?> generateLinkToken(@RequestBody GenerateLinkTokenPayload generateLinkTokenPayload,
			@RequestParam(value = "hip-id") String hipId, HttpServletRequest servletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
			String payload = objMapper.writeValueAsString(generateLinkTokenPayload);
			String requestId = generateLinkTokenPayload.getRequestId();
			abdmCentralDbSave.processPayload(payload, 4, servletRequest);
			webHookService.saveAndReturnResponse(payload, generateLinkTokenUrl, "V3-LINK-TOKEN", requestId, null, 33,
					null, hipId, null, null);
			bean.setMessage("LinkToken Generated Successfully");
			bean.setStatus(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(e);
			bean.setMessage("Something went Wrong..");
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<>(bean, bean.getStatus());

	}

	@PostMapping("/link/carecontextv3")
	@ApiOperation(value = "LINK CARE CONTEXT V3")
	public ResponseEntity<?> linkCareContextv3(@RequestBody LinkCareContextV3Payload linkCareContextv3,
			@RequestParam(value = "hip-id") String hipId, HttpServletRequest servletRequest) {
		ResponseBean bean = new ResponseBean();
		LOGGER.info("====================LINK CARE CONTEXT V3 STARTED===================");
		bean.setStatus(HttpStatus.OK);
		bean.setData(null);
		String requestId = "";
		try {
			requestId = linkCareContextv3.getRequestId();
			String token = linkCareContextv3.getToken();
			String payload = objMapper.writeValueAsString(linkCareContextv3);
			abdmCentralDbSave.processPayload(payload, 4, servletRequest);
			linkCareContextv3.setRequestId(null);
			linkCareContextv3.setToken(null);
			webHookService.saveAndReturnResponse(payload, linkCareContextUrl, "LINK-CARE-CONTEXT-V3", requestId, null,
					35, null, hipId, null, token);
		} catch (Exception e) {
			LOGGER.error("===================ERROR IN LINK CARE CONTEXT=================" + e.getMessage());
			bean.setData(e);
			bean.setMessage("Something went Wrong..");
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			e.printStackTrace();
		}
		return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
	}

	@PostMapping("/care-contextsv3/on-discover")
	@ApiOperation(value = "ON DISCOVER V3")
	public ResponseEntity<?> careContextOnDiscoverV3(@Valid @RequestBody CareContextOnDiscoverV3 onDiscover)
			throws JsonMappingException, JsonProcessingException {
//		validator.validateFields(onDiscover);
		log.info("=========================ON DISCOVER V3 STARTED==============================");
		ResponseBean bean = null;
		try {
			String onDiscoverPayload = objMapper.writeValueAsString(onDiscover);
			return webHookService.saveAndReturnResponse(onDiscoverPayload, v3onDiscover, "ON-DISCOVER-V3",
					onDiscover.getResponse().getRequestId(), onDiscover.getTransactionId(), 10, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("===================ERROR IN ON DISCOVER V3=================" + e.getMessage());
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
		}

	}

	// HIU ID in headers is sent as authToken to saveAndReturnResponse
	@PostMapping("care-contextsv3/link/on-init")
	@ApiOperation(value = "LINKS ON INIT")
	public ResponseEntity<?> careContextV3LinksOnInit(@Valid @RequestBody LinksOnInit linksOnInit) {
		ResponseBean bean = new ResponseBean();
		try {
			validator.validateFields(linksOnInit);
			String payLoad = objMapper.writeValueAsString(linksOnInit);
			return webHookService.saveAndReturnResponse(payLoad, v3linksOnInitUrl, "LINKS-ON-INIT-V3",
					linksOnInit.getResponse().getRequestId(), linksOnInit.getTransactionId(), 12, null, null, null,
					linksOnInit.getHiuId());
		} catch (Exception e) {
			log.error("========================ERROR IN LINKS ON INIT V3========================");
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			e.printStackTrace();
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
		}
	}

	@PostMapping("links/link/v3on-confirm")
	@ApiOperation(value = "V3 LINKS ON CONFIRM")
	public ResponseEntity<?> linksOnConfirmv3(@Valid @RequestBody LinkOnConfirm linksOnConfirm) {
		ResponseBean bean = new ResponseBean();
		bean.setStatus(HttpStatus.OK);
		try {
			String payLoad = objMapper.writeValueAsString(linksOnConfirm);
			return webHookService.saveAndReturnResponse(payLoad, v3linksONConfirm, "LINKS-ON-CONFIRM-V3",
					linksOnConfirm.getResponse().getRequestId(), null, 14, null, null, null, null);
		} catch (Exception e) {
			log.error("========================ERROR IN LINKS ON CONFIRM V3========================");
			e.printStackTrace();
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
		}
	}
}
