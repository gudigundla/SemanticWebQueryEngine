package com.gudigundla.jena;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.gudigundla.postgres.JDBCHelper;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;

public class RdfGenerator {

	Model model ;
	OntModel ontModel;
	InfModel infModel;
	String ns = "http://web.cs.dal.ca/~hari/pokm/owl/obis.owl#";
	String path = "files\\RDF\\";
	private String ontologyPath = "files\\OWL\\";
	
	public RdfGenerator() throws IOException{ 
		super();
		createModel(selectFile());
//		createModel();
	}
	
	public void generateRDF(){
	Connection conn = new JDBCHelper().getConnection();
		if(conn!=null) {
			try{
	// Creating Statement for query execution
	Statement stmt = conn.createStatement();
	// creating Query String
	String query = "select  * from obis.drs order by random() LIMIT 15";
	
	// excecuting query
	ResultSet rs = stmt.executeQuery(query);
	ResultSetMetaData md = rs.getMetaData();
	while (rs.next()) {
	Resource drs      = model.createResource(ns+"drs");
	Resource resource = model.createResource(ns+"resource");	
	Resource sname    = model.createResource(ns+"sname");
	
	Resource drsInstance = model.createResource(ns+"drs" +rs.getInt("id"), drs);

	int resource_id = rs.getInt("resource_id");
	System.out.println(resource_id);
	Resource resourceInstance = model.createResource(ns+"resource"+resource_id, resource);
	
	Statement stmt1 = conn.createStatement();
	String query1 = "SELECT * FROM obis.resources WHERE id='" +resource_id + "'" ;
	ResultSet rs1 = stmt1.executeQuery(query1);
	ResultSetMetaData md1 = rs1.getMetaData();
	rs1.next();
	for (int i = 1; i <= md1.getColumnCount(); i++) {
		if(i==1) {
			Property columnName = model.createProperty(ns+"id_resource"); 
			resourceInstance.addProperty(columnName,rs1.getString("id") );
		}
		
		if(i!= 1 ) {
	String col_name = md1.getColumnName(i).trim();
	//System.out.println(col_name+ "  " + i);
	
	String value = rs1.getString(col_name);
	if(value == null)
		value = "";
	
	Property columnName = model.createProperty(ns+col_name); 
	resourceInstance.addProperty(columnName,value );
		}
	}

	int sname_id = rs.getInt("sname_id");
	Resource snameInstance = model.createResource(ns+"sname"+sname_id, sname);
	
	Statement stmt2 = conn.createStatement();
	String query2 = "SELECT * FROM obis.snames WHERE id='" +sname_id + "'" ;
	ResultSet rs2 = stmt2.executeQuery(query2);
	ResultSetMetaData md2 = rs2.getMetaData();
	rs2.next();
	for (int i = 1; i <= md2.getColumnCount(); i++) {
		if(i==1) {
			Property columnName = model.createProperty(ns+"id_sname"); 
			snameInstance.addProperty(columnName,rs2.getString("id") );
		}
		if(i==2) {
			Property columnName = model.createProperty(ns+"snamestring"); 
			snameInstance.addProperty(columnName,rs2.getString("sname") );
		}
		if(i>2) {
	String col_name = md2.getColumnName(i).trim();
	//System.out.println(col_name+ "  " + i);
	
	String value = rs2.getString(col_name);
	if(value == null)
		value = "";
	
	Property columnName = model.createProperty(ns+col_name); 
	snameInstance.addProperty(columnName,value );
		}
	}
	
	Property resourceProperty = model.createProperty(ns+"resource_id"); 
	drsInstance.addProperty(resourceProperty, resourceInstance);
	
	Property snameProperty = model.createProperty(ns+"sname_id"); 
	drsInstance.addProperty(snameProperty, snameInstance);

	
	for (int i = 1; i <= md.getColumnCount(); i++) {
		if(i!= 2 && 1!=3 ) {
	String col_name = md.getColumnName(i).trim();
	//System.out.println(col_name+ "  " + i);
	
	String value = rs.getString(col_name);
	if(value == null)
		value = "";
	
	Property columnName = model.createProperty(ns+col_name); 
	drsInstance.addProperty(columnName,value );
		}
	}
			
} //while
}
catch(Exception e){
	System.out.println(e.toString());			

}
		}
		
		model.setNsPrefix("obis", ns);
		
		serializeModelToTextFile(path +"obis.rdf");
		
	}// method generateRDF
	
	public void updateModel(Species parent, Species child) {
		
		//model = new learningSPARQL().model;
		System.out.println(ns+ "sname" +parent.getId());
		Resource parentSpecies = model.getResource(ns+ "sname" +parent.getId());
		Resource childSpecies = model.getResource(ns+ "sname" +child.getId());
		
		Property subSpeciesOfProperty = model.createProperty(ns+"subSpeciesOf"); 
		childSpecies.addProperty(subSpeciesOfProperty, parentSpecies);
		
		System.out.println("model updated..");
		//serializeModelToTextFile("obis_with_species_hierarchy.rdf");
	}
	
