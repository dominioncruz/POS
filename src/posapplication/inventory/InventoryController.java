/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package posapplication.inventory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import posapplication.components.product.ProductFunctions;
import posapplication.reusableFunctions.DatabaseConnection;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import posapplication.components.product.ProductController;
import posapplication.models.product;
import posapplication.reusableFunctions.centerScreen;
import posapplication.reusableFunctions.imageUpload;

/**
 * FXML Controller class
 *
 * @author HP PROBOOK 430 G3
 */
public class InventoryController implements Initializable {

    String userEmail;
    private final centerScreen centerScreenAccess;
    static DatabaseConnection databaseConnection;
    private final imageUpload imageChooser;
    ProductFunctions myProductFunctions;
    private final inventoryMethods currentInventoryMethods;
    public List<Node> productVBoxes = new ArrayList<>();

    product currentProduct;
    
    @FXML
    private Label sideBarDate;
    @FXML
    private HBox signOutUser;
    @FXML
    private TextField productSearchBar;
    @FXML
    private Circle profileImageCircle;
    @FXML
    private Label userFullName;
    @FXML
    private FlowPane productsContainer;

    @FXML
    private Label submitButton;
    @FXML
    private VBox editPageContainer;
    @FXML
    private TextField productNameInput;
    @FXML
    private TextField productDescription;
    @FXML
    private DatePicker production_date;
    @FXML
    private DatePicker expiry_date;
    @FXML
    private TextField quantity;
    @FXML
    private TextField price;
    @FXML
    private TextField manufacturer_name;
    @FXML
    private TextField product_code;
    @FXML
    private TextField low_stock_count;
    @FXML
    private BorderPane enitreScreen;
    @FXML
    private VBox invalidDetailVBox;
    @FXML
    private ProgressIndicator loadingBar;
    @FXML
    private ScrollPane listOfProductsScrollPane;
    @FXML
    private Rectangle productImage;
    @FXML
    private VBox successBox;
    @FXML
    private Label successMessage;
    @FXML
    private Label errorMessage;

    TextField[] myInputList = new TextField[6];
    @FXML
    private Button createProductButton;

    /**
     * Initializes the controller class.
     *
     * @throws java.lang.ClassNotFoundException
     */
    public InventoryController() throws ClassNotFoundException {
        this.myProductFunctions = new ProductFunctions();
        this.centerScreenAccess = new centerScreen();
        this.imageChooser = new imageUpload();
        this.currentInventoryMethods = new inventoryMethods();
    }

    public void modifyProductDetails(String productCode, String description, Date expiryDate, Image image, int low_stock_count_value, Date manufacturing_date, String manufacturer, int priceValue, String productName, int quantityValue, product newProduct) {
        editPageContainer.setVisible(true);
        submitButton.setVisible(false);
        listOfProductsScrollPane.setDisable(true);
        productNameInput.setText(productName);
        productDescription.setText(description);
        LocalDate manufactureDate = new java.sql.Date(manufacturing_date.getTime()).toLocalDate();
        production_date.setValue(manufactureDate);
        LocalDate expireDate = new java.sql.Date(expiryDate.getTime()).toLocalDate();
        expiry_date.setValue(expireDate);
        quantity.setText(String.valueOf(quantityValue));
        price.setText(String.valueOf(priceValue));
        manufacturer_name.setText(manufacturer);
        product_code.setText(productCode);
        low_stock_count.setText(String.valueOf(low_stock_count_value));
        productImage.setFill(new ImagePattern(image));
        currentProduct = newProduct;
        createProductButton.setText("Update");

    }

    private void filterProducts(String searchQuery) {
        productsContainer.getChildren().clear();
        if (searchQuery.equals("")) {
            for (Node node : productVBoxes) {
                if (node instanceof VBox productVBox) {
                    ProductController productController = (ProductController) productVBox.getProperties().get("controller");
                    productsContainer.getChildren().add(productVBox);  // Add matching products
                }
            }

        } else {
            for (Node node : productVBoxes) {
                if (node instanceof VBox productVBox) {
                    ProductController productController = (ProductController) productVBox.getProperties().get("controller");
                    if (productController != null && containsSearchQuery(productController.getCurrentProduct(), searchQuery)) {
                        productsContainer.getChildren().add(productVBox);  // Add matching products
                    }
                }
            }
        }
    }

    private boolean containsSearchQuery(product product, String searchQuery) {
        // Check if product code or name contains the search query (case-insensitive)
        String code = product.getProductCode().toLowerCase();
        String name = product.getProductName().toLowerCase();
        searchQuery = searchQuery.toLowerCase();

        return code.contains(searchQuery) || name.contains(searchQuery);
    }

