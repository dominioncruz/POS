/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.reusableFunctions;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.shape.Rectangle;
import posapplication.manager.managerFunctions;
import posapplication.models.employeeSales;
import posapplication.models.salesAmount;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class managerTimerClass {

    private final TimeThread timer;
    private final Thread t1;
    private final DatabaseConnection databaseConnection;
    managerFunctions currentManagerFunctions;
    Label totalDailyEarning;
    Rectangle bestSellerImage;
    Label bestSellerTotal;
    BarChart<String, Number> dailyBarChart;
    BarChart<String, Number> weeklyBarChart;
    BarChart<String, Number> monthlyBarChart;
    BarChart<String, Number> yearlyBarChart;
    TableView<employeeSales> cashierSalesTable;
    TableView<salesAmount> productSalesTable;

    public managerTimerClass(
            DatabaseConnection db,
            managerFunctions managerfunc,
            Label totalDaily,
            Rectangle sellerImage,
            Label sellerTotal,
            BarChart<String, Number> daily,
            BarChart<String, Number> weekly,
            BarChart<String, Number> monthly,
            BarChart<String, Number> yearly,
            TableView<employeeSales> cashier,
            TableView<salesAmount> product
    ) throws ClassNotFoundException {

        this.databaseConnection = db;
        this.currentManagerFunctions = managerfunc;
        this.totalDailyEarning = totalDaily;
        this.bestSellerImage = sellerImage;
        this.timer = new TimeThread();
        this.t1 = new Thread(timer);
        this.bestSellerTotal = sellerTotal;
        this.dailyBarChart = daily;
        this.weeklyBarChart = weekly;
        this.monthlyBarChart = monthly;
        this.yearlyBarChart = yearly;
        this.cashierSalesTable = cashier;
        this.productSalesTable = product;
        t1.start();
    }

    public class TimeThread implements Runnable {

        private volatile boolean stopTimer = false;

        @Override
        public void run() {
            try {
                while (!stopTimer) {
                    Platform.runLater(() -> {
                        try {
                            currentManagerFunctions.getManagerNecessaryDetails(databaseConnection, totalDailyEarning, bestSellerImage, bestSellerTotal, dailyBarChart, weeklyBarChart, monthlyBarChart, yearlyBarChart, cashierSalesTable, productSalesTable);
                        } catch (SQLException ex) {
                            Logger.getLogger(managerTimerClass.class.getName()).log(Level.SEVERE, null, ex);
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
