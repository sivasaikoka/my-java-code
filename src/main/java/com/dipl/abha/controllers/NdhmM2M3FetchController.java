package com.dipl.abha.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.config.TenantContext;
import com.dipl.abha.dto.AbhaQueryTable;
import com.dipl.abha.dto.AbhaRegistrationDTO;
import com.dipl.abha.dto.CallBackDTO;
import com.dipl.abha.dto.Demographic;
import com.dipl.abha.dto.Identifier;
import com.dipl.abha.dto.NDHMResponse;
import com.dipl.abha.dto.NotifyResultSetDto;
import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.entities.CallBackApiResponse;
import com.dipl.abha.entities.HIUDecryptedDocument;
import com.dipl.abha.repositories.HIUDecryptedDocumentRepository;
import com.dipl.abha.repositories.HIURequestRepository;
import com.dipl.abha.util.FhirBundleReader;
import com.dipl.abha.util.JdbcTemplateHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping({ "/milestone2", "/milestone3" })
public class NdhmM2M3FetchController {
	@Autowired
	private HIURequestRepository hiuRequestRepo;

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private FhirBundleReader fhirBundleReader;

	@Autowired
	private HIUDecryptedDocumentRepository hiuDecryptedDocumentRepo;

	@Autowired
	private JdbcTemplateHelper jdbcTemplateHelper;

	@Autowired
	private NDHMM2CallBackController ndhmM2CallBackController;

	@GetMapping(value = "/getCallBack")
	@ApiOperation(value = "Get all Documents List")
	public ResponseEntity<?> allCallBack(
			@RequestParam(value = "requestId", required = true) Optional<String> requestId) {
		ResponseBean bean = new ResponseBean();
		bean.setStatus(HttpStatus.OK);
		CallBackApiResponse callBack = null;
		Map<String, Object> careContext = null;
		CallBackDTO callBacktDto = null;
		try {
			if (requestId.isPresent()) {
				callBacktDto = new CallBackDTO();
				careContext = hiuRequestRepo.findByRequestIdNew(requestId.get());
				if (careContext != null) {
					callBacktDto.setAbha_no(
							careContext.get("abha_no") != null ? careContext.get("abha_no").toString() : "");
					callBacktDto.setId(Long.parseLong(careContext.get("id").toString()));
					callBacktDto.setRequest(careContext.get("request") != null
							? mapper.readValue(careContext.get("request").toString(), HashMap.class)
							: null);
					callBacktDto.setResponse(careContext.get("response") != null
							? mapper.readValue(careContext.get("response").toString(), HashMap.class)
							: null);
					callBacktDto.setRequestid(
							careContext.get("requestid") != null ? careContext.get("requestid").toString() : null);
				}
			}
			if (callBacktDto != null) {
				bean.setMessage("success");
				bean.setData(callBacktDto);
			} else {
				bean.setMessage("No Records Found");
				bean.setData(callBack);
			}
		} catch (Exception e) {
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage("failed");
			bean.setData(e.toString());
			e.printStackTrace();
		}
		return new ResponseEntity<>(bean, bean.getStatus());
	}

	@GetMapping("/get-document-by-consent-id-page")
	@ApiOperation("Get Documents By Consents Id")
	public ResponseEntity<?> getDocumentsByConsentIdPagination(@RequestParam(value = "consentId") String consentId,
			@RequestBody PagingRequest pagingRequest) {
		ResponseBean bean = new ResponseBean();
		bean.setStatus(HttpStatus.OK);
		try {
			org.springframework.data.domain.Pageable pageable = PageRequest.of(pagingRequest.getPage(),
					pagingRequest.getSize());
			if (pagingRequest.getSize() == null || pagingRequest.getSize() == 0) {
				bean.setStatus(HttpStatus.ACCEPTED);
				bean.setMessage("Size should not be null or zero");
				bean.setData("{}");
				return new ResponseEntity<>(bean, HttpStatus.ACCEPTED);
			} else if (pagingRequest.getPage() == null) {
				bean.setStatus(HttpStatus.ACCEPTED);
				bean.setMessage("Page should not be null or negative");
				bean.setData("{}");
				return new ResponseEntity<>(bean, HttpStatus.ACCEPTED);
			}
//			Page<List<HIUDecryptedDocument>> documentsByPageNation = hiuDecryptedDocumentRepo.getDocumentByConsentRequestCode(consentId,pageable);
//		List<HIUDecryptedDocument> listOfDocumentLists = documentsByPageNation.getContent().get(0);
//
//			// Flatten the list of lists into a single list
//			List<HIUDecryptedDocument> documents = listOfDocumentLists.stream()
//			    .flatMap(List::stream)
//			    .collect(Collectors.toList());
//			if (documents != null && !documents.isEmpty()) {
//				bean.setMessage("success");
//				bean.setData(documents);
//			} else {
//				bean.setMessage("No Records Found");
//				bean.setData(documents);
//			}
		} catch (Exception e) {
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage("failed");
			bean.setData(e.toString());
		}
		return new ResponseEntity<>(bean, bean.getStatus());
	}

