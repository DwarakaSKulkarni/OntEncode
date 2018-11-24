package com.uvce.cse.searchiot.encodeont.encode;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.util.iterator.ExtendedIterator;

import com.uvce.cse.searchiot.encodeont.config.BasicConfig;
import com.uvce.cse.searchiot.encodeont.query.OntologyDetails;

/**
 * Encodes a given ontology based on prime number based encoding scheme.
 * 
 * @author Santosh Pattar
 * @author Dwaraka Kulkarni
 * @version 1.0
 */
public class EncodeOntology extends OntologyDetails {

	/** Holds the encoding number of the classes in the ontology. */
	private Map<String, Integer> encodedClasses;

	/** Holds the encoding number of the data properties in the ontology. */
	private Map<String, Integer> encodedDataProperties;

	/** Holds the encoding number of the object properties in the ontology. */
	private Map<String, Integer> encodedObjectProperties;

	/** Holds the encoding number of the individuals in the ontology. */
	private Map<String, Integer> encodedIndividuals;

	/** Holds the encoding number of all the concepts in the ontology. */
	private Map<String, Integer> encodedConcepts;

	/**
	 * Constructor to initialize the members with default values.
	 * <br/>
	 * Calls the encode ontology method.
	 */
	public EncodeOntology() {

		super();

		this.setDepthofClass(new Hashtable<String, Integer>());
		this.encodedClasses = new Hashtable<String, Integer>();
		this.setEncodedDataProperties(new Hashtable<String, Integer>());
		this.setEncodedObjectProperties(new Hashtable<String, Integer>());
		this.encodedIndividuals = new Hashtable<String, Integer>();
		this.encodedConcepts = new Hashtable<String, Integer>();

		this.encodeOntology();
	}

	/**
	 * Constructor to initialize the Jena model for a given ontology file.
	 * <br/>
	 * Calls the encode ontology method.
	 */
	public EncodeOntology(String owlPath) {

		super(owlPath);

		this.setDepthofClass(new Hashtable<String, Integer>());
		this.encodedClasses = new Hashtable<String, Integer>();
		this.setEncodedDataProperties(new Hashtable<String, Integer>());
		this.setEncodedObjectProperties(new Hashtable<String, Integer>());
		this.encodedIndividuals = new Hashtable<String, Integer>();
		this.encodedConcepts = new Hashtable<String, Integer>();

		this.encodeOntology();

	}
	
	/**
	 * Constructor to initialize the Jena model for given configuration parameters.
	 * <br/>
	 * Calls the encode ontology method.
	 */
	public EncodeOntology(BasicConfig bConfig) {

		super(bConfig);

		this.setDepthofClass(new Hashtable<String, Integer>());
		this.encodedClasses = new Hashtable<String, Integer>();
		this.setEncodedDataProperties(new Hashtable<String, Integer>());
		this.setEncodedObjectProperties(new Hashtable<String, Integer>());
		this.encodedIndividuals = new Hashtable<String, Integer>();
		this.encodedConcepts = new Hashtable<String, Integer>();

		this.encodeOntology();

	}

	/**
	 * Gets the code assigned to all the object properties in the ontology.
	 * 
	 * @return the list of object properties and their codes.
	 */
	public Map<String, Integer> getEncodedObjectProperties() {
		return encodedObjectProperties;
	}

	/**
	 * Sets the encoded numbers of object properties.
	 * 
	 * @param encodedObjectProperties encoded numbers to be set.
	 */
	public void setEncodedObjectProperties(Map<String, Integer> encodedObjectProperties) {
		this.encodedObjectProperties = encodedObjectProperties;
	}

	/**
	 * Gets the code assigned to all the data properties in the ontology.
	 * 
	 * @return the list of data properties and their codes.
	 */
	public Map<String, Integer> getEncodedDataProperties() {
		return encodedDataProperties;
	}

	/**
	 * Sets the encoded numbers of object properties.
	 * 
	 * @param encodedDataProperties encoded numbers of object properties to be set.
	 */
	public void setEncodedDataProperties(Map<String, Integer> encodedDataProperties) {
		this.encodedDataProperties = encodedDataProperties;
	}

	/**
	 * Gets the code assigned to all the classes in the ontology.
	 * 
	 * @return the list of classes and their code values.
	 */
	public Map<String, Integer> getEncodedClasses() {
		return this.encodedClasses;
	}

