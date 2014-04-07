package com.gudigundla.jena;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Vector;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.query.ResultSet;

public class learningSPARQL {

	//	String path = "C:\\Users\\Haree\\Desktop\\MACS Project\\RDF\\";
	String path = "files\\RDF\\";
	Vector<Species> species;
	Model model;
	
	public void simpleSPARQL() throws IOException{
	
		// Open the bloggers RDF graph from the filesystem
		InputStream in = new FileInputStream(new File(path + "obis.rdf"));

		// Create an empty in-memory model and populate it from the graph
		Model model = ModelFactory.createMemModelMaker().createDefaultModel();
		model.read(in,null); // null base URI, since model URIs are absolute
		in.close();

		// Create a new query
		String queryString = 
			"PREFIX obis: <http://web.cs.dal.ca/~hari/pokm/owl/obis.owl#> " +
			"SELECT ?drs ?id " +
			"WHERE {" +
			"      ?drs obis:snamestring ?id . " +
			//"      ?drs obis:sname ?depth . " +
						"      }";

		String queryToGetSpecies = 
				"PREFIX obis: <http://web.cs.dal.ca/~hari/pokm/owl/obis.owl#> " +
				"SELECT ?sname_id " +
				"WHERE { " +
				"?drs ?sname_id ?sname  ." +
				"?drs a <obis:drs> . " +
				"?sname_id a <obis:sname_id> . " +
				"?sname a <obis:sname> . " +
				" }";
		
		Query query = QueryFactory.create(queryToGetSpecies);

		System.out.println(queryToGetSpecies);
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		// Output query results	
		ResultSetFormatter.out(System.out, results, query);

		// Important - free up resources used running the query
		qe.close();

	}
	
	public Vector<Species> getSpeciesList() throws IOException{
		
		species = new Vector<Species>();
//		species.add(new Species("Select one from list", "none"));
		
		// Open the bloggers RDF graph from the filesystem
				InputStream in = new FileInputStream(new File(path + "obis.rdf"));

				// Create an empty in-memory model and populate it from the graph
				model = ModelFactory.createMemModelMaker().createDefaultModel();
				model.read(in,null); // null base URI, since model URIs are absolute
				in.close();

				// Create a new query
				String queryString = 
					"PREFIX obis: <http://web.cs.dal.ca/~hari/pokm/owl/obis.owl#> " +
					"SELECT ?speciesName ?id " +
					"WHERE {" +
					"      ?sname obis:snamestring ?speciesName . " +
					"      ?sname obis:id_sname ?id . " +
					"      }";
				
				Query query = QueryFactory.create(queryString);

				// Execute the query and obtain results
				QueryExecution qe = QueryExecutionFactory.create(query, model);
				ResultSet rs = qe.execSelect();

				// Output query results on console	
				//ResultSetFormatter.out(System.out, results, query);

				while(rs.hasNext()){

					 QuerySolution binding = rs.nextSolution();
					 String sname = binding.get("speciesName").toString();
					 String id = binding.get("id").toString();
				
					 species.add(new Species(sname, id));
					 //System.out.println(binding.get("speciesName")); 
					}

		//Sorting Vector using Java 8 Lambda Expressions
		species.sort((s1,s2) -> s1.getSname().compareTo(s2.getSname()));		 
		species.add(0, new Species("Select one from list", "none"));
		
		// Important - free up resources used running the query
		qe.close();
		return species;

	}
	
	
	
}
