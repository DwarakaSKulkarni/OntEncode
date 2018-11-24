package com.uvce.cse.searchiot.encodeont.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.log4j.Logger;

import com.uvce.cse.searchiot.encodeont.config.BasicConfig;

/**
 * Utility class to deal with the Ontology using Jena library.
 * 
 * Reads an RDF/XML OWL file into in-memory model and prints it. Supports
 * querying of the ontology through SPARQL end-points.
 * 
 * @author Santosh Pattar
 * @author Dwaraka Kulkarni
 * @version 3.0
 */
public class JenaUtils {

	/** Holds the relative-path to the RDF/XML OWL file */
	protected String owlPath;

	/** Holds the URI of the OWL file */
	protected String ontNS;
	
	//FIXME: ont namespace is not to be defined manually.
	/** Holds all the global namespaces */
	protected static String prefixNS =  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+ "\n" +
										"PREFIX owl: <http://www.w3.org/2002/07/owl#>" + "\n" +
										"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+ "\n" +
										"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "\n" +
										"PREFIX ";
	
	/** An in-memory Ontology Model in Jena to hold smart-airport ontology */
	protected OntModel ontModel;

	/** Logger system */
	protected static final Logger log = Logger.getLogger("JenaUtils");

	/** A string to hold the SPARQL com.uvce.cse.searchiot.encodeont.query */
	protected String sQuery;

	/** Holds the result of a SPARQL com.uvce.cse.searchiot.encodeont.query */
	public ResultSet result;

	/**
	 * Constructor to initialize the Jena Ontmodel.
	 * 
	 * Default RDF/XML OWL file is read. 
	 * Reads the RDF/XML OWL file into a in-memory Jena model.
	 */
	protected JenaUtils() {
		this.owlPath = "./src/main/resources/owlfiles/finaltest.owl";
		
		this.setOwlPath(owlPath);
		this.readOwlFile();
		this.setOntNS();
		this.setPrefixNS();
	}
	
	/**
	 * Constructor to initialize the Jena Ontmodel using configuration parameters.
	 * <br/>
	 * RDF/XML OWL file from configuration file is read. 
	 * Reads the RDF/XML OWL file into a in-memory Jena model.
	 */
	protected JenaUtils(BasicConfig bConfig) {
		this.owlPath = bConfig.getConfig("owl.filepath");
		
		this.setOwlPath(owlPath);
		this.readOwlFile();
		this.setOntNS();
		this.setPrefixNS();
	}

	/**
	 * Constructor to initialize the Jena Ontmodel with the given OWL file.
	 * <br/>
	 * Reads the RDF/XML OWL file into a in-memory Jena model.
	 */
	public JenaUtils(String owlPath) {
		
		this.setOwlPath(owlPath);
		
		this.readOwlFile();
		this.setOntNS();
		this.setPrefixNS();
	}

	/**
	 * Gets the path of the ontology's OWL file.
	 * 
	 * @return the absolute path of the OWL file.
	 */
	public String getOwlPath() {
		return this.owlPath;
	}

	/**
	 * Sets the path of the ontology's OWL file.
	 * 
	 * @param path the absolute path of the OWL file.
	 */
	protected void setOwlPath(String path) {
		this.owlPath = path;
	}

	/**
	 * Gets the NameSpace of the ontology.
	 * 
	 * @return the NameSpace of the ontology.
	 */
	public String getOntNS() {
		return this.ontNS;
	}

	/**
	 * Sets the NameSpace of the ontology.
	 */
	private void setOntNS() {
		OntClass ontCls = null;
		ExtendedIterator<OntClass> cls = this.ontModel.listClasses();

		while (cls.hasNext()) {
			ontCls = cls.next();
			break;
		}

		String nameSpace = ontCls.getNameSpace();
		this.ontNS = nameSpace;
		
		System.out.println("Ontology namespace is:" + this.ontNS);
	}

	/**
	 * Gets the Jena model.
	 * 
	 * @return the in-memory Jena model.
	 */
	public OntModel getOntModel() {
		return this.ontModel;
	}

	/**
	 * Gets the SPARQL com.uvce.cse.searchiot.encodeont.query.
	 * 
	 * @return the SPARQL com.uvce.cse.searchiot.encodeont.query.
	 */
	public String getsQuery() {
		return this.sQuery;
	}

	/**
	 * Sets the SPARQL com.uvce.cse.searchiot.encodeont.query.
	 * 
	 * @param sQuery the com.uvce.cse.searchiot.encodeont.query to be set.
	 */
	public void setsQuery(String sQuery) {
		this.sQuery = sQuery;
	}

	/**
	 * Sets the SPARQL com.uvce.cse.searchiot.encodeont.query result.
	 * 
	 * @param qResult the com.uvce.cse.searchiot.encodeont.query to be set.
	 */
	protected void setQResult(ResultSet qResult) {
		this.result = qResult;
	}

	/**
	 * Gets the SPARQL com.uvce.cse.searchiot.encodeont.query result.
	 * 
	 * @return the result of a SPARQL com.uvce.cse.searchiot.encodeont.query.
	 */
	public ResultSet getQResult() {
		return this.result;
	}

	/**
	 * Sets the ontology prefix.
	 * 
	 * @param sQuery the com.uvce.cse.searchiot.encodeont.query to be set.
	 */
	private void setPrefixNS() {
		//FIXME: this is not to be done manually.
		JenaUtils.prefixNS = JenaUtils.prefixNS + "as:<" + this.ontNS + ">";
		
		System.out.println("Prefix for the SPARQL queries is:" + JenaUtils.prefixNS);
	}

	/**
	 * Reads an ontology RDF/XML file into the Jena Model.
	 */
	protected void readOwlFile() {

		this.ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

		InputStream iStream;
		try {
			iStream = new FileInputStream(this.getOwlPath());
			this.ontModel.read(iStream, "RDF/XML");
			
			System.out.println("In-memory Jena ontology model created for file at path:" + this.getOwlPath());
		} catch (FileNotFoundException e) {
			System.err.println("RDF/XML OWL file not found at the path: " + this.getOwlPath());
			e.printStackTrace();
			System.exit(1);
		}

	}

	/**
	 * Prints the ontology OWL file on the standard output stream.
	 */
	public void printOwlFile() {
		this.ontModel.write(System.out, "RDF/XML");
	}

	/**
	 * Writes the ontology OWL file to the given path.
	 */
	protected void printOwlFile(String path) {
		OutputStream out;
		try {
			out = new FileOutputStream(path);
			this.ontModel.writeAll(out, "RDF/XML");
		} catch (FileNotFoundException e) {
			System.err.println("RDF/XML OWL file could not be written at the path: " + path);
			e.printStackTrace();
			System.exit(1);
		}

	}

	/**
	 * Executes the given SPARQL com.uvce.cse.searchiot.encodeont.query.
	 * 
	 * @param sQuery the SPARQL com.uvce.cse.searchiot.encodeont.query to be executed.
	 *            
	 * @return results of the com.uvce.cse.searchiot.encodeont.query.
	 */
	public ResultSet runSparqlQuery(String sQuery) {

		sQuery = JenaUtils.prefixNS + sQuery;
		Query query = QueryFactory.create(sQuery);
		QueryExecution qexec = QueryExecutionFactory.create(query, this.ontModel);
		this.result = qexec.execSelect();

		return this.result;
	}
}