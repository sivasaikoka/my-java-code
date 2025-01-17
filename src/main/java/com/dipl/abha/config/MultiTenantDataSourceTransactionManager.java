package com.dipl.abha.config;

import org.springframework.orm.jpa.JpaTransactionManager;

public class MultiTenantDataSourceTransactionManager extends JpaTransactionManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8276505092192647047L;

	public MultiTenantDataSourceTransactionManager() {
		super();
	}
}
