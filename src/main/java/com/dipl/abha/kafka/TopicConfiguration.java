//package com.dipl.abha.kafka;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
//import org.apache.kafka.clients.admin.AdminClient;
//import org.apache.kafka.clients.admin.AdminClientConfig;
//import org.apache.kafka.clients.admin.ListTopicsOptions;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//import org.springframework.kafka.core.KafkaAdmin;
//
//@Configuration
//public class TopicConfiguration {
//
////	@Value("${KAFKA-URL}")
////	private String kafkaUrl;
//	
//	private static final String CONSENTS_ON_HIU_NOTIFY = "CONSENTS-ON-HIU-NOTIFY";
//
//	private static final String CONSENTS_DATA_PUSH_REQUEST = "CONSENTS-DATA-PUSH-REQUEST";
//
//	private static final String HEALTH_INFORMATION_TRANSFER = "HEALTH-INFORMATION-TRANSFER";
//	
//	@Bean
//	public KafkaAdmin admin() throws InterruptedException, ExecutionException {
//		Map<String, Object> configs = new HashMap<String, Object>();
//		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.20.125:9092");
//		 AdminClient adminClient = AdminClient.create(configs);
//		ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
//	    listTopicsOptions.listInternal(true);
//
//	    System.out.println("topics:" + adminClient.listTopics(listTopicsOptions).names().get());
//		return new KafkaAdmin(configs);
//	}
//
//	@Bean
//	public NewTopic topic1() {
//		return TopicBuilder.name(CONSENTS_ON_HIU_NOTIFY).partitions(1).replicas(1).build();
//	}
//	@Bean
//	public NewTopic topic2() {
//		return TopicBuilder.name(CONSENTS_DATA_PUSH_REQUEST).partitions(1).replicas(1).build();
//	}
//	@Bean
//	public NewTopic topic3() {
//		return TopicBuilder.name(HEALTH_INFORMATION_TRANSFER).partitions(1).replicas(1).build();
//	}
//	
//
//	
//}