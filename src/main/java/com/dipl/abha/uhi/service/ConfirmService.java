package com.dipl.abha.uhi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dipl.abha.dto.ResponseBean;
import com.dipl.abha.uhi.dto.RequestDto;
import com.dipl.abha.uhi.dto.confirmPayload;
import com.dipl.abha.util.ConstantUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConfirmService {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private UHIService uhiService;

	public confirmPayload bulidOnConfirm(String request, confirmPayload confirm) {
		confirmPayload confirmPayload = null;
		try {
			confirmPayload = mapper.readValue(request, confirmPayload.class);

			if (confirm.getMessage().getOrder().getPayment().getStatus().equals("PAID")) {

				confirmPayload.getMessage().getOrder().setState("CONFIRMED");
			} else if (confirmPayload.getMessage().getOrder().equals("FAILED")) {
				confirmPayload.getMessage().getOrder().setState("FAILED");
			} else if (confirmPayload.getMessage().getOrder().equals("PROCESSING")) {
				confirmPayload.getMessage().getOrder().setState("PROCESSING");
			}else if (confirmPayload.getMessage().getOrder().equals("COMPLETED")) {
				confirmPayload.getMessage().getOrder().setState("COMPLETED");
			}

		} catch (Exception e) {
			log.error("===========HSPA CONFIRM method error:========" + e);
			e.printStackTrace();
		}
		return confirmPayload;

	}

	public ResponseEntity<?> onConfirm(confirmPayload confirm) throws JsonMappingException, JsonProcessingException {
		ResponseBean bean = new ResponseBean();
		ResponseEntity<?> restData = null;
		try {
			String requestDto = mapper.writeValueAsString(restData);
			restData = uhiService.restCallApi(null, requestDto,
					confirm.getContext().getConsumer_uri() + "/on_confirm");
		} catch (Exception e) {
			log.error("===========HSPA CONFIRM method error:========" + e);
			bean.setStatus(HttpStatus.EXPECTATION_FAILED);
			bean.setMessage(ConstantUtil.SOMETHING_WENT_WRONG);
			bean.setData(e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(bean, HttpStatus.EXPECTATION_FAILED);
		}

		return restData;
	}
}
