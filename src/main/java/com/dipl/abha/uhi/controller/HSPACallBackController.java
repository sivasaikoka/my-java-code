package com.dipl.abha.uhi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.service.AllAbdmCentralDbSave;
import com.dipl.abha.uhi.dto.CancelPayload;
import com.dipl.abha.uhi.dto.OnInit;
import com.dipl.abha.uhi.dto.confirmPayload;
import com.dipl.abha.uhi.dto.onSelect;
import com.dipl.abha.uhi.service.UHIService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("hspa")
public class HSPACallBackController {
	
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private AllAbdmCentralDbSave abdmCentralDbSave;
	
	
	@Autowired
	private UHIService service;


	@PostMapping("/on_select")
	public ResponseEntity<?> onselect(@RequestBody String request) {
		ResponseBean bean = new ResponseBean();
		try {
			log.info("===========HSPA  ON_SELECT========================");
			log.info("=====on_select===={}", request);
			onSelect onSelect = objectMapper.readValue(request, onSelect.class);
			abdmCentralDbSave.saveHspaRequest("/on_select", onSelect.getContext().getTransaction_id(), null, request,
					null);
			log.info("CALLING REST API =======>{}" , request);
			return service.checkRestCallSuccessOrNot(service
					.restCallApi(null, request, onSelect.getContext().getConsumer_uri()+ "/eua/on_select"));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity<>(bean, bean.getStatus());
	}

	@PostMapping("/on_init")
	public ResponseEntity<?> onInit(@RequestBody String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
			log.info("===========STARTED EUA ON_INIT========================");
			log.info("on_init==={}",request);
			OnInit onInit = objectMapper.readValue(request, OnInit.class);
			System.out.println("onInit=======>" + request);
			abdmCentralDbSave.saveHspaRequest("/on_init", onInit.getContext().getTransaction_id(),
					onInit.getMessage().getOrder().getId(), request, null);
			
			log.info("REST API CALL==={}",request);
			return service.checkRestCallSuccessOrNot(service
					.restCallApi(null, request,onInit.getContext().getConsumer_uri()+ "/eua/on_init"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity<>(bean, bean.getStatus());
	}

	@PostMapping("/on_confirm")
	public ResponseEntity<?> onConfirm(@RequestBody String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
//		log.info("===========STARTED EUA ON_INIT========================");
			log.info("HSPA ON_CONFIRM==={}",request);
			confirmPayload onconfirm = objectMapper.readValue(request,confirmPayload.class);
			log.info("RESTAPI CALLING=======>{}" , request);
			abdmCentralDbSave.saveHspaRequest("/on_confirm", onconfirm.getContext().getTransaction_id(),
					onconfirm.getMessage().getOrder().getId(), request, null);
			
			log.info("REST API CALL==={}",request);
			return service.checkRestCallSuccessOrNot(service
					.restCallApi(null, request,onconfirm.getContext().getConsumer_uri()+ "/eua/on_confirm"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity<>(bean, bean.getStatus());
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
			// TODO: handle exception
			e.printStackTrace();
		}

		return new ResponseEntity<>(bean, bean.getStatus());
	}

	@PostMapping("/on_update")
	public ResponseEntity<?> onUpdate(@RequestBody String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
//			OnStatus onStatus = objectMapper.readValue(request, OnStatus.class);
			System.out.println("onStatus=======>" + request);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	@PostMapping("/on_message")
	public ResponseEntity<?> onMessage(@RequestBody String request, HttpServletRequest httpServletRequest) {
		ResponseBean bean = new ResponseBean();
		try {
//			OnStatus onStatus = objectMapper.readValue(request, OnStatus.class);
			System.out.println("onStatus=======>" + request);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	@PostMapping("/hspa/on_cancel")
	public ResponseEntity<?> onCancel(@RequestBody String request, HttpServletRequest httpServletRequest) {
		try {
			log.info("HSPA ON_CANCEL==={}",request);
			CancelPayload onCancel = objectMapper.readValue(request,CancelPayload.class);
			log.info("RESTAPI CALLING=======>{}" , request);
			abdmCentralDbSave.saveHspaRequest("/on_cancel", onCancel.getContext().getTransaction_id(),
					onCancel.getMessage().getOrder().getId(), request, null);
			
			log.info("REST API CALL==={}",request);
			return service.checkRestCallSuccessOrNot(service
					.restCallApi(null, request,onCancel.getContext().getConsumer_uri()+ "/eua/on_cancel"));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

}
