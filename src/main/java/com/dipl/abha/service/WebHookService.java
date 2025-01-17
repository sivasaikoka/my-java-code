package com.dipl.abha.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.dto.AbhaQueryTable;
import com.dipl.abha.dto.AbhaRegistrationDTO;
import com.dipl.abha.dto.Error;
import com.dipl.abha.dto.NotifyResultSetDto;
import com.dipl.abha.dto.Resp;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.dto.TenanIdAndIsIntegratedModule;
import com.dipl.abha.entities.HIPCallback;
import com.dipl.abha.entities.HIPNotifyLog;
import com.dipl.abha.entities.HIPRequest;
import com.dipl.abha.m2.afterpushnotifyhippayloads.Notification;
import com.dipl.abha.m2.afterpushnotifyhippayloads.Notifier;
import com.dipl.abha.m2.afterpushnotifyhippayloads.NotifyHipAfterDataPush;
import com.dipl.abha.m2.afterpushnotifyhippayloads.StatusNotification;
import com.dipl.abha.m2.afterpushnotifyhippayloads.StatusResponse;
import com.dipl.abha.m2.authonnotifydirectmodepayload.DirectAuthOnNotify;
import com.dipl.abha.m2.datapushpayload.DataPushEntry;
import com.dipl.abha.m2.datapushpayload.DataPushPayload;
import com.dipl.abha.m2.datapushpayload.DhPublicKey;
import com.dipl.abha.m2.datapushpayload.KeyMaterialPayload;
import com.dipl.abha.m2.discoverpayload.CareContext;
import com.dipl.abha.m2.fhirbundle.Content;
import com.dipl.abha.m2.fhirbundle.Entry;
import com.dipl.abha.m2.fhirbundle.FhirBundle;
import com.dipl.abha.m2.fhirbundle.Resource;
import com.dipl.abha.m2.linkoninit.Link;
import com.dipl.abha.m2.linkoninit.LinksOnInit;
import com.dipl.abha.m2.linkoninit.Meta;
import com.dipl.abha.m2.linksonconfirm.LinkOnConfirm;
import com.dipl.abha.m2.linksonconfirm.Patient;
import com.dipl.abha.m2.ondiscoverpayload.OnDiscoverPayload;
import com.dipl.abha.m2.ondiscoverpayload.PatientOnDiscover;
import com.dipl.abha.m2.onnofitypayload.Acknowledgement;
import com.dipl.abha.m2.onnofitypayload.OnNotifyPayload;
import com.dipl.abha.m2.onrequestypayload.HiRequest;
import com.dipl.abha.m2.onrequestypayload.OnRequestPayload;
import com.dipl.abha.m2.onshareprofilepayload.OnShareProfilePayload;
import com.dipl.abha.m2.onshareprofilepayload.ShareAcknowledgement;
import com.dipl.abha.m2.patientstatusonnotifypayload.Acknowledgment;
import com.dipl.abha.m2.patientstatusonnotifypayload.PatientStatuOnNotify;
import com.dipl.abha.m2.shareprofilepayload.ProfileSharePayload;
import com.dipl.abha.patient.dto.GarAddress;
import com.dipl.abha.patient.dto.Guarator;
import com.dipl.abha.patient.dto.PatientAddress;
import com.dipl.abha.patient.dto.PatientDemographics;
import com.dipl.abha.patient.dto.SecGarAddress;
import com.dipl.abha.patient.dto.SecGuarator;
import com.dipl.abha.patient.dto.SecPatAddress;
import com.dipl.abha.patient.entities.Center;
import com.dipl.abha.patient.entities.PatientDemography;
import com.dipl.abha.repositories.CenterRepository;
import com.dipl.abha.repositories.HIPCallbackRepository;
import com.dipl.abha.repositories.HIPNotifyLogRepository;
import com.dipl.abha.repositories.HIPRequestRepository;
import com.dipl.abha.repositories.PatientAddressRepository;
import com.dipl.abha.repositories.PatientRegistrationRepository;
import com.dipl.abha.util.ConstantUtil;
import com.dipl.abha.util.JdbcTemplateHelper;
import com.dipl.abha.v3.dto.CareContextOnDiscoverV3;
import com.dipl.abha.v3.dto.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WebHookService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebHookService.class);

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private CenterRepository centerRepository;

	@Value("${FHIR_FILE_PATH}")
	private String fhirFilePath;

	@Value("${VMED_GET_DATA_DISCOVERY}")
	private String vmedGetDiscoveryData;

	@Value("${VMED_CARE_CONTEXT_BY_INTERACTION_IDS}")
	private String vmedCareContextByInteractionIdsUrl;

	@Value("${VMED_SAVE_CARE_CONTEXST_LOGS}")
	private String vmedAddCareContextLogsUrl;

	@Value("${VMED_UPDATE_INTERACTION_STATUS}")
	private String vmedUpdateInteractionStatus;

	@Autowired
	private HIPRequestRepository hipRequestRepository;

	@Autowired
	private HIPCallbackRepository hipCallbackRepository;

	@Autowired
	private HIPNotifyLogRepository hipNotifyLogRepository;

	@Autowired
	private PatientRegistrationRepository patientRegRepository;

	@Autowired
	private PatientAddressRepository patientAddressRepo;

	@Autowired
	private ABHAM2Service abhaM2Service;

	@Autowired
	private JdbcTemplateHelper jdbcTemplateHelper;

	@Autowired
	private ExtractResultSetService extractResultSetService;

