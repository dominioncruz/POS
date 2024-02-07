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

    public void initializeProductList(FlowPane productsContainer, InventoryController inventoryController, DatabaseConnection dbConnect) throws SQLException, IOException {
        ResultSet rs = dbConnect.getAllProducts();
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
        }

    }
    
    public void initializeProductList(FlowPane productsContainer, SalespersonController salesController, DatabaseConnection dbConnect) throws SQLException, IOException {
        ResultSet rs = dbConnect.getAllProducts();
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
        }

    }
}
