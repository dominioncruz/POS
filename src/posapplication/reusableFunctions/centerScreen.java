/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.reusableFunctions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import posapplication.cashier.SalespersonController;
import posapplication.infotech.InfotechController;
import posapplication.inventory.InventoryController;
import posapplication.login.LoginPageController;
import posapplication.manager.ManagerController;
import posapplication.receipt.ReceiptController;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class centerScreen {

    public void centerFrame(Stage stage, String route, DatabaseConnection DatabaseConnection) throws IOException {
        
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
        Parent root = loader.load();
        stage.close();
        Stage newStage = new Stage();
        newStage.setResizable(false);
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        LoginPageController nextController = loader.getController();
        nextController.setData(DatabaseConnection);
        newStage.getIcons().add(new Image("/posapplication/images/logo-150x150.png"));
        newStage.setTitle("Login");
        newStage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        newStage.setX((screenBounds.getWidth() - newStage.getWidth()) / 2);
        newStage.setY((screenBounds.getHeight() - newStage.getHeight()) / 2);
    }

    ;
    
    public void centerInfoTech(Stage stage, ResultSet rs, String route, DatabaseConnection DatabaseConnection) throws IOException, ClassNotFoundException, SQLException {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("firstname", rs.getString("first_name"));
        parameters.put("lastname", rs.getString("last_name"));
        parameters.put("email", rs.getString("email"));
        java.sql.Blob columnValue = rs.getBlob("profile_photo");

        if (columnValue != null) {
            byte[] imageData = columnValue.getBytes(1, (int) columnValue.length());
            Image image = new Image(new ByteArrayInputStream(imageData));
            parameters.put("profilePhoto", image);
        }
        parameters.put("databaseConnection", DatabaseConnection);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
        Parent root = loader.load();
        stage.setMinHeight(760);
        stage.setMinWidth(1360);
        stage.setResizable(true);
        Scene scene = new Scene(root);
        InfotechController nextController = loader.getController();
        nextController.setData(parameters);
        stage.setScene(scene);
        stage.setTitle("Home page");
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

    }

    public void centerInventory(Stage stage, ResultSet rs, String route, DatabaseConnection DatabaseConnection) throws IOException, ClassNotFoundException, SQLException {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("firstname", rs.getString("first_name"));
        parameters.put("lastname", rs.getString("last_name"));
        parameters.put("email", rs.getString("email"));
        parameters.put("profilePhoto", rs.getBlob("profile_photo"));
        java.sql.Blob columnValue = rs.getBlob("profile_photo");

        if (columnValue != null) {
            byte[] imageData = columnValue.getBytes(1, (int) columnValue.length());
            Image image = new Image(new ByteArrayInputStream(imageData));
            parameters.put("profilePhoto", image);
        }
        parameters.put("databaseConnection", DatabaseConnection);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
        Parent root = loader.load();
        stage.setMinHeight(760);
        stage.setMinWidth(1360);
        stage.setResizable(true);
        Scene scene = new Scene(root);
        InventoryController nextController = loader.getController();
        nextController.setData(parameters);
        stage.setScene(scene);
        stage.setTitle("Home page");
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

    }
;
    public void centerSales(Stage stage, ResultSet rs, String route, DatabaseConnection DatabaseConnection) throws IOException, ClassNotFoundException, SQLException {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("firstname", rs.getString("first_name"));
        parameters.put("lastname", rs.getString("last_name"));
        parameters.put("email", rs.getString("email"));
        parameters.put("profilePhoto", rs.getBlob("profile_photo"));
        java.sql.Blob columnValue = rs.getBlob("profile_photo");

        if (columnValue != null) {
            byte[] imageData = columnValue.getBytes(1, (int) columnValue.length());
            Image image = new Image(new ByteArrayInputStream(imageData));
            parameters.put("profilePhoto", image);
        }
        parameters.put("databaseConnection", DatabaseConnection);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
        Parent root = loader.load();
        stage.setMinHeight(760);
        stage.setMinWidth(1360);
        stage.setResizable(true);
        Scene scene = new Scene(root);
        SalespersonController nextController = loader.getController();
        nextController.setData(parameters);
        stage.setScene(scene);
        stage.setTitle("Home page");
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

    }
;
    public void centerManager(Stage stage, ResultSet rs, String route, DatabaseConnection DatabaseConnection) throws IOException, ClassNotFoundException, SQLException {

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("firstname", rs.getString("first_name"));
        parameters.put("lastname", rs.getString("last_name"));
        parameters.put("email", rs.getString("email"));
        parameters.put("profilePhoto", rs.getBlob("profile_photo"));
        java.sql.Blob columnValue = rs.getBlob("profile_photo");

        if (columnValue != null) {
            byte[] imageData = columnValue.getBytes(1, (int) columnValue.length());
            Image image = new Image(new ByteArrayInputStream(imageData));
            parameters.put("profilePhoto", image);
        }
        parameters.put("databaseConnection", DatabaseConnection);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
        Parent root = loader.load();
        stage.setMinHeight(760);
        stage.setMinWidth(1360);
        stage.setResizable(true);
        Scene scene = new Scene(root);
        ManagerController nextController = loader.getController();
        nextController.setData(parameters);
        stage.setScene(scene);
        stage.setTitle("Home page");
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

    };
    
    public void showReceipt(Stage stage, String route, String fullName, VBox listOfOrderVBox, String paymentMethod, String totalCost ) throws IOException, ClassNotFoundException, SQLException{
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("fullName", fullName);
        parameters.put("listOfOrder", listOfOrderVBox);
        parameters.put("paymentMethod", paymentMethod);
        parameters.put("totalCost", totalCost);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(route));
        Parent root = loader.load();
        ReceiptController nextController = loader.getController();
        nextController.setData(parameters);
        Stage secondaryStage = new Stage();
        
        secondaryStage.setResizable(false);
        Scene scene = new Scene(root);
        secondaryStage.setScene(scene);
        secondaryStage.initModality(Modality.APPLICATION_MODAL);
        secondaryStage.setTitle("Receipt Preview");

        // Size the Stage to fit the content
        secondaryStage.sizeToScene();
        secondaryStage.setFullScreen(false);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        secondaryStage.setX((screenBounds.getWidth() - secondaryStage.getWidth()) / 2);
        secondaryStage.setY((screenBounds.getHeight() - secondaryStage.getHeight()) / 2);
        // Show the Stage (this is optional and can be removed if you don't want to show the preview)
        secondaryStage.show();
        
    }
    

}
