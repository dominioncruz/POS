/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class employeeSales {

    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
    private SimpleStringProperty sales;
    private SimpleStringProperty total;

    public employeeSales(String lastName, String email, String sales, String total) {
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.sales = new SimpleStringProperty(sales);
        this.total = new SimpleStringProperty(total);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getSales() {
        return sales.get();
    }

    public void setSales(String sales) {
        this.sales.set(sales);
    }

    public StringProperty salesProperty() {
        return sales;
    }

    public String getTotal() {
        return total.get();
    }

    public void setTotal(String total) {
        this.total.set(total);
    }

    public StringProperty totalProperty() {
        return total;
    }

}
