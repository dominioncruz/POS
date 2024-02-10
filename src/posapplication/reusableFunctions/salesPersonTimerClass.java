/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.reusableFunctions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import posapplication.cashier.SalespersonController;
import posapplication.cashier.salesPersonFunctions;
import posapplication.components.product.ProductFunctions;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class salesPersonTimerClass {
    
    private TimeThread timer;
    private Thread t1;
    private DatabaseConnection databaseConnection;
    SalespersonController salesPersonController;
    ProductFunctions myProductFunctions;
    FlowPane productsContainer;
    List<String> lowStockItems;
    List<Node> productVBoxes;
    VBox listOfMessages;
    salesPersonFunctions salesPersonfunctions;

    public salesPersonTimerClass(DatabaseConnection db, SalespersonController salesControl, ProductFunctions prodFunc, FlowPane prodCont, List<String> lowStockList, VBox messageList, salesPersonFunctions salesFunc, List<Node> prodListNode) throws ClassNotFoundException {
        this.databaseConnection = db;
        this.timer = new TimeThread();
        this.t1 = new Thread(timer);
        this.salesPersonController = salesControl;
        this.myProductFunctions = prodFunc;
        this.productsContainer = prodCont;
        this.lowStockItems = lowStockList;
        this.listOfMessages = messageList;
        this.salesPersonfunctions = salesFunc;
        this.productVBoxes = prodListNode;
        t1.start();
    }

    // Constructor modified to accept lowStockItems list
    public salesPersonTimerClass(DatabaseConnection db, SalespersonController salesControl, ProductFunctions prodFunc, FlowPane prodCont, List<String> lowStockList, VBox messageList, salesPersonFunctions salesFunc, List<Node> prodListNode, ArrayList<String> lowStockItems) throws ClassNotFoundException {
        this(db, salesControl, prodFunc, prodCont, lowStockList, messageList, salesFunc, prodListNode);
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
                            myProductFunctions.initializeProductList(productsContainer, salesPersonController, databaseConnection, lowStockItems, listOfMessages, salesPersonfunctions, productVBoxes);
                        } catch (SQLException ex) {
                            Logger.getLogger(salesPersonTimerClass.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(salesPersonTimerClass.class.getName()).log(Level.SEVERE, null, ex);
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
