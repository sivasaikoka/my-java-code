package com.dipl.abha.uhi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.service.AllAbdmCentralDbSave;
import com.dipl.abha.uhi.dto.CancelPayload;
import com.dipl.abha.uhi.dto.OnInit;
import com.dipl.abha.uhi.dto.RequestDto;
import com.dipl.abha.uhi.dto.confirmPayload;
import com.dipl.abha.uhi.dto.onSelect;
import com.dipl.abha.util.ConstantUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EUACallBackController {
	
	@Value("${ON_SEARCH}")
	private String on_search;

	@Autowired
	private ObjectMapper objectMapper;


	@Autowired
	private AllAbdmCentralDbSave abdmCentralDbSave;

	@PostMapping("/on_search")
	public void onSearch(@RequestBody String request, HttpServletRequest httpServletRequest) {
		try {
			log.info("===========STARTED EUA ON_SEARCH========================");
			log.info("on_search===" + request);
			RequestDto requestDto = objectMapper.readValue(request, RequestDto.class);
			abdmCentralDbSave.saveEuaRequest("/on_search", requestDto.getContext().getTransaction_id(), null, request,
					null);
		} catch (Exception e) {
			log.error("ERROR at ##EUA ON_SEARCH#: {}  ============>", e.getLocalizedMessage());
			log.error("EXCEPTION OCCURED :{}  ============>", e);
			log.error(e.getMessage());
			log.error("ERROR CAUSE:  {} ============>", e.getCause());
			log.info("ON-SEARCH CALLED, UNABLE TO SAVE IN DB: {}==========>");
			e.printStackTrace();
		}

	}

	@PostMapping("/on_select")
	public void onselect(@RequestBody String request) {
		try {
			log.info("===========STARTED EUA ON_SELECT========================");
			log.info("on_select===" + request);
			onSelect onSelect = objectMapper.readValue(request, onSelect.class);
			onSelect.getContext().setAction("/on_select");
			abdmCentralDbSave.saveEuaRequest("/on_select", onSelect.getContext().getTransaction_id(), null, request,
					null);
		} catch (Exception e) {
			log.error("===========EUA ON_SELECT METHOD ERROR:========" + e);
			e.printStackTrace();
			log.error("ERROR at ##EUA ON_SELECT#: {}  ============>", e.getLocalizedMessage());
			log.error(e.getMessage());
			log.info("ON_SELECT CALLED, UNABLE TO SAVE IN DB: {}==========>");
			log.error("ERROR CAUSE:  {} ============>", e.getCause());
		}

	}

	@PostMapping("/on_init")
	public void onInit(@RequestBody String request, HttpServletRequest httpServletRequest) {
		try {
			log.info("===========STARTED EUA ON_INIT========================");
			log.info("on_init===" + request);
			OnInit onInit = objectMapper.readValue(request, OnInit.class);
			System.out.println("onInit=======>" + request);
						abdmCentralDbSave.saveEuaRequest("/on_init", onInit.getContext().getTransaction_id(),
					onInit.getMessage().getOrder().getId(), request, null);

		} catch (Exception e) {
			log.error("===========EUA ON_INIT METHOD ERROR:========" + e);
			log.error("ERROR at ##EUA ON_INIT#: {}  ============>", e.getLocalizedMessage());
			log.error(e.getMessage());
			log.info("ON_INIT CALLED, UNABLE TO SAVE IN DB: {}==========>");
			log.error("ERROR CAUSE:  {} ============>", e.getCause());
		}

	}

	@PostMapping("/on_confirm")
	public void onConfirm(@RequestBody String request, HttpServletRequest httpServletRequest) {
		try {
			log.info("===========STARTED EUA ON_CONFIRM========================");
			log.info("========on_confirm===={}" , request);
			confirmPayload confirmPayload = objectMapper.readValue(request, confirmPayload.class);
			System.out.println("onConfirm=======>" + request);
						abdmCentralDbSave.saveEuaRequest("/on_confirm", confirmPayload.getContext().getTransaction_id(),
								confirmPayload.getMessage().getOrder().getId(), request, null);
						switch (confirmPayload.getMessage().getOrder().getState()) {
						case "CONFIRMED":
							abdmCentralDbSave.saveAppointmentBookingDetails(confirmPayload.getContext().getTransaction_id(), confirmPayload.getMessage().getOrder().getId(),
									confirmPayload.getMessage().getOrder().getFulfillment().getId(), confirmPayload.getMessage().getOrder().getCustomer().getId(),confirmPayload.getMessage().getOrder().getState());
							break;
						case "FAILED":
							abdmCentralDbSave.saveAppointmentBookingDetails(confirmPayload.getContext().getTransaction_id(), confirmPayload.getMessage().getOrder().getId(),
									confirmPayload.getMessage().getOrder().getFulfillment().getId(), confirmPayload.getMessage().getOrder().getCustomer().getId(),confirmPayload.getMessage().getOrder().getState());
							break;
						case "PROCESSING":
							abdmCentralDbSave.saveAppointmentBookingDetails(confirmPayload.getContext().getTransaction_id(), confirmPayload.getMessage().getOrder().getId(),
									confirmPayload.getMessage().getOrder().getFulfillment().getId(), confirmPayload.getMessage().getOrder().getCustomer().getId(),confirmPayload.getMessage().getOrder().getState());
							break;
						case "COMPLTED":
							abdmCentralDbSave.updateConsulationStatus(confirmPayload.getMessage().getOrder().getId(),confirmPayload.getMessage().getOrder().getState(),null);
							break;

						default:
							break;
						}
						

		} catch (Exception e) {
			log.error("===========EUA ON_CONFIRM METHOD ERROR:========" + e.getMessage());
			log.error("ERROR at ##EUA ON_ConfIRM#: {}  ============>", e.getLocalizedMessage());
			log.error(e.getMessage());
			log.info("ON_INIT CALLED, UNABLE TO SAVE IN DB: {}==========>");
			log.error("ERROR CAUSE:  {} ============>", e.getCause());
		
		}

	}

	@PostMapping("/on_status")
	public ResponseEntity<?> onStatus(@RequestBody String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
//			OnStatus onStatus = objectMapper.readValue(request, OnStatus.class);
			System.out.println("onStatus=======>" + request);
			bean.setData(request);
			bean.setStatus(HttpStatus.OK);
		} catch (Exception e) {
			log.error("===========EUA STATUS METHOD ERROR:========" + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<>(bean, bean.getStatus());
	}

	@PostMapping("/on_update")
	public ResponseEntity<?> onUpdate(@RequestBody String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
//			OnStatus onStatus = objectMapper.readValue(request, OnStatus.class);
			System.out.println("onUpdate=======>" + request);

		} catch (Exception e) {
			log.error("===========EUA ON_UPDATE METHOD ERROR:========" + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}

		return null;
	}

	@PostMapping("/on_message")
	public ResponseEntity<?> onMessage(@RequestBody String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
//			OnStatus onStatus = objectMapper.readValue(request, OnStatus.class);
			System.out.println("onMessage=======>" + request);

		} catch (Exception e) {
			log.error("===========EUA ON_MESSAGE METHOD ERROR:========" + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}

		return null;
	}

	@PostMapping("/on_cancel")
	public ResponseEntity<?> onCancel(@RequestBody String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
			log.info("===========STARTED EUA ON_CANCEL========================");
			log.info("========on_cancel===={}" , request);
			CancelPayload cancelPayload = objectMapper.readValue(request, CancelPayload.class);
			System.out.println("onCancel=======>" + request);
						abdmCentralDbSave.saveEuaRequest("/on_cancel", cancelPayload.getContext().getTransaction_id(),
								cancelPayload.getMessage().getOrder().getId(), request, null);
						abdmCentralDbSave.updateConsulationStatus(cancelPayload.getMessage().getOrder().getId(),cancelPayload.getMessage().getOrder().getState(),cancelPayload.getMessage().getOrder().getFulfillment().getTags().getCancelledby());

		} catch (Exception e) {
			log.error("===========EUA ON_CANCEL METHOD ERROR:========" + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}

		return null;
	}

//	@PostMapping("/findByTransactionId/{transactionId}")
//	public ResponseEntity<?> findAllSearchDetails(@PathVariable("transactionId") String transactionId) {
//		ResponseBean bean = new ResponseBean();
//		try {
//			log.info("transactionId==="+transactionId);
//			List<EUARequestAndResponse> responseDate = abdmCentralDbSave
//			
//			
//			
//			bean.setData(request);
//			bean.setMessage("Sucess");
//			bean.setStatus(HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return new ResponseEntity<> (bean,bean.getStatus());
//	}

}
