/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package posapplication.components.message;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import posapplication.cashier.SalespersonController;
import posapplication.infotech.InfotechController;
import posapplication.inventory.InventoryController;
import posapplication.manager.ManagerController;
import posapplication.models.message;


/**
 * FXML Controller class
 *
 * @author HP PROBOOK 430 G3
 */
public class MessageController implements Initializable {

    private InfotechController infoTechController;
    private InventoryController inventoryController;
    private ManagerController managerController;
    private SalespersonController salespersonController;
    private message currentMessage;
    private String email;
    @FXML
    private Rectangle notiicationColorRetangle;
    @FXML
    private Label notificationTime;
    @FXML
    private Label notificationTItle;
    @FXML
    private Label notificationContent;

    /**
     * Initializes the controller class.
     *
     * @param infoTechController
     */
    public void setInfoTechControllerReference(InfotechController infoTechController) {
        this.infoTechController = infoTechController;
    }
    
    public void setInventoryControllerReference(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }
    
    public void setManagerControllerReference(ManagerController managerController) {
        this.managerController = managerController;
    }
    
     public void setCashierControllerReference(SalespersonController salespersonController) {
        this.salespersonController = salespersonController;
    }
    
    

    public message getCurrentMessage() {
        return currentMessage;
    }

    private static final String[] COLORS = {
        "#1e90ff", "#dc9d00", "#de3d63", "#ff2301", "#de4c8a", "#474b4e",
        "#00bb2d", "#256d7b", "#8e402a", "#4c2f27"
    };

    public static String pickRandomColor() {
        // Create a random number generator
        Random random = new Random();

        // Select a random index from the COLORS array
        int randomIndex = random.nextInt(COLORS.length);

        // Return the color at the random index
        return COLORS[randomIndex];
    }

    public void setData(message currentMessage, String email) {
        this.currentMessage = currentMessage;
        this.email = email;
        notiicationColorRetangle.setFill(Color.web(pickRandomColor()));
        notificationTime.setText(currentMessage.getTime());
        notificationTItle.setText(currentMessage.getTitle());
        notificationContent.setText(currentMessage.getSummary());

    }

    public void openMessage(message currentMessage) {
        infoTechController.showMessage(currentMessage, email);
    }
    
    public void openInfoTechNotification(message currentMessage){
        infoTechController.showInfoOtherMessage(currentMessage);
    }
    
    public void openInventoryMessage(message currentMessage) {
        inventoryController.showMessage(currentMessage, email);
    }
    
    public void openOtherInventoryMessage(message currentMessage) {
        inventoryController.showInfoOtherMessage(currentMessage);
    }
    
    public void openManagerMessage(message currentMessage) {
        managerController.showMessage(currentMessage, email);
    }
    
     public void openOtherManagerMessage(message currentMessage) {
        managerController.showManagerOtherMessage(currentMessage);
    }
    
    
    
     public void openOtherSalesMessage(message currentMessage) {
        salespersonController.showInfoOtherMessage(currentMessage);
    }
    
    
    public void openCashierMessage(message currentMessage) {
        salespersonController.showMessage(currentMessage, email);
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