    public void setData(HashMap<String, Object> data) throws SQLException, IOException {

        userFullName.setText((String) data.get("firstname") + " " + (String) data.get("lastname"));
        userEmail = (String) data.get("email");
        Image profileImage = (Image) data.get("profilePhoto");
        databaseConnection = (DatabaseConnection) data.get("databaseConnection");
        if (profileImage == null) {
            profileImage = new Image(getClass().getResource("../images/userIcon.png").toExternalForm(), 150, 150, true, true);
        }
        profileImageCircle.setFill(new ImagePattern(profileImage));

        myProductFunctions.initializeProductList(productsContainer, this, databaseConnection);
        
        TextField[] newArray = {productNameInput, quantity, price, manufacturer_name, product_code, low_stock_count};
        System.arraycopy(newArray, 0, myInputList, 0, 6);

        for (Node node : productsContainer.getChildren()) {
            if (node instanceof VBox) {
                productVBoxes.add(node);
            }
        }

        //currentInfoTechMethods.initializeScheduleScreenFields();
        /*
        currentInfoTechMethods.initializeButtons(genderComboBox, roleComboBox, genderSearchValue, roleSearchValue, hourComboBox, minuteComboBox);
        currentInfoTechMethods.initializeTabs(buttonPaneMap, registerBorderPane, searchBorderPane, registerSideBarHBox, searchSideBarVBox, backUpSideBarHBox, schedulePane);
        currentInfoTechMethods.initializeInputFields(registrationMap, firstNameValue, firstNameBorder, lastNameValue, lastNameBorder, emailValue, emailBorder, phoneValue, phoneBorder);
        currentInfoTechMethods.initializeInputFields(searchMap, firstNameSearchvalue, firstNameBorder1, lastNameSearchValue, lastNameBorder1, emailSearchValue, emailBorder1, phoneSearchValue, phoneBorder1);
        scheduleTime = currentInfoTechMethods.initializeScheduleTime(userEmail.getText(), hourComboBox, minuteComboBox, databaseConnection);
        timerClassToWorkWith = new timerClass(scheduleTime);
         */
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        quantity.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });

        // Add event filter to ensure only numeric input for price field
        price.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });

        // Add event filter to ensure only numeric input for low_stock_count field
        low_stock_count.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
            }
        });

        productSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue);
        });

    }

    @FXML
    private void signOut(MouseEvent event) throws IOException {
        HBox logOutButton = (HBox) event.getSource();
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        currentStage.close();
        centerScreenAccess.centerFrame(currentStage, "/posapplication/login/loginPage.fxml", databaseConnection);
    }

    @FXML
    private void addNewProduct(MouseEvent event) {
        editPageContainer.setVisible(true);
        submitButton.setVisible(false);
        listOfProductsScrollPane.setDisable(true);
        productNameInput.setText("");
        productDescription.setText("");
        quantity.setText("");
        price.setText("");
        manufacturer_name.setText("");
        product_code.setText("");
        low_stock_count.setText("");
        Image productNoPhotoImage = new Image(getClass().getResource("../images/noThumb.jpg").toExternalForm(), 150, 150, true, true);
        productImage.setFill(new ImagePattern(productNoPhotoImage));
        createProductButton.setText("Create");
    }

    @FXML
    private void cancelInfoUpdate(ActionEvent event) {
        editPageContainer.setVisible(false);
        submitButton.setVisible(true);
        listOfProductsScrollPane.setDisable(false);
        productNameInput.setText("");
        productDescription.setText("");
        quantity.setText("");
        price.setText("");
        manufacturer_name.setText("");
        product_code.setText("");
        low_stock_count.setText("");
        productImage.setFill(Color.web("#1e90ff"));
    }

    @FXML
    private void updateProductInfo(ActionEvent event) {
        if ("Create".equals(createProductButton.getText())) {
            currentInventoryMethods.createNewProduct(editPageContainer, createProductButton, listOfProductsScrollPane, productNameInput, productDescription, quantity, price, manufacturer_name, product_code, low_stock_count, production_date, expiry_date, enitreScreen, invalidDetailVBox, loadingBar, successBox, successMessage, errorMessage, myInputList, databaseConnection, productImage, this, productsContainer, productVBoxes, submitButton);

        } else {
            currentInventoryMethods.updateProductInformation(editPageContainer, createProductButton, listOfProductsScrollPane, productNameInput, productDescription, quantity, price, manufacturer_name, product_code, low_stock_count, production_date, expiry_date, enitreScreen, invalidDetailVBox, loadingBar, successBox, successMessage, errorMessage, myInputList, databaseConnection, productImage, this, productsContainer, currentProduct, submitButton);
        }

    }

    @FXML
    private void closeWarningBox(ActionEvent event) {
        enitreScreen.setDisable(false);
        invalidDetailVBox.setVisible(false);
        successBox.setVisible(false);
    }

    @FXML
    private void updateProductImage(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {
        imageChooser.uploadFile(productImage);
    }

}