	@GetMapping(value = "/getlatestcarecontext")
	@ApiOperation(value = "Get all Documents List")
	public ResponseEntity<?> getLatestCareContext(
			@RequestParam(value = "labReportId", required = false) Optional<String> labReportId,
			@RequestParam(value = "radiologyReportId", required = false) Optional<String> radiologyReportId,
			@RequestParam(value = "consultationId", required = false) Optional<String> consultationId,
			@RequestParam(value = "interactionId") Optional<String> interactionId,
			@RequestParam(value = "lab_interactionId_id") Optional<String> labInteractionId,
			@RequestParam(value = "discharge_no") Optional<String> dischageNO) {
		ResponseBean bean = new ResponseBean();
		bean.setStatus(HttpStatus.OK);
		List<NotifyResultSetDto> careContextRecords = new ArrayList<>();
		List<String> careContexts = new ArrayList<String>();
		try {
			List<AbhaQueryTable> notifyTenantQueries = jdbcTemplateHelper.getResults(
					"select * from public.abha_query_table where  tenant_id = " + TenantContext.getCurrentTenant()
							+ " and query_type in ('GET-CONSULTATION-CARE-CONTEXTS-FOR-DIRECT-AUTH',"
							+ "'GET-LAB-CARE-CONTEXTS-FOR-DIRECT-AUTH','GET-RADIOLOGY-CARE-CONTEXTS-FOR-DIRECT-AUTH',"
							+ "'GET-DISCHARGE-SUMMARY-CARE-CONTEXTS-FOR-DIRECT-AUTH')",
					AbhaQueryTable.class);
			if (labReportId.isPresent() || labInteractionId.isPresent()) {
				if (labReportId.isPresent()) {
					careContexts.add(labReportId.get());
				} else {
					careContexts.add(labInteractionId.get());
				}
				careContextRecords = ndhmM2CallBackController.returnCareContexts(notifyTenantQueries, careContexts,
						"GET-LAB-CARE-CONTEXTS-FOR-DIRECT-AUTH");
			} else if (radiologyReportId.isPresent()) {
				careContexts.add(radiologyReportId.get());
				careContextRecords = ndhmM2CallBackController.returnCareContexts(notifyTenantQueries, careContexts,
						"GET-RADIOLOGY-CARE-CONTEXTS-FOR-DIRECT-AUTH");
			} else if (consultationId.isPresent() || interactionId.isPresent()) {
				if (consultationId.isPresent()) {
					careContexts.add(consultationId.get());
				} else {
					careContexts.add(interactionId.get());
				}
				careContextRecords = ndhmM2CallBackController.returnCareContexts(notifyTenantQueries, careContexts,
						"GET-CONSULTATION-CARE-CONTEXTS-FOR-DIRECT-AUTH");
			} else if (dischageNO.isPresent()) {
				careContexts.add(dischageNO.get());
				careContextRecords = ndhmM2CallBackController.returnCareContexts(notifyTenantQueries, careContexts,
						"GET-DISCHARGE-SUMMARY-CARE-CONTEXTS-FOR-DIRECT-AUTH");
			}
			if (!careContextRecords.isEmpty()) {
				bean.setMessage("success");
				bean.setData(careContextRecords);
			} else {
				bean.setMessage("No Records Found");
				bean.setData(careContextRecords);
			}
		} catch (Exception e) {
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage("failed");
			bean.setData(e.toString());
			e.printStackTrace();
		}
		return new ResponseEntity<>(bean, bean.getStatus());
	}

