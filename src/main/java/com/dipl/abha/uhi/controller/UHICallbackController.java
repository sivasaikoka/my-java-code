package com.dipl.abha.uhi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dipl.abha.dto.ResponseBean;

@RestController
public class UHICallbackController {

	@Value("${PROVIDER_ID}")
	private String provider_id;

	@Value("${PROVIDER_URI}")
	private String provider_uri;

	@PostMapping("/api/v1/on_confirm_audit")
	public ResponseEntity<?> onConfirmAudit(@RequestBody String request) {
		ResponseBean bean = new ResponseBean();
		try {
//			OnConfirm onConfirmAudit = objectMapper.readValue(request, OnConfirm.class);
			System.out.println("onConfirmAudit=======>" + request);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	@PostMapping("/api/v1/on_status_audit")
	public ResponseEntity<?> onStatusAudit(@RequestBody String request) {
		ResponseBean bean = new ResponseBean();
		try {
//			OnConfirm onConfirmAudit = objectMapper.readValue(request, OnConfirm.class);
			System.out.println("onConfirmAudit=======>" + request);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	@PostMapping("/api/v1/on_cancel_audit")
	public ResponseEntity<?> onCancelAudit(@RequestBody String request) {
		ResponseBean bean = new ResponseBean();
		try {
//			OnCancelAudit onCancelAudit = objectMapper.readValue(request, OnCancelAudit.class);
			System.out.println("onConfirmAudit=======>" + request);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

}
