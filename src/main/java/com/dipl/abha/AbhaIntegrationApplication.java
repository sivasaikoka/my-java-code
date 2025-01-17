package com.dipl.abha;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Configuration
@EntityScan({ "com.dipl.abha.patient.entities", "com.dipl.abha.entities", "com.dipl.abha.uhi.entities" ,"com.dipl.abha.uhi.dto"})
@EnableJpaRepositories({ "com.dipl.abha.repositories", "com.dipl.abha.uhi.repositories" })
public class AbhaIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbhaIntegrationApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public JSONObject jsonObject() {
		return new JSONObject();
	}


}