	/**
	 * Gets the code assigned to all the individuals of classes in the ontology.
	 * 
	 * @return the list of individuals of classes and their codes.
	 */
	public Map<String, Integer> getEncodedIndividuals() {
		return this.encodedIndividuals;
	}

	/**
	 * Gets the code assigned to all the individuals of classes in the ontology.
	 * 
	 * @return the list of individuals of classes and their codes.
	 */
	public Map<String, Integer> getEncodedConcepts() {
		return this.encodedConcepts;
	}

	/**
	 * Generates the list of prime numbers.
	 * 
	 * @param num upper bound for generation.
	 *            
	 * @return the generated list in array form.
	 */
	public List<Integer> getAllPrimeNumbers(int num) {

		List<Integer> primeList = new ArrayList<Integer>();
		int p;

		if (num > 2) {
			for (int i = 2, count = 0; count < num; i++) {
				p = 0;
				for (int j = 2; j < i; j++) {
					if (i % j == 0)
						p++;
				}

				if (p == 0) {
					primeList.add(i);
					count++;
				}
			}
		} else if (num == 2) {
			primeList.add(2);
		} else {
			System.err.println("No prime number less than 2");
			System.exit(2);
		}

		return primeList;
	}

	/**
	 * Multiplies the encodedNumbers of subclasses with its parent class's prime code.
	 * 
	 * @param distinctClass a distinct class in the ontology.
	 * @return none
	 */
	public void multiplyEncodedNumbers(String distinctClass) {

		int code1, code2, newCode;
		List<String> dSubClasses = new ArrayList<String>();

		code1 = this.encodedClasses.get(distinctClass);
		dSubClasses = this.getSubClasses(distinctClass);
		if (!dSubClasses.isEmpty()) {
			for (String dSub : dSubClasses) {
				code2 = this.encodedClasses.get(dSub);
				newCode = code1 * code2;
				this.encodedClasses.put(dSub, newCode);
				this.multiplyEncodedNumbers(dSub);
			}
		}
	}

	/**
	 * Encodes the classes of the ontology with a prime number.
	 * 
	 * @return encoded list of classes.
	 */
	public Map<String, Integer> encodeHierarchy() {

		String topClass;
		int topCount, count, i = 0;
		List<String> subClasses = new ArrayList<String>();
		List<String> desClasses = new ArrayList<String>();
		List<String> dClasses = new ArrayList<String>();

		int numOfClass = this.getNumberofClasses();
		List<Integer> primeNumbers = this.getAllPrimeNumbers(numOfClass);

		List<String> rootClasses = this.getAllRootClasses();

		while (rootClasses.size() > 0) {

			topClass = rootClasses.get(0);
			topCount = this.getNumberofChildClasses(topClass);

			for (String root : rootClasses) {
				count = this.getNumberofChildClasses(root);
				if (count > topCount) {
					topClass = root;
					break;
				}
			}

			if (this.encodedClasses.get(topClass) == null) {
				this.encodedClasses.put(topClass, primeNumbers.get(i));
				i = i + 1;
			}
			subClasses = this.getSubClasses(topClass);
			for (String sub : subClasses) {
				if (this.encodedClasses.get(sub) == null) {
					this.encodedClasses.put(sub, primeNumbers.get(i));
					i = i + 1;
				}
				desClasses = this.getSubClasses(sub);
				if (!desClasses.isEmpty()) {
					for (Object des : desClasses) {
						rootClasses.add(des.toString());
					}
				}

			}
			rootClasses.remove(topClass);
		}

		dClasses = this.getAllRootClasses();
		for (String dClass : dClasses) {
			this.multiplyEncodedNumbers(dClass);
		}
		return this.encodedClasses;
	}

