package com.uvce.cse.searchiot.encodeont.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.Properties;

/**
 * Configuration parameters of the application.
 * <br\>
 * Provides methods to read and write the configuration parameters.
 * 
 * @author Santosh Pattar
 * @author Dwaraka Kulkarni
 * @version 1.0
 */
public class BasicConfig {
	
	/** Path of the configuration file. */
	private final String configPath = "./src/main/resources/config.properties";
	
	/** Path of the Default OWL file. */
	//private final String defaultOwlPath = "./src/main/resources/owlfiles/pizza.owl";
	private final String defaultOwlPath = "";

	/** File Type of the Default OWL file. */
	//private final String defaultOwlType = "RDF/XML";
	private final String defaultOwlType = "";
	
	/** Holds the object of the com.uvce.cse.searchiot.encodeont.config.properties file. */
	File configFile;
	
	/** Map of key, value pair for the configuration property. */
	private HashMap<String, String> configProperties;

	/** 
	 * Constructor to initialize the default configuration properties.
	 */
	public BasicConfig() {
		
		this.configFile = new File(this.configPath);
		
		this.configProperties = new HashMap<String, String>();
		
		this.setConfig("owl.filepath", this.defaultOwlPath);
		this.setConfig("owl.filetype", this.defaultOwlType);
	}

	/**
	 * Sets the configuration property.
	 * 
	 * @param key name of the configuration property.
	 * @param value the value of the configuration property.
	 */	
	public void setConfig(String key, String value) {
		FileReader reader = null;
		FileWriter writer = null;
		
		try {			
			reader = new FileReader(this.configFile);
			
			Properties props = new Properties();
			props.load(reader);
			props.setProperty(key, value);
			
			writer = new FileWriter(this.configFile);
			props.store(writer, "settings");
			
			this.configProperties.put(key, value);
			
		} catch (FileNotFoundException fex) {
			System.out.println("No such file found\n");
		} catch (IOException ioex) {
			System.out.println("IOException \n");
		} finally {
			try {
				writer.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * Gets the value of the configuration property.
	 * 
	 * @return the value of the configuration property.
	 */
	public String getConfig(String key) {
		
		String value = "";
		
		try {
			FileReader reader = new FileReader(this.configFile);
			
			Properties props = new Properties();
			props.load(reader);
			value = props.getProperty(key);
			
			reader.close();
		} catch (FileNotFoundException fex) {
			System.out.println("No such file found\n");
		} catch (IOException ioex) {
			System.out.println("IOException \n");
		}

		return value;
	}
}