/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package posapplication.manager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import posapplication.cashier.salesPersonFunctions;
import posapplication.components.product.ProductFunctions;
import posapplication.models.employeeSales;
import posapplication.models.salesAmount;
import posapplication.reusableFunctions.DatabaseConnection;
import posapplication.reusableFunctions.centerScreen;
import java.sql.ResultSet;
import posapplication.models.message;
import posapplication.reusableFunctions.managerTimerClass;

/**
 * FXML Controller class
 *
 * @author HP PROBOOK 430 G3
 */
public class ManagerController implements Initializable {

    String userEmail;
    static DatabaseConnection databaseConnection;
    managerFunctions currentManagerFunctions;
    private final centerScreen centerScreenAccess;
    managerTimerClass timerClassToWorkWith;

    @FXML
    private Circle profilePicture;
    @FXML
    private Label fullName;
    @FXML
    private BarChart<String, Number> dailyBarChart;
    @FXML
    private BarChart<String, Number> weeklyBarChart;
    @FXML
    private BarChart<String, Number> monthlyBarChart;
    @FXML
    private BarChart<String, Number> yearlyBarChart;
    @FXML
    private TableView<employeeSales> cashierSalesTable;
    @FXML
    private Rectangle bestSellerImage;
    @FXML
    private Label bestSellerTotal;
    @FXML
    private Label totalDailyEarning;
    @FXML
    private Button dailySalesButton;
    @FXML
    private Button weeklySalesButton;
    @FXML
    private Button monthlySalesButton;
    @FXML
    private Button yearlySalesButton;
    @FXML
    private TableColumn<employeeSales, String> name;
    @FXML
    private TableColumn<employeeSales, String> email_address;
    @FXML
    private TableColumn<employeeSales, String> sales;
    @FXML
    private TableColumn<employeeSales, String> total;
    @FXML
    private TableView<salesAmount> productSalesTable;
    @FXML
    private TableColumn<salesAmount, String> product;
    @FXML
    private TableColumn<salesAmount, String> items_sold;
    @FXML
    private HBox entireScreen;
    @FXML
    private VBox messagesVBox;
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

    public ManagerController() throws ClassNotFoundException {
        this.currentManagerFunctions = new managerFunctions();
        this.centerScreenAccess = new centerScreen();
    }

    public void setData(HashMap<String, Object> data) throws IOException, SQLException, ClassNotFoundException {

        fullName.setText((String) data.get("firstname") + " " + (String) data.get("lastname"));
        userEmail = (String) data.get("email");
        Image profileImage = (Image) data.get("profilePhoto");
        databaseConnection = (DatabaseConnection) data.get("databaseConnection");
        if (profileImage == null) {
            profileImage = new Image(getClass().getResource("../images/userIcon.png").toExternalForm(), 150, 150, true, true);
        }
        profilePicture.setFill(new ImagePattern(profileImage));

        currentManagerFunctions.getManagerNecessaryDetails(databaseConnection, totalDailyEarning, bestSellerImage, bestSellerTotal, dailyBarChart, weeklyBarChart, monthlyBarChart, yearlyBarChart, cashierSalesTable, productSalesTable);
        
        ResultSet rs = databaseConnection.getBirthdays();
        while (rs.next()) {
            currentManagerFunctions.addBirthdayToView(rs, this, messagesVBox);
        }
        
        rs = databaseConnection.getAllExpiringProducts();
        while (rs.next()) {
            currentManagerFunctions.addNewInfoToMessagses("Expiry", "Product expiration", "The product: " + rs.getString("name") + " is about to expire. It expires on "  + rs.getDate("expiry_date"), this, messagesVBox);
        }
        
        timerClassToWorkWith = new managerTimerClass(databaseConnection, currentManagerFunctions, totalDailyEarning, bestSellerImage, bestSellerTotal, dailyBarChart, weeklyBarChart, monthlyBarChart, yearlyBarChart, cashierSalesTable, productSalesTable);
    }

    ;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentManagerFunctions.initialTable(name, email_address , sales, total);
        currentManagerFunctions.initialOtherTable(product, items_sold);
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
    private void toggleDailySales(ActionEvent event) {
        dailySalesButton.getStyleClass().add("selectedButton");
        weeklySalesButton.getStyleClass().remove("selectedButton");
        monthlySalesButton.getStyleClass().remove("selectedButton");
        yearlySalesButton.getStyleClass().remove("selectedButton");
        dailyBarChart.setVisible(true);
        weeklyBarChart.setVisible(false);
        monthlyBarChart.setVisible(false);
        yearlyBarChart.setVisible(false);
    }

    @FXML
    private void toggleWeeklySales(ActionEvent event) {
        dailySalesButton.getStyleClass().remove("selectedButton");
        weeklySalesButton.getStyleClass().add("selectedButton");
        monthlySalesButton.getStyleClass().remove("selectedButton");
        yearlySalesButton.getStyleClass().remove("selectedButton");
        dailyBarChart.setVisible(false);
        weeklyBarChart.setVisible(true);
        monthlyBarChart.setVisible(false);
        yearlyBarChart.setVisible(false);
    }

    @FXML
    private void toggleMonthlySales(ActionEvent event) {
        dailySalesButton.getStyleClass().remove("selectedButton");
        weeklySalesButton.getStyleClass().remove("selectedButton");
        monthlySalesButton.getStyleClass().add("selectedButton");
        yearlySalesButton.getStyleClass().remove("selectedButton");
        dailyBarChart.setVisible(false);
        weeklyBarChart.setVisible(false);
        monthlyBarChart.setVisible(true);
        yearlyBarChart.setVisible(false);
    }

    @FXML
    private void toggleYearlySales(ActionEvent event) {
        dailySalesButton.getStyleClass().remove("selectedButton");
        weeklySalesButton.getStyleClass().remove("selectedButton");
        monthlySalesButton.getStyleClass().remove("selectedButton");
        yearlySalesButton.getStyleClass().add("selectedButton");
        dailyBarChart.setVisible(false);
        weeklyBarChart.setVisible(false);
        monthlyBarChart.setVisible(false);
        yearlyBarChart.setVisible(true);
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
    
    public void showManagerOtherMessage(message currentMessage) {
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
