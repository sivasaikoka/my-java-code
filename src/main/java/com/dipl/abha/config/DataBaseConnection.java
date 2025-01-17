package com.dipl.abha.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DataBaseConnection {
		private static DataBaseConnection instance;
		private Connection connection;
		private DataBaseConnection(){
			
		}

		private DataBaseConnection(String url ,String userName ,String password) throws SQLException {
			try {
				Class.forName("org.postgresql.Driver");
				this.connection = DriverManager.getConnection(url, userName, password);
			} catch (ClassNotFoundException ex) {
				System.out.println("Database Connection Creation Failed : " + ex.getMessage());
			}
		}

		public Connection getConnection() {
			return connection;
		}

		public static DataBaseConnection getInstance(String url ,String userName ,String password) throws SQLException {
			if (instance == null) {
				instance = new DataBaseConnection(url, userName, password);
			} else if (instance.getConnection().isClosed()) {
				instance = new DataBaseConnection(url, userName, password);
			}
			return instance;
		}
	
}