	@GetMapping("/get-consents-list-by-doctor")
	@ApiOperation("Get All Notify Consents")
	public ResponseEntity<?> getConsentsList(
			@RequestParam(value = "doctorId", required = false) Optional<Long> doctorId,
			@RequestParam("beneficiary_id") Long beneficiaryId) {
		ResponseBean bean = new ResponseBean();
		bean.setStatus(HttpStatus.OK);
		List<Map<String, Object>> consents = null;
		try {
			if (doctorId.isPresent()) {
				consents = hiuRequestRepo.findConsentsByDoctorId(doctorId.get(), beneficiaryId);
			} else {
				consents = hiuRequestRepo.findConsentsByDoctorId(-1L, beneficiaryId);
			}
			if (consents != null && !consents.isEmpty()) {
				bean.setMessage("success");
				bean.setData(consents);
			} else {
				bean.setMessage("No Records Found");
				bean.setData(consents);
			}
		} catch (Exception e) {
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage("failed");
			bean.setData(e.toString());
		}
		return new ResponseEntity<>(bean, bean.getStatus());
	}
	
	

	@GetMapping("/get-document-by-consent-id")
	@ApiOperation("Get Documents By Consents Id")
	public ResponseEntity<?> getDocumentsByConsentId(@RequestParam(value = "consentId") String consentId) {
		ResponseBean bean = new ResponseBean();
		bean.setStatus(HttpStatus.OK);
		try {
			List<HIUDecryptedDocument> documents = hiuDecryptedDocumentRepo.getDocumentByConsentRequestCode(consentId);
			if (documents != null && !documents.isEmpty()) {
				bean.setMessage("success");
				bean.setData(documents);
			} else {
				bean.setMessage("No Records Found");
				bean.setData(documents);
			}
		} catch (Exception e) {
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage("failed");
			bean.setData(e.toString());
		}
		return new ResponseEntity<>(bean, bean.getStatus());
	}

	@GetMapping(value = "/get-m3-call-back")
	@ApiOperation(value = "Get all Documents List")
	public ResponseEntity<?> getM3CallBack(@RequestParam(value = "requestId") Optional<String> requestId) {
		ResponseBean bean = new ResponseBean();
		bean.setStatus(HttpStatus.OK);
		CallBackApiResponse callBack = null;
		Map<String, Object> careContext = null;
		CallBackDTO callBacktDto = null;
		try {
			if (requestId.isPresent()) {
				callBacktDto = new CallBackDTO();
				careContext = hiuRequestRepo.findByM3RequestIdNew(requestId.get());
				if (careContext != null) {
					callBacktDto.setAbha_no(
							careContext.get("abha_no") != null ? careContext.get("abha_no").toString() : "");
					callBacktDto.setId(
							careContext.get("id") != null ? Long.parseLong(careContext.get("id").toString()) : null);
					callBacktDto.setRequest(careContext.get("request") != null
							? mapper.readValue(careContext.get("request").toString(), HashMap.class)
							: null);
					callBacktDto.setResponse(careContext.get("response") != null
							? mapper.readValue(careContext.get("response").toString(), HashMap.class)
							: null);
					callBacktDto.setRequestid(
							careContext.get("requestid") != null ? careContext.get("requestid").toString() : null);
				}
			}
			if (callBacktDto != null) {
				bean.setMessage("success");
				bean.setData(callBacktDto);
			} else {
				bean.setMessage("No Records Found");
				bean.setData(callBack);
			}
		} catch (Exception e) {
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage("failed");
			bean.setData(e.toString());
			e.printStackTrace();
		}
		return new ResponseEntity<>(bean, bean.getStatus());
	}
	