//	@Autowired
//	private NDHMM2CallBackController ndhmM2Controller;

	@Value("${HIMS_TENANT_IDS}")
	private Set<Integer> himsTenantIds;

	public OnDiscoverPayload constructOnDiscoverPayload(String requestId, String transactionId,
			List<CareContext> careContexts, String patienReference, String patientDisplay, Error error,
			List<String> matchedBy) {
		OnDiscoverPayload onDiscover = new OnDiscoverPayload();
		onDiscover.setRequestId(UUID.randomUUID().toString());
		onDiscover.setTimeStamp(LocalDateTime.now(ZoneOffset.UTC));
		onDiscover.setTransactionId(transactionId);
		PatientOnDiscover patientDis = new PatientOnDiscover();
		Resp resp = new Resp();
		resp.setRequestId(requestId);
		onDiscover.setResp(resp);
		patientDis.setMatchedBy(matchedBy);
		patientDis.setCareContexts(careContexts);
		patientDis.setDisplay(patientDisplay);
		patientDis.setReferenceNumber(patienReference);
		onDiscover.setPatient(patientDis);
		onDiscover.setError(error);
		return onDiscover;
	}

	public CareContextOnDiscoverV3 constructOnDiscoverPayloadV3(String requestId, String transactionId,
			List<CareContext> careContexts, String patienReference, String patientDisplay, Error error,
			List<String> matchedBy) {
		CareContextOnDiscoverV3 onDiscover = new CareContextOnDiscoverV3();
		onDiscover.setRequestId(UUID.randomUUID().toString());
		onDiscover.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).toString());
		onDiscover.setTransactionId(transactionId);
		com.dipl.abha.v3.dto.Patient patientDis = new com.dipl.abha.v3.dto.Patient();
		Response resp = new Response();
		resp.setRequestId(requestId);
		onDiscover.setResponse(resp);
		if (patienReference != null && patientDisplay != null) {
			onDiscover.setMatchedBy(matchedBy);
			patientDis.setHiType("Prescription");
			patientDis.setCount(careContexts.size());
			patientDis.setCareContexts(careContexts);
			patientDis.setDisplay(patientDisplay);
			patientDis.setReferenceNumber(patienReference);
			onDiscover.setPatient(Arrays.asList(patientDis));
		}
		onDiscover.setError(error);
		return onDiscover;
	}

	public CareContextOnDiscoverV3 findCareContexts(Optional<AbhaRegistrationDTO> patientId, String requestId,
			String transactionId, Error error, List<String> matchedBy, String patientRef, String patientDisplay,
			String abhaNumber) {
		CareContextOnDiscoverV3 onDiscover = new CareContextOnDiscoverV3();
		try {
			if (error != null) {
				onDiscover = this.constructOnDiscoverPayloadV3(requestId, transactionId, null, null, null, error,
						matchedBy);
				return onDiscover;
			}
			List<NotifyResultSetDto> careContext = new ArrayList<>();
			List<AbhaQueryTable> notifyTenantQueries = jdbcTemplateHelper.getResults(
					"select * from public.abha_query_table where tenant_id = " + TenantContext.getCurrentTenant()
							+ " and query_type in ('GET-DISCOVER-RECORDS','VMED_GET_DATA_DISCOVERY')",
					AbhaQueryTable.class);

			Object tenantUrlIsIntegratedModule = jdbcTemplateHelper.getTenantUrlIsIntegratedModule(
					"select is_integrated_module, integrated_tenant_id  from orgnization_registration or2 \r\n"
							+ "			inner join integrated_tenant_mapping on tenant_id = or2.id where or2.id = '"
							+ TenantContext.getCurrentTenant() + "'");
			AbhaRegistrationDTO abhaRegistration = null;
			if (!notifyTenantQueries.isEmpty()) {
				if (tenantUrlIsIntegratedModule != null) {
					TenanIdAndIsIntegratedModule tenantIdofIntegratedModule = (TenanIdAndIsIntegratedModule) tenantUrlIsIntegratedModule;
					if (tenantIdofIntegratedModule != null && tenantIdofIntegratedModule.getIs_integrated_module()) {
						LOGGER.info("This Tenant is Integrated Module " + TenantContext.getCurrentTenant()
								+ " In Integrateion With " + tenantIdofIntegratedModule.getIntegrated_tenant_id());
						Object abhaRegistrationObj = jdbcTemplateHelper
								.getAbhaRegistration("select * from public.abha_registration where tenant_id ='"
										+ tenantIdofIntegratedModule.getIntegrated_tenant_id() + "' and abha_no = '"
										+ abhaNumber + "' limit 1");
						if (abhaRegistrationObj != null) {
							abhaRegistration = (AbhaRegistrationDTO) abhaRegistrationObj;
						}
					} else {
						LOGGER.info("This Tenant Is Not Integrated Module " + TenantContext.getCurrentTenant());
					}
				}
				if (patientId.isPresent()) {
					for (AbhaQueryTable s : notifyTenantQueries) {
						if ("GET-DISCOVER-RECORDS".equals(s.getQueryType())) {
							careContext.addAll(extractResultSetService
									.executeDynamicQueryFromDBForNotify(this.returnFinalQueryForDirectAuth(s.getQuery(),
											"'" + patientId.get().getPatient_id() + "'")));

						}
						if ("VMED_GET_DATA_DISCOVERY".equals(s.getQueryType())) {
							if (abhaRegistration != null) {
								ResponseBean resp = new ResponseBean();
								Map<String, Long> map = new HashMap<String, Long>();
								map.put("patientId", abhaRegistration.getPatient_id());
								resp = abhaM2Service.vmedRestCall(objMapper.writeValueAsString(map), s.getQuery(),
										"NOTIFY");
								if (resp != null && resp.getMessage() != null && !resp.getMessage().isEmpty()
										&& "success".equals(resp.getMessage())) {
									TypeReference<List<NotifyResultSetDto>> listType = new TypeReference<List<NotifyResultSetDto>>() {
									};
									careContext.addAll(objMapper.readValue(objMapper.writeValueAsString(resp.getData()),
											listType));
								}
							}
						}
					}
					List<CareContext> careList = new ArrayList<>();
					if (!careContext.isEmpty()) {
						int count = 0;
						for (NotifyResultSetDto s : careContext) {
							LOGGER.info("Care Context Count : {}", count);
							if (count > 20) {
								LOGGER.info("Care Context Count is greater than 20 : {}", count);
								break;
							} else {
								CareContext care = new CareContext();
								care.setReferenceNumber(s.getCareContextRef());
								care.setDisplay(s.getCareContextDisplay());
								careList.add(care);
							}
							count++;
						}
						onDiscover = this.constructOnDiscoverPayloadV3(requestId, transactionId, careList, patientRef,
								patientDisplay, null, matchedBy);
					} else {
						error = new Error(1037, "Care Context Not Found");
						
						onDiscover = this.constructOnDiscoverPayloadV3(requestId, transactionId, careList, null, null,
								error, matchedBy);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return onDiscover;
	}

	public OnNotifyPayload constructOnNotifyPayload(String concentId, String requestId, String status, Error error) {
		OnNotifyPayload onNotify = new OnNotifyPayload();
		Acknowledgement acknowledgement = new Acknowledgement();
		Resp resp = new Resp();
		resp.setRequestId(requestId);
		acknowledgement.setStatus(status);
		acknowledgement.setConsentId(concentId);
		onNotify.setAcknowledgement(acknowledgement);
		onNotify.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
		onNotify.setRequestId(UUID.randomUUID().toString());
		onNotify.setResp(resp);
		onNotify.setError(error);
		onNotify.setResponse(resp);
		return onNotify;
	}

	public OnRequestPayload constructOnRequestPayload(String requestId, String transactionId, Error error) {
		HiRequest hiRequest = new HiRequest();
		hiRequest.setTransactionId(transactionId);
		hiRequest.setSessionStatus("ACKNOWLEDGED");
		Resp resp = new Resp(); 
		resp.setRequestId(requestId);
		OnRequestPayload onRequestPayload = new OnRequestPayload();
		onRequestPayload.setRequestId(UUID.randomUUID().toString());
		onRequestPayload.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
		onRequestPayload.setHiRequest(hiRequest);
		onRequestPayload.setResp(resp);
		onRequestPayload.setResponse(resp);
		onRequestPayload.setError(error);
		return onRequestPayload;
	}

	public String buildFhirBundle(String docName, String patientName, String encodedData, String visitDate,
			String fileType, String interactionId, String docRef, String fileTypeNew) {
		FhirBundle fhir = new FhirBundle();
		String concent = "";
		String fhirString = "";
		String refNumber = UUID.randomUUID().toString();
		String documentRef = "DocumentReference/" + interactionId;
		try {
			ObjectMapper objMapper = new ObjectMapper();
			String date = visitDate.replace(" ", "T") + "+05:30";
			Path path = Paths.get(fhirFilePath + "/fhirbundle.txt");
			byte[] data = Files.readAllBytes(path);
			concent = new String(data);
			fhir = objMapper.readValue(concent, FhirBundle.class);
			fhir.setResourceType("Bundle");
			fhir.setId(interactionId);
			fhir.getIdentifier().setValue(interactionId);
			List<Entry> entry = fhir.getEntry();
			Entry entry0 = entry.get(0);
			entry0.getResource().setDate(date);
			entry0.getResource().setTitle(fileType + " " + interactionId);
			entry0.getResource().getSection().get(0).getEntry().get(0).setReference(documentRef);
			entry0.getResource().getSection().get(0).setTitle(fileType);
//			if (fileType.contains("DIAGNOSTIC REPORTS")) {
//				entry0.getResource().getSection().remove(1);
//				entry0.getResource().getSection().remove(2);
//				entry0.getResource().getSection().remove(3);
//				entry0.getResource().getSection().remove(4);
//				entry0.getResource().getSection().remove(5);
//				entry0.getResource().getSection().remove(6);
//				Set<String> collect = entry0.getResource().getSection().stream().map(p -> p.getTitle())
//						.collect(Collectors.toSet());
//				if()
//				
//			}
			Entry entry1 = entry.get(1);
			fhir.getMeta().setLastUpdated(date);
			entry1.getResource().setDate(date);
			fhir.setTimestamp(date);
			Resource resource = entry1.getResource();
			List<com.dipl.abha.m2.fhirbundle.Name> name = resource.getName();
			Entry entry2 = entry.get(2);
			name.get(0).setText(docName);
			Resource resource2 = entry2.getResource();
			List<com.dipl.abha.m2.fhirbundle.Name> name2 = resource2.getName();
			name2.get(0).setText(patientName);
			Entry entry3 = entry.get(3);
			entry3.getResource().getPeriod().setStart(date);
			Entry entry4 = entry.get(4);
			entry4.setFullUrl(documentRef);
			Resource resource4 = entry4.getResource();
			resource4.setId(documentRef);
			resource4.getType().getCoding().get(0).setDisplay(fileTypeNew);
			resource4.getContent().get(0).getAttachment().setTitle(fileTypeNew);
			List<Content> content4 = resource4.getContent();
//			content4.get(0).getAttachment().setUrl(encodedData);
			content4.get(0).getAttachment().setData(encodedData);
			Entry entry5 = entry.get(5);
			entry5.getResource().setPerformedDateTime(date);
			fhirString = objMapper.writeValueAsString(fhir);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fhirString;
	}

//	public static void main(String[] args) {
//		FhirBundle fhir = new FhirBundle();
//		String concent = "";
//		String fhirString = "";
//		String refNumber = UUID.randomUUID().toString();
//		String documentRef = "DocumentReference/" + 123;
//		String fileType = "DIAGNOSTIC REPORTS";
//		try {
//			ObjectMapper objMapper = new ObjectMapper();
//			String date = LocalDateTime.now().toString();
//			Path path = Paths.get("src/main/resources/fhirbundle.txt");
//			byte[] data = Files.readAllBytes(path);
//			concent = new String(data);
//			fhir = objMapper.readValue(concent, FhirBundle.class);
//			fhir.setResourceType("Bundle");
//			fhir.setId("4345434");
//			fhir.getIdentifier().setValue("345");
//			List<Entry> entry = fhir.getEntry();
//			Entry entry0 = entry.get(0);
//			entry0.getResource().setDate(date);
//			entry0.getResource().setTitle(fileType + " ");
//			entry0.getResource().getSection().get(0).getEntry().get(0).setReference(documentRef);
//			entry0.getResource().getSection().get(0).setTitle(fileType);
//
////			if (fileType.contains("DIAGNOSTIC REPORTS")) {
////				System.out.println(entry0.getResource().getSection().size());
////				entry0.getResource().getSection().remove(1);
////				entry0.getResource().getSection().remove(2);
////				entry0.getResource().getSection().remove(3);
////				entry0.getResource().getSection().remove(4);
////				
////				entry0.getResource().getSection().get(0).getTitle()
////			
////
////			}
//			Entry entry1 = entry.get(1);
//			fhir.getMeta().setLastUpdated(date);
//			entry1.getResource().setDate(date);
//			fhir.setTimestamp(date);
//			Resource resource = entry1.getResource();
//			List<com.dipl.abha.m2.fhirbundle.Name> name = resource.getName();
//			Entry entry2 = entry.get(2);
//			name.get(0).setText("");
//			Resource resource2 = entry2.getResource();
//			List<com.dipl.abha.m2.fhirbundle.Name> name2 = resource2.getName();
//			name2.get(0).setText("");
//			Entry entry3 = entry.get(3);
//			entry3.getResource().getPeriod().setStart(date);
//			Entry entry4 = entry.get(4);
//			entry4.setFullUrl(documentRef);
//			Resource resource4 = entry4.getResource();
//			resource4.setId(documentRef);
//			resource4.getType().getCoding().get(0).setDisplay("");
//			resource4.getContent().get(0).getAttachment().setTitle("");
//			List<Content> content4 = resource4.getContent();
////			content4.get(0).getAttachment().setUrl(encodedData);
//			content4.get(0).getAttachment().setData("");
//			Entry entry5 = entry.get(5);
//			entry5.getResource().setPerformedDateTime(date);
//			fhirString = objMapper.writeValueAsString(fhir);
//			System.out.println(fhirString);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public DataPushPayload constructDataPushPayload(String transactionId, String careContextRef, String data,
			String publicKey, String nonce, int pageNo, int pagecount, String dataCheckSum, LocalDateTime dataEraseAt) {
		DataPushPayload dataPushPayload = new DataPushPayload();
		dataPushPayload.setPageNumber(pageNo);
		dataPushPayload.setPageCount(1);
		dataPushPayload.setTransactionId(transactionId);
		DataPushEntry entry = new DataPushEntry();
		entry.setCareContextReference(careContextRef);
		entry.setChecksum(dataCheckSum);
		entry.setContent(data);
		entry.setMedia("application/fhir+json");
		List<DataPushEntry> entryList = new ArrayList<>();
		entryList.add(entry);
		dataPushPayload.setEntries(entryList);
		KeyMaterialPayload keyMaterial = new KeyMaterialPayload();
		keyMaterial.setCryptoAlg("ECDH");
		keyMaterial.setCurve("Curve25519");
		DhPublicKey dhPublicKey = new DhPublicKey();
		dhPublicKey.setExpiry(dataEraseAt);
		dhPublicKey.setParameters("Curve25519/32byte random key");
		dhPublicKey.setKeyValue(publicKey);
		keyMaterial.setDhPublicKey(dhPublicKey);
		keyMaterial.setNonce(nonce);
		dataPushPayload.setKeyMaterial(keyMaterial);
		return dataPushPayload;
	}

	public String convertUrlDocToBase64(String prescirptionPath) {
		String eStr = "";
		try {
			URL url = new URL(prescirptionPath);
			String fileName = prescirptionPath.substring(prescirptionPath.lastIndexOf("/") + 1,
					prescirptionPath.length());
			String fileNameNoExt = fileName.replace(".pdf", "");
			System.out.println("TRIMMED FILE            " + fileNameNoExt);
			File tempFile = File.createTempFile(fileNameNoExt, ".pdf");
			LOGGER.info("=========file Name========");
			LOGGER.info(fileName);
			LOGGER.info("=================");
			FileUtils.copyURLToFile(url, tempFile);
			byte[] array = Files.readAllBytes(Paths.get(tempFile.getAbsolutePath()));
			System.out.println("=========" + tempFile.getAbsolutePath());
			eStr = new String(org.bouncycastle.util.encoders.Base64.encode(array));
			tempFile.delete();
			LOGGER.info("========================CONVERTING BASE64 DONE============");
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			LOGGER.error(fnfe.getLocalizedMessage());
			fnfe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return eStr;
	}

	public byte[] convertUrlDocToBytes(String prescirptionPath) {
		String eStr = "";
		byte[] array = null;
		try {
			URL url = new URL(prescirptionPath);
			String fileName = prescirptionPath.substring(prescirptionPath.lastIndexOf("/") + 1,
					prescirptionPath.length());
			String fileNameNoExt = fileName.replace(".pdf", "");
			System.out.println("TRIMMED FILE            " + fileNameNoExt);
			File tempFile = File.createTempFile(fileNameNoExt, ".pdf");
			LOGGER.info("=========file Name========");
			LOGGER.info(fileName);
			LOGGER.info("=================");
			FileUtils.copyURLToFile(url, tempFile);
			array = Files.readAllBytes(Paths.get(tempFile.getAbsolutePath()));
			System.out.println("=========" + tempFile.getAbsolutePath());
			eStr = new String(org.bouncycastle.util.encoders.Base64.encode(array));
			tempFile.delete();
			LOGGER.info("========================CONVERTING BASE64 DONE============");
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			LOGGER.error(fnfe.getLocalizedMessage());
			fnfe.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return array;
	}

	public LinksOnInit constructLinksOnInit(String requestId, String transaxtionId, String referenceNumber,
			Error error) {
		LinksOnInit linksOnInit = new LinksOnInit();
		Resp resp = new Resp();
		resp.setRequestId(requestId);
		linksOnInit.setRequestId(UUID.randomUUID().toString());
		linksOnInit.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
		linksOnInit.setTransactionId(transaxtionId);
		linksOnInit.setResp(resp);
		linksOnInit.setResponse(resp);
		Link link = new Link();
		link.setReferenceNumber(referenceNumber);
		link.setAuthenticationType("DIRECT");
		Meta meta = new Meta();
		meta.setCommunicationExpiry(ConstantUtil.indianTimeStampWith5mins());
		meta.setCommunicationHint("OTP");
		meta.setCommunicationMedium("MOBILE");
		link.setMeta(meta);
		linksOnInit.setLink(link);
		linksOnInit.setError(error);
		return linksOnInit;
	}

	public LinkOnConfirm constructLinksOnConfirmPayload(String requestId, String patienRef, Error error,
			Set<String> careContext) throws JsonProcessingException, Exception {
		LinkOnConfirm linkOnConfirm = new LinkOnConfirm();
		List<com.dipl.abha.m2.linksonconfirm.CareContext> careContexts = new ArrayList<>();
		Resp resp = new Resp();
		resp.setRequestId(requestId);
		linkOnConfirm.setResponse(resp);
		linkOnConfirm.setRequestId(UUID.randomUUID().toString());
		linkOnConfirm.setResp(resp);
		linkOnConfirm.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
		HIPRequest hipRequest = hipRequestRepository.findOnDiscover(patienRef);
		if (hipRequest != null) {
			CareContextOnDiscoverV3 onDiscoverPayload = objMapper.readValue(hipRequest.getRequestJson().toString(),
					CareContextOnDiscoverV3.class);
			if (onDiscoverPayload != null && onDiscoverPayload.getPatient() != null
					&& onDiscoverPayload.getPatient().get(0).getCareContexts() != null
					&& !onDiscoverPayload.getPatient().get(0).getCareContexts().isEmpty()) {
				onDiscoverPayload.getPatient().get(0).getCareContexts().forEach(s -> {
					if (careContext.stream()
							.filter(search -> (search != null && search.compareTo(s.getReferenceNumber()) == 0))
							.findAny().isPresent()) {
						com.dipl.abha.m2.linksonconfirm.CareContext previousCareContext = new com.dipl.abha.m2.linksonconfirm.CareContext();
						previousCareContext.setDisplay(s.getDisplay());
						previousCareContext.setReferenceNumber(s.getReferenceNumber());
						careContexts.add(previousCareContext);
					}
				});
				Patient patient = new Patient();
				patient.setHiType("Prescription");
				patient.setCount(careContexts.size());
				patient.setCareContexts(careContexts);
				patient.setDisplay(onDiscoverPayload.getPatient().get(0).getDisplay());
				patient.setReferenceNumber(onDiscoverPayload.getPatient().get(0).getReferenceNumber());
				linkOnConfirm.setPatient(Arrays.asList(patient));
				linkOnConfirm.setError(error);
			}
		}

		LOGGER.debug("DISCOVER ON CONFIRM PAYLOAD {}===>", objMapper.writeValueAsString(linkOnConfirm));
		return linkOnConfirm;
	}

	public DirectAuthOnNotify constructDirectAuthOnNotify(String requestId, Error error) {
		DirectAuthOnNotify directAuthOnNofity = new DirectAuthOnNotify();
		Resp resp = new Resp();
		com.dipl.abha.m2.authonnotifydirectmodepayload.Acknowledgement ack = new com.dipl.abha.m2.authonnotifydirectmodepayload.Acknowledgement();
		ack.setStatus("OK");
		resp.setRequestId(requestId);
		directAuthOnNofity.setRequestId(UUID.randomUUID().toString());
		directAuthOnNofity.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
		directAuthOnNofity.setAcknowledgement(ack);
		directAuthOnNofity.setResp(resp);
		directAuthOnNofity.setError(error);
		return directAuthOnNofity;
	}

	public NotifyHipAfterDataPush constructNotifyHipAfterPushPayload(String concentId, String transactionId,
			List<String> careContextreference, String hipId,String hiuId, String notifierType, String sessionStatus,
			String description) throws JsonProcessingException {
		List<StatusResponse> statusResponseList = new ArrayList<>();
		NotifyHipAfterDataPush notifyHip = new NotifyHipAfterDataPush();
		notifyHip.setRequestId(UUID.randomUUID().toString());
		notifyHip.setTimestamp(LocalDateTime.now(ZoneOffset.UTC).toString());
		Notification notification = new Notification();
		notification.setConsentId(concentId);
		notification.setDoneAt(ConstantUtil.utcTimeStamp());
		notification.setTransactionId(transactionId);
		notifyHip.setNotification(notification);
		Notifier notifier = new Notifier();
		notifier.setId(hipId);
		notifier.setType(notifierType);
		notification.setNotifier(notifier);
		careContextreference.forEach(s -> {
			StatusResponse statusResponse = new StatusResponse();
			statusResponse.setCareContextReference(s);
			statusResponse.setDescription(description);
			statusResponse.setHiStatus("OK");
			statusResponseList.add(statusResponse);
		});
		notification.setNotifier(notifier);
		StatusNotification statusNotification = new StatusNotification();
		statusNotification.setHipId(hiuId);
		statusNotification.setSessionStatus(sessionStatus);
		statusNotification.setStatusResponses(statusResponseList);
		notification.setStatusNotification(statusNotification);
		return notifyHip;
	}

	public OnShareProfilePayload constructOnShareProfilePayload(ProfileSharePayload profileSharePayload,
			String tokenNumber, Error error) {
		OnShareProfilePayload onShareProfile = new OnShareProfilePayload();
		ShareAcknowledgement ack = new ShareAcknowledgement();
		ack.setStatus("SUCCESS");
		ack.setTokenNumber(tokenNumber);
		ack.setHealthId(profileSharePayload.getProfile().getPatient().getHealthId());
		Resp resp = new Resp();
		resp.setRequestId(profileSharePayload.getRequestId());
		onShareProfile.setAcknowledgement(ack);
		onShareProfile.setRequestId(UUID.randomUUID().toString());
		onShareProfile.setResp(resp);
		onShareProfile.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
		onShareProfile.setError(error);
		return onShareProfile;
	}

	public PatientStatuOnNotify constructPatientStatusOnNotifyPayload(String requestId, Error error) {
		PatientStatuOnNotify patientOnNotify = new PatientStatuOnNotify();
		patientOnNotify.setRequestId(UUID.randomUUID().toString());
		patientOnNotify.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
		Resp resp = new Resp();
		resp.setRequestId(requestId);
		if (error != null) {
			patientOnNotify.setError(error);
		} else {
			Acknowledgment ack = new Acknowledgment();
			ack.setStatus("OK");
			patientOnNotify.setAcknowledgment(ack);
		}
		patientOnNotify.setResp(resp);
		return patientOnNotify;

	}

	public ResponseEntity<ResponseBean> saveAndReturnResponse(String payload, String url, String apiType,
			String requestId, String txnId, int hipRequestTypeId, String abhano, String consentArtefactCode,
			String interactionId, String authToken) {
		HIPRequest hipRequest = null;
		ResponseBean bean = null;
		try {
			ResponseEntity<ResponseBean> auth = abhaM2Service.restCallApi(payload, url, apiType, requestId,
					consentArtefactCode, authToken);
			if (auth.getStatusCode().compareTo(HttpStatus.ACCEPTED) == 0
					|| auth.getStatusCode().compareTo(HttpStatus.OK) == 0) {
				if ("DATA-PUSH".equals(apiType)) {
					hipRequest = this.constructHipRequestInsert(requestId, hipRequestTypeId, payload, abhano,
							consentArtefactCode, txnId, interactionId, null);
					bean = new ResponseBean(HttpStatus.OK, apiType + " SUCCESSFUL AND SAVED IN DB", hipRequest, null);
				} else {
					hipRequest = this.insertIntoHipRequest(requestId, hipRequestTypeId, payload, abhano,
							consentArtefactCode, txnId, interactionId, null);
					bean = new ResponseBean(HttpStatus.OK, apiType + " SUCCESSFUL AND SAVED IN DB", hipRequest, null);
				}
			} else {
				if ("DATA-PUSH".equals(apiType)) {
					hipRequest = this.constructHipRequestInsert(requestId, hipRequestTypeId, payload, abhano,
							consentArtefactCode, txnId, interactionId, auth.getBody().getMessage());
					bean = new ResponseBean(HttpStatus.OK,
							apiType + " CALLED SUCCESSFULL AND DID NOT SEND SUCCESS CODE", hipRequest, null);
				} else {
					hipRequest = this.insertIntoHipRequest(requestId, hipRequestTypeId, payload, abhano,
							consentArtefactCode, txnId, interactionId, auth.getBody().getMessage());
					bean = new ResponseBean(HttpStatus.OK,
							apiType + " CALLED SUCCESSFULL AND DID NOT SEND SUCCESS CODE", hipRequest, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR IN " + apiType, e.getMessage(), null);
			LOGGER.info("ERROR IN " + apiType);

		}
		return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
	}

	public HIPRequest insertIntoHipRequest(String requestId, Integer hipRequestTypeId, String requestPayload,
			String abhano, String consentArtefactCode, String txnId, String interactionId, String errorResponseJson)
			throws JsonMappingException, JsonProcessingException {
		JsonNode json = null;
		HIPRequest savedHipRequest = null;
		try {
			HIPRequest hipRequest = new HIPRequest();
			hipRequest.setRequestCode(requestId);
			hipRequest.setTxnCode(txnId);
			if (requestPayload != null && !requestPayload.isEmpty()) {
				json = objMapper.readTree(requestPayload);
			}
			hipRequest.setRequestJson(json);
			hipRequest.setHipRequestTypeId(hipRequestTypeId);
			hipRequest.setAbhaNo(abhano);
			hipRequest.setConsentArtefactCode(consentArtefactCode);
			hipRequest.setInteractionId(interactionId);
			hipRequest.setIsActive(true);
			hipRequest.setIsDeleted(false);
			hipRequest.setErrorResponseJson(errorResponseJson);
			hipRequest.setCreatedOn(LocalDateTime.now());
			savedHipRequest = hipRequestRepository.save(hipRequest);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("ERROR in HIP REQUEST SAVE");
		}
		return savedHipRequest;
	}

	public HIPRequest constructHipRequestInsert(String requestId, Integer hipRequestTypeId, String requestPayload,
			String abhano, String consentArtefactCode, String txnId, String interactionId, String errorResponseJson)
			throws Exception {
		JsonNode json = null;
		HIPRequest hipRequest = null;
		try {
			hipRequest = new HIPRequest();
			hipRequest.setRequestCode(requestId);
			hipRequest.setTxnCode(txnId);
			if (requestPayload != null && !requestPayload.isEmpty()) {
				try {
					json = objMapper.readTree(requestPayload);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			hipRequest.setRequestJson(json);
			hipRequest.setHipRequestTypeId(hipRequestTypeId);
			hipRequest.setAbhaNo(abhano);
			hipRequest.setConsentArtefactCode(consentArtefactCode);
			hipRequest.setInteractionId(interactionId);
			hipRequest.setIsActive(true);
			hipRequest.setIsDeleted(false);
			hipRequest.setErrorResponseJson(errorResponseJson);
			hipRequest.setCreatedOn(LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("ERROR in HIP REQUEST SAVE");
		}
		return hipRequest;
	}

	public HIPCallback insertIntoHipCallBack(String requestId, String txnId, String response, String consentRequestCode,
			String patientRefNo, Integer requestTypeId, String abhano, String errorMessage)
			throws JsonMappingException, JsonProcessingException {
		HIPCallback savedHipResponse = null;
		JsonNode json = null;
		try {
			HIPCallback hipResponse = new HIPCallback();
			hipResponse.setRequestCode(requestId);
			hipResponse.setTxnCode(txnId);
			if (response != null && !response.isEmpty()) {
				json = objMapper.readTree(response);
			}
			hipResponse.setResponseJson(json);
			hipResponse.setConsentRequestCode(consentRequestCode);
			hipResponse.setPatientRefNo(patientRefNo);
			hipResponse.setHipRequestTypeId(requestTypeId);
			hipResponse.setAbhaNo(abhano);
			hipResponse.setErrorMessage(errorMessage);
			hipResponse.setIsActive(true);
			hipResponse.setIsDeleted(false);
			hipResponse.setCreatedOn(LocalDateTime.now());
			savedHipResponse = hipCallbackRepository.save(hipResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savedHipResponse;
	}

	public HIPNotifyLog insertIntoHipNotifyLogs(String requestId, String status, String txnId, String consentId,
			String healthId) {
		HIPNotifyLog saveHipNotifyLog = null;
		try {
			HIPNotifyLog hipNotifyLog = new HIPNotifyLog();
			hipNotifyLog.setRequestCode(requestId);
			hipNotifyLog.setStatus(status);
			hipNotifyLog.setTxnCode(txnId);
			hipNotifyLog.setHipRequestTypeId(17);
			hipNotifyLog.setConsentRequestCode(consentId);
			if (!"GRANTED".equals(status)) {
				HIPNotifyLog healthIdByConsentId = hipNotifyLogRepository.getHealthIdByConsentId(consentId);
				if (healthIdByConsentId != null) {
					hipNotifyLog.setAbhaNo(healthIdByConsentId.getAbhaNo());
				}
			} else {
				hipNotifyLog.setAbhaNo(healthId);
			}

			saveHipNotifyLog = hipNotifyLogRepository.save(hipNotifyLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveHipNotifyLog;
	}

	public List<String> searchByManyMatches(AbhaRegistrationDTO benRegistration, String mobileNo, char gender,
			String yearOfBirthString) {
		List<String> matchedBy = new ArrayList<String>();
		if (benRegistration != null) {
			if (!mobileNo.isEmpty() && benRegistration.getMobile_no() != null
					&& !benRegistration.getMobile_no().isEmpty()) {
				if (benRegistration.getMobile_no().equals(mobileNo)) {
					matchedBy.add("MOBILE");
				}
			}
			if (benRegistration.getYear_of_birth() != null) {
				Long lesstwoYears = benRegistration.getYear_of_birth() - 2;
				Long moretwoyears = benRegistration.getYear_of_birth() + 2;
				if ((Integer.parseInt(yearOfBirthString) <= moretwoyears
						&& Integer.parseInt(yearOfBirthString) >= lesstwoYears)) {
					matchedBy.add("YEAR OF BIRTH");
				}
			}
			String genderString = gender + "";
			if (benRegistration.getGender().equals(genderString)) {
				matchedBy.add("GENDER");
			}
		}

		return matchedBy;
	}

	public String returnDateInFormat(LocalDateTime timeToModify) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDateTime = timeToModify.format(dateTimeFormatter);
		return formattedDateTime;
	}

	public OnShareProfilePayload savePatient(ProfileSharePayload profileSharePayload,
			HttpServletRequest httpServletRequest) {
		String mobilNo = "";
		String healthNumber = "";
		String healthId = "";
		String email = "";
		OnShareProfilePayload onShareProfilePayload = new OnShareProfilePayload();
		Resp resp = new Resp();
		try {
			resp.setRequestId(profileSharePayload.getRequestId());
			onShareProfilePayload.setResp(resp);
			onShareProfilePayload.setRequestId(UUID.randomUUID().toString());
			onShareProfilePayload.setTimestamp(LocalDateTime.now(ZoneOffset.UTC));
			ShareAcknowledgement ack = new ShareAcknowledgement();
			ack.setStatus("SUCCESS");
			ack.setTokenNumber("" + generateRandomNumber());
			ack.setHealthId(profileSharePayload.getProfile().getPatient().getHealthId());
			onShareProfilePayload.setAcknowledgement(ack);
			com.dipl.abha.m2.shareprofilepayload.Patient patient = profileSharePayload.getProfile().getPatient();
			if (patient.getIdentifiers().size() > 0) {
				for (int i = 0; i < patient.getIdentifiers().size(); i++) {
					if (patient.getIdentifiers().get(i).getType().equals("MOBILE")) {
						mobilNo = patient.getIdentifiers().get(i).getValue();
					}
					if (patient.getIdentifiers().get(i).getType().equals("NDHM_HEALTH_NUMBER")) {
						healthNumber = patient.getIdentifiers().get(i).getValue();
					}
					if (patient.getIdentifiers().get(i).getType().equals("HEALTH_ID")) {
						healthId = patient.getIdentifiers().get(i).getValue();
					}
					if (patient.getIdentifiers().get(i).getType().equals("EMAIL")) {
						email = patient.getIdentifiers().get(i).getValue();
					}
				}
			}
			if (healthId.isEmpty()) {
				healthId = patient.getHealthId();
			}
			if (healthNumber.isEmpty()) {
				healthNumber = patient.getHealthIdNumber();
			}
			PatientDemography findByHealthId = null;

			if (!mobilNo.isEmpty() && !healthId.isEmpty()) {
				findByHealthId = patientRegRepository.findByHealthIdAndMobileNoReturnDemo(healthId, mobilNo);
			} else if (!healthId.isEmpty()) {
				findByHealthId = patientRegRepository.findByHealthIdNew(healthId);
			} else if (!healthNumber.isEmpty()) {
				findByHealthId = patientRegRepository.findByHealthNumberNew(healthNumber);
			}

			PatientDemography patientNew = new PatientDemography();
			com.dipl.abha.patient.entities.PatientAddress addresss = new com.dipl.abha.patient.entities.PatientAddress();
			PatientDemography saved = null;
			if (findByHealthId == null) {
				addresss.setMobile1(mobilNo);
				addresss.setEmail(email);
				addresss.setPrimaryAddress("N");
				addresss.setAddressTypeId(2);
				addresss.setStatus(1);
				addresss.setTransferFlag("N");
				addresss.setAddress1(patient.getAddress().getLine());
				patientNew.setAge(27);

				String date = patient.getDayOfBirth() < 10 ? "0" + patient.getDayOfBirth()
						: patient.getDayOfBirth() + "";
				String month = patient.getMonthOfBirth() < 10 ? "0" + patient.getMonthOfBirth()
						: patient.getMonthOfBirth() + "";
				String dob = patient.getYearOfBirth() + "-" + month + "-" + date;
				LocalDate dateOfBirth = LocalDate.parse(dob);

				patientNew.setDob(dateOfBirth);
				if (patient.getGender().equals("M")) {
					patientNew.setGenderId(1L);
				} else if (patient.getGender().equals("F")) {
					patientNew.setGenderId(2L);
				} else if (patient.getGender().equals("O")) {
					patientNew.setGenderId(3L);
				}
				patientNew.setNationalHealthId(healthId);
				patientNew.setNationalHealthNumber(healthNumber);
				patientNew.setIsAbhaLinked("1");
				patientNew.setId(0l);
				String[] names = patient.getName().split(" ");
				if (names.length >= 3) {
					patientNew.setPatientFirstName(names[0]);
					patientNew.setPatientMiddleName(names[1]);
					patientNew.setPatientLastName(names[2]);
				} else if (names.length == 2) {
					patientNew.setPatientFirstName(names[0]);
					patientNew.setPatientLastName(names[1]);
				} else if (names.length == 1) {
					patientNew.setPatientFirstName(patient.getName());
					patientNew.setPatientLastName("");
				}
				patientNew.setOrgId(99l);
				patientNew.setCenterId(99L);
				patientNew.setMrnNo(this.getMrnNumberNew(patientNew.getPatientFirstName()));
				patientNew.setAge(Period.between(dateOfBirth, LocalDate.now()).getYears());
				if (patient.getAddress().getPincode() != null) {
					addresss.setZip(patient.getAddress().getPincode());
				} else {
					addresss.setZip(null);
				}
				patientNew.setPatientAddress(addresss);
				saved = patientRegRepository.save(patientNew);
				addresss.setPatientId(saved.getId());
				addresss.setCenterId(99L);
				patientAddressRepo.save(addresss);
			} else {
				findByHealthId.setNationalHealthId(healthId);
				findByHealthId.setNationalHealthNumber(healthNumber);
				findByHealthId.setIsAbhaLinked("1");
				patientRegRepository.save(findByHealthId);
			}
			System.out.println(objMapper.writeValueAsString(onShareProfilePayload));
		} catch (Exception e) {
			onShareProfilePayload.setError(new Error(1001, "Internal Server Error"));
		}

		return onShareProfilePayload;
	}

	public PatientDemographics buildPatientDemogrphics(ProfileSharePayload profileSharePayload) {
		String mobilNo = "";
		String healthNumber = "";
		String healthId = "";
		String email = "";
		PatientDemography findByHealthId = null;
		String patientFirstName = "";
		String patientMiddleName = "";
		String patientLastName = "";
		int genderId = 0;
		String address = "";
		String pinCode = "";
		PatientDemographics patientDemographics = new PatientDemographics();
		com.dipl.abha.m2.shareprofilepayload.Patient patient = profileSharePayload.getProfile().getPatient();
		if (patient != null) {
			if (patient.getIdentifiers().size() > 0) {
				for (int i = 0; i < patient.getIdentifiers().size(); i++) {
					if (patient.getIdentifiers().get(i).getType().equals("MOBILE")) {
						mobilNo = patient.getIdentifiers().get(i).getValue();
					}
					if (patient.getIdentifiers().get(i).getType().equals("NDHM_HEALTH_NUMBER")) {
						healthNumber = patient.getIdentifiers().get(i).getValue();
					}
					if (patient.getIdentifiers().get(i).getType().equals("HEALTH_ID")) {
						healthId = patient.getIdentifiers().get(i).getValue();
					}
					if (patient.getIdentifiers().get(i).getType().equals("EMAIL")) {
						email = patient.getIdentifiers().get(i).getValue();
					}
				}
			}
			if (healthId.isEmpty()) {
				healthId = patient.getHealthId();
			}
			if (healthNumber.isEmpty()) {
				healthNumber = patient.getHealthIdNumber();
			}

			if (!mobilNo.isEmpty() && !healthId.isEmpty()) {
				findByHealthId = patientRegRepository.findByHealthIdAndMobileNoReturnDemo(healthId, mobilNo);
			} else if (!healthId.isEmpty()) {
				findByHealthId = patientRegRepository.findByHealthIdNew(healthId);
			} else if (!healthNumber.isEmpty()) {
				findByHealthId = patientRegRepository.findByHealthNumberNew(healthNumber);
			}

			String date = patient.getDayOfBirth() < 10 ? "0" + patient.getDayOfBirth() : patient.getDayOfBirth() + "";
			String month = patient.getMonthOfBirth() < 10 ? "0" + patient.getMonthOfBirth()
					: patient.getMonthOfBirth() + "";
			String dob = patient.getYearOfBirth() + "-" + month + "-" + date;
			LocalDate dateOfBirth = LocalDate.parse(dob);
			if (patient.getGender().equals("M")) {
				genderId = 1;
			} else if (patient.getGender().equals("F")) {
				genderId = 2;
			} else if (patient.getGender().equals("O")) {
				genderId = 3;
			}
			String[] names = patient.getName().split(" ");
			if (names.length >= 3) {
				patientFirstName = names[0];
				patientMiddleName = names[1];
				patientLastName = names[2];
			} else if (names.length == 2) {
				patientFirstName = names[0];
				patientMiddleName = names[1];
			} else if (names.length == 1) {
				patientFirstName = names[0];
			}

			if (patient.getAddress() != null) {
				pinCode = patient.getAddress().getPincode();
				address = patient.getAddress().getLine();
			}

			patientDemographics.setNationalHealthId(healthId);
			patientDemographics.setNationalHealthNumber(healthNumber);
			patientDemographics.setAge(patientDemographics != null ? patientDemographics.getAge()
					: Period.between(dateOfBirth, LocalDate.now()).getYears());
			patientDemographics.setBloodGroup(patientDemographics != null ? patientDemographics.getBloodGroup() : "");
			patientDemographics.setCanViewInOtherCenters(
					patientDemographics != null ? patientDemographics.getCanViewInOtherCenters() : "");
			patientDemographics.setCasetypeId(patientDemographics != null ? patientDemographics.getCasetypeId() : null);
			patientDemographics.setDeclared(patientDemographics != null ? patientDemographics.getDeclared() : "N");
			patientDemographics.setDob(patientDemographics != null ? patientDemographics.getDob() : dob);
			patientDemographics.setEducationId(patientDemographics != null ? patientDemographics.getEducationId() : "");
			patientDemographics.setId(patientDemographics != null ? patientDemographics.getId() : 0);
			patientDemographics.setReferral_id(patientDemographics != null ? patientDemographics.getReferral_id() : 4);
			patientDemographics.setFamliySize(patientDemographics != null ? patientDemographics.getFamliySize() : null);
			patientDemographics.setGenderId(patientDemographics != null ? patientDemographics.getGenderId() : genderId);
			patientDemographics.setHospitalConfirmation(
					patientDemographics != null ? patientDemographics.getHospitalConfirmation() : "N");
			patientDemographics.setMlc(patientDemographics != null ? patientDemographics.getMlc() : "N");
			patientDemographics.setIdentificationCard(
					patientDemographics != null ? patientDemographics.getIdentificationCard() : "N");
			patientDemographics.setIdentificationTypeId(
					patientDemographics != null ? patientDemographics.getIdentificationTypeId() : null);
			patientDemographics
					.setIncomegroupId(patientDemographics != null ? patientDemographics.getIncomegroupId() : null);
			patientDemographics
					.setIsEmergency(patientDemographics != null ? patientDemographics.getIsEmergency() : "Y");
			patientDemographics
					.setIsTelemedicine(patientDemographics != null ? patientDemographics.getIsTelemedicine() : "");
			patientDemographics
					.setMaritalstatus(patientDemographics != null ? patientDemographics.getMaritalstatus() : 0);
			patientDemographics.setMayCallThisNumber(
					patientDemographics != null ? patientDemographics.getMayCallThisNumber() : "");
			patientDemographics
					.setMayLeaveMessage(patientDemographics != null ? patientDemographics.getMayLeaveMessage() : "");
			patientDemographics.setMrnNo(patientDemographics != null ? patientDemographics.getMrnNo() : "");
			patientDemographics
					.setNationality(patientDemographics != null ? patientDemographics.getNationality() : null);
			patientDemographics.setNhifCardno(patientDemographics != null ? patientDemographics.getNhifCardno() : null);
			patientDemographics.setRaceId(patientDemographics != null ? patientDemographics.getRaceId() : null);
			patientDemographics
					.setOccupationId(patientDemographics != null ? patientDemographics.getOccupationId() : null);
			patientDemographics.setOrganDonationStatus(
					patientDemographics != null ? patientDemographics.getOrganDonationStatus() : "N");
			patientDemographics.setPatientCategoryId(
					patientDemographics != null ? patientDemographics.getPatientCategoryId() : null);
			patientDemographics.setPatientFirstName(
					patientDemographics != null ? patientDemographics.getPatientFirstName() : patientFirstName);
			patientDemographics.setPatientMiddleName(
					patientDemographics != null ? patientDemographics.getPatientMiddleName() : patientMiddleName);
			patientDemographics.setPatientLastName(
					patientDemographics != null ? patientDemographics.getPatientLastName() : patientLastName);
			patientDemographics.setPatientType(patientDemographics != null ? patientDemographics.getPatientType() : 2);
			patientDemographics
					.setPatientsourceId(patientDemographics != null ? patientDemographics.getPatientsourceId() : null);
			patientDemographics.setPatientsourcedtls(
					patientDemographics != null ? patientDemographics.getPatientsourcedtls() : "");
			patientDemographics
					.setPaymentTypeId(patientDemographics != null ? patientDemographics.getPaymentTypeId() : 1);
			patientDemographics
					.setPoliceStation(patientDemographics != null ? patientDemographics.getPoliceStation() : "");
			patientDemographics
					.setRelationshipId(patientDemographics != null ? patientDemographics.getRelationshipId() : 0);
			patientDemographics.setReligionId(patientDemographics != null ? patientDemographics.getReligionId() : 0);
			patientDemographics
					.setSalutationId(patientDemographics != null ? patientDemographics.getSalutationId() : null);
			patientDemographics.setStatus(patientDemographics != null ? patientDemographics.getStatus() : 1);
			patientDemographics
					.setTransferFlag(patientDemographics != null ? patientDemographics.getTransferFlag() : "N");
			patientDemographics.setTariffCategory(
					patientDemographics != null ? patientDemographics.getTariffCategory() : 2000000000026L);
			patientDemographics.setTreatingDoctorId(
					patientDemographics != null ? patientDemographics.getTreatingDoctorId() : null);
			patientDemographics.setTribe(patientDemographics != null ? patientDemographics.getTribe() : null);
			patientDemographics.setUhid(patientDemographics != null ? patientDemographics.getUhid() : null);
			patientDemographics
					.setPatientDocument(patientDemographics != null ? patientDemographics.getPatientDocument() : "");
			com.dipl.abha.patient.entities.PatientAddress patientAddress = patientDemographics != null
					? patientAddressRepo.findByPatientId(patientDemographics.getId())
					: null;
			patientDemographics.setPatientAddress(this.buildPatientAddress(patientAddress, mobilNo, pinCode, address));

			patientDemographics.setGarAddress(this.buildGarAddress());
			patientDemographics.setGuarator(this.buildGuarator(null));
			patientDemographics.setSecGarAddress(this.buildSecGarAddress());
			patientDemographics.setSecPatAddress(this.buildSecPatAddress(1L));
			patientDemographics.setSecGarAddress(this.buildSecGarAddress());
			patientDemographics.setSecGuarator(this.buildSecGuaratorPayload(1L));
			patientDemographics.setCreditDetails(new ArrayList<>());
		}
		return patientDemographics;

	}

	public PatientAddress buildPatientAddress(com.dipl.abha.patient.entities.PatientAddress patientAddress2,
			String mobile, String pinCode, String address) {
		PatientAddress patientAddress = new PatientAddress();
		patientAddress.setAddress1(patientAddress2 != null ? patientAddress2.getAddress1() : address);
		patientAddress.setAddress2(patientAddress2 != null ? patientAddress2.getAddress2() : "");
		patientAddress.setAreaId(patientAddress2 != null ? patientAddress2.getAreaId() : null);
		patientAddress.setCityId(patientAddress2 != null ? patientAddress2.getCityId() : null);
		patientAddress.setCountryId(patientAddress2 != null ? patientAddress2.getCountryId() : null);
		patientAddress.setDistrictId(patientAddress2 != null ? patientAddress2.getDistrictId() : null);
		patientAddress.setEmail(patientAddress2 != null ? patientAddress2.getEmail() : "");
		patientAddress.setId(patientAddress2 != null ? patientAddress2.getId() : 0);
		patientAddress.setMobile1(patientAddress2 != null ? patientAddress2.getMobile1() : mobile);
		patientAddress.setMobile2(patientAddress2 != null ? patientAddress2.getMobile2() : null);
		patientAddress.setOfficePhone(patientAddress2 != null ? patientAddress2.getOfficePhone() : null);
		patientAddress.setPassword(patientAddress2 != null ? patientAddress2.getPassword() : "");
		patientAddress.setSendEmail(patientAddress2 != null ? patientAddress2.getSendEmail() : "Y");
		patientAddress.setSendSms(patientAddress2 != null ? patientAddress2.getSendSms() : "Y");
		patientAddress.setStateId(patientAddress2 != null ? patientAddress2.getStateId() : null);
		patientAddress.setTransferFlag(patientAddress2 != null ? patientAddress2.getTransferFlag() : "N");
		patientAddress.setFax(patientAddress2 != null ? patientAddress2.getFax() : "");
		patientAddress.setWebaddress(patientAddress2 != null ? patientAddress2.getWebaddress() : "");
		patientAddress.setZip(patientAddress2 != null ? patientAddress2.getZip() : pinCode);
		patientAddress.setAddressTypeId(patientAddress2 != null ? patientAddress2.getAddressTypeId() : 1);
		patientAddress.setPatientId(patientAddress2 != null ? patientAddress2.getPatientId() : 0);
		patientAddress.setPrimaryAddress(patientAddress2 != null ? patientAddress2.getPrimaryAddress() : "Y");
		patientAddress.setStateId(patientAddress2 != null ? patientAddress2.getStateId() : 1);
		patientAddress.setHomePhone(patientAddress2 != null ? patientAddress2.getHomePhone() : "");
		return patientAddress;
	}

	public GarAddress buildGarAddress() {
		GarAddress garAddress = new GarAddress();
		garAddress.setAddress1("");
		garAddress.setAddress2("");
		garAddress.setAreaId(null);
		garAddress.setCityId(null);
		garAddress.setCountryId(null);
		garAddress.setDistrictId(null);
		garAddress.setEmail("");
		garAddress.setHomePhone("");
		garAddress.setMobile1("");
		garAddress.setMobile2("");
		garAddress.setOfficePhone("");
		garAddress.setPassword("");
		garAddress.setSendEmail("N");
		garAddress.setSendSms("N");
		garAddress.setWebaddress("");
		garAddress.setStateId(null);
		garAddress.setTransferFlag("N");
		garAddress.setZip(null);
		garAddress.setPatientId(null);
		garAddress.setStatus(1);
		garAddress.setFax("");
		garAddress.setId(0);
		garAddress.setGuarantorId(null);
		garAddress.setGuarantorId(1);
		garAddress.setPrimaryGuarantor("N");
		return garAddress;
	}

	public Guarator buildGuarator(Long guratorId) {
		Guarator guarator = new Guarator();
		guarator.setAddress("");
		guarator.setGuarantorName("");
		guarator.setTransferFlag("N");
		guarator.setGuarantorRelationshipid(0);
		guarator.setStatus(1);
		guarator.setId(guratorId);
		return guarator;
	}

	public SecPatAddress buildSecPatAddress(Long secPatAddressId) {
		SecPatAddress secPatAddress = new SecPatAddress();
		secPatAddress.setAddress1("");
		secPatAddress.setAddress2("");
		secPatAddress.setAreaId(null);
		secPatAddress.setCityId(null);
		secPatAddress.setCountryId(null);
		secPatAddress.setDistrictId(null);
		secPatAddress.setEmail("");
		secPatAddress.setFax("");
		secPatAddress.setHomePhone("");
		secPatAddress.setId(secPatAddressId);
		secPatAddress.setMobile1("");
		secPatAddress.setMobile2("");
		secPatAddress.setOfficePhone("");
		secPatAddress.setPassword("");
		secPatAddress.setSendEmail("");
		secPatAddress.setSendSms("N");
		secPatAddress.setStateId(null);
		secPatAddress.setTransferFlag("N");
		secPatAddress.setWebaddress("");
		secPatAddress.setAddressTypeId("2");
		secPatAddress.setPatientId(null);
		secPatAddress.setPrimaryAddress("");
		secPatAddress.setStatus(1);
		return secPatAddress;
	}

	public SecGarAddress buildSecGarAddress() {
		SecGarAddress secGarAddress = new SecGarAddress();
		secGarAddress.setAddress1("");
		secGarAddress.setAddress2("");
		secGarAddress.setAreaId(null);
		secGarAddress.setCityId(null);
		secGarAddress.setCountryId(null);
		secGarAddress.setDistrictId(null);
		secGarAddress.setEmail("");
		secGarAddress.setFax("");
		secGarAddress.setHomePhone("");
		secGarAddress.setId(0);
		secGarAddress.setMobile1("");
		secGarAddress.setMobile2("");
		secGarAddress.setOfficePhone("");
		secGarAddress.setPassword("");
		secGarAddress.setSendSms("N");
		secGarAddress.setStateId(null);
		secGarAddress.setTransferFlag("N");
		secGarAddress.setWebaddress("");
		secGarAddress.setZip(null);
		secGarAddress.setGuarantorTypeId("2");
		secGarAddress.setGuarantorId(null);
		secGarAddress.setPatientId(null);
		secGarAddress.setPrimaryGuarantor("N");
		secGarAddress.setStatus(1);
		secGarAddress.setSendEmail("N");
		return secGarAddress;
	}

	public SecGuarator buildSecGuaratorPayload(Long secGuratorId) {
		SecGuarator secGuratot = new SecGuarator();
		secGuratot.setAddress("");
		secGuratot.setGuarantorName("");
		secGuratot.setGuarantorRelationshipid(0);
		secGuratot.setId(secGuratorId);
		secGuratot.setStatus(1);
		secGuratot.setTransferFlag("N");
		return secGuratot;
	}

	public String getMrnNumberNew(String firstName) {
		String firstChar = !firstName.isEmpty() && firstName != null ? firstName.toUpperCase().substring(0, 1) : "Z";
		Optional<Center> centrOpt = centerRepository.findByIdAndOrgId(99L, 99L);
		if (!centrOpt.isPresent()) {
			return null;
		}
		String ccCode = "";
		if (centrOpt.isPresent()) {
			ccCode = centrOpt.get().getCode();
		}
		String CODENOCHECK = ccCode + "0";

		if (firstChar != null) {
			CODENOCHECK = ccCode + firstChar;
		}
		String mrnoSr = patientRegRepository.generateMrnoNewR(CODENOCHECK + "%");
		return CODENOCHECK + mrnoSr;
	}

	public static int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(10) + 1;
	}

//	public String returnCareContextsIds(List<Long> consultationIds) {
//		String consultationIdsString = "";
//		for (int i = 0; i < consultationIds.size(); i++) {
//			if (i == consultationIds.size() - 1) {
//				consultationIdsString = consultationIdsString.concat("'" + consultationIds.get(i) + "'");
//			} else {
//				consultationIdsString = consultationIdsString.concat("'" + consultationIds.get(i) + "',");
//			}
//		}
//		return consultationIdsString;
//	}

	public String returnCareContextsIdsAsString(List<String> consultationIds) {
		String consultationIdsString = "";
		for (int i = 0; i < consultationIds.size(); i++) {
			if (i == consultationIds.size() - 1) {
				consultationIdsString = consultationIdsString.concat("'" + consultationIds.get(i) + "'");
			} else {
				consultationIdsString = consultationIdsString.concat("'" + consultationIds.get(i) + "',");
			}
		}
		return consultationIdsString;
	}

	public String returnFinalQuery(String partialQuery, String consultationIdsString, LocalDate fromDate,
			LocalDate toDate) throws JsonProcessingException {
		if (consultationIdsString.isEmpty()) {
			consultationIdsString = "('')";
		} else {
			consultationIdsString = "(" + consultationIdsString + ")";
		}
		Map<String, String> replacementStrings = Map.of("?1", consultationIdsString, "?2",
				"'" + fromDate.toString() + "'", "?3", "'" + toDate.toString() + "'");
		StrSubstitutor sub = new StrSubstitutor(replacementStrings, "{", "}");
		String finalQuery = sub.replace(partialQuery);
//		System.out.println("Final Query which is executing ==========>   " + finalQuery);
		LOGGER.info("Final Query which is executing ==========>   " + finalQuery);
		return finalQuery;
	}

	public String returnFinalQueryForDirectAuth(String partialQuery, String consultationIdsString)
			throws JsonProcessingException {
		if (consultationIdsString.isEmpty()) {
			consultationIdsString = "('')";
		} else {
			consultationIdsString = "(" + consultationIdsString + ")";
		}
		Map<String, String> replacementStrings = Map.of("?1", consultationIdsString);
		StrSubstitutor sub = new StrSubstitutor(replacementStrings, "{", "}");
		String finalQuery = sub.replace(partialQuery);
//		System.out.println("Final Query which is executing ==========>   " + finalQuery);
		LOGGER.info("Final Query which is executing ==========>   " + finalQuery);
		return finalQuery;
	}

	public String returnFinalQueryForUpdate(String partialQuery, String consultationIdsString, String status)
			throws JsonProcessingException {
		if (consultationIdsString.isEmpty()) {
			consultationIdsString = "('')";
		} else {
			consultationIdsString = "(" + consultationIdsString + ")";
		}
		Map<String, String> replacementStrings = Map.of("?1", status, "?2", consultationIdsString);
		StrSubstitutor sub = new StrSubstitutor(replacementStrings, "{", "}");
		String finalQuery = sub.replace(partialQuery);
//		System.out.println("Final Query which is executing ==========>   " + finalQuery);
		LOGGER.info("Final Query which is executing ==========>   " + finalQuery);
		return finalQuery;
	}

}
