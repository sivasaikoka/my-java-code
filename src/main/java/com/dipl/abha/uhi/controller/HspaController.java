package com.dipl.abha.uhi.controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.others.PayloadException;
import com.dipl.abha.service.AllAbdmCentralDbSave;
import com.dipl.abha.uhi.dto.CancelPayload;
import com.dipl.abha.uhi.dto.InitPayload;
import com.dipl.abha.uhi.dto.RequestDto;
import com.dipl.abha.uhi.dto.SearchByDoctorSpeciality;
import com.dipl.abha.uhi.dto.confirmPayload;
import com.dipl.abha.uhi.entities.EUARequestAndResponse;
import com.dipl.abha.uhi.service.ConfirmService;
import com.dipl.abha.uhi.service.InitService;
import com.dipl.abha.uhi.service.SearchService;
import com.dipl.abha.uhi.service.UHIService;
import com.dipl.abha.util.ConstantUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HspaController {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SearchService searchService;

	@Autowired
	private InitService initService;

	@Autowired
	private ConfirmService confirmService;

	@Autowired
	private AllAbdmCentralDbSave abdmCentralDbSave;

	@Autowired
	private UHIService service;

	@PostMapping("/search")
	public ResponseEntity<?> Search(@RequestBody String request, HttpServletRequest httpServletRequest)
			throws PayloadException, Exception {
		ResponseBean bean = new ResponseBean();
		ResponseEntity<?> searchSpecialityType = null;
		try {
			log.info("#======================= ENTER INTO HSPA SEARCH ==================#{}" ,request);
			SearchByDoctorSpeciality search = objectMapper.readValue(request, SearchByDoctorSpeciality.class);
			abdmCentralDbSave.saveHspaRequest("/search", search.getContext().getTransaction_id(), null, null, request);
			String fromDate = this.convertDateFormate(search.getMessage().getIntent().getFulfillment().getStart().getTime().getTimestamp().toString());
			String toDate = this.convertDateFormate(search.getMessage().getIntent().getFulfillment().getEnd().getTime().getTimestamp().toString());
			
			log.info("X-Gateway-Authorization====>{}"
					, httpServletRequest.getHeader(ConstantUtil.X_GATEWAY_AUTHORIZATION));
			if (httpServletRequest.getHeader(ConstantUtil.X_GATEWAY_AUTHORIZATION) != null) {
				if (search.getMessage().getIntent() != null) {
					if (search.getMessage().getIntent().getFulfillment().getAgent() == null
							&& search.getMessage().getIntent().getProvider() == null
							&& search.getMessage().getIntent().getCategory() != null) {
						if (search.getMessage().getIntent().getCategory().getDescriptor().getCode() != null
								&& search.getMessage().getIntent().getCategory().getDescriptor().getName() != null) {
							searchSpecialityType = searchService.findDoctorSpeaciality(search,
									searchService.findByDoctorBySearchType(
											ConstantUtil.GET_HPR_DOCTOR_DETAILS_BY_SPEACIALITY,
											search.getMessage().getIntent().getCategory().getDescriptor().getName(),
											null, null, null, fromDate, toDate));
						}
					} else if (search.getMessage().getIntent().getCategory() != null
							&& search.getMessage().getIntent().getFulfillment() != null
							&& search.getMessage().getIntent().getProvider() == null) {
						if (search.getMessage().getIntent().getCategory().getDescriptor().getCode() != null
								&& search.getMessage().getIntent().getCategory().getDescriptor().getName() != null
								&& search.getMessage().getIntent().getFulfillment().getAgent().getName() != null) {
							searchSpecialityType = searchService.findDoctorDoctorNameAndSpeaciality(search,
									searchService.findByDoctorBySearchType(
											ConstantUtil.GET_HPR_DOCTOR_DETAILS_BY_DOCTORNAME_SPEACIALITY,
											search.getMessage().getIntent().getCategory().getDescriptor().getName(),
											search.getMessage().getIntent().getFulfillment().getAgent().getName(), null,
											null, fromDate, toDate));

						}
					} else if (search.getMessage().getIntent().getCategory() != null
							&& search.getMessage().getIntent().getFulfillment().getAgent() != null
							&& search.getMessage().getIntent().getProvider() != null) {
						if (search.getMessage().getIntent().getCategory().getDescriptor().getName() != null
								&& search.getMessage().getIntent().getFulfillment().getAgent().getName() != null
								&& search.getMessage().getIntent().getProvider().getDescriptor().getName() != null) {
							searchSpecialityType = searchService.findDoctorDoctorNameAndSpeacialityAndFacility(search,
									searchService.findByDoctorBySearchType(
											ConstantUtil.GET_HPR_DOCTOR_DETAILS_BY_SPEACIALITY_DOCTORNAME_FACILITY,
											search.getMessage().getIntent().getCategory().getDescriptor().getName(),
											search.getMessage().getIntent().getFulfillment().getAgent().getName(),
											search.getMessage().getIntent().getProvider().getDescriptor().getName(),
											null, fromDate, toDate));
						}

					}

					else if (search.getMessage().getIntent().getFulfillment().getAgent() == null
							&& search.getMessage().getIntent().getProvider() != null
							&& search.getMessage().getIntent().getCategory() != null) {
						if (search.getMessage().getIntent().getCategory().getDescriptor().getName() != null
								&& search.getMessage().getIntent().getProvider().getDescriptor().getName() != null) {
							searchSpecialityType = searchService.findDoctorSpeacialityAndfacility(search,
									searchService.findByDoctorBySearchType(
											ConstantUtil.GGET_HPR_DOCTOR_DETAILS_BY_SPEACIALITY_FACILITY,
											search.getMessage().getIntent().getCategory().getDescriptor().getName(),
											null,
											search.getMessage().getIntent().getProvider().getDescriptor().getName(),
											null, fromDate, toDate));

						}

					}

					else if (search.getMessage().getIntent().getFulfillment().getAgent() != null
							&& search.getMessage().getIntent().getProvider() == null
							&& search.getMessage().getIntent().getCategory() == null) {
						if (search.getMessage().getIntent().getFulfillment().getAgent().getId() != null) {
							searchSpecialityType = searchService.findHprId(search,
									searchService.findByDoctorBySearchType(ConstantUtil.GET_HPR_DOCTOR_DETAILS_BY_HPRID,
											null, null, null,
											search.getMessage().getIntent().getFulfillment().getAgent().getId(),
											fromDate, toDate));

						} else if (search.getMessage().getIntent().getFulfillment().getAgent().getName() != null) {
							searchSpecialityType = searchService.findDoctorName(search,
									searchService.findByDoctorBySearchType(
											ConstantUtil.GET_HPR_DOCTOR_DETAILS_BY_DOCTORNAME, null,
											search.getMessage().getIntent().getFulfillment().getAgent().getName(), null,
											null, fromDate, toDate));

						}

					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("=================EXCEPTION HSPA SEARCH================" + e.getMessage());
			bean = new ResponseBean(HttpStatus.INTERNAL_SERVER_ERROR, ConstantUtil.SOMETHING_WENT_WRONG, e.getMessage(),
					null);
			return new ResponseEntity<ResponseBean>(bean, bean.getStatus());
		}

		return searchSpecialityType;
	}

	

	@PostMapping("/select")
	public ResponseEntity<?> select(@RequestBody String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		RequestDto requestDto = null;
		ResponseEntity<?> selectDoctor = null;

		try {
			if (httpServletRequest.getHeader(ConstantUtil.AUTHORIZATION) == null) {
				requestDto = objectMapper.readValue(request, RequestDto.class);
				abdmCentralDbSave.saveHspaRequest("/select", requestDto.getContext().getTransaction_id(), null, null,
						request);
				List<EUARequestAndResponse> FindOnSearch = abdmCentralDbSave.FindByApiType("/on_search",requestDto.getContext().getTransaction_id());
				log.info("transctionId=============" + FindOnSearch.get(0).getTransactionId());
				if (!FindOnSearch.isEmpty() || FindOnSearch != null) {
					if (FindOnSearch.get(0).getTransactionId().equals(requestDto.getContext().getTransaction_id())) {
						log.info("requestDto==================>" + requestDto);
						selectDoctor = searchService
								.select(searchService.BulidSelectCatalog(requestDto, "SELECT-DOCTOR"), requestDto);
					} else {
						log.info("Transaction id not matching");

					}
				}

			}
			return selectDoctor;

		} catch (Exception e) {
			// TODO: handle exception
			log.error("HSPA select method error: " + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}

	}

	@PostMapping("/init")
	public ResponseEntity<?> init(@RequestBody String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		InitPayload onInit = null;
		ResponseEntity<?> oncall = null;

		try {
			log.info("===============STARTED INIT API================");
			onInit = objectMapper.readValue(request, InitPayload.class);
			List<EUARequestAndResponse> FindOnInit= abdmCentralDbSave.FindByApiType("/on_select",onInit.getContext().getTransaction_id());
			abdmCentralDbSave.saveHspaRequest("/init", onInit.getContext().getTransaction_id(), null, null, request);
			if (!FindOnInit.isEmpty() || FindOnInit != null) {
				if (FindOnInit.get(0).getTransactionId() == FindOnInit.get(0).getTransactionId()) {
					String OnSearchResponse = FindOnInit.get(0).getResponseJson().toString();
					RequestDto requestDto = objectMapper.readValue(OnSearchResponse, RequestDto.class);
					log.info("requestDto==================>{}" , requestDto);
					System.out.println("init=======>" + requestDto);
					oncall = initService.onInit(initService.bulidOnInit(onInit));

				}else {
					log.info("Transaction id not matched");
				}

			}

		} catch (Exception e) {
			log.error("===========HSPA INIT method error:========" + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}

		return oncall;
	}

	@PostMapping("/confirm")
	public ResponseEntity<?> confirm(@RequestBody String request) {
		ResponseBean bean = new ResponseBean();
		confirmPayload confirmPayload = null;
		ResponseEntity<?> oncall = null;

		try {
			log.info("===============STARTED CCONFIRM API================");
			confirmPayload = objectMapper.readValue(request, confirmPayload.class);
			List<EUARequestAndResponse> FindOnConfirm = abdmCentralDbSave.FindByApiType("/on_init",confirmPayload.getContext().getTransaction_id());
			abdmCentralDbSave.saveHspaRequest("/init", confirmPayload.getContext().getTransaction_id(), null, null, request);
			if (!FindOnConfirm.isEmpty() || FindOnConfirm != null) {
				if(confirmPayload.getMessage().getOrder().getPayment().getStatus().equals("PAID") || confirmPayload.getMessage().getOrder().getPayment().getStatus().equals("FREE") ) {
				if(confirmPayload.getMessage().getOrder().getId() != null) {
					if(confirmPayload.getMessage().getOrder().getId().equals(FindOnConfirm.get(0).getOrderId())) {
						if (FindOnConfirm.get(0).getTransactionId() == FindOnConfirm.get(0).getTransactionId()) {
					String onConfirm = FindOnConfirm.get(0).getResponseJson().toString();
					log.info("requestDto==================>{}" , onConfirm);
					oncall = confirmService.onConfirm(confirmService.bulidOnConfirm(onConfirm,confirmPayload));

				}else {
					log.info("Transaction id not matched");
				}

			
			}else {
				log.info("================order id not matching===============");
			}
				}
				else {
				log.info("================order id should not be null===============");
			}
			}else {
				log.info("============plese pay the payment===============");
			}
			}
		} catch (Exception e) {
			log.error("===========HSPA confirm method error:========" + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}

		return oncall;
	}

	@PostMapping("/status")
	public ResponseEntity<?> status(@RequestBody String request) {
		ResponseBean bean = new ResponseBean();
		InitPayload onInit = null;
		ResponseEntity<?> status = null;
		try {
			System.out.println("status=======>" + request);

			log.info("===============STARTED cancel API================");
			onInit = objectMapper.readValue(request, InitPayload.class);
			List<EUARequestAndResponse> FindOnStatus = abdmCentralDbSave.FindByApiType("/on_confirm",onInit.getContext().getTransaction_id());
			abdmCentralDbSave.saveHspaRequest("/status", onInit.getContext().getTransaction_id(), null, null, request);
			if (!FindOnStatus.isEmpty() || FindOnStatus != null) {
				if (FindOnStatus.get(0).getTransactionId() == FindOnStatus.get(0).getTransactionId()) {
					String OnStatusResponse = FindOnStatus.get(0).getResponseJson().toString();
					RequestDto requestDto = objectMapper.readValue(OnStatusResponse, RequestDto.class);
					log.info("requestDto==================>" + requestDto);
					System.out.println("init=======>" + requestDto);

					status = service
							.checkRestCallSuccessOrNot(service.restCallApi(null, OnStatusResponse.replace(" ", ""),
									onInit.getContext().getConsumer_uri() + "/on_status"));
				}

			}
			return status;
		} catch (Exception e) {
			log.error("===========HSPA INIT method error:========" + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}

	}

	@PostMapping("/cancel")
	public ResponseEntity<?> cancel(@RequestBody String request) {
		ResponseBean bean = new ResponseBean();
		ResponseEntity<?> cancel = null;
		try {
			log.info("==================STARTED HSPA CANCEL===================={}",request);
			CancelPayload cancelPayload= objectMapper.readValue(request, CancelPayload.class);
			List<EUARequestAndResponse> findOnCancel = abdmCentralDbSave.FindByApiType("/on_confirm",cancelPayload.getContext().getTransaction_id());
			abdmCentralDbSave.saveHspaRequest("/cancel", cancelPayload.getContext().getTransaction_id(), cancelPayload.getMessage().getOrder().getId(), null, request);
			if(findOnCancel.get(0).getTransactionId().equals(cancelPayload.getContext().getTransaction_id()) && findOnCancel.get(0).getOrderId().equals(cancelPayload.getMessage().getOrder().getId())) {
				String canceResponse = findOnCancel.get(0).getResponseJson().toString();
				cancel = service
						.checkRestCallSuccessOrNot(service.restCallApi(null, canceResponse.replace(" ", ""),
								cancelPayload.getContext().getConsumer_uri() + "/on_cancel"));
			
			}
			
			System.out.println("cancel=======>" + request);
			bean.setStatus(HttpStatus.OK);
			return cancel;
		} catch (Exception e) {
			log.error("===========HSPA INIT method error:========" + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}

	}
	
	private String convertDateFormate(String date) {
		String outputDate = "";
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, inputFormatter);
             outputDate = zonedDateTime.toLocalDate().format(outputFormatter);
            System.out.println("Converted Date: " + outputDate);
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
        }
		return outputDate;
	}

}
