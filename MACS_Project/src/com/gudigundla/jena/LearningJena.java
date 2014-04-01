package com.gudigundla.jena;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;

public class LearningJena {
	String path = "F:\\Macs Project\\RDF\\";
	
public void createRDF() throws IOException {
		
		String ns = "http://dal.ca#"; 
	
		// create an empty Model
		Model model = ModelFactory.createDefaultModel();
				
		// create the resource
		Resource haree = model.createResource(ns + "haree");
		
		Property roomie = model.createProperty(ns + "roomMate");
		Property employer = model.createProperty(ns+ "job"); 
				
		Resource thandu = model.createResource(ns +"thandu");
		thandu.addProperty(roomie, haree);
		thandu.addProperty(employer, "dalplex1");
		
		Resource wassup =  model.createResource(ns +"wassup",thandu);
		wassup.addProperty(employer, "god knows");
		wassup.addProperty(roomie, haree);
		
		
		model.setNsPrefix("gudi", ns);
		
		
		//model.write(System.out, "RDF/XML-ABBREV");
		//model.write( System.out, FileUtils.langXMLAbbrev );
		
		
		OutputStream out = new FileOutputStream(path + "sample.rdf");
		
		model.write(out, "RDF/XML-ABBREV");
		out.close();
		
	}

public void readRDF() throws IOException {
	
	// create an empty model
	 Model model = ModelFactory.createDefaultModel();

	// use the FileManager to find the input file
	 InputStream in = FileManager.get().open( path + "sample2.rdf" );
	if (in == null) {
	    throw new IllegalArgumentException(
	                                 "File: " + path + " not found");
	}

	// read the RDF/XML file
	model.read(in, null);
	in.close();

	// write it to standard out
	model.write(System.out);
}
	
public void createIndividual() throws FileNotFoundException {

	String ns = "http://web.cs.dal.ca/~hari/pokm/owl/obis.owl#";
	
	OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF );
	
	InputStream in = FileManager.get().open( "F:\\Macs Project\\OWL\\obis.owl" );
	if (in == null) {
	    throw new IllegalArgumentException(
	                                 "File: " + path + " not found");
	}
	
	m.read(in,"");
	
	//OntClass c = m.createClass( ns + "drs" );
	// first way: use a call on OntModel
	//Individual drs1 = m.createIndividual( ns + "drs1", c );

	Resource drs1 = m.createResource(ns + "drs1");
	drs1.addProperty(m.createProperty(ns+ "accuracy") , "value");
		
	OutputStream out = new FileOutputStream("F:\\Macs Project\\OWL\\test.rdf");

	
	m.write(out);

	// second way: use a call on OntClass
	//Individual ind1 = c.createIndividual( ns + "ind1" );

}

	
	
} //class
