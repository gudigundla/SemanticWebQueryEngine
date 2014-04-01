package com.gudigundla.UI;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.gudigundla.jena.Species;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class ResultTableModel extends AbstractTableModel {

	final List<String> variables;  
    final List<QuerySolution> results;  
	String[] columnNames={"drs URI", "latitude", "longitude", "date collected", "species"};
	
	public ResultTableModel(ResultSet resultset, Species species) {
		results = new ArrayList<QuerySolution>();
		variables = resultset.getResultVars();

		while (resultset.hasNext()) {
			QuerySolution binding = resultset.nextSolution();
			float latitude = Float.parseFloat(binding.get("latitude")
					.toString());
			float longitude = Float.parseFloat(binding.get("longitude")
					.toString());
			Calendar dateCollected = getDateCollectedFromString(binding.get("date_collected").toString());
	
			if(dateCollected==null)
			System.out.println("dateCollected is null" );
			
			if(dateCollected != null)
			if (    (latitude < species.getMinLatitude()
					&& longitude < species.getMinLatitude()) ||
					(latitude > species.getMaxLatitude()
					&& longitude > species.getMaxLongitude()) || 
					dateCollected.compareTo(species.getFrom())<0 ||
					dateCollected.compareTo(species.getTo())>0	)
				continue;
			else
				if (     (latitude < species.getMinLatitude()
						&& longitude < species.getMinLatitude()) ||
						(latitude > species.getMaxLatitude()
						&& longitude > species.getMaxLongitude())	)
				continue;
	
			results.add(binding);
		}
	}
    
	private Calendar getDateCollectedFromString(String dateCollectedPattern) {
		Calendar dateCollected;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = null;
		try {
			if(dateCollectedPattern.length()>=19) {
			date = formatter.parse(dateCollectedPattern.substring(0, 18));
			System.out.println("*** "+date.toString()); 
			dateCollected = Calendar.getInstance();
			if(date!=null)
			dateCollected.setTime(date);
			return dateCollected;
			}
			else
				System.out.println("*** date is null");
				return null;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return variables.size();
	}

	@Override
	public int getRowCount() {
		return results.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		return results.get(row).get(variables.get(column));  
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column].toString();  
	}

}
