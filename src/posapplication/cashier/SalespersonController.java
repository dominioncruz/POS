/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package posapplication.cashier;

import posapplication.receipt.NodePrinter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Date;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import posapplication.components.customerOrder.CustomerOrderController;
import posapplication.components.product.ProductController;
import posapplication.components.product.ProductFunctions;
import posapplication.models.product;
import posapplication.reusableFunctions.DatabaseConnection;
import posapplication.reusableFunctions.centerScreen;
import java.sql.ResultSet;
import posapplication.models.message;
import posapplication.reusableFunctions.InventoryTimerClass;
import posapplication.reusableFunctions.salesPersonTimerClass;

/**
 * FXML Controller class
 *
 * @author HP PROBOOK 430 G3
 */
public class SalespersonController implements Initializable {

    String userEmail;
    private NodePrinter printer = new NodePrinter();
    private final centerScreen centerScreenAccess;
    static DatabaseConnection databaseConnection;
    public List<Node> productVBoxes = new ArrayList<>();
    List<String> lowStockItems = new ArrayList<>();
    ProductFunctions myProductFunctions;
    salesPersonFunctions salesPersonfunctions;
    salesPersonTimerClass timerClassToWorkWith;
    @FXML
    private BorderPane entireScreen;
    @FXML
    private HBox navBarCart;
    @FXML
    private HBox shoppingPage;
    @FXML
    private TextField productSearchBar;
    @FXML
    private Label userFullName;
    @FXML
    private Circle profileImageCircle;
    @FXML
    private ScrollPane listOfProductsScrollPane;
    @FXML
    private FlowPane productsContainer;
    @FXML
    private VBox emailBorder;
    @FXML
    private TextField customerName;
    @FXML
    private ScrollPane listOfOrderScrollPane;
    @FXML
    private VBox listOfOrderVBox;
    @FXML
    private Label totalCost;
    @FXML
    private Label cashPaymentMethod;
    @FXML
    private Label debitPaymentMethod;
    @FXML
    private Label transferPaymentMethod;

    public List<Label> paymentMethods = new ArrayList<>();
    @FXML
    private VBox invalidDetailVBox;
    @FXML
    private Label errorMessage;
    @FXML
    private ProgressIndicator loadingBar;
    @FXML
    private VBox printOpionPopUp;
    @FXML
    private Label successMessage1;
    @FXML
    private VBox sideCustomerOrder;
    @FXML
    private Button completeButton;
    @FXML
    private VBox openShopPage;
    @FXML
    private VBox openMessagesPage;
    @FXML
    private VBox messagesContainer;
    @FXML
    private VBox birthdayCard;
    @FXML
    private Label userNameBirthdayMessage;
    @FXML
    private VBox messageBox;
    @FXML
    private Label messageTitle;
    @FXML
    private Label messageSummary;
    @FXML
    private Label messageContent;

    public SalespersonController() throws ClassNotFoundException {
        this.myProductFunctions = new ProductFunctions();
        this.salesPersonfunctions = new salesPersonFunctions();
        this.centerScreenAccess = new centerScreen();
    }

