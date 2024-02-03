/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package posapplication.components.product;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import posapplication.inventory.InventoryController;
import posapplication.models.product;

/**
 * FXML Controller class
 *
 * @author HP PROBOOK 430 G3
 */
public class ProductController implements Initializable {

    @FXML
    private VBox card;
    @FXML
    private Label quantity;
    @FXML
    private Rectangle product_image;
    @FXML
    private Label name;
    @FXML
    private Label description;
    @FXML
    private Label manufacturer;
    @FXML
    private Label manufacture_date;
    @FXML
    private Label expiry_date;
    @FXML
    private Label product_code;

    private InventoryController inventoryController;
    private product currentProduct;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setInventoryControllerReference(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }
    
    public product getCurrentProduct() {
        return currentProduct;
    }

    public void setData(product newProduct) {
        this.currentProduct = newProduct;
        quantity.setText(String.valueOf(newProduct.getQuantity()));
        name.setText(newProduct.getProductName());
        description.setText(newProduct.getDescription());
        manufacturer.setText(newProduct.getMaunfacturer());
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MM yyyy");
        manufacture_date.setText(dateFormat.format(newProduct.getManufacturing_date()));
        expiry_date.setText(dateFormat.format(newProduct.getExpiry_date()));
        product_code.setText(newProduct.getProductCode());
        product_image.setFill(new ImagePattern(newProduct.getImage()));

    }
    
     public void modifyProduct(product newProduct) throws SQLException {
           inventoryController.modifyProductDetails(newProduct.getProductCode(), newProduct.getDescription(), newProduct.getExpiry_date(), newProduct.getImage(), newProduct.getLow_stock_count(), newProduct.getManufacturing_date(), newProduct.getMaunfacturer(), newProduct.getPrice(), newProduct.getProductName(),  newProduct.getQuantity(), newProduct);
    }


}
