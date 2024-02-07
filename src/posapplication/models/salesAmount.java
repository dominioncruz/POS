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
public class salesAmount {

    private SimpleStringProperty product;
    private SimpleStringProperty items_sold;

    public salesAmount(String product, String items_sold) {
        this.product = new SimpleStringProperty(product);
        this.items_sold = new SimpleStringProperty(items_sold);
    }
    
    public String getProduct() {
        return product.get();
    }

    public void setProduct(String product) {
        this.product.set(product);
    }

    public StringProperty productProperty() {
        return product;
    }

    public String getEmail() {
        return items_sold.get();
    }

    public void setEmail(String items_sold) {
        this.items_sold.set(items_sold);
    }

    public StringProperty items_soldProperty() {
        return items_sold;
    }
}
