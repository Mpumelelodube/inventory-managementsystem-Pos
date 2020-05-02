package dataModels;

import javafx.beans.property.*;

public class SalesData {

    private final StringProperty salesDate;
    private final IntegerProperty salesId;
    private final StringProperty itemId;
    private final StringProperty itemName;
    private final IntegerProperty quantity;
    private final StringProperty category;
    private final DoubleProperty sellingPrice;
    private final DoubleProperty total;
    private final IntegerProperty receiptNumber;


    public SalesData(String salesDate, Integer salesId, String itemId, String name, Integer quantity, String category,Double sellingPrice, Double total,Integer receiptNumber) {
        this.salesDate = new SimpleStringProperty(salesDate);
        this.salesId = new SimpleIntegerProperty(salesId);
        this.itemId = new SimpleStringProperty(itemId);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.itemName = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.sellingPrice = new SimpleDoubleProperty(sellingPrice);
        this.total = new SimpleDoubleProperty(total);
        this.receiptNumber = new SimpleIntegerProperty(receiptNumber);
    }

    public String getSalesDate() {
        return salesDate.get();
    }

    public StringProperty salesDateProperty() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate.set(salesDate);
    }

    public int getSalesId() {
        return salesId.get();
    }

    public IntegerProperty salesIdProperty() {
        return salesId;
    }

    public void setSalesId(int salesId) {
        this.salesId.set(salesId);
    }

    public String getItemId() {
        return itemId.get();
    }

    public StringProperty itemIdProperty() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId.set(itemId);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public String getItemName() {
        return itemName.get();
    }

    public StringProperty itemNameProperty() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public double getSellingPrice() {
        return sellingPrice.get();
    }

    public DoubleProperty sellingPriceProperty() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice.set(sellingPrice);
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public int getReceiptNumber() {
        return receiptNumber.get();
    }

    public IntegerProperty receiptNumberProperty() {
        return receiptNumber;
    }

    public void setReceiptNumber(int receiptNumber) {
        this.receiptNumber.set(receiptNumber);
    }
}
