package com.gudigundla.postgres;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
 
public class JDBCHelper {
 
//	public static void main(String[] argv) {

	public Connection getConnection() {
		Connection connection = null;
		 
		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");
 
		try {
 
			Class.forName("org.postgresql.Driver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return connection;
 
		}
 
		System.out.println("PostgreSQL JDBC Driver Registered!");
 
		
		try {
 
			connection = DriverManager.getConnection(
					"jdbc:postgresql://nautilus-vm.mathstat.dal.ca/obis", "pokm",
					"d4lp0km");
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return connection;
 
		}
 
		if (connection != null) {
			
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
		
		return connection;

	}
	
	//}
 
}