    private void filterProducts(String searchQuery) {
        productsContainer.getChildren().clear();
        if (searchQuery.equals("")) {
            for (Node node : productVBoxes) {
                if (node instanceof StackPane productVBox) {
                    ProductController productController = (ProductController) productVBox.getProperties().get("controller");
                    productsContainer.getChildren().add(productVBox);  // Add matching products
                }
            }

        } else {
            for (Node node : productVBoxes) {
                if (node instanceof StackPane productVBox) {
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

    public void increasePrice(int value) {
        totalCost.setText(String.valueOf(Integer.parseInt(totalCost.getText()) + value));
    }

    public void reducePrice(int value) {
        totalCost.setText(String.valueOf(Integer.parseInt(totalCost.getText()) - value));
    }

    public void setData(HashMap<String, Object> data) throws SQLException, IOException, ClassNotFoundException {

        userFullName.setText((String) data.get("firstname") + " " + (String) data.get("lastname"));
        userEmail = (String) data.get("email");
        Image profileImage = (Image) data.get("profilePhoto");
        databaseConnection = (DatabaseConnection) data.get("databaseConnection");
        if (profileImage == null) {
            profileImage = new Image(getClass().getResource("../images/userIcon.png").toExternalForm(), 150, 150, true, true);
        }
        profileImageCircle.setFill(new ImagePattern(profileImage));

        myProductFunctions.initializeProductList(productsContainer, this, databaseConnection, lowStockItems, messagesContainer, salesPersonfunctions, productVBoxes);

        ResultSet rs = databaseConnection.getBirthdays();
        while (rs.next()) {
            salesPersonfunctions.addBirthdayToView(rs, this, messagesContainer);
        }

        rs = databaseConnection.getBirthdays();
        while (rs.next()) {
            salesPersonfunctions.addBirthdayToView(rs, this, messagesContainer);
        }

        rs = databaseConnection.getAllExpiringProducts();
        while (rs.next()) {
            salesPersonfunctions.addNewInfoToMessagses("Expiry", "Product expiration", "The product: " + rs.getString("name") + " is about to expire. It expires on "  + rs.getDate("expiry_date"), this, messagesContainer);
        }

        timerClassToWorkWith = new salesPersonTimerClass(databaseConnection, this, myProductFunctions, productsContainer, lowStockItems, messagesContainer, salesPersonfunctions, productVBoxes);

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

    ;
     
     
    public void addToCart(
            String productCode,
            String description,
            Date expiry_date,
            Image image,
            int low_stock_count,
            Date manufacturing_date,
            String maunfacturer,
            int price,
            String productName,
            int quantity,
            product newProduct
    ) throws SQLException, IOException {
        completeButton.setDisable(false);
        salesPersonfunctions.addNewProductToCart(
                productCode,
                productName,
                maunfacturer,
                manufacturing_date,
                expiry_date,
                quantity,
                price,
                image,
                low_stock_count,
                description,
                this,
                listOfOrderVBox,
                newProduct
        );
    }

    ;
    
    public void removeCurrentOrder(VBox currentProduct, int cost) {
        totalCost.setText(String.valueOf(Integer.parseInt(totalCost.getText()) - cost));
        listOfOrderVBox.getChildren().remove(currentProduct);
        if (listOfOrderVBox.getChildren().isEmpty()) {
            completeButton.setDisable(true);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        productSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue);
        });

        paymentMethods.add(cashPaymentMethod);
        paymentMethods.add(debitPaymentMethod);
        paymentMethods.add(transferPaymentMethod);
    }

    @FXML
    private void signOut(MouseEvent event) throws IOException {
        timerClassToWorkWith.stopTimer();
        HBox logOutButton = (HBox) event.getSource();
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        currentStage.close();
        centerScreenAccess.centerFrame(currentStage, "/posapplication/login/loginPage.fxml", databaseConnection);
    }

    @FXML
    private void togglePassword(MouseEvent event) {
    }

    @FXML
    private void completeTransaction(ActionEvent event) throws SQLException {
        try {
            entireScreen.setDisable(true);
            loadingBar.setVisible(true);
            String buyerName = customerName.getText();
            String paymentMethodUsed = null;
            String product_code = null;
            String quantity = null;
            String amount = null;
            for (Label paymentMethod : paymentMethods) {
                if (paymentMethod.getStyleClass().contains("selected_payment_method")) {
                    paymentMethodUsed = paymentMethod.getText();
                }
            }
            boolean result = true;

            for (Node node : listOfOrderVBox.getChildren()) {
                if (node instanceof VBox) {
                    CustomerOrderController controller = (CustomerOrderController) node.getProperties().get("controller");
                    Map<String, String> orderDetails = controller.getData();
                    product_code = orderDetails.get("product_code");
                    quantity = orderDetails.get("quantity");
                    amount = orderDetails.get("cost");
                    result = salesPersonfunctions.addNewSale(buyerName, product_code, quantity, amount, paymentMethodUsed, userEmail, databaseConnection);
                }
            }
            if (result) {
                salesPersonfunctions.addNewInfoToMessagses("Sales", "New transaction", "You made a successful sale, total cost: " + totalCost.getText(), this, messagesContainer);
                printOpionPopUp.setVisible(true);
            } else {
                errorMessage.setText("An error occured somewhere");
                invalidDetailVBox.setVisible(true);
            }
        } catch (Exception e) {
            errorMessage.setText("An error occured somewhere");
            invalidDetailVBox.setVisible(true);
        } finally {
            loadingBar.setVisible(false);
        }

    }

    @FXML
    private void toggloPaymentMethod(MouseEvent event) {
        for (Label paymentMethod : paymentMethods) {
            paymentMethod.getStyleClass().remove("selected_payment_method");
            if (event.getSource() == paymentMethod) {
                paymentMethod.getStyleClass().add("selected_payment_method");
            }
        }
    }

    @FXML
    private void closeWarningBox(ActionEvent event) throws SQLException, IOException {
        printOpionPopUp.setVisible(false);
        invalidDetailVBox.setVisible(false);
        entireScreen.setDisable(false);

        listOfOrderVBox.getChildren().clear();
        completeButton.setDisable(true);

        for (Label paymentMethod : paymentMethods) {
            paymentMethod.getStyleClass().remove("selected_payment_method");
        }
        cashPaymentMethod.getStyleClass().add("selected_payment_method");
        totalCost.setText("0");
        customerName.setText("");
        myProductFunctions.initializeProductList(productsContainer, this, databaseConnection, lowStockItems, messagesContainer, salesPersonfunctions, productVBoxes);
    }

    @FXML
    private void printReceipt(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        printOpionPopUp.setVisible(false);
        entireScreen.setDisable(false);
        String paymentMethodUsed = "";
        for (Label paymentMethod : paymentMethods) {
                if (paymentMethod.getStyleClass().contains("selected_payment_method")) {
                    paymentMethodUsed = paymentMethod.getText();
                }
            }
        Stage currentStage = (Stage) messageContent.getScene().getWindow();
        centerScreen currentScreen = new centerScreen();
        currentScreen.showReceipt(
                currentStage, 
                "/posapplication/receipt/receipt.fxml", 
                customerName.getText(),
                listOfOrderVBox, 
                paymentMethodUsed,
                totalCost.getText()
        );
        listOfOrderVBox.getChildren().clear();
        completeButton.setDisable(true);

        for (Label paymentMethod : paymentMethods) {
            paymentMethod.getStyleClass().remove("selected_payment_method");
        }
        cashPaymentMethod.getStyleClass().add("selected_payment_method");
        totalCost.setText("0");
        customerName.setText("");
        myProductFunctions.initializeProductList(productsContainer, this, databaseConnection, lowStockItems, messagesContainer, salesPersonfunctions, productVBoxes);
    }

    public void showMessage(message currentMessage, String email) {
        birthdayCard.setVisible(false);
        messageBox.setVisible(false);

        if (email.equals(userEmail)) {
            birthdayCard.setVisible(true);
        } else {

            messageTitle.setText(currentMessage.getTitle());
            messageSummary.setText(currentMessage.getSummary());
            messageContent.setText(currentMessage.getContent());
            messageBox.setVisible(true);

        }

    }

    public void showInfoOtherMessage(message currentMessage) {
        birthdayCard.setVisible(false);
        messageBox.setVisible(false);

        {
            messageTitle.setText(currentMessage.getTitle());
            messageSummary.setText(currentMessage.getSummary());
            messageContent.setText(currentMessage.getContent());
            messageBox.setVisible(true);
        }

    }

    @FXML
    private void closeMessages(MouseEvent event) {
        birthdayCard.setVisible(false);
        messageBox.setVisible(false);
    }

}
