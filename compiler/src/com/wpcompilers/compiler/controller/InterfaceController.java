package com.wpcompilers.compiler.controller;

import java.util.ArrayList;


import com.wpcompilers.compiler.model.Scanner;
import com.wpcompilers.compiler.model.Token;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class InterfaceController {
	
	@FXML
	private TextArea txtPrograma;
	
	@FXML
	private TextArea txtOutput;
	
	
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
	
}
