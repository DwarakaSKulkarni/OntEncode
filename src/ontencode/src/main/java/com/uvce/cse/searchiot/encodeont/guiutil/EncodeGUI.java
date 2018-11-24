package com.uvce.cse.searchiot.encodeont.guiutil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;

import com.uvce.cse.searchiot.encodeont.config.BasicConfig;
import com.uvce.cse.searchiot.encodeont.csvutil.CsvUtils;
import com.uvce.cse.searchiot.encodeont.encode.EncodeOntology;

/**
 * Swing based GUI for the OntEncode application.
 * 
 * @author Santosh Pattar
 * @author Dwaraka Kulkarni
 * @version 1.0
 */
public class EncodeGUI implements ActionListener {

	/** Main frame of the application. */
	private JFrame mainFrame;

	/** Main frame's width and height */
	private int frameWidth, frameHeight;
	
	/** Input panel for in-built/external ontology file. */
	private JPanel sPanel = new JPanel();

	/** Encode button - on click calls encode ontology. */
	private JButton encodeButton;
	
	/** Available input options for ontology file selection. */
	private String[] selOption = { "In-Built", "External File" };
	private final JComboBox<String> dropList = new JComboBox<String>(this.selOption);
	
	private JSplitPane inOutPanel;
	
	/** JTable for ontology concept and its encoded number. */
	private T1Data encodeTable;
	
	/** Configuration parameters */
	private BasicConfig basicConfig;
	
	/** Name of the ontology file selected for a particular run. */
	private static String expName = null;
	
	/** Holds input selection for in-built/external ontology file */
	private static String owlChoice = null;
	
	/** Holds in-built ontology file selected. */
	private static String exampleChoosen = null;
	
	/** Path for in-built pizza ontology file. */
	private final String pizzaPath = "./src/main/resources/owlfiles/pizza.owl";
	
	/** Path for in-built wine ontology file */
	private final String winePath = "./src/main/resources/owlfiles/wine.owl";
	// TODO: Initialize the inbuilt owl paths here.
	
	/** CSV utility to generate performance measures and encoded list. */
	private static CsvUtils csvU;

	/**
	 * Constructor to initialize default values.
	 */
	public EncodeGUI() {
		this.basicConfig = new BasicConfig();
		EncodeGUI.csvU = new CsvUtils();
		
		this.frameWidth = 600;
		this.frameHeight = 300;
		
		EncodeGUI.exampleChoosen = null;
	}
	
	/**
	 * Constructor to initialize with given configuration parameters.
	 * 
	 * @param bConfig the configuration parameters.
	 */
	public EncodeGUI(BasicConfig bConfig) {
		this.basicConfig = bConfig;
		EncodeGUI.csvU = new CsvUtils();
		
		this.frameWidth = 600;
		this.frameHeight = 300;
		
		EncodeGUI.exampleChoosen = null;
	}

	/**
	 * Renders the application GUI.
	 */
	public void prepareGUI() {

		System.out.println("OntEncode: An Ontology Encoding Tool");
		
		this.mainFrame = new JFrame("OntEncode: An Ontology Encoding Tool");
		this.mainFrame.setVisible(true);
		this.mainFrame.setResizable(true);
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.setSize(this.frameWidth, this.frameHeight);

		inOutPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		inOutPanel.setResizeWeight(0.8);
		inOutPanel.setEnabled(false);
		inOutPanel.setDividerSize(10);

		JSplitPane inPanel = inputPanel();
		JPanel outPanel = encodePanel();

		inOutPanel.add(inPanel);
		inOutPanel.add(outPanel);

		mainFrame.add(inOutPanel);
		mainFrame.repaint();
		mainFrame.pack();
	}

	/**
	 * Input Panel -- in-built/external + file selection.
	 * 
	 * @return the in-built/external + file selection panel.
	 */
	public JSplitPane inputPanel() {
		
		JSplitPane iPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		iPanel.setResizeWeight(0.5);
		iPanel.setEnabled(false);
		iPanel.setDividerSize(10);
		
		JPanel selPanel = this.selPanel();	
		
		iPanel.add(selPanel);
		
		this.internalFilePanel();
		iPanel.add(this.sPanel);

		return iPanel;
	}
	
	/**
	 * In-built/external ontology file selection option.
	 * 
	 * @return the file selection panel.
	 */
	public JPanel selPanel() {
		
		JPanel sPanel = new JPanel();

		JLabel inputLabel = new JLabel("Select the ontology file source:");

		dropList.setEditable(false);
		dropList.addActionListener(this);

		sPanel.add(inputLabel);
		sPanel.add(dropList);
		
		return sPanel;
	}
	
	/**
	 * Renders the encode button panel.
	 * 
	 * @return the encode button panel.
	 */
	public JPanel encodePanel() {

		JPanel oPanel = new JPanel();

		this.encodeButton = new JButton("Encode");
		this.encodeButton.addActionListener(this);

		oPanel.add(encodeButton);

		return oPanel;

	}

