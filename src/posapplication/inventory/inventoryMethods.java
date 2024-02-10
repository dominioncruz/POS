/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.inventory;

import com.mysql.cj.jdbc.Blob;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import posapplication.models.product;
import posapplication.reusableFunctions.DatabaseConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import posapplication.components.product.ProductController;
import posapplication.components.product.ProductFunctions;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.layout.HBox;
import posapplication.components.message.MessageController;
import posapplication.models.message;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class inventoryMethods {
    
    

// Send and receive messages as needed


    public void updateProductInformation(
            VBox editPageContainer,
            Button submitButton,
            ScrollPane listOfProductsScrollPane,
            TextField productNameInput,
            TextField productDescription,
            TextField quantity,
            TextField price,
            TextField manufacturer_name,
            TextField product_code,
            TextField low_stock_count,
            DatePicker production_date,
            DatePicker expiry_date,
            BorderPane enitreScreen,
            VBox invalidDetailVBox,
            ProgressIndicator loadingBar,
            VBox successBox,
            Label successMessage,
            Label errorMessage,
            TextField[] myInputList,
            DatabaseConnection databaseConnection,
            Rectangle imageContainer,
            InventoryController inventoryController,
            FlowPane productsContainer,
            product currentProduct,
            Label actualSubmitButton,
            List<Node> productVBoxes,
            ProductFunctions myProductFunctions,
            VBox listOfMessages,
            List<String> lowStockItems
    ) {
        try {
            loadingBar.setVisible(true);
            boolean metRequirements = true;

            enitreScreen.setDisable(true);

            for (TextField input : myInputList) {

                if (input.getText().isEmpty()) {
                    metRequirements = false;
                }

            }
            if (metRequirements) {
                if (production_date.getValue().isAfter(LocalDate.now())) {
                    metRequirements = false;
                    errorMessage.setText("Invalid production date");
                } else if (expiry_date.getValue().isBefore(LocalDate.now()) || expiry_date.getValue().isEqual(LocalDate.now())) {
                    metRequirements = false;
                    errorMessage.setText("Invalid expiry date");
                } else {

                    boolean dataEntryState = databaseConnection.updateProductInDatabase(product_code.getText(), productNameInput.getText(), manufacturer_name.getText(), production_date.getValue(), expiry_date.getValue(), quantity.getText(), price.getText(), ((ImagePattern) imageContainer.getFill()).getImage(), productDescription.getText(), low_stock_count.getText(),
                            "UPDATE products SET product_code = ?, name = ?, manufacturer = ?, manufacturing_date = ?, expiry_date = ?, quantity = ?, price = ?, image = ?, description = ?, low_stock_count = ? WHERE product_code = ?");
                    if (dataEntryState == false) {
                        metRequirements = false;
                        errorMessage.setText("An occured somewhere");
                    } else {
                        successMessage.setText("Product updated successfully");
                        myProductFunctions.initializeProductList(productsContainer, inventoryController, databaseConnection, lowStockItems, listOfMessages, this, productVBoxes);
                      
                        addNewInfoToMessagses("Update", "Product detail updated", "You updated details for product: " + productNameInput.getText(), inventoryController, listOfMessages);
                        successBox.setVisible(true);
                        enitreScreen.setDisable(false);
                        resetFields(product_code, productNameInput, manufacturer_name, production_date, expiry_date, quantity, price, productDescription, low_stock_count, submitButton, editPageContainer, imageContainer, listOfProductsScrollPane, actualSubmitButton);
                    }
                }
            } else {
                errorMessage.setText("Please enter all fields");
            }

            if (!metRequirements) {
                invalidDetailVBox.setVisible(true);
                Toolkit.getDefaultToolkit().beep();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            loadingBar.setVisible(false);
        }

    }

    public void createNewProduct(
            VBox editPageContainer,
            Button submitButton,
            ScrollPane listOfProductsScrollPane,
            TextField productNameInput,
            TextField productDescription,
            TextField quantity,
            TextField price,
            TextField manufacturer_name,
            TextField product_code,
            TextField low_stock_count,
            DatePicker production_date,
            DatePicker expiry_date,
            BorderPane enitreScreen,
            VBox invalidDetailVBox,
            ProgressIndicator loadingBar,
            VBox successBox,
            Label successMessage,
            Label errorMessage,
            TextField[] myInputList,
            DatabaseConnection databaseConnection,
            Rectangle imageContainer,
            InventoryController inventoryController,
            FlowPane productsContainer,
            Label actualSubmitButton,
            List<Node> productVBoxes,
            ProductFunctions myProductFunctions,
            VBox listOfMessages,
            List<String> lowStockItems
    ) {
        try {
            loadingBar.setVisible(true);
            boolean metRequirements = true;

            enitreScreen.setDisable(true);

            for (TextField input : myInputList) {

                if (input.getText().isEmpty()) {
                    metRequirements = false;
                }

            }
            if (metRequirements) {
                if (production_date.getValue().isAfter(LocalDate.now())) {
                    metRequirements = false;
                    errorMessage.setText("Invalid production date");
                } else if (expiry_date.getValue().isBefore(LocalDate.now()) || expiry_date.getValue().isEqual(LocalDate.now())) {
                    metRequirements = false;
                    errorMessage.setText("Invalid expiry date");
                } else {

                    boolean dataEntryState = databaseConnection.createNewProductInDatabase(product_code.getText(), productNameInput.getText(), manufacturer_name.getText(), production_date.getValue(), expiry_date.getValue(), quantity.getText(), price.getText(), ((ImagePattern) imageContainer.getFill()).getImage(), productDescription.getText(), low_stock_count.getText(), "INSERT INTO products values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    if (dataEntryState == false) {
                        metRequirements = false;
                        errorMessage.setText("An occured somewhere");
                    } else {
                        successMessage.setText("Product added successfully");
                        myProductFunctions.initializeProductList(productsContainer, inventoryController, databaseConnection, lowStockItems, listOfMessages, this, productVBoxes);
                        
                        addNewInfoToMessagses("Upload", "New product added", "You added a new product to inventory: " + productNameInput.getText(), inventoryController, listOfMessages);
                        successBox.setVisible(true);
                        enitreScreen.setDisable(false);
                        resetFields(product_code, productNameInput, manufacturer_name, production_date, expiry_date, quantity, price, productDescription, low_stock_count, submitButton, editPageContainer, imageContainer, listOfProductsScrollPane, actualSubmitButton);
                    }
                }
            } else {
                errorMessage.setText("Please enter all fields");
            }

            if (!metRequirements) {
                invalidDetailVBox.setVisible(true);
                Toolkit.getDefaultToolkit().beep();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            loadingBar.setVisible(false);
        }
    }

    public void resetFields(
            TextField product_code,
            TextField productNameInput,
            TextField manufacturer_name,
            DatePicker production_date,
            DatePicker expiry_date,
            TextField quantity,
            TextField price,
            TextField productDescription,
            TextField low_stock_count,
            Button submitButton,
            VBox editPageContainer,
            Rectangle imageContainer,
            ScrollPane listOfProductsScrollPane,
            Label actualSubmitButton
    ) {
        product_code.setText("");
        productNameInput.setText("");
        manufacturer_name.setText("");
        production_date.setValue(LocalDate.now());
        expiry_date.setValue(LocalDate.now());
        quantity.setText("");
        price.setText("");
        Image productNoPhotoImage = new Image(getClass().getResource("../images/noThumb.jpg").toExternalForm(), 150, 150, true, true);
        imageContainer.setFill(new ImagePattern(productNoPhotoImage));
        productDescription.setText("");
        low_stock_count.setText("");
        submitButton.setText("Create");
        editPageContainer.setVisible(false);
        listOfProductsScrollPane.setDisable(false);
        actualSubmitButton.setVisible(true);
    }

    public product addContentToView(
            String productCodeFetched,
            String name,
            String maunfacturerName,
            Date manufacturing_date_fetched,
            Date expiry_date_fetched,
            int quantity_fetched,
            int price_fetched,
            Image image_fetched,
            int low_stock_count_fetched,
            String description_fetched,
            InventoryController inventoryController,
            FlowPane productsContainer
    ) throws SQLException, IOException {

        String productName = name;
        String productCode = productCodeFetched;
        String maunfacturer = maunfacturerName;
        Date manufacturing_date = manufacturing_date_fetched;
        Date expiry_date = expiry_date_fetched;
        int quantity = quantity_fetched;
        int price = price_fetched;
        int low_stock_count = low_stock_count_fetched;

        String description = description_fetched;

        product newProduct = new product();
        if (image_fetched != null) {
            newProduct.setImage(image_fetched);
        }

        newProduct.setProductName(productName);
        newProduct.setProductCode(productCode);
        newProduct.setManufacturing_date(manufacturing_date);
        newProduct.setExpiry_date(expiry_date);
        newProduct.setMaunfacturer(maunfacturer);
        newProduct.setQuantity(quantity);
        newProduct.setPrice(price);

        newProduct.setDescription(description);
        newProduct.setLow_stock_count(low_stock_count);

        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../components/product/product.fxml"));
            StackPane productVBox = fxmlloader.load();
            ProductController currentProductController = fxmlloader.getController();
            currentProductController.setInventoryControllerReference(inventoryController);
            currentProductController.setData(newProduct);
            productVBox.getProperties().put("controller", currentProductController);

            productVBox.setOnMouseClicked(event -> {

                try {
                    currentProductController.modifyProduct(newProduct);

                } catch (SQLException ex) {
                    Logger.getLogger(ProductFunctions.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            productsContainer.getChildren().add(productVBox);
            return newProduct;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void addBirthdayToView(
            ResultSet rs,
            InventoryController inventoryController,
            VBox messagesContainer
    ) throws SQLException, IOException {
        
        
    
        LocalTime time = LocalTime.now();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Format the LocalTime object using the formatter
        String formattedTime = time.format(formatter);
        
        String title = "Birthday" ;
        String summary = rs.getString("first_name") + "'s birthday";
        String content = "Today is " + rs.getString("first_name") + "'s birthday, do wish them a happy birthday";
        
        message currentMessage = new message();

        currentMessage.setTime(formattedTime);
        currentMessage.setTitle(title);
        currentMessage.setSummary(summary);
        currentMessage.setContent(content);

        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../components/message/message.fxml"));
            HBox currentMessageHBox = fxmlloader.load();
            MessageController currentMessageController = fxmlloader.getController();
            currentMessageController.setInventoryControllerReference(inventoryController);
            currentMessageController.setData(currentMessage, rs.getString("email"));
            currentMessageHBox.getProperties().put("controller", currentMessageController);

            currentMessageHBox.setOnMouseClicked(event -> {
                    currentMessageController.openInventoryMessage(currentMessage);
            });
            messagesContainer.getChildren().add(currentMessageHBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addNewInfoToMessagses(
            String title,
            String summary,
            String content,
            InventoryController inventController,
            VBox messagesContainer
    ) throws SQLException, IOException {
        
        
    
        LocalTime time = LocalTime.now();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Format the LocalTime object using the formatter
        String formattedTime = time.format(formatter);
        
        message currentMessage = new message();

        currentMessage.setTime(formattedTime);
        currentMessage.setTitle(title);
        currentMessage.setSummary(summary);
        currentMessage.setContent(content);

        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../components/message/message.fxml"));
            HBox currentMessageHBox = fxmlloader.load();
            MessageController currentMessageController = fxmlloader.getController();
            currentMessageController.setInventoryControllerReference(inventController);
            currentMessageController.setData(currentMessage, null);
            currentMessageHBox.getProperties().put("controller", currentMessageController);

            currentMessageHBox.setOnMouseClicked(event -> {
                    currentMessageController.openOtherInventoryMessage(currentMessage);
            });
            messagesContainer.getChildren().add(currentMessageHBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
