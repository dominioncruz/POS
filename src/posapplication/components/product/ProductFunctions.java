/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.components.product;

import com.mysql.cj.jdbc.Blob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javafx.scene.layout.FlowPane;
import posapplication.inventory.InventoryController;
import posapplication.reusableFunctions.DatabaseConnection;
import java.sql.Date;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import posapplication.models.product;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import posapplication.inventory.inventoryMethods;
import posapplication.cashier.SalespersonController;
import posapplication.cashier.salesPersonFunctions;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class ProductFunctions {

    inventoryMethods myInventoryMethods = new inventoryMethods();
    salesPersonFunctions salesMethods = new salesPersonFunctions();

    //private final DatabaseConnection currentConnection;
    public ProductFunctions() throws ClassNotFoundException {
    }

    public void initializeProductList(FlowPane productsContainer, InventoryController inventoryController, DatabaseConnection dbConnect, List<String> lowStockItems, VBox listOfMessages, inventoryMethods currentInventoryMethods, List<Node> productVBoxes) throws SQLException, IOException {
        ResultSet rs = dbConnect.getAllProducts();
        LocalDate currentDate = LocalDate.now();
        productsContainer.getChildren().clear();
        productVBoxes.clear();
        while (rs.next()) {
            
            java.sql.Blob columnValue = rs.getBlob("image");
            byte[] imageData = columnValue.getBytes(1, (int) columnValue.length());
            Image image = new Image(new ByteArrayInputStream(imageData));
            myInventoryMethods.addContentToView(rs.getString("product_code"),
                    rs.getString("name"),
                    rs.getString("manufacturer"),
                    rs.getDate("manufacturing_date"),
                    rs.getDate("expiry_date"),
                    rs.getInt("quantity"),
                    rs.getInt("price"),
                    image,
                    rs.getInt("low_stock_count"),
                    rs.getString("description"),
                    inventoryController,
                    productsContainer
            );
            
            if(rs.getInt("quantity") <= rs.getInt("low_stock_count")){
                if(!lowStockItems.contains(rs.getString("name"))){
                    currentInventoryMethods.addNewInfoToMessagses("Warning", "Low stock", "Product: " + rs.getString("name") + " is almost out of stock, only " + rs.getInt("quantity") + " left.", inventoryController, listOfMessages);
                    lowStockItems.add(rs.getString("name"));
                }
            }
        }
                        
        for (Node node : productsContainer.getChildren()) {
            if (node instanceof StackPane) {
                productVBoxes.add(node);
            }
        }

    }
    
    public void initializeProductList(FlowPane productsContainer, SalespersonController salesController, DatabaseConnection dbConnect, List<String> lowStockItems, VBox listOfMessages, salesPersonFunctions salesMethods, List<Node> productVBoxes) throws SQLException, IOException {
        ResultSet rs = dbConnect.getAllProducts();
        productsContainer.getChildren().clear();
        productVBoxes.clear();
        while (rs.next()) {
            java.sql.Blob columnValue = rs.getBlob("image");
            byte[] imageData = columnValue.getBytes(1, (int) columnValue.length());
            Image image = new Image(new ByteArrayInputStream(imageData));
            salesMethods.addContentToView(rs.getString("product_code"),
                    rs.getString("name"),
                    rs.getString("manufacturer"),
                    rs.getDate("manufacturing_date"),
                    rs.getDate("expiry_date"),
                    rs.getInt("quantity"),
                    rs.getInt("price"),
                    image,
                    rs.getInt("low_stock_count"),
                    rs.getString("description"),
                    salesController,
                    productsContainer
            );
            
            if(rs.getInt("quantity") <= rs.getInt("low_stock_count")){
                if(!lowStockItems.contains(rs.getString("name"))){
                    salesMethods.addNewInfoToMessagses("Warning", "Low stock", "Product: " + rs.getString("name") + " is almost out of stock, only " + rs.getInt("quantity") + " left.", salesController, listOfMessages);
                    lowStockItems.add(rs.getString("name"));
                }
            }
        }
        for (Node node : productsContainer.getChildren()) {
            if (node instanceof StackPane) {
                productVBoxes.add(node);
            }
        }

    }
}
