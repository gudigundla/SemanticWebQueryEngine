package com.gudigundla.UI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import com.gudigundla.postgres.JDBCHelper;

public class PopulateUIComboBoxes {

	Connection conn = new JDBCHelper().getConnection();
	Vector<String> snames;
	public Vector<String> populateCBs(){
		snames =  new Vector<String>();
		if(conn!=null) {
	try{
	// Creating Statement for query execution
	Statement stmt = conn.createStatement();
	// creating Query String
	String query = "SELECT sname FROM obis.snames LIMIT 10";
	// excecuting query
	
	ResultSet rs = stmt.executeQuery(query);
	ResultSetMetaData md = rs.getMetaData();
	while (rs.next()) {
		snames.add(rs.getString(1));
	}
	}
	catch (Exception e) {
		System.out.print(e.toString()); 
	}
	}
		
		return snames;
	}
	
}
