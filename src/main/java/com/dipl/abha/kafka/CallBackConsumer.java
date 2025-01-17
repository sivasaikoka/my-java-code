//package com.dipl.abha.kafka;
//
//import java.io.IOException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import com.dipl.abha.controllers.NDHMCallBackControllerM3;
//import com.dipl.abha.controllers.NDHMM3Controller;
//import com.dipl.abha.dto.ProcessAndDecryptDTO;
//import com.dipl.abha.m3.consentHiuOnNotify.ConsentsHiuOnNotify;
//import com.dipl.abha.requestypayload.RequestPayload;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Service
//public class CallBackConsumer {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(CallBackConsumer.class);
//	@Autowired
//	private ObjectMapper objMapper;
//	@Autowired
//	private NDHMCallBackControllerM3 ndhmM3CallBack;
//	@Autowired
//	private NDHMM3Controller ndhmM3Controller;
//
//	@KafkaListener(topics = "CONSENTS-ON-HIU-NOTIFY", groupId = "group_id")
//	public void consumeHiuNotify(String request) throws IOException {
//		LOGGER.info(String.format(String.format("#### -> CONSENTS-HIU-NOTIFY payload -> %s", request)));
//		ndhmM3Controller.consentOnNotify(objMapper.readValue(request, ConsentsHiuOnNotify.class));
//	}
//
//	@KafkaListener(topics = "CONSENTS-DATA-PUSH-REQUEST", groupId = "group_id")
//	public void consumeConsentHealthInfoRequest(String request) throws IOException {
//		LOGGER.info(String
//				.format(String.format(String.format("#### -> CONSENTS-DATA-PUSH-REQUEST payload -> %s", request))));
//		ndhmM3Controller.cmRequest(objMapper.readValue(request, RequestPayload.class));
//	}
//
//	@KafkaListener(topics = "HEALTH-INFORMATION-TRANSFER", groupId = "group_id")
//	public void consumeHelthInfoHiuOnRequest(String request) throws IOException {
//		LOGGER.info(String.format(
//				String.format(String.format("#### -> HEALTH-INFORMATION-HIU-ON-REQUEST payload -> %s", request))));
//		ndhmM3CallBack.decryptDocumentAndStoreInDd(objMapper.readValue(request, ProcessAndDecryptDTO.class));
//	}
//
//}