	/**
	 * Encodes the classes of the ontology with a prime number.
	 * 
	 * @param rootClasses a list of root classes of the ontology.
	 * 
	 * @return encoded list of classes.
	 */
	public Map<String, Integer> encodeClasses(List<String> rootClasses) {

		System.out.println("Encoding Classes ...");
		
		String topClass;
		int topCount, count, i = 0;
		List<String> subClasses = new ArrayList<String>();
		List<String> desClasses = new ArrayList<String>();
		List<String> dClasses = new ArrayList<String>();

		int numOfClass = this.getNumberofClasses();
		List<Integer> primeNumbers = this.getAllPrimeNumbers(numOfClass);

		while (rootClasses.size() > 0) {

			topClass = rootClasses.get(0);
			topCount = this.getNumberofChildClasses(topClass);

			for (String root : rootClasses) {
				count = this.getNumberofChildClasses(root);
				if (count > topCount) {
					topClass = root;
					break;
				}
			}
			if (this.encodedClasses.get(topClass) == null) {
				this.encodedClasses.put(topClass, primeNumbers.get(i));
				i = i + 1;
			}
			subClasses = this.getSubClasses(topClass);
			for (String sub : subClasses) {
				if (this.encodedClasses.get(sub) == null) {
					this.encodedClasses.put(sub, primeNumbers.get(i));
					i = i + 1;
				}
				desClasses = this.getSubClasses(sub);
				if (!desClasses.isEmpty()) {
					for (Object des : desClasses) {
						rootClasses.add(des.toString());
					}
				}

			}
			rootClasses.remove(topClass);
		}

		dClasses = this.getAllRootClasses();

		for (String dClass : dClasses) {
			this.multiplyEncodedNumbers(dClass);
		}

		return this.encodedClasses;
	}

	/**
	 * Encodes the instances of all classes of the ontology with a prime number.
	 * 
	 * @param individuals a list of individuals with their respective classes.
	 * @return encoded list of individuals
	 */
	public Map<String, Integer> encodeIndividuals(Map<String, String> individuals) {
		
		System.out.println("Encoding Individuals ...");
		
		Map<String, Integer> encodedList = new Hashtable<String, Integer>();
		int numOfDataProps, i = 0, classCode, dataPropCode;
		String domainClass;
		numOfDataProps = individuals.size();
		List<Integer> primeNumbers = this.getAllPrimeNumbers(numOfDataProps);

		for (String dataProp : individuals.keySet()) {
			domainClass = individuals.get(dataProp);
			classCode = this.encodedClasses.get(domainClass);
			dataPropCode = primeNumbers.get(i) * classCode;
			encodedList.put(dataProp, dataPropCode);
			i = i + 1;
		}

		return encodedList;
	}

	/**
	 * Encodes the properties of all classes of the ontology with a prime number.
	 * 
	 * @param ontProps properties of a class.
	 * 
	 * @return encoded list of properties.
	 */
	public Map<String, Integer> encodeProperties(Map<String, List<String>> ontProps) {
		
		System.out.println("Encoding Properites ...");
		
		Map<String, Integer> encodedList = new Hashtable<String, Integer>();
		int numOfDataProps, i = 0, classCode, code, propCode;
		List<String> classList;
		numOfDataProps = ontProps.size();
		List<Integer> primeNumbers = this.getAllPrimeNumbers(numOfDataProps);

		for (String prop : ontProps.keySet()) {
			classCode = 1;
			classList = ontProps.get(prop);
			for (String dom : classList) {
				code = this.encodedClasses.get(dom);
				classCode = classCode * code;
			}
			propCode = primeNumbers.get(i) * classCode;
			encodedList.put(prop, propCode);
			i = i + 1;
		}

		return encodedList;
	}

	/**
	 * Lists the data properties of all classes in the ontology.
	 * 
	 * @return data properties and their respective class.
	 */
	public Map<String, List<String>> getListDataProperties() {

		Map<String, List<String>> classDataProps = new Hashtable<String, List<String>>();
		String dataProp;
		List<String> domainClass;
		OntProperty ontProp;
		ExtendedIterator<DatatypeProperty> dataProps;

		dataProps = this.ontModel.listDatatypeProperties();

		while (dataProps.hasNext()) {
			ontProp = (OntProperty) dataProps.next();
			dataProp = ontProp.toString().replace(this.getOntNS(), "");
			domainClass = this.getDomain(ontProp);
			classDataProps.put(dataProp, domainClass);
		}

		return classDataProps;
	}

