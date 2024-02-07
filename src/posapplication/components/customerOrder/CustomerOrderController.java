/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package posapplication.components.customerOrder;

import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import posapplication.cashier.SalespersonController;
import posapplication.models.order;

/**
 * FXML Controller class
 *
 * @author HP PROBOOK 430 G3
 */
public class CustomerOrderController implements Initializable {
    SalespersonController salesController;
    private order currentOrder;
    private VBox currentOrderVBox;
    
    @FXML
    private Label productName;
    @FXML
    private Label companyName;
    @FXML
    private Label count;
    @FXML
    private Label cost;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void reduceCount(MouseEvent event) {
        if(Integer.parseInt(count.getText()) > 1){
            count.setText(String.valueOf(Integer.parseInt(count.getText()) - 1));
            cost.setText(String.valueOf(Integer.parseInt(count.getText()) * currentOrder.getPrice()));
            salesController.reducePrice(currentOrder.getPrice() );
        }else{
            Toolkit.getDefaultToolkit().beep();
        }
        
    }

    @FXML
    private void increaseCount(MouseEvent event) {
        if(currentOrder.getQuantity() > Integer.parseInt(count.getText())){
            count.setText(String.valueOf(Integer.parseInt(count.getText()) + 1));
            cost.setText(String.valueOf(Integer.parseInt(count.getText()) * currentOrder.getPrice()));
            salesController.increasePrice(currentOrder.getPrice());
        }else{
            Toolkit.getDefaultToolkit().beep();
        }
        
    }

    public void setSalesControllerReference(SalespersonController salesController) {
        this.salesController = salesController;
    }
    
     public void setSalesVBox(VBox currentVBox) {
        this.currentOrderVBox = currentVBox;
    }

    public order getCurrentOrder() {
        return currentOrder;
    }

    public void setData(order newOrder) {
        
        this.currentOrder = newOrder;
        count.setText("1");
        //quantity.setText(String.valueOf(newProduct.getQuantity()));
        productName.setText(newOrder.getProductName());
        companyName.setText(newOrder.getMaunfacturer());
        cost.setText(String.valueOf(Integer.parseInt(count.getText()) * currentOrder.getPrice()));
        salesController.increasePrice(Integer.parseInt(cost.getText()));
        //SimpleDateFormat dateFormat = new SimpleDateFormat("d MM yyyy");
        //expiry_date.setText(dateFormat.format(newProduct.getExpiry_date()));
        //product_code.setText(newProduct.getProductCode());

    }
    
    public Map<String, String> getData(){
        Map<String, String> orderDetails = new HashMap<>();
        orderDetails.put("cost", cost.getText());
        orderDetails.put("quantity", count.getText());
        orderDetails.put("product_code", currentOrder.getProductCode());
        return orderDetails;
    }

    @FXML
    private void removeItem(MouseEvent event) {
        salesController.removeCurrentOrder(currentOrderVBox, Integer.parseInt(cost.getText()));
    }
    
}