	/**
	 * Renders in-built ontology file selection options.
	 * Different available files are listed as radio buttons. 
	 */
	protected void internalFilePanel() {

		System.out.println("In-built ontology file choice selected.");	
		
		final JRadioButton b1 = new JRadioButton("Pizza");
		final JRadioButton b2 = new JRadioButton("Wine");
		final JRadioButton b3 = new JRadioButton("ex3");

		b1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {      				
				EncodeGUI.owlChoice = "inbuilt";   
				EncodeGUI.exampleChoosen = "pizza";	
				EncodeGUI.expName = "pizza";
	         }  
		});
		
		b2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {   				
				EncodeGUI.owlChoice = "inbuilt";   
		        EncodeGUI.exampleChoosen = "wine";	
				EncodeGUI.expName = "wine";
			}
		});
		
		b3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {   				
				EncodeGUI.owlChoice = "inbuilt";   
				// TODO: set the exampleChoosen and expName for inbuilt file here.
		        EncodeGUI.exampleChoosen = "";	
			}
		});
		
		ButtonGroup group = new ButtonGroup();

		group.add(b1);
		group.add(b2);
		group.add(b3);
		
		JPanel rPanel = new JPanel() {
			public Dimension getPreferredSize()	{
				Dimension preferredSize = super.getPreferredSize();

				Border border = getBorder();
				int borderWidth = 0;

				if (border instanceof TitledBorder)	{
					Insets insets = getInsets();
					TitledBorder titledBorder = (TitledBorder)border;
					borderWidth = titledBorder.getMinimumSize(this).width + insets.left + insets.right;
				}

				int preferredWidth = Math.max(preferredSize.width, borderWidth);

				return new Dimension(preferredWidth, preferredSize.height);
			}
		};
		
		rPanel.setLayout(new GridLayout(3, 1));
		rPanel.contains(10, 10);
		rPanel.add(b1);
		rPanel.add(b2);
		rPanel.add(b3);

		TitledBorder  border = new TitledBorder(BorderFactory.createEtchedBorder(), "Select the Ontology File:");
		rPanel.setBorder(border);
		
		this.sPanel.removeAll();
		this.sPanel.add(rPanel);
		
		this.mainFrame.revalidate();
		this.mainFrame.repaint();
		this.mainFrame.pack();
	}

	/**
	 * Renders external ontology file selection option.
	 */
	protected void externalFilePanel() {

		System.out.println("External ontology file choice selected.");
		
		EncodeGUI.owlChoice = "external";
		EncodeGUI.expName = "external-ontology";
		
		JButton browseButton = new JButton("Browse");		
		browseButton.addActionListener(this);
		
		JLabel browseLabel = new JLabel("Select the external OWL file:");

		this.sPanel.removeAll();
		this.sPanel.add(browseLabel);
		this.sPanel.add(browseButton);

		this.mainFrame.revalidate();
		this.mainFrame.repaint();
		this.mainFrame.pack();
	}

	/**
	 * Renders dialog window for external ontology file selection.
	 */
	public void browseButton() {

		JFileChooser fBrowser = new JFileChooser();

		fBrowser.setDialogTitle("Choose an Ontology Definition file: ");
		fBrowser.setAcceptAllFileFilterUsed(false);
		fBrowser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileNameExtensionFilter owlFilter = new FileNameExtensionFilter("RDF/XML file", "owl");
		fBrowser.addChoosableFileFilter(owlFilter);

		int browseStatus = fBrowser.showOpenDialog(null);

		if (browseStatus == JFileChooser.APPROVE_OPTION) {
			if (fBrowser.getSelectedFile().isFile()) {
				EncodeGUI.owlChoice = "external";
			}
		}
	}

	/** 
	 * Calls encode ontology method based on input selections. 
	 */
	protected void encodeOntology() {
		
		// Input not given.
		if(EncodeGUI.owlChoice == null) {
			JOptionPane.showMessageDialog(this.mainFrame, "Select an input file.", "Inane error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// For in-built selection.
		if(EncodeGUI.owlChoice == "inbuilt") {
			
			// Pizza selection.
			if(EncodeGUI.exampleChoosen == "pizza") {
				this.basicConfig.setConfig("owl.filepath", this.pizzaPath);
				this.basicConfig.setConfig("owl.filetype", "RDF/XML");
				
				System.out.println("Pizza Ontology selected as input.");
			}
			
			// Wine selection.
			if(EncodeGUI.exampleChoosen == "wine") {
				this.basicConfig.setConfig("owl.filepath", this.winePath);
				this.basicConfig.setConfig("owl.filetype", "RDF/XML");
				
				System.out.println("Wine Ontology selected as input.");
			}
		}
		
		// TODO: set config for path and type here.
		// External file selection.
		if(EncodeGUI.owlChoice == "external") {
			
		}
		
		// Start timer. 
		long startTime = System.nanoTime();
		
		// Encode the input ontology.
		EncodeOntology entOnt = new EncodeOntology(this.basicConfig);
		Map<String, Integer> codes = entOnt.getEncodedConcepts();
		
		// Stop timer.
		long endTime = System.nanoTime();
		
		// Calculate the time taken to encode.
		long duration = (endTime - startTime); 
		System.out.println("Time taken to encode the ontology: " + duration + " ns");
		
		// Log the performance measures. 
		List<String> performanceData = new ArrayList<String>();
		performanceData.add(EncodeGUI.expName); // ontology file selection for the run.
		long millis=System.currentTimeMillis();  
		java.util.Date date=new java.util.Date(millis); // Timestamp for the run.
		performanceData.add(date.toString());
		performanceData.add(Long.toString(duration)); // Execution time.		
		EncodeGUI.csvU.setCsvPath(EncodeGUI.csvU.getCsvPathDir() + "performance_result.csv");
		EncodeGUI.csvU.writeIntoCSVFile(performanceData);
		
		// Write the encoded number to output CSV file.
		EncodeGUI.csvU.setCsvPath(EncodeGUI.csvU.getCsvPathDir() + "encoded_list.csv");
		EncodeGUI.csvU.writeIntoCSVFile(codes);
		
		// Render output CSV as JTable.
		this.outputPanel();
	}
	
	/**
	 * Renders the output ontology concepts and their encoded numbers in a JTable.
	 */
	public void outputPanel() {
		
		this.mainFrame.remove(inOutPanel);

        this.encodeTable = new T1Data();
        this.mainFrame.setContentPane(encodeTable);
		
        // FIXME: back button listener should reset the configuration as well as the frame to initial screen.  
		/*JPanel backButtonPanel = new JPanel();
		JButton backButton = new JButton("Back");
		backButton.addActionListener(this);
		backButtonPanel.add(backButton);
		this.mainFrame.add(backButtonPanel);*/
		
		this.mainFrame.revalidate();
		this.mainFrame.repaint();
		this.mainFrame.pack();
	}

    // FIXME: back button listener should reset the configuration as well as the frame to initial screen. 
	public void backGUI() {
		
	}
	
	/**
	 * Action listener for: <br/>
	 * - drop down list --> in-built/external selection. <br/>
	 * - browse button --> external file selection. <br/>
	 * - encode button --> calls the encode ontology method. <br/>
	 * - back button --> go back to initial screen. 
	 * 
	 * @param e action trigger (drop down list, browse, encode or back buttons).
	 */
	public void actionPerformed(ActionEvent e) {
		
        String command = e.getActionCommand();
        
        if (command.equals("Browse")) {
        	this.browseButton();
        }
        
        if (command.equals("Encode")) {
        	this.encodeOntology();
        }
        
        if (command.equals("comboBoxChanged")) {
        	String sel = (String) this.dropList.getSelectedItem();

			switch (sel) {
			
				case "External File":
					this.externalFilePanel();
					break;
					
				default:
					this.internalFilePanel();
					break;
			}
        }
        
        if (command.equals("Back")) {
        	this.backGUI();
        }
    }
	
	/**
	 * Renders the output CSV file as JTable.
	 */
	public class T1Data extends JPanel{
		
		private final JTable table;

	    public T1Data() {
	    	
	        this.table = new JTable(new MyModel());
	        this.table.setPreferredScrollableViewportSize(new Dimension(600, 300));
	        this.table.setFillsViewportHeight(true);
	        
	        JScrollPane scrollPane = new JScrollPane(this.table);
	        add(scrollPane, BorderLayout.CENTER);
	        setBorder(new EmptyBorder(5, 5, 5, 5));
	        
	        MyModel newModel = new MyModel();
	        this.table.setModel(newModel);
	        
	        File dataFile = new File(EncodeGUI.csvU.getCsvPath());
	        CSVFile reader = new CSVFile();
	        ArrayList<String[]> Rs2 = reader.ReadCSVfile(dataFile);
	        newModel.AddCSVData(Rs2);
	    }
	    
	    /**
	     * Reads a CSV file into the array of strings.
	     */
	    public class CSVFile {
	    	
	        private final ArrayList<String[]> rowData = new ArrayList<String[]>();
	        private String[] oneRow;

	        public ArrayList<String[]> ReadCSVfile(File dataFile) {
	            try {
	                BufferedReader reader = new BufferedReader(new FileReader(dataFile));
	                while (reader.ready()) {
	                    String st = reader.readLine();
	                    oneRow = st.split(",|\\s|;");
	                    rowData.add(oneRow);
	                } 
	                reader.close();
	            }
	            catch (Exception e) {
	                String errmsg = e.getMessage();
	                System.out.println("File not found:" + errmsg);
	            }
	            return rowData;
	        }
	    }
	    
	    class MyModel extends AbstractTableModel {
	    	
	        private final String[] columnNames = { "Ontology Concept", "Encoded Number" };
	        private ArrayList<String[]> Data = new ArrayList<String[]>();

	        public void AddCSVData(ArrayList<String[]> DataIn) {
	            this.Data = DataIn;
	            this.fireTableDataChanged();
	        }

	        public int getColumnCount() {
	            return columnNames.length;
	        }

	        public int getRowCount() {
	            return Data.size();
	        }

	        public String getColumnName(int col) {
	            return columnNames[col];
	        }

	        public Object getValueAt(int row, int col) {
	            return Data.get(row)[col];
	        }
	    }
	}
}