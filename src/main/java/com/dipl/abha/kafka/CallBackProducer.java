//package com.dipl.abha.kafka;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CallBackProducer {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(CallBackProducer.class);
//
//	private static final String CONSENTS_ON_HIU_NOTIFY = "CONSENTS-ON-HIU-NOTIFY";
//
//	private static final String CONSENTS_DATA_PUSH_REQUEST = "CONSENTS-DATA-PUSH-REQUEST";
//
//	private static final String HEALTH_INFORMATION_TRANSFER = "HEALTH-INFORMATION-TRANSFER";
//
//	private final KafkaTemplate<String, String> kafkaTemplate;
//
//	public CallBackProducer(KafkaTemplate<String, String> kafkaTemplate) {
//		this.kafkaTemplate = kafkaTemplate;
//	}
//
//	public void sentPayloadToKafkaStream(String payLoad, String topicType) {
//		try {
//			LOGGER.info(String.format("$$$$ => Producing message: %s", payLoad));
//			switch (topicType) {
//			case CONSENTS_ON_HIU_NOTIFY:
//				this.kafkaTemplate.send(CONSENTS_ON_HIU_NOTIFY, payLoad);
//				break;
//			case CONSENTS_DATA_PUSH_REQUEST:
//				this.kafkaTemplate.send(CONSENTS_DATA_PUSH_REQUEST, payLoad);
//				break;
//			case HEALTH_INFORMATION_TRANSFER:
//				this.kafkaTemplate.send(HEALTH_INFORMATION_TRANSFER, payLoad);
//				break;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOGGER.error(e.getMessage());
//		}
//	}
//}
