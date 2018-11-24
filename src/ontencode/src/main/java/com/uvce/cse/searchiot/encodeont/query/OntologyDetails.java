package com.uvce.cse.searchiot.encodeont.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.RDFNode;

import com.uvce.cse.searchiot.encodeont.config.BasicConfig;
import com.uvce.cse.searchiot.encodeont.util.JenaUtils;

/**
 * This class is used to extract information from the ontology.
 * <br/>
 * Semantic-Knowledge in the ontology is extracted into the CSV files. Classes
 * are encoded with a unique number, based on the hierarchy.
 * 
 * @author Santosh Pattar
 * @author Dwaraka Kulkarni
 * @version 3.0
 */
public class OntologyDetails extends JenaUtils {

	/** Holds the answer from a SPARQL query */
	private QuerySolution solution;

	/** A RDF node to hold the value */
	private RDFNode node;

	/**
	 * A dictionary of key,value pair to hold a class and its depth in the ontology.
	 */
	private Map<String, Integer> depthofClass;

	/**
	 * Constructor to initialize with the default parameters.
	 */
	protected OntologyDetails() {
		super();
	}

	/**
	 * Constructor to initialize parameters for a given OWL file.
	 * 
	 * @param owlPath the path of the OWL file.
	 */
	protected OntologyDetails(String owlPath) {
		super(owlPath);
	}
	
	/**
	 * Constructor to initialize parameters for set of given configurations.
	 * 
	 * @param owlPath the path of the OWL file.
	 */
	protected OntologyDetails(BasicConfig bConfig) {
		super(bConfig);
	}

	/**
	 * Gets the dictionary map of class and its depth in the ontology.
	 * 
	 * @return the dictionary map.
	 */
	protected Map<String, Integer> getDepthofClass() {
		return depthofClass;
	}

	/**
	 * Sets the dictionary map of class and its depth in the ontology.
	 * 
	 * @param depthofClass the dictionary map.
	 */
	protected void setDepthofClass(Map<String, Integer> depthofClass) {
		this.depthofClass = depthofClass;
	}

	/**
	 * Finds the depth of the longest inherited branch in a given ontology.
	 * 
	 * @return depth number of classes in the longest inheritance chain.
	 */
	public int getMaximumDepthofOntology() {

		String sQuery = "SELECT (max(?depth) as ?maxdepth)" 
				+ "WHERE {"
				+ "		SELECT ?class (count(?mid)-1 as ?depth) {" 
				+ "		{" 
				+ "    		SELECT distinct ?root {"
				+ "      		?root a owl:Class ." 
				+ "      		FILTER NOT EXISTS {"
				+ "        			?root rdfs:subClassOf ?superroot ."
				+ "        			FILTER ( ?root != ?superroot )" 
				+ "      		}" 
				+ "    		}" 
				+ "		}"
				+ "  		?class rdfs:subClassOf* ?mid ." 
				+ "  		?mid rdfs:subClassOf* ?root ." 
				+ "		}"
				+ "	GROUP BY ?class" 
				+ "	ORDER BY ?depth" 
				+ "}";

		this.setQResult(this.runSparqlQuery(sQuery));

		int depth = 0;
		while (this.getQResult().hasNext()) {
			this.solution = this.getQResult().next();
			this.node = this.solution.get("maxdepth");
			depth = this.node.asLiteral().getInt();
		}

		return depth;
	}

	/**
	 * Finds the depth of each class in the ontology.
	 * 
	 * @return depth level of each class in the ontology.
	 */
	public Map<String, Integer> getDepthofClasses() {

		String classname;
		RDFNode node1;
		int depth;

		String sQuery = " SELECT ?class (count(?mid)-1 as ?depth) {" 
				+ "  {" 
				+ "    SELECT distinct ?root {"
				+ "      ?root a owl:Class" 
				+ "      FILTER NOT EXISTS {" 
				+ "        ?root rdfs:subClassOf ?superroot ."
				+ "        FILTER ( ?root != ?superroot )" 
				+ "      }" 
				+ "    }" 
				+ "  }"
				+ "  ?class rdfs:subClassOf* ?mid ." 
				+ "  ?mid rdfs:subClassOf* ?root ." + "}" 
				+ "GROUP BY ?class "
				+ "ORDER BY ?depth ";

		this.setQResult(this.runSparqlQuery(sQuery));
		while (this.getQResult().hasNext()) {
			this.solution = this.getQResult().next();
			this.node = this.solution.get("class");
			classname = this.node.toString().replace(this.getOntNS(), "");
			node1 = this.solution.get("depth");
			depth = node1.asLiteral().getInt();
			this.depthofClass.put(classname, depth);
		}

		return this.depthofClass;
	}

	/**
	 * Generates the list of root classes for the given ontology.
	 * 
	 * @return list of root classes.
	 */
	public List<String> getAllRootClasses() {

		List<String> rootclass = new ArrayList<String>();
		String root;

		String sQuery = "SELECT DISTINCT ?root {" 
				+ "      ?root a owl:Class ." 
				+ "      FILTER NOT EXISTS {"
				+ "        ?root rdfs:subClassOf ?superroot ." 
				+ "        FILTER ( ?root != ?superroot )" 
				+ "      }"
				+ "}";

		this.setQResult(this.runSparqlQuery(sQuery));

		while (this.getQResult().hasNext()) {
			this.solution = this.getQResult().next();
			this.node = this.solution.get("root");
			root = this.node.toString().replace(this.getOntNS(), "");
			rootclass.add(new String(root));
		}

		return rootclass;
	}

