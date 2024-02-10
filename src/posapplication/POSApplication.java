/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package posapplication;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import posapplication.reusableFunctions.DatabaseConnection;
import posapplication.reusableFunctions.centerScreen;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class POSApplication extends Application {

    DatabaseConnection connection = DatabaseConnection.getInstance();

    @Override
    public void start(Stage primaryStage) throws IOException {
        centerScreen screenInstance = new centerScreen();
        screenInstance.centerFrame(primaryStage, "/posapplication/login/loginPage.fxml", connection);
    }    
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);
    }

}
