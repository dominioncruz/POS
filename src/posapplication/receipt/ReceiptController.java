/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package posapplication.receipt;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import posapplication.components.customerOrder.CustomerOrderController;

/**
 * FXML Controller class
 *
 * @author HP PROBOOK 430 G3
 */
public class ReceiptController implements Initializable {

    @FXML
    private Label fullName;
    @FXML
    private Label todaysDate;
    @FXML
    private VBox transactionDetailsBox;
    @FXML
    private Label totalCost;
    @FXML
    private ScrollPane entireContainer;
    @FXML
    private Label paymentMethod;

    private NodePrinter printer = new NodePrinter();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setData(HashMap<String, Object> data) {
        fullName.setText((String) data.get("fullName"));
        totalCost.setText((String) data.get("totalCost"));
        paymentMethod.setText((String) data.get("paymentMethod"));
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String formattedDate = today.format(formatter);
        todaysDate.setText(formattedDate);
        StringBuilder scriptContent = new StringBuilder();

        for (Node node : ((VBox) data.get("listOfOrder")).getChildren()) {
            if (node instanceof VBox) {
                CustomerOrderController controller = (CustomerOrderController) node.getProperties().get("controller");
                Map<String, String> orderDetails = controller.getData();
                String prodict_name = orderDetails.get("product_name");
                String quantity = orderDetails.get("quantity");
                String amount = orderDetails.get("cost");
                
                HBox productEntry = new HBox();
                productEntry.setSpacing(10); // Set spacing between elements

                // Add product details
                Text productText = new Text(prodict_name + "  x" + quantity);
                productEntry.getChildren().add(productText);
                productText = new Text(amount);
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                productEntry.getChildren().add(spacer);
                productEntry.getChildren().add(productText);
                transactionDetailsBox.getChildren().add(productEntry);
            }
        }
    }

    @FXML
    private void printReceipt(MouseEvent event) {
        Rectangle printBounds = new Rectangle(entireContainer.getWidth(), entireContainer.getHeight());
        printer.setPrintRectangle(printBounds);
        
        
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean success = printer.print(job, true, entireContainer);
            if (success) {
                job.endJob(); 
            }
        }
        Stage currentStage = (Stage) entireContainer.getScene().getWindow();
        currentStage.close();
    }

}
