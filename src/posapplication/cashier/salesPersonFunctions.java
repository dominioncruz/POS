/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.cashier;

import java.io.IOException;
import java.util.Date;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import posapplication.models.product;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import posapplication.components.product.ProductController;
import posapplication.components.product.ProductFunctions;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import posapplication.components.customerOrder.CustomerOrderController;
import posapplication.models.order;
import posapplication.reusableFunctions.DatabaseConnection;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class salesPersonFunctions {

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
            SalespersonController salesController,
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
        newProduct.setManufacturing_date((java.sql.Date) manufacturing_date);
        newProduct.setExpiry_date((java.sql.Date) expiry_date);
        newProduct.setMaunfacturer(maunfacturer);
        newProduct.setQuantity(quantity);
        newProduct.setPrice(price);

        newProduct.setDescription(description);
        newProduct.setLow_stock_count(low_stock_count);

        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../components/product/product.fxml"));
            StackPane productVBox = fxmlloader.load();
            ProductController currentProductController = fxmlloader.getController();
            currentProductController.setSalesControllerReference(salesController);
            currentProductController.setData(newProduct);
            productVBox.getProperties().put("controller", currentProductController);
            if (quantity == 0) {
                productVBox.setDisable(true);
            }
            
            LocalDate expiryDate = newProduct.getExpiry_date().toLocalDate();
            
            LocalDate currentDate = LocalDate.now();
            
            if (currentDate.equals(expiryDate)||currentDate.isAfter(expiryDate)) {
                productVBox.setDisable(true);
            }

            productVBox.setOnMouseClicked(event -> {
                try {
                    try {
                        currentProductController.addProductToCart(newProduct);
                    } catch (IOException ex) {
                        Logger.getLogger(salesPersonFunctions.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

    public order addNewProductToCart(
            String productCode,
            String productName,
            String maunfacturer,
            Date manufacturing_date,
            Date expiry_date,
            int quantity,
            int price,
            Image image,
            int low_stock_count,
            String description,
            SalespersonController salesPerson,
            VBox listOfOrderVBox,
            product newProduct
    ) throws SQLException, IOException {
        String catProductName = productName;
        String catProductCode = productCode;
        String catProductmaunfacturer = maunfacturer;
        Date catProductmanufacturing_date = manufacturing_date;
        Date catProductexpiry_date = expiry_date;
        int catProductquantity = quantity;
        int catProductprice = price;
        int catProductlow_stock_count = low_stock_count;
        product productReference = newProduct;

        String catProductdescription = description;

        order newOrder = new order();
        if (image != null) {
            newOrder.setImage(image);
        }

        newOrder.setProductName(catProductName);
        newOrder.setProductCode(catProductCode);
        newOrder.setManufacturing_date((java.sql.Date) catProductmanufacturing_date);
        newOrder.setExpiry_date((java.sql.Date) catProductexpiry_date);
        newOrder.setMaunfacturer(catProductmaunfacturer);
        newOrder.setQuantity(catProductquantity);
        newOrder.setPrice(catProductprice);

        newOrder.setDescription(catProductdescription);
        newOrder.setLow_stock_count(catProductlow_stock_count);
        newOrder.setCurrentProduct(productReference);

        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../components/customerOrder/customerOrder.fxml"));
            VBox orderVBox = fxmlloader.load();
            CustomerOrderController currentOrderController = fxmlloader.getController();
            currentOrderController.setSalesControllerReference(salesPerson);
            currentOrderController.setSalesVBox(orderVBox);
            currentOrderController.setData(newOrder);
            orderVBox.getProperties().put("controller", currentOrderController);
            listOfOrderVBox.getChildren().add(orderVBox);
            return newOrder;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addNewSale(
            String buyerName,
            String product_code,
            String quantity,
            String amount,
            String paymentMethodUsed,
            String userEmail,
            DatabaseConnection db
    ) throws SQLException {
        boolean result = db.addNewSaleForSeller(buyerName, product_code, quantity, amount, paymentMethodUsed, userEmail);
        return result;
    }

}
