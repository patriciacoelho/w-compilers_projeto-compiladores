package com.wpcompilers.compiler;
	

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	private static Scene frontPage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent fxmlGUICompiler = FXMLLoader.load(getClass().getResource("view/interface.fxml"));
    	frontPage = new Scene(fxmlGUICompiler);

    	primaryStage.setScene(frontPage);
    	primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
