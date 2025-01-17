package com.dipl.abha.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
public class AppConfig extends WebMvcConfigurerAdapter   {


	@Autowired
	MultiTenantInterceptor multiTenantInterceptor;
	
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(multiTenantInterceptor);
	}
}
