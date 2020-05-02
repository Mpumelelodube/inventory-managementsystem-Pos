package dataModels;

import javafx.beans.property.*;

public class ProductData {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty category;
    private final StringProperty brand;
    private final IntegerProperty quantity;
    private final DoubleProperty costPrice;
    private final DoubleProperty markup;
    private final DoubleProperty sellingPrice;
    private final IntegerProperty damaged;

    public ProductData(String id, String name, String category, String brand,
                       Integer quantity, Double costPrice, Double markup,
                       Double sellingPrice, Integer damaged) {

        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name) ;
        this.category = new SimpleStringProperty(category) ;
        this.quantity = new SimpleIntegerProperty(quantity);
        this.costPrice = new SimpleDoubleProperty(costPrice);
        this.markup = new SimpleDoubleProperty(markup);
        this.sellingPrice = new SimpleDoubleProperty(sellingPrice);
        this.damaged = new SimpleIntegerProperty(damaged);
        this.brand = new SimpleStringProperty(brand);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
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

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public double getCostPrice() {
        return costPrice.get();
    }

    public DoubleProperty costPriceProperty() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice.set(costPrice);
    }

    public double getMarkup() {
        return markup.get();
    }

    public DoubleProperty markupProperty() {
        return markup;
    }

    public void setMarkup(double markup) {
        this.markup.set(markup);
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

    public int getDamaged() {
        return damaged.get();
    }

    public IntegerProperty damagedProperty() {
        return damaged;
    }

    public void setDamaged(int damaged) {
        this.damaged.set(damaged);
    }

    public String getBrand() {
        return brand.get();
    }

    public StringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }
}