	/**
	 * Lists the object properties of all classes in the ontology.
	 * 
	 * @return object properties and their respective class.
	 */
	public Map<String, List<String>> getListObjectProperties() {

		Map<String, List<String>> classObjectProps = new Hashtable<String, List<String>>();
		String objProp;
		OntProperty ontProp;
		ExtendedIterator<ObjectProperty> objectProps;
		List<String> domainClass, rangeClass;

		objectProps = this.ontModel.listObjectProperties();

		while (objectProps.hasNext()) {
			List<String> classList = new ArrayList<String>();
			ontProp = (OntProperty) objectProps.next();
			objProp = ontProp.toString().replace(this.getOntNS(), "");
			domainClass = this.getDomain(ontProp);
			rangeClass = this.getRange(ontProp);
			classList.addAll(domainClass);
			classList.addAll(rangeClass);
			classObjectProps.put(objProp, classList);
		}

		return classObjectProps;
	}

	/**
	 * Lists the individuals of all classes in the ontology.
	 * 
	 * @return individuals and their respective class.
	 */
	public Map<String, String> listIndividuals() {

		Map<String, String> classIndividuals = new Hashtable<String, String>();
		String individual, ontClass;
		ExtendedIterator<Individual> instances;
		Individual indv;

		instances = this.ontModel.listIndividuals();
		while (instances.hasNext()) {
			indv = (Individual) instances.next();
			// System.out.println("indv--"+indv);
			individual = indv.toString().replace(this.getOntNS(), "");
			ontClass = indv.getOntClass().toString().replace(this.getOntNS(), "");
			classIndividuals.put(individual, ontClass);
		}

		return classIndividuals;
	}

	/**
	 * Lists the domain classes of the ontproperty in the ontology.
	 * 
	 * @param property an ontproperty of the ontology class.
	 * 
	 * @return list of domain classes of the property.
	 */
	public List<String> getDomain(OntProperty property) {
		ExtendedIterator<? extends OntResource> domClass;
		String domain;
		List<String> domainList = new ArrayList<String>();

		domClass = property.listDomain();
		while (domClass.hasNext()) {
			domain = domClass.next().toString().replace(this.getOntNS(), "");
			domainList.add(domain);
		}
		return domainList;
	}

	/**
	 * Lists the range classes of the ontproperty in the ontology.
	 * 
	 * @param property an ontproperty of the ontology class.
	 * 
	 * @return list of range classes of the property.
	 */
	public List<String> getRange(OntProperty property) {
		ExtendedIterator<? extends OntResource> rngClass;
		String range, nameSpace;
		OntResource rng;
		List<String> rangeList = new ArrayList<String>();

		rngClass = property.listRange();
		while (rngClass.hasNext()) {
			rng = rngClass.next();
			nameSpace = rng.getNameSpace();
			if (nameSpace != null && nameSpace.equalsIgnoreCase(this.getOntNS())) {
				range = rng.toString().replace(this.getOntNS(), "");
				rangeList.add(range);
			} else
				continue;
		}
		return rangeList;
	}
	
	/**
	 * Encodes the classes,their data and object properties and also individuals of
	 * the ontology.
	 */
	public void encodeOntology() {

		System.out.println("Encoding the ontology ...");
		
		// Map<String, Integer> encodedList = new Hashtable<String, Integer>();
		Map<String, List<String>> concepts = new Hashtable<String, List<String>>();
		Map<String, String> individuals = new Hashtable<String, String>();

		// Classes
		List<String> rootClassList = this.getAllRootClasses();
		if(rootClassList.size() !=0 ) {
			this.encodedClasses = this.encodeClasses(rootClassList);
			this.encodedConcepts.putAll(this.encodedClasses);
		} else {
			System.out.println("No Classes found in the ontology.");
		}

		// DataProperties
		concepts = this.getListDataProperties();
		if(concepts.size() !=0 ) {
			this.setEncodedDataProperties(this.encodeProperties(concepts));
			this.encodedConcepts.putAll(this.getEncodedDataProperties());
		} else {
			System.out.println("No Data Properties found in the ontology.");
		}

		// ObjectProperties
		concepts = this.getListObjectProperties();
		if(concepts.size() !=0 ) {
			this.setEncodedObjectProperties(this.encodeProperties(concepts));
			this.encodedConcepts.putAll(this.getEncodedObjectProperties());
		} else {
			System.out.println("No Object Properties found in the ontology.");
		}

		// Individuals
		individuals = this.listIndividuals();
		if(individuals.size() !=0 ) {
			this.encodedIndividuals = this.encodeIndividuals(individuals);
			this.encodedConcepts.putAll(this.encodedIndividuals);
		} else {
			System.out.println("No Individuals found in the ontology.");
		}		
		
		System.out.println("Encoding Completed !");
	}
}