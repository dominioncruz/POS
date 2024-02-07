/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.models;
import java.sql.Date;
import javafx.scene.image.Image;
/**
 *
 * @author HP PROBOOK 430 G3
 */
public class order {
    private String productName;
    private String productCode;
    private String maunfacturer;
    private Date manufacturing_date;
    private Date expiry_date;
    private int quantity;
    private int price;
    private int low_stock_count;
    private Image image;
    private String description;
    private product currentProduct;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getMaunfacturer() {
        return maunfacturer;
    }

    public void setMaunfacturer(String maunfacturer) {
        this.maunfacturer = maunfacturer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getManufacturing_date() {
        return manufacturing_date;
    }

    public void setManufacturing_date(Date manufacturing_date) {
        this.manufacturing_date = manufacturing_date;
    }

    public Date getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(Date expiry_date) {
        this.expiry_date = expiry_date;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getLow_stock_count() {
        return low_stock_count;
    }

    public void setLow_stock_count(int low_stock_count) {
        this.low_stock_count = low_stock_count;
    }

    public product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(product currentProduct) {
        this.currentProduct = currentProduct;
    }
    
}
