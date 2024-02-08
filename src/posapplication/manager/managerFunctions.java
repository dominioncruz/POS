/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.manager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import posapplication.reusableFunctions.DatabaseConnection;
import java.sql.ResultSet;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import posapplication.components.message.MessageController;
import posapplication.models.employeeSales;
import posapplication.models.message;
import posapplication.models.salesAmount;
import posapplication.models.salesSummary;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class managerFunctions {

    public void getManagerNecessaryDetails(
            DatabaseConnection con,
            Label totalSaleValue,
            Rectangle bestSellerImage,
            Label bestSellerTotal,
            BarChart<String, Number> dailyBarChart,
            BarChart<String, Number> weeklyBarChart,
            BarChart<String, Number> monthlyBarChart,
            BarChart<String, Number> yearlyBarChart,
            TableView<employeeSales> cashierSalesTable,
            TableView<salesAmount> productSalesTable
    ) throws SQLException {
        String query = "SELECT u.last_name, u.email, s.product_code, s.quantity, s.amount, s.time_of_sale, p.name, p.image FROM sales s INNER JOIN products p ON s.product_code = p.product_code INNER JOIN personal_details u ON s.seller_email = u.email";

        ResultSet rs = con.fetchRelevantManagerData(query);
        getHighestSeller(rs, bestSellerImage, bestSellerTotal);
        int currentDailySale = getSalesForToday(rs);
        getDailySales(rs, dailyBarChart);
        getWeeklySales(rs, weeklyBarChart);
        getAnnualMonthsSales(rs, monthlyBarChart);
        getYearlySales(rs, yearlyBarChart);

        fillTable(rs, cashierSalesTable);
        getFiveHighestSellers(rs, productSalesTable);
        totalSaleValue.setText(String.valueOf(currentDailySale));

    }

    public void getHighestSeller(ResultSet rs, Rectangle bestSellerImage, Label bestSellerTotal) throws SQLException {
        Map<String, Integer> productAmounts = new HashMap<>();

        try {
            while (rs.next()) {
                String productName = rs.getString("name");
                int quantity = rs.getInt("quantity");

                productAmounts.put(productName, productAmounts.getOrDefault(productName, 0) + quantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String highestSeller = null;
        int highestSales = 0;
        for (Map.Entry<String, Integer> entry : productAmounts.entrySet()) {
            if (entry.getValue() > highestSales) {
                highestSeller = entry.getKey();
                highestSales = entry.getValue();
            }
        }

        rs.beforeFirst();

        while (rs.next()) {
            if (highestSeller != null && rs.getString("name").equals(highestSeller)) {
                java.sql.Blob columnValue = rs.getBlob("image");

                if (columnValue != null) {
                    byte[] imageData = columnValue.getBytes(1, (int) columnValue.length());
                    Image image = new Image(new ByteArrayInputStream(imageData));
                    Image profileImage = (Image) image;
                    bestSellerImage.setFill(new ImagePattern(profileImage));
                }

            }
        }
        bestSellerTotal.setText(String.valueOf(highestSales) + " sales!");
        rs.beforeFirst();
    }

    public int getSalesForToday(ResultSet rs) throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        int totalSaleValue = 0;

        while (rs.next()) {

            LocalDate dateFromDatabase = rs.getTimestamp("time_of_sale").toLocalDateTime().toLocalDate();

            if (dateFromDatabase.isAfter(yesterday) && dateFromDatabase.isEqual(today)) {
                totalSaleValue += rs.getInt("amount");
            }
        }
        rs.beforeFirst();

        return totalSaleValue;
    }

    public void getDailySales(ResultSet rs, BarChart<String, Number> dailyBarChart) throws SQLException {
        Map<DayOfWeek, Integer> dailyTotalSales = new TreeMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            dailyTotalSales.put(day, 0);
        }

        try {
            while (rs.next()) {
                LocalDate saleDate = rs.getTimestamp("time_of_sale").toLocalDateTime().toLocalDate();
                DayOfWeek dayOfWeek = saleDate.getDayOfWeek();
                int amount = rs.getInt("amount");

                // Only update the total sales if the amount is non-zero
                if (amount > 0) {
                    dailyTotalSales.merge(dayOfWeek, amount, Integer::sum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the chart axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create a series for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Populate the series with the daily total sales data
        for (Map.Entry<DayOfWeek, Integer> entry : dailyTotalSales.entrySet()) {
            String dayOfWeekLabel = entry.getKey().getDisplayName(TextStyle.SHORT, Locale.getDefault());
            series.getData().add(new XYChart.Data<>(dayOfWeekLabel, entry.getValue()));
        }

        rs.beforeFirst();

        // Add the series to the chart
        dailyBarChart.getData().clear();
        dailyBarChart.getData().add(series);
    }

    public void getWeeklySales(ResultSet rs, BarChart<String, Number> weeklyBarChart) throws SQLException {
        // Use ISO week fields to determine the week number
        YearMonth currentMonth = YearMonth.now();

        // Use ISO week fields to determine the week number within the current month
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfMonth();

        // Prepopulate the weeklyTotalSales map with zeros for each week of the current month
        Map<Integer, Integer> weeklyTotalSales = new TreeMap<>();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();
        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(7)) {
            int weekNumber = date.get(woy);
            weeklyTotalSales.put(weekNumber, 0);
        }

        try {
            while (rs.next()) {
                LocalDate saleDate = rs.getTimestamp("time_of_sale").toLocalDateTime().toLocalDate();
                Month saleMonth = saleDate.getMonth();
                int saleWeek = saleDate.get(woy);
                int amount = rs.getInt("amount");

                // Only process sales for the current month
                if (saleMonth == currentMonth.getMonth()) {
                    // Update the total sales for the week within the current month
                    weeklyTotalSales.merge(saleWeek, amount, Integer::sum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the chart axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create a series for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Populate the series with the weekly total sales data for the current month
        for (Map.Entry<Integer, Integer> entry : weeklyTotalSales.entrySet()) {
            String weekLabel = "Week " + entry.getKey();
            series.getData().add(new XYChart.Data<>(weekLabel, entry.getValue()));
        }

        rs.beforeFirst();

        // Clear any existing data and add the new series to the chart
        weeklyBarChart.getData().clear();
        weeklyBarChart.getData().add(series);
    }

    public void getAnnualMonthsSales(ResultSet rs, BarChart<String, Number> monthlyBarChart) throws SQLException {
        // Use a TreeMap to store the total sales for each month
        Map<Month, Integer> monthlyTotalSales = new TreeMap<>();
        for (Month month : Month.values()) {
            monthlyTotalSales.put(month, 0);
        }

        try {
            while (rs.next()) {
                LocalDate saleDate = rs.getTimestamp("time_of_sale").toLocalDateTime().toLocalDate();
                Month saleMonth = saleDate.getMonth();
                int amount = rs.getInt("amount");

                // Update the total sales for the month
                monthlyTotalSales.merge(saleMonth, amount, Integer::sum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the chart axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create a series for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Populate the series with the monthly total sales data
        for (Map.Entry<Month, Integer> entry : monthlyTotalSales.entrySet()) {
            String monthLabel = entry.getKey().getDisplayName(TextStyle.FULL, Locale.getDefault());
            series.getData().add(new XYChart.Data<>(monthLabel, entry.getValue()));
        }

        rs.beforeFirst();
        // Clear any existing data and add the new series to the chart
        monthlyBarChart.getData().clear();
        monthlyBarChart.getData().add(series);
    }

    public void getYearlySales(ResultSet rs, BarChart<String, Number> yearlyBarChart) throws SQLException {
        // Determine the range of years to include in the chart
        int startYear = Year.now().getValue() - 3; // Two years ago
        int endYear = Year.now().getValue() + 3; // Four years in the future

        // Use a TreeMap to store the total sales for each year
        Map<Integer, Integer> yearlyTotalSales = new TreeMap<>();
        for (int year = startYear; year <= endYear; year++) {
            yearlyTotalSales.put(year, 0);
        }

        try {
            while (rs.next()) {
                LocalDate saleDate = rs.getTimestamp("time_of_sale").toLocalDateTime().toLocalDate();
                int saleYear = saleDate.getYear();
                int amount = rs.getInt("amount");

                // Only process sales within the specified range of years
                if (saleYear >= startYear && saleYear <= endYear) {
                    // Update the total sales for the year
                    yearlyTotalSales.merge(saleYear, amount, Integer::sum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the chart axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create a series for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Populate the series with the yearly total sales data
        for (Map.Entry<Integer, Integer> entry : yearlyTotalSales.entrySet()) {
            String yearLabel = String.valueOf(entry.getKey());
            series.getData().add(new XYChart.Data<>(yearLabel, entry.getValue()));
        }

        rs.beforeFirst();
        // Clear any existing data and add the new series to the chart
        yearlyBarChart.getData().clear();
        yearlyBarChart.getData().add(series);
    }

    public void initialTable(
            TableColumn<employeeSales, String> lastNameColumn,
            TableColumn<employeeSales, String> emailColumn,
            TableColumn<employeeSales, String> salesColumn,
            TableColumn<employeeSales, String> totalColumn
    ) {
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        salesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        salesColumn.setCellValueFactory(new PropertyValueFactory<>("sales"));

        totalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    public void initialOtherTable(
            TableColumn<salesAmount, String> productName,
            TableColumn<salesAmount, String> sales
    ) {
        productName.setCellFactory(TextFieldTableCell.forTableColumn());
        productName.setCellValueFactory(new PropertyValueFactory<>("product"));

        sales.setCellFactory(TextFieldTableCell.forTableColumn());
        sales.setCellValueFactory(new PropertyValueFactory<>("items_sold"));
    }

    public void fillTable(ResultSet rs, TableView<employeeSales> cashierSalesTable) throws SQLException {
        cashierSalesTable.getItems().clear();
        Map<String, salesSummary> salesMap = new HashMap<>();

        while (rs.next()) {
            String lastName = rs.getString("last_name");
            String email = rs.getString("email");
            int quantity = rs.getInt("quantity");
            int amount = rs.getInt("amount");

            salesSummary currentsalesSummary = salesMap.getOrDefault(email, new salesSummary());
            currentsalesSummary.setEmployeeEmail(email);
            currentsalesSummary.setEmployeeLastName(lastName);
            currentsalesSummary.setTotalSales(currentsalesSummary.getTotalSales() + quantity);
            currentsalesSummary.setTotalEarnings(currentsalesSummary.getTotalEarnings() + amount);

            salesMap.put(email, currentsalesSummary);
        }

        ObservableList<employeeSales> existingData = FXCollections.observableArrayList(); // Initialize the list outside the loop

        for (Map.Entry<String, salesSummary> entry : salesMap.entrySet()) {
            String email = entry.getKey();
            salesSummary summary = entry.getValue();

            employeeSales salePerformance = new employeeSales(summary.getEmployeeLastName(), email, String.valueOf(summary.getTotalSales()), String.valueOf(summary.getTotalEarnings()));
            existingData.add(salePerformance);
        }

        rs.beforeFirst();
        cashierSalesTable.setItems(existingData);
    }

    public void getFiveHighestSellers(ResultSet rs, TableView<salesAmount> productSalesTable) throws SQLException {
        Map<String, Integer> productAmounts = new HashMap<>();

        // Calculate total sales for each seller
        while (rs.next()) {
            String sellerName = rs.getString("name");
            int quantity = rs.getInt("quantity");

            productAmounts.put(sellerName, productAmounts.getOrDefault(sellerName, 0) + quantity);
        }

        // Sort the sellers based on their sales quantity
        Map<Integer, String> sortedSellers = new TreeMap<>((a, b) -> b - a); // Sort in descending order
        for (Map.Entry<String, Integer> entry : productAmounts.entrySet()) {
            sortedSellers.put(entry.getValue(), entry.getKey());
        }

        // Create a list to hold the top 5 sellers
        ObservableList<salesAmount> topSellersList = FXCollections.observableArrayList();

        // Get top 5 highest sellers
        int count = 0;
        for (Map.Entry<Integer, String> entry : sortedSellers.entrySet()) {
            if (count >= 5) {
                break; // Break the loop if we have found top 5 sellers
            }
            String sellerName = entry.getValue();
            salesAmount sellerInfo = new salesAmount(sellerName, String.valueOf(entry.getKey()));
            topSellersList.add(sellerInfo);

            count++;
        }

        // Clear existing items in the TableView
        productSalesTable.getItems().clear();

        // Set the data to the table
        productSalesTable.setItems(topSellersList);
    }
    
    public void addBirthdayToView(
            ResultSet rs,
            ManagerController managerController,
            VBox messagesContainer
    ) throws SQLException, IOException {
        
        
    
        LocalTime time = LocalTime.now();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Format the LocalTime object using the formatter
        String formattedTime = time.format(formatter);
        
        String title = "Birthday" ;
        String summary = rs.getString("first_name") + "'s birthday";
        String content = "Today is " + rs.getString("first_name") + "'s birthday, do wish them a happy birthday";
        
        message currentMessage = new message();

        currentMessage.setTime(formattedTime);
        currentMessage.setTitle(title);
        currentMessage.setSummary(summary);
        currentMessage.setContent(content);

        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../components/message/message.fxml"));
            HBox currentMessageHBox = fxmlloader.load();
            MessageController currentMessageController = fxmlloader.getController();
            currentMessageController.setManagerControllerReference(managerController);
            currentMessageController.setData(currentMessage, rs.getString("email"));
            currentMessageHBox.getProperties().put("controller", currentMessageController);

            currentMessageHBox.setOnMouseClicked(event -> {
                    currentMessageController.openManagerMessage(currentMessage);
            });
            messagesContainer.getChildren().add(currentMessageHBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
