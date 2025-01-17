package com.dipl.abha.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;

/**
 * @author madhumohan.p To get the data from master data source
 */
public class MultiTenantDataSourceFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiTenantDataSourceFactory.class);

	
	public static DataSource getDataSource(Long tenantId,String dbUrl, String dbUserName, String dbPaswrd) {
		DataSource dataSource = null;
		try {
			Connection conn = DataBaseConnection.getInstance(dbUrl, dbUserName, dbPaswrd).getConnection();
			Statement stmt;
			stmt = conn.createStatement();
			String query = "select distinct or1.id,concat(ds.ip_address,':',ds.port,'/',database_name,'?currentSchema=',or1.schema_name )as serverdetails,ds.user_name,ds.password from orgnization_registration or1 join database_server ds on ds.id=or1.database_server_id where or1.id="
					+ tenantId;
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String userName = rs.getString("user_name");
				String password = rs.getString("password");
				String databaseName = rs.getString("serverdetails");
				LOGGER.info("Data Base Server Details====================================>" + databaseName);
				LOGGER.info("Data Base userName============================>" + userName);
				LOGGER.info("Data Base password==============================>" + password);
				dataSource = DataSourceBuilder.create().driverClassName("org.postgresql.Driver")
						.url("jdbc:postgresql://" + databaseName).username(userName).password(password).build();
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSource;
	}

	public static Connection getConnection(String dbUrl, String dbUserName, String dbPaswrd) {
		try {
			Class.forName("org.postgresql.Driver");
			Connection conn = DriverManager.getConnection(dbUrl, dbUserName, dbPaswrd);
			return conn;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}