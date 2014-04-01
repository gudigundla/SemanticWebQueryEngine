package com.gudigundla.MACS;

import java.io.IOException;

import com.gudigundla.jena.*;
import com.gudigundla.postgres.JDBCHelper;

public class MainCLass {

	public static void main(String[] args) throws IOException {
		
		System.out.println("It all starts here..");
		
	LearningJena test = new	LearningJena();
	JDBCHelper test1 = new JDBCHelper();	
	RdfGenerator test2 = new RdfGenerator();
	learningSPARQL test3 = new learningSPARQL();
	
	//test.createRDF(); 
	//test.readRDF();
	//test.createIndividual();
	//test1.connectingPostgresObis();
	test2.generateRDF();
	//test3.simpleSPARQL();
	//test3.getSpeciesList();
	}
}