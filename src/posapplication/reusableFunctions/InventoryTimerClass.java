package posapplication.reusableFunctions;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import posapplication.models.product;
import java.sql.ResultSet;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import posapplication.components.product.ProductFunctions;
import posapplication.inventory.InventoryController;
import posapplication.inventory.inventoryMethods;

public class InventoryTimerClass {

    private TimeThread timer;
    private Thread t1;
    private DatabaseConnection databaseConnection;
    InventoryController inventControl;
    ProductFunctions myProductFunctions;
    FlowPane productsContainer;
    List<String> lowStockItems;
    List<Node> productVBoxes;
    VBox listOfMessages;
    inventoryMethods currentInventoryMethods;

    public InventoryTimerClass(DatabaseConnection db, InventoryController inventController, ProductFunctions prodFunc, FlowPane prodCont, List<String> lowStockList, VBox messageList, inventoryMethods inventMethods, List<Node> prodListNode) throws ClassNotFoundException {
        this.databaseConnection = db;
        this.timer = new TimeThread();
        this.t1 = new Thread(timer);
        this.inventControl = inventController;
        this.myProductFunctions = prodFunc;
        this.productsContainer = prodCont;
        this.lowStockItems = lowStockList;
        this.listOfMessages = messageList;
        this.currentInventoryMethods = inventMethods;
        this.productVBoxes = prodListNode;
        t1.start();
    }

    // Constructor modified to accept lowStockItems list
    public InventoryTimerClass(DatabaseConnection db, InventoryController inventController, ProductFunctions prodFunc, FlowPane prodCont, List<String> lowStockList, VBox messageList, inventoryMethods inventMethods, List<Node> prodListNode, ArrayList<String> lowStockItems) throws ClassNotFoundException {
        this(db, inventController, prodFunc, prodCont, lowStockList, messageList, inventMethods, prodListNode);
        this.lowStockItems = lowStockItems;
    }

    public class TimeThread implements Runnable {

        private volatile boolean stopTimer = false;

        @Override
        public void run() {
            try {
                while (!stopTimer) {
                    Platform.runLater(() -> {
                        try {
                            myProductFunctions.initializeProductList(productsContainer, inventControl, databaseConnection, lowStockItems, listOfMessages, currentInventoryMethods, productVBoxes);
                        } catch (SQLException ex) {
                            Logger.getLogger(InventoryTimerClass.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(InventoryTimerClass.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    TimeUnit.SECONDS.sleep(10); // Fetch every 10 seconds
                }
            } catch (InterruptedException err) {
                System.out.println(err);
            }
        }

        public void stopTimer() {
            stopTimer = true;
        }
    }

    public void stopTimer() {
        timer.stopTimer();
    }
}