	@GetMapping("/get-document-by-consent-id/get-simplified-bundle")
	@ApiOperation("Get Documents By Consents Id")
	public ResponseEntity<?> getDocumentsByConsentIdNew(@RequestParam(value = "consentId") String consentId,@RequestBody(required = false) PagingRequest pagingRequest) {
		ResponseBean bean = new ResponseBean();
		bean.setStatus(HttpStatus.OK);
		if(pagingRequest !=null) {
			try {
				org.springframework.data.domain.Pageable pageable =null;
				if (pagingRequest.getSize() == null || pagingRequest.getSize() == 0) {
					bean.setStatus(HttpStatus.ACCEPTED);
					bean.setMessage("Size should not be null or zero");
					bean.setData("{}");
					return new ResponseEntity<>(bean, HttpStatus.ACCEPTED);
				} else if (pagingRequest.getPage() == null) {
					bean.setStatus(HttpStatus.ACCEPTED);
					bean.setMessage("Page should not be null or negative");
					bean.setData("{}");
					return new ResponseEntity<>(bean, HttpStatus.ACCEPTED);
				}else {
				 pageable = PageRequest.of(pagingRequest.getPage(),
							pagingRequest.getSize());
				}
				List<HIUDecryptedDocument> documentsByPageNation = hiuDecryptedDocumentRepo.getDocumentByConsentRequestCode(consentId, pageable);
				for(HIUDecryptedDocument decryptedDocument:documentsByPageNation) {
					String simplifiedBundle=fhirBundleReader.reader(decryptedDocument.getDecryptedDocument());
					decryptedDocument.setSimplifiedBundleJson(simplifiedBundle);
				}
				List<HIUDecryptedDocument> processedDocuments = new ArrayList<>(documentsByPageNation);
	
			    Page<HIUDecryptedDocument> updatedPage = new PageImpl<>(processedDocuments, pageable, processedDocuments.size());
				if (updatedPage != null && !updatedPage.isEmpty()) {
					bean.setMessage("success");
					bean.setData(updatedPage);
				} else {
					bean.setMessage("No Records Found");
					bean.setData(updatedPage);
				}
			} catch (Exception e) {
				e.printStackTrace();
				bean.setStatus(HttpStatus.EXPECTATION_FAILED);
				bean.setMessage("failed");
				bean.setData(e.toString());
			}
			return new ResponseEntity<>(bean, bean.getStatus());
		}else {
			try {
				List<HIUDecryptedDocument> documents = hiuDecryptedDocumentRepo.getDocumentByConsentRequestCode(consentId);
				for(HIUDecryptedDocument decryptedDocument:documents) {
					String simplifiedBundle=fhirBundleReader.reader(decryptedDocument.getDecryptedDocument());
					decryptedDocument.setSimplifiedBundleJson(simplifiedBundle);
				}
				if (documents != null && !documents.isEmpty()) {
					bean.setMessage("success");
					bean.setData(documents);
				} else {
					bean.setMessage("No Records Found");
					bean.setData(documents);
				}
			} catch (Exception e) {
				bean.setStatus(HttpStatus.EXPECTATION_FAILED);
				bean.setMessage("failed");
				bean.setData(e.toString());
			}
			return new ResponseEntity<>(bean, bean.getStatus());
		}
		
		
		
	}


	@GetMapping("/get-demography-object")
	public ResponseEntity<ResponseBean> getDemograpyObject(@RequestParam("patientId") String beneficiaryId) {
		ResponseBean bean = new ResponseBean();
		Demographic demographicDetails = null;
		try {
			Object object = jdbcTemplateHelper.getAbhaRegistration(
					"select * from abha_registration ar where ar.tenant_id = '" + TenantContext.getCurrentTenant()
							+ "' and ar.patient_id = '" + beneficiaryId + "' limit 1");
			AbhaRegistrationDTO patientDetails = object != null ? (AbhaRegistrationDTO) object : null;
			if (patientDetails != null) {
				if (patientDetails.getAbha_profile() != null && !patientDetails.getAbha_profile().isEmpty()
						&& !patientDetails.getAbha_profile().equals("null")) {
					NDHMResponse ndhmResponse = mapper.readValue(patientDetails.getAbha_profile(), NDHMResponse.class);
					demographicDetails = new Demographic();
					demographicDetails.setName(ndhmResponse.getName());
					String month = (Integer.parseInt(ndhmResponse.getMonthOfBirth()) < 10)
							? "0" + ndhmResponse.getMonthOfBirth()
							: ndhmResponse.getMonthOfBirth();
					String date = (Integer.parseInt(ndhmResponse.getDayOfBirth()) < 10)
							? "0" + ndhmResponse.getDayOfBirth()
							: ndhmResponse.getDayOfBirth();
					demographicDetails.setDateOfBirth(ndhmResponse.getYearOfBirth() + "-" + month + "-" + date);
					demographicDetails.setGender(ndhmResponse.getGender());
					Identifier identifier = new Identifier();
					identifier.setType("MOBILE");
					identifier.setValue(ndhmResponse.getMobile());
					demographicDetails.setIdentifier(identifier);
					bean.setData(demographicDetails);
					bean.setMessage("DEMOGRAPHIC OBJECT");
					bean.setStatus(HttpStatus.OK);
				} else {
					bean.setData(null);
					bean.setMessage(
							"UN-FORTUNATELY DEMOGRAPHIC DETAILS OF THE PATIENT IS NOT FOUND,PLEASE ENTER MANUALLY");
					bean.setStatus(HttpStatus.OK);
				}
			} else {
				bean.setData(null);
				bean.setMessage("No Records");
				bean.setStatus(HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			bean.setData(demographicDetails);
			bean.setMessage("SOMETING WENT WRONG");
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<>(bean, HttpStatus.OK);
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	private static class PagingRequest {
		@JsonProperty("page")
		private Integer page;
		@JsonProperty("size")
		private Integer size;
		@JsonProperty("search_string")
		private String searchString;
	}
}
