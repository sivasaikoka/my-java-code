package com.dipl.abha.controllers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.entities.HIURequest;
import com.dipl.abha.m2.requestypayload.RequestPayload;
import com.dipl.abha.m3.consentFetch.ConsentsFetch;
import com.dipl.abha.m3.consentHiuOnNotify.Acknowledgement;
import com.dipl.abha.m3.consentHiuOnNotify.ConsentsHiuOnNotify;
import com.dipl.abha.m3.consentRequestInit.ConsentRequestInitPayload;
import com.dipl.abha.m3.consentRequestInit.ConsentRequestV3Init;
import com.dipl.abha.m3.consentRequestOnInit.ConsentStatus;
import com.dipl.abha.m3.findpatient.PatientFindPayload;
import com.dipl.abha.others.PayloadException;
import com.dipl.abha.others.PayloadValidator;
import com.dipl.abha.repositories.HIUDecryptedDocumentRepository;
import com.dipl.abha.service.ABHAM3Service;
import com.dipl.abha.service.AllAbdmCentralDbSave;
import com.dipl.abha.util.ConstantUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import lombok.Synchronized;

/**
 * 
 * @author Abhinay
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value = { "m3/v0.5", "m3/v3" })
public class NDHMM3Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(NDHMM3Controller.class);

	@Value("${FIND_PATIENT}")
	private String findPatientUrl;

	@Value("${CONSENT_REQUEST_INIT}")
	private String consentRequestInitUrl;

	@Value("${CONSENT_REQUEST_INIT_V3}")
	private String v3consentRequestInitUrl;

	@Value("${CONSENT_STATUS}")
	private String consentStatusUrl;

	@Value("${CONSENT_STATUS_V3}")
	private String v3consentStatusUrl;

	@Value("${CONSENTS_HIU_ON_NOTIFY}")
	private String consentHiuOnNotifysUrl;

	@Value("${CONSENTS_HIU_ON_NOTIFY_V3}")
	private String consentHiuOnNotifysUrlv3;

	@Value("${CONSENTS_FETCH}")
	private String consentFetchUrl;

	@Value("${CONSENTS_FETCH_V3}")
	private String v3consentFetchUrl;

	@Value("${CM_REQUEST}")
	private String cmRequestUrl;

	@Value("${HEALTH_INFORMATION_REQUEST}")
	private String healthInformationRequestUrl;

	@Autowired
	private ABHAM3Service abham3Service;

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private ABHAM3Service abhaM3Service;

	@Autowired
	private AllAbdmCentralDbSave abdmCentralDbSave;

	@Autowired
	private HIUDecryptedDocumentRepository decryptedDocumentRepo;

	@Autowired
	private PayloadValidator validator;

	@PostMapping("/patients/find")
	@ApiOperation(value = "FIND PATIENT")
	private ResponseEntity<?> creatingCompanyUrlForCallBacks(@RequestBody PatientFindPayload findPatient,
			@RequestParam Long caseId, HttpServletRequest httpServletRequest) {
		LOGGER.info("FINDNG PATIENT WITH ABHA ID");
		ResponseBean bean = new ResponseBean();
		String requestId = "";
		String findPatientPayload = "";
		String healthId = "";
		try {
			requestId = findPatient.getRequestId();
			healthId = findPatient.getQuery().getPatient().getId();
			findPatientPayload = objMapper.writeValueAsString(findPatient);
			ResponseEntity<ResponseBean> auth = abham3Service.callingNdhmApi(findPatientPayload, findPatientUrl,
					"FIND-PATIENT", null, requestId);
			abdmCentralDbSave.processPayload(findPatientPayload, 1, httpServletRequest);
			return this.saveAndReturnResponse(auth, requestId, null, findPatientPayload, 9, null, healthId,
					"FIND PATEINT", null);
		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(null);
			bean.setMessage("ERROR IN FIND PATIENT");
			LOGGER.info("ERROR IN FIND PATIENT");
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/consent-requests/init")
	@ApiOperation(value = "CONSENT REQUESTS INIT")
	private ResponseEntity<?> consentRequestInit(@RequestBody String consentReqInit, @RequestParam Long beneficiaryId,
			HttpServletRequest httpServletRequest, @RequestParam(value = "doctor_id") Optional<Long> doctorId) {
		LOGGER.info("CONSENT REQUESTS INIT");
		ResponseBean bean = new ResponseBean();
		String requestId = "";
		String healthId = "";
		try {
			ConsentRequestInitPayload concentInit = objMapper.readValue(consentReqInit,
					ConsentRequestInitPayload.class);
			if (LocalDateTime.now()
					.isAfter(this.returnLocalTime(concentInit.getConsent().getPermission().getDataEraseAt()))) {
				bean.setData(null);
				bean.setMessage("CONSENT EXPIRY MUST BE IN FUTURE");
				LOGGER.info("CONSENT EXPIRY MUST BE IN FUTURE");
				bean.setStatus(HttpStatus.BAD_REQUEST);
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.BAD_REQUEST);
			}
			requestId = concentInit.getRequestId();
			concentInit.getConsent().getPermission().getDateRange()
					.setFrom(this.returnTimeUTC(concentInit.getConsent().getPermission().getDateRange().getFrom()) + 'Z');
			concentInit.getConsent().getPermission().getDateRange()
					.setMyto(this.returnTimeUTC(concentInit.getConsent().getPermission().getDateRange().getMyto()) + 'Z');
			concentInit.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
			concentInit.getConsent().getPermission().setDataEraseAt(
					this.returnTimeUTC(concentInit.getConsent().getPermission().getDataEraseAt()) + 'Z');
			healthId = concentInit.getConsent().getPatient().getId();
			String finalConsentRequest = objMapper.writeValueAsString(concentInit);
			abdmCentralDbSave.processPayload(finalConsentRequest, 1, httpServletRequest);
			ResponseEntity<ResponseBean> auth = abham3Service.callingNdhmApi(finalConsentRequest, consentRequestInitUrl,
					"CONSENT-REQUEST-INIT", null, requestId);
			return this.saveAndReturnResponse(auth, requestId, null, finalConsentRequest, 1, beneficiaryId, healthId,
					"CONSENT REQUEST INIT", doctorId.isPresent() ? doctorId.get() : null);
		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(null);
			bean.setMessage("ERROR IN CONSENT REQUEST INIT ");
			LOGGER.info("ERROR IN CONSENT REQUEST INIT ");
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/consent-requests/status")
	@ApiOperation(value = "KNOW CONSENT STATUS")
	private ResponseEntity<?> consentRequestStatus(@RequestBody ConsentStatus consentStaus) {
		LOGGER.info("CONSENT REQUESTS INIT");
		ResponseBean bean = new ResponseBean();
		String requestId = "";
		String consentStatusPayload = "";
		try {
			requestId = consentStaus.getRequestId();
			consentStatusPayload = objMapper.writeValueAsString(consentStaus);
			ResponseEntity<ResponseBean> auth = abham3Service.callingNdhmApi(consentStatusPayload, consentStatusUrl,
					"CONSENT-STATUS", null, requestId);
			if (auth.getStatusCode().compareTo(HttpStatus.ACCEPTED) == 0
					|| auth.getStatusCode().compareTo(HttpStatus.OK) == 0) {
				bean.setData(null);
				this.saveAndReturnResponse(auth, requestId, "", consentStatusPayload, 12, null, null, "CONSENT-STATUS",
						null);
				bean.setMessage("CONSENT STATUS SUCCESSFUL AND SAVED IN DB");
				LOGGER.info("CONSENT STATUS SUCCESSFUL AND SAVED IN DB");
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
			} else {
				this.saveAndReturnResponse(auth, requestId, "", consentStatusPayload, 12, null, null, "CONSENT-STATUS",
						null);
				bean.setData(null);
				bean.setMessage("CONSENT STATUS  CALLED SUCCESSFULL AND DID NOT SEND SUCCESS CODE");
				LOGGER.info("CONSENT STATUS  CALLED SUCCESSFULL AND DID NOT SEND SUCCESS CODE");
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(null);
			bean.setMessage("ERROR IN CONSENT STATUS ");
			LOGGER.info("ERROR IN CONSENT STATUS ");
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/consents/hiu/on-notify")
	@ApiOperation(value = "CONSENTS HIU ON-NOTIFY")
	public ResponseEntity<?> consentOnNotify(@RequestBody ConsentsHiuOnNotify consentsHiuOnNotify,
			HttpServletRequest httpServletRequest) {
		LOGGER.info("CONSENT REQUESTS INIT");
		ResponseBean bean = new ResponseBean();
		String requestId = "";
		String consentHiuOnNotifyPayload = "";
		String status = consentsHiuOnNotify.getStatusForRef();
		try {
			consentsHiuOnNotify.setStatusForRef(null);
			requestId = consentsHiuOnNotify.getRequestId();
			consentHiuOnNotifyPayload = objMapper.writeValueAsString(consentsHiuOnNotify);
			abdmCentralDbSave.updateCallBackInCentralDb(Integer.parseInt(TenantContext.getCurrentTenant()),
					consentHiuOnNotifyPayload, "", consentsHiuOnNotify.getResponse().getRequestId(), "");
			ResponseEntity<ResponseBean> auth = abham3Service.callingNdhmApiv3(consentsHiuOnNotify,
					consentHiuOnNotifysUrlv3, "CONSENT-HIU-ON-NOTIFY");
			if (status.equals("GRANTED")) {
				if (!consentsHiuOnNotify.getAcknowledgement().isEmpty()) {
					for (Acknowledgement ca : consentsHiuOnNotify.getAcknowledgement()) {
						ConsentRequestV3Init consentFetch = new ConsentRequestV3Init();
//						ConsentsFetch consentFetch = new ConsentsFetch();
						consentFetch.setRequestId(UUID.randomUUID().toString());
						consentFetch.setTimestamp(ConstantUtil.utcTimeStamp());
						consentFetch.setConsentId(ca.getConsentId());
						consentFetch.setHiuId(consentsHiuOnNotify.getHiuId());
//						this.consentFetch(consentFetch, httpServletRequest);
						this.consentFetchv3(consentFetch, httpServletRequest);
					}
				}
			} else {
				if (!consentsHiuOnNotify.getAcknowledgement().isEmpty()) {
					Set<String> consentIds = consentsHiuOnNotify.getAcknowledgement().stream()
							.filter(s -> s.getConsentId() != null && !s.getConsentId().isEmpty())
							.map(p -> p.getConsentId()).collect(Collectors.toSet());
					if (!consentIds.isEmpty()) {
						decryptedDocumentRepo.deleteExpireOrGrantedFromDecryptDocs(consentIds);
						decryptedDocumentRepo.deleteExpireOrGrantedHiuRespDocs(consentIds);
					}
				}
			}
			return this.saveAndReturnResponse(auth, requestId, null, consentHiuOnNotifyPayload, 4, null, null,
					"CONSENT HIU ON NOTIFY", null);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("ERROR IN CONSENT HIU ON NOTIFY ");
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PostMapping("/consents/fetch")
	@ApiOperation(value = "CONSENT FETCH")
	@Synchronized
	private ResponseEntity<?> consentFetch(@RequestBody ConsentsFetch consentFetch,
			HttpServletRequest httpServletRequest) {
		LOGGER.info("CONSENT FETCH");
		ResponseBean bean = new ResponseBean();
		String requestId = "";
		String consentFetchPayload = "";
		try {
			requestId = consentFetch.getRequestId();
			consentFetchPayload = objMapper.writeValueAsString(consentFetch);
			abdmCentralDbSave.processPayload(consentFetchPayload, 1, httpServletRequest);
			ResponseEntity<ResponseBean> auth = abham3Service.callingNdhmApi(consentFetchPayload, consentFetchUrl,
					"CONSENT-FETCH", null, requestId);
			return this.saveAndReturnResponse(auth, requestId, null, consentFetchPayload, 5, null, null,
					"CONSENT FETCH", null);
		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(null);
			bean.setMessage("ERROR IN CONSENT FETCH ");
			LOGGER.info("ERROR IN CONSENT FETCH ");
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/health-information/cm/request")
	@ApiOperation(value = "CM REQUEST")
	public ResponseEntity<?> cmRequest(@RequestBody RequestPayload requestPayload,
			HttpServletRequest httpServletRequest) {
		LOGGER.info("CM REQUEST");
		ResponseBean bean = new ResponseBean();
		String requestId = "";
		String cmRequest = "";
		String hiuId = "";
		try {
			requestId = requestPayload.getRequestId();
			String privateKey = requestPayload.getPrivateKey();
			requestPayload.setPrivateKey(null);
			hiuId = httpServletRequest.getHeader("X-HIU-ID");
			cmRequest = objMapper.writeValueAsString(requestPayload);
			abdmCentralDbSave.processPayload(cmRequest, 1, httpServletRequest);
//			ResponseEntity<ResponseBean> auth = abham3Service.callingNdhmApi(cmRequest, cmRequestUrl, "CM-REQUEST");
//			return this.saveAndReturnResponse(auth, requestId, privateKey, cmRequest, 7, null, null, "CM REQUEST",
//					null);
			ResponseEntity<ResponseBean> auth = abham3Service.callingNdhmApi(cmRequest, healthInformationRequestUrl,
					"HEALTH-INFORMATION-REQUEST", hiuId, requestId);
			return this.saveAndReturnResponse(auth, requestId, privateKey, cmRequest, 7, null, null,
					"HEALTH-INFORMATION-REQUEST", null);
		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(null);
			bean.setMessage("ERROR IN HEALTH_INFORMATION REQUEST ");
			LOGGER.info("ERROR IN HEALTH_INFORMATION REQUEST ");
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<ResponseBean> saveAndReturnResponse(ResponseEntity<ResponseBean> auth, String requestId,
			String txnId, String requestPayload, int apiTypeId, Long caseId, String healthId, String apiType,
			Long doctorId) {
		HIURequest hiuRequest = null;
		ResponseBean bean = new ResponseBean();
		bean.setStatus(HttpStatus.OK);
		try {
			if (auth.getStatusCode().compareTo(HttpStatus.ACCEPTED) == 0
					|| auth.getStatusCode().compareTo(HttpStatus.OK) == 0) {
				hiuRequest = abhaM3Service.insertIntoHiuRequest(requestId, txnId, requestPayload, apiTypeId, caseId,
						healthId, null, doctorId);
				bean.setData(hiuRequest);
				bean.setMessage(apiType + " SUCCESSFUL AND SAVED IN DB");
				LOGGER.info(apiType + " SUCCESSFUL AND SAVED IN DB");
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.OK);
			} else {
				hiuRequest = abhaM3Service.insertIntoHiuRequest(requestId, txnId, requestPayload, apiTypeId, caseId,
						healthId, auth.getBody().getMessage(), doctorId);
				bean.setData(hiuRequest);
				bean.setMessage(apiType + " CALLED SUCCESSFULL AND DID NOT SEND SUCCESS CODE");
				LOGGER.info(apiType + " CALLED SUCCESSFULL AND DID NOT SEND SUCCESS CODE");
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(null);
			bean.setMessage("ERROR IN " + apiType);
			bean.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			LOGGER.info("ERROR IN " + apiType);
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public LocalDateTime returnLocalTime(String customTimestamp) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		LocalDateTime localDateTime = LocalDateTime.parse(customTimestamp, formatter);
		return localDateTime;
	}

	public String returnTimeUTC(String customTimestamp) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		LocalDateTime localDateTime = LocalDateTime.parse(customTimestamp, formatter);
		System.out.println(localDateTime.minusHours(5).minusMinutes(30).toString());
		return localDateTime.minusHours(5).minusMinutes(30).toString() + ".000";
	}

	public String returnTime(String customTimestamp) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
		LocalDateTime localDateTime = LocalDateTime.parse(customTimestamp, formatter);
		return localDateTime.toString() + ".000";
	}

	@PostMapping("/consent-requestsv3/init")
	@ApiOperation(value = "CONSENT REQUESTS V3 INIT")
	private ResponseEntity<?> consentRequestv3Init(@RequestBody String consentReqInit,
			@RequestParam(value = "beneficiary_id") Optional<Long> beneficiaryId, HttpServletRequest httpServletRequest,
			@RequestParam(value = "doctor_id") Optional<Long> doctorId, BindingResult bindinResultSet) {
		LOGGER.info("CONSENT REQUESTS INIT");
		ResponseBean bean = new ResponseBean();
//		String requestId = "";
//		String consentReqPayload = "";
		String healthId = "";
		try {
			ConsentRequestV3Init concentInit = objMapper.readValue(consentReqInit, ConsentRequestV3Init.class);

			try {
				validator.validateFields(concentInit);
			} catch (PayloadException e) {
				bean.setStatus(HttpStatus.BAD_REQUEST);
				bean.setMessage(e.getErrorMap().entrySet().stream().map(p -> p.getValue().toString())
						.collect(Collectors.joining(",")));
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.BAD_REQUEST);

			}

			if (LocalDateTime.now()
					.isAfter(this.returnLocalTime(concentInit.getConsent().getPermission().getDataEraseAt()))) {
				bean.setData(null);
				bean.setMessage("CONSENT EXPIRY MUST BE IN FUTURE");
				LOGGER.info("CONSENT EXPIRY MUST BE IN FUTURE");
				bean.setStatus(HttpStatus.BAD_REQUEST);
				return new ResponseEntity<ResponseBean>(bean, HttpStatus.BAD_REQUEST);
			}

			concentInit.getConsent().getPermission().getDateRange().setFrom(
					this.returnTimeUTC(concentInit.getConsent().getPermission().getDateRange().getFrom()) + 'Z');
			concentInit.getConsent().getPermission().getDateRange().setMyto(
					this.returnTimeUTC(concentInit.getConsent().getPermission().getDateRange().getMyto()) + 'Z');
			concentInit.getConsent().getPermission().setDataEraseAt(
					this.returnTimeUTC(concentInit.getConsent().getPermission().getDataEraseAt()) + 'Z');

			healthId = concentInit.getConsent().getPatient().getId();
			String finalConsentRequest = objMapper.writeValueAsString(concentInit);
			abdmCentralDbSave.processPayload(finalConsentRequest, 1, httpServletRequest);
			ResponseEntity<ResponseBean> auth = abham3Service.callingNdhmApiv3(concentInit, v3consentRequestInitUrl,
					"CONSENT-REQUEST-INIT-V3");
			return this.saveAndReturnResponse(auth, concentInit.getRequestId(), null, finalConsentRequest, 1,
					beneficiaryId.isPresent() ? beneficiaryId.get() : null, healthId, "CONSENT REQUEST INIT V3",
					doctorId.isPresent() ? doctorId.get() : null);
		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(null);
			bean.setMessage("ERROR IN CONSENT REQUEST INIT V3");
			LOGGER.info("ERROR IN CONSENT REQUEST INIT V3");
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/consent-requestsv3/status")
	@ApiOperation(value = "KNOW CONSENT STATUS V3")
	private ResponseEntity<?> consentRequestStatusv3(@RequestBody String consentStaus) {
		LOGGER.info("CONSENT STATUS INIT v3");
		ResponseBean bean = new ResponseBean();
		String requestId = "";
		String consentStatusPayload = "";
		try {
			ConsentRequestV3Init concentStatus = objMapper.readValue(consentStaus, ConsentRequestV3Init.class);
			requestId = concentStatus.getRequestId();
			consentStatusPayload = objMapper.writeValueAsString(concentStatus);
			ResponseEntity<ResponseBean> auth = abham3Service.callingNdhmApiv3(concentStatus, v3consentStatusUrl,
					"CONSENT-STATUS-V3");
			bean.setData(null);
			return this.saveAndReturnResponse(auth, requestId, null, consentStatusPayload, 12, null, null,
					"CONSENT-STATUS-V3", null);
		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(null);
			bean.setMessage("ERROR IN CONSENT STATUS V3");
			LOGGER.info("ERROR IN CONSENT STATUS V3");
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/consentsv3/fetch")
	@ApiOperation(value = "CONSENT FETCH")
	@Synchronized
	private ResponseEntity<?> consentFetchv3(@RequestBody ConsentRequestV3Init concentFetch,
			HttpServletRequest httpServletRequest) {
		LOGGER.info("CONSENT FETCH");
		ResponseBean bean = new ResponseBean();
		String requestId = "";
		String consentFetchPayload = "";
		try {
//			ConsentRequestV3Init consentFetch = objMapper.readValue(concentFetch, ConsentRequestV3Init.class);
			requestId = concentFetch.getRequestId();
			consentFetchPayload = objMapper.writeValueAsString(concentFetch);
			abdmCentralDbSave.processPayload(consentFetchPayload, 1, httpServletRequest);
			ResponseEntity<ResponseBean> auth = abham3Service.callingNdhmApiv3(concentFetch, v3consentFetchUrl,
					"CONSENT-FETCH-V3");
			return this.saveAndReturnResponse(auth, requestId, null, consentFetchPayload, 5, null, null,
					"CONSENT FETCH V3", null);
		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(null);
			bean.setMessage("ERROR IN CONSENT FETCH V3");
			LOGGER.info("ERROR IN CONSENT FETCH V3");
			return new ResponseEntity<ResponseBean>(bean, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}