package com.uvce.cse.searchiot.encodeont.app;

import com.uvce.cse.searchiot.encodeont.config.BasicConfig;
import com.uvce.cse.searchiot.encodeont.guiutil.EncodeGUI;

/**
 * Application to encode a given ontology using prime-based encoding technique.
 * 
 * @author Santosh Pattar
 * @author Dwaraka Kulkarni
 * @version 1.0
 */
public class TestApp {
	public static void main(String args[]) throws Exception {
						
		BasicConfig basicConfig = new BasicConfig();
		
		EncodeGUI guiTool = new EncodeGUI(basicConfig);
		guiTool.prepareGUI();
	}
}