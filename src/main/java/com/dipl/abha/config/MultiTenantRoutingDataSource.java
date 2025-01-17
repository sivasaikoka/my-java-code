package com.dipl.abha.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

@Component
public class MultiTenantRoutingDataSource extends AbstractRoutingDataSource {

	protected Map<Object, Object> targetDataSources;

	@Value("${DBURL}")
	private String dbUrl;

	@Value("${DBUSERNAME}")
	private String dbUserName;

	@Value("${DBPASSWORD}")
	private String dbPassword;

	@Override
	public void afterPropertiesSet() {
		targetDataSources = new HashMap<>();
	}

	@Override
	protected Object determineCurrentLookupKey() {
		System.out.println(dbUserName);
		String tenantId = TenantContext.getCurrentTenant();

		Long tenantId1 = Long.parseLong(tenantId);

		System.out.println(tenantId);

		System.out.println("DataSource LookUp key" + "[" + tenantId + "]");
		if (!targetDataSources.containsKey(tenantId)) {
			synchronized (this) {
				if (!targetDataSources.containsKey(tenantId)) {
					DataSource dataSource = MultiTenantDataSourceFactory.getDataSource(tenantId1, dbUrl, dbUserName,
							dbPassword);
					if (dataSource != null) {
						targetDataSources.put(tenantId, dataSource);
						System.out.println("Registering new dataSource for LookUp key ::" + tenantId1);
					} else {
						System.out.println("Registering new dataSource for LookUp key ::" + tenantId1);
					}
				}
			}
		}
		return tenantId;
	}

	@Override
	protected DataSource determineTargetDataSource() {
		Object lookupKey = determineCurrentLookupKey();
		DataSource dataSource = (DataSource) this.targetDataSources.get(lookupKey);
		if (dataSource == null) {
			throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
		}

		return dataSource;
	}

}