	private void createModel() throws IOException{
		InputStream in = new FileInputStream(new File(path + "obis.rdf"));
		// Create an empty in-memory model and populate it from the graph
		model = ModelFactory.createMemModelMaker().createDefaultModel();
		//ModelFactory.c	
		model.read(in,null); // null base URI, since model URIs are absolute
		in.close();
		System.out.println("new model created..");
		//System.out.println(selectFile());
		createInferredModel();
	}
	
	private void createModel(String path) throws IOException{
		InputStream in = new FileInputStream(new File(path));
		// Create an empty in-memory model and populate it from the graph
		model = ModelFactory.createMemModelMaker().createDefaultModel();
		model.read(in,null); // null base URI, since model URIs are absolute
		in.close();
		System.out.println("new model created from " + path);
		//createInferredModel();
	}
	
	private void createOntology() {
		ontModel = ModelFactory.createOntologyModel();
	    InputStream in = FileManager.get().open(ontologyPath  + "obis.owl");
	    if (in == null) {
	        throw new IllegalArgumentException( "File: " + ontologyPath + "obis.owl not found");
	    }
	    ontModel.read(in, "");
	    System.out.println("ont model created..");
	}
	
	public void createInferredModel() {
		createOntology();
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
	    reasoner = reasoner.bindSchema(ontModel);
	    infModel = ModelFactory.createInfModel(reasoner,model);
	    System.out.println("inferred model created..");
	    serializeModelToTextFile(path + "inferedModel.rdf");
	}
	
	private String selectFile() {
		JFileChooser chooser = new JFileChooser(path);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("RDF Files", "rdf");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Select a file to load the model..");
		int returnValue = chooser.showOpenDialog(null);
		try {
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				return chooser.getSelectedFile().getAbsolutePath();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
				return null;
		}
		return null;
	}

	
	public void saveFile() {
		JFileChooser chooser = new JFileChooser(path);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("RDF Files", "rdf");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Save the model..");
		int returnValue = chooser.showSaveDialog(null);
		try {
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				serializeModelToTextFile(chooser.getSelectedFile().getAbsolutePath());
				}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void serializeModelToTextFile(String fileName ){
		//writing RDF to file obis.rdf
				OutputStream out = null;
				try {
					out = new FileOutputStream(fileName);
				} catch (FileNotFoundException e) {
					System.out.println("File problem !!" );
					e.printStackTrace();
				}
				model.write(out, "RDF/XML-ABBREV");
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("file saved at " + fileName );
}
	
	public com.hp.hpl.jena.query.ResultSet getDrsFromSpecies(Species species){

	String updatedQueryString = 
			"PREFIX obis: <http://web.cs.dal.ca/~hari/pokm/owl/obis.owl#> " +
			"SELECT ?drs ?latitude ?longitude ?date_collected ?sname " +
			"WHERE {" +
						"{" +
			"      ?drs obis:sname_id  <" + ns + "sname"+ species.getId() + ">  . " +
			"<" + ns + "sname"+ species.getId() + ">   obis:snamestring ?sname . " +
			"      ?drs obis:latitude  ?latitude . " + 
			"      ?drs obis:longitude  ?longitude . " +
			"      ?drs obis:datecollected  ?date_collected . " +
			"      		 }"
			+ "    UNION  " +
					  " { " +
			" ?child	obis:subSpeciesOf   <" + ns + "sname"+ species.getId() + "> . " +
			" ?child obis:snamestring ?sname . " +
			"      ?drs obis:sname_id  ?child  . " +
			"      ?drs obis:latitude  ?latitude . "    +
			"      ?drs obis:longitude  ?longitude . " +
			"      ?drs obis:datecollected  ?date_collected . " +
//			"      ?drs obis:latitude  ?latitude   FILTER(?latitude > 30.0 ) "    +
			"      	    } " +
			"      }";
	
	
		Query query = QueryFactory.create(updatedQueryString);

//		 Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, infModel);
		com.hp.hpl.jena.query.ResultSet rs = qe.execSelect();

//		 Output query results on console	
//		ResultSetFormatter.out(System.out, rs, query);


//		 Important - free up resources used running the query
//				qe.close();
		
//		testingModel();
		
		return rs;
	}

	private void testingModel() {
		String queryString = 
				"PREFIX obis: <http://web.cs.dal.ca/~hari/pokm/owl/obis.owl#> " +
				"SELECT ?children ?sname  " +
				"WHERE { " +
				//"<"+ns + "sname275634>	?relation <" + ns + "sname385449> "+  " . " +
				" ?children	obis:subSpeciesOf   obis:sname385449 . " +
				" ?children obis:snamestring ?sname . " +
				"      }";
		System.out.println(queryString);	
		
			Query query = QueryFactory.create(queryString);

			// Execute the query and obtain results
			QueryExecution qe = QueryExecutionFactory.create(query, infModel);
			com.hp.hpl.jena.query.ResultSet rs = qe.execSelect();

			// Output query results on console	
			ResultSetFormatter.out(System.out, rs, query);
				
	}
	
}// class