	/**
	 * Generates the number of child classes for a given class.
	 * 
	 * @param class the parent class.
	 * @return the number of subclasses for the given class.
	 */
	public int getNumberofChildClasses(String root) {

		int count = 0, subcount;
		RDFNode node2;
		String rootclass;

		String sQuery = "SELECT DISTINCT ?class (count(?subclass) as ?subcount)" 
				+ "WHERE {"
				+ "		?subclass rdfs:subClassOf ?class ." 
				+ "}" 
				+ "GROUP BY ?class";

		this.setQResult(this.runSparqlQuery(sQuery));

		while (this.getQResult().hasNext()) {
			this.solution = this.getQResult().next();
			this.node = this.solution.get("class");
			rootclass = this.node.toString().replace(this.getOntNS(), "");
			node2 = this.solution.get("subcount");
			subcount = node2.asLiteral().getInt();
			if (root.equalsIgnoreCase(rootclass)) {
				count = subcount;
			}
		}

		return count;
	}

	/**
	 * Generates the list of child classes for a given class.
	 * 
	 * @param class the parent class.
	 * @return the list of subclasses for the given class.
	 */
	public List<String> getSubClasses(String root) {

		RDFNode node2;
		String rootclass, subclass;
		List<String> subClassList = new ArrayList<String>();

		String sQuery = "SELECT ?class ?subclass " 
				+ "WHERE {" 
				+ "		?subclass rdfs:subClassOf ?class" 
				+ "}";

		this.setQResult(this.runSparqlQuery(sQuery));

		while (this.getQResult().hasNext()) {
			this.solution = this.getQResult().next();
			this.node = this.solution.get("class");
			rootclass = this.node.toString().replace(this.getOntNS(), "");
			node2 = this.solution.get("subclass");
			subclass = node2.toString().replace(this.getOntNS(), "");
			if (root.equalsIgnoreCase(rootclass)) {
				subClassList.add(subclass);
			}
		}

		return subClassList;
	}

	/**
	 * Generates the number of classes in the ontology.
	 * 
	 * @return number of classes.
	 */
	public int getNumberofClasses() {

		int count = 0;
		String sQuery = "SELECT (count(DISTINCT ?class) as ?numofclass)" 
				+ "WHERE {" 
				+ "		?class a owl:Class ."
				+ "}";

		this.setQResult(this.runSparqlQuery(sQuery));

		while (this.getQResult().hasNext()) {
			this.solution = this.getQResult().next();
			this.node = this.solution.get("numofclass");
			count = this.node.asLiteral().getInt();
		}

		return count;
	}

	/**
	 * Generates the list of distinct classes in the ontology.
	 * 
	 * @return the list of distinct classes.
	 */
	public List<String> distinctClasses() {

		String dClass;
		List<String> classList = new ArrayList<String>();

		String sQuery = "SELECT DISTINCT ?class " 
				+ "WHERE {" 
				+ "		?class a owl:Class ." 
				+ "}";

		this.setQResult(this.runSparqlQuery(sQuery));

		while (this.getQResult().hasNext()) {
			this.solution = this.getQResult().next();
			this.node = this.solution.get("class");
			dClass = this.node.toString().replace(this.getOntNS(), "");
			classList.add(dClass);
		}

		return classList;
	}

	/**
	 * Generates the list of services in the ontology.
	 * 
	 * @return the list of services.
	 */
	public List<String> listOfServices() {

		String service;
		List<String> serviceList = new ArrayList<String>();

		String sQuery = "SELECT ?service " 
				+ "WHERE {" 
				+ "	?subclass rdfs:subClassOf* as:Service."
				+ "	?service rdf:type ?subclass ." 
				+ "}";

		this.setQResult(this.runSparqlQuery(sQuery));

		while (this.getQResult().hasNext()) {
			this.solution = this.getQResult().next();
			this.node = this.solution.get("service");
			service = this.node.toString().replace(this.getOntNS(), "");
			serviceList.add(service);
		}

		return serviceList;
	}

	/**
	 * Generates the list of labels of the service class individuals in the
	 * ontology.
	 * 
	 * @return the list of services.
	 */
	public List<String> listLabelsOfServices() {

		String service;
		List<String> serviceList = new ArrayList<String>();

		String sQuery = "SELECT  ?object " 
				+ "WHERE {" 
				+ "?subclass rdfs:subClassOf* as:Service."
				+ "?service rdf:type ?subclass." 
				+ "?service rdfs:label ?object." 
				+ "}";

		this.setQResult(this.runSparqlQuery(sQuery));

		while (this.getQResult().hasNext()) {
			this.solution = this.getQResult().next();
			this.node = this.solution.get("object");
			service = this.node.toString();
			serviceList.add(service);
		}

		return serviceList;
	}

	/**
	 * Generates the list of labels of the service class's subclasses in the
	 * ontology.
	 * 
	 * @return the list of Service type classes
	 */
	public List<String> listLabelsOfServiceSubClasses() {

		String service;
		List<String> serviceList = new ArrayList<String>();

		String sQuery = "SELECT DISTINCT ?object" 
				+ "	WHERE {" 
				+ "	?subclass rdfs:subClassOf* as:Service."
				+ "	?subclass rdf:type owl:Class. " 
				+ "	?subclass rdfs:label ?object." 
				+ "}";

		this.setQResult(this.runSparqlQuery(sQuery));

		while (this.getQResult().hasNext()) {
			this.solution = this.getQResult().next();
			this.node = this.solution.get("object");
			service = this.node.toString();
			serviceList.add(service);
		}

		return serviceList;
	}
}