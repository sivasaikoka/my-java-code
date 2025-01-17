package com.dipl.abha.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;



/**
 * @author madhumohan.p
 *
 */
@Configuration
public class DbConfig {


	@Autowired
	MultiTenantRoutingDataSource dataSource;
	Properties hibernateProperties() {
		return new Properties() {
			/**
			*
			*/
			private static final long serialVersionUID = 1L;

			{
				setProperty("hibernate.show_sql", "true");
				setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
				setProperty("hibernate.query.startup_check", "false");
			}
		};
	}

	@Bean(name = "transactionManager")
	public MultiTenantDataSourceTransactionManager getTransactionManager() {
		MultiTenantDataSourceTransactionManager txManager = new MultiTenantDataSourceTransactionManager();
		txManager.setDataSource(dataSource);
		return txManager;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setDataSource(dataSource);
// Classpath scanning of @Component, @Service, etc annotated class
		entityManagerFactory.setPackagesToScan("com.dipl","org.dipl.entity");

// Vendor adapter
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

// Hibernate properties

		entityManagerFactory.setJpaProperties(hibernateProperties());

		return entityManagerFactory;
	}

}
