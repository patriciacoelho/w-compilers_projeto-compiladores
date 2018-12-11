package com.wpcompilers.compiler.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


import com.wpcompilers.compiler.model.Scanner;
import com.wpcompilers.compiler.model.Token;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class InterfaceController {
	
	@FXML
	private TextArea txtPrograma;
	
	@FXML
	private TextArea txtOutput;
	
	
	@FXML
	private void initialize() {
		txtOutput.setEditable(false);
	}
	
	@FXML
	private void handleRun(ActionEvent event) throws Exception {
		ArrayList<Token> lexicalProgram;
		Scanner scanner = new Scanner(txtPrograma.getText());
		txtOutput.clear();
		try {
			lexicalProgram = scanner.read();
			for(int i=0;i<lexicalProgram.size(); i++) {
				txtOutput.appendText(lexicalProgram.get(i).toString()+'\n');
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@FXML
	private void handleClose() {
		System.exit(0);
	}
	
	@FXML
	private void handleLoad() throws IOException{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("./"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt", "*.pas"));
		File fileName = fileChooser.showOpenDialog(null);
		if(fileName != null) {
			try {
				BufferedReader reader;
				reader = new BufferedReader(new FileReader(fileName));
				String response = new String();
				for (String line; (line = reader.readLine()) != null; response += line+'\n');
				txtPrograma.setText(response);
				reader.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				
			}
		}
		
		
	}
	
	@FXML
	private void handleSave() throws IOException {
		FileChooser fileChooser = new FileChooser();
		File fileName = fileChooser.showSaveDialog(null);
		if(fileName != null) {
			FileWriter fileWriter;
	        fileWriter = new FileWriter(fileName);
	        fileWriter.write(txtPrograma.getText());
	        fileWriter.close();
		}
	}
	
	
	
	
}
