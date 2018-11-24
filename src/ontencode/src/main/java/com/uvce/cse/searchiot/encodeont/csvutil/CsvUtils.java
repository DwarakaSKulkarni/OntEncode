package com.uvce.cse.searchiot.encodeont.csvutil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * CSV utilites.
 * <br/>
 * Provides methods to write data to a CSV file.
 * 
 * @author Santosh Pattar
 * @author Dwaraka Kulkarni
 * @version 1.0
 */
public class CsvUtils {

	/** Path of the CSV directory. */
	private final String csvPathDir = "./src/main/resources/csvfiles/";

	/** Path of the CSV file. */
	private String csvPath;
	
	/**
	 * Constructor to initialize the members with default values.
	 */
	public CsvUtils() {
		File inDir = new File(this.csvPathDir);
		inDir.mkdir();
	}
	
	/**
	 * Constructor to initialize the path for a given CSV file.
	 */
	public CsvUtils(String path) {
		this.csvPath = path;
	}
	
	/**
	 * Gets the path of the CSV directory.
	 * 
	 * @return the CSV directory's path.
	 */
	public String getCsvPathDir() {
		return this.csvPathDir;
	}
	
	/**
	 * Gets the path of the CSV file.
	 * 
	 * @return the CSV file's path.
	 */
	public String getCsvPath() {
		return this.csvPath;
	}
	
	/**
	 * Sets the path of the CSV file.
	 * 
	 * @return the CSV file's path.
	 */
	public void setCsvPath(String cPath) {
		this.csvPath = cPath;
	}

	/**
	 * Reads from Map and Writes the key,value pair to the CSV file.
	 * 
	 * @param encodedConcepts the map of encoded concepts.
	 * @param filename path of the file where encoded concepts will be written.
	 */
	public void writeIntoCSVFile(Map<String, Integer> encodedConcepts) {

		CSVPrinter csvPrinter;
		int individualCode;

		try {
			
			FileWriter fileWriter = new FileWriter(this.csvPath);
			
			csvPrinter = new CSVPrinter(
									fileWriter, 
									CSVFormat.DEFAULT
							);

			for (String individual : encodedConcepts.keySet()) {
				individualCode = encodedConcepts.get(individual);
				csvPrinter.printRecord(individual, individualCode);
			}

			fileWriter.flush();
			fileWriter.close();
			csvPrinter.close();     
			
		} catch(IOException e) {
			System.out.println(e);
		}

		System.out.println("Encoded list written to CSV file at path: " + this.csvPath);
	}
	
	/**
	 * Reads from string list and Writes them to the CSV file.
	 * 
	 * @param data the list strings to be written.
	 */
	public void writeIntoCSVFile(List<String> data) {

		try {
			
			BufferedWriter br = new BufferedWriter(new FileWriter(this.csvPath, true));
			StringBuilder sb = new StringBuilder();

			for (String element : data) {
			 sb.append(element);
			 sb.append(",");
			}
			
			sb.append("\n");

			br.write(sb.toString());
			br.close();  
			
		} catch(IOException e) {
			System.out.println(e);
		}

		System.out.println("Performance result written to CSV file at path: " + this.csvPath);
	}
}