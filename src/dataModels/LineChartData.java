package dataModels;

import javafx.beans.property.*;

public class LineChartData {
    private final StringProperty month;
    private final StringProperty total;
    private final StringProperty cost;

    public LineChartData(String month, String total, String cost) {
        this.month = new SimpleStringProperty(month);
        this.total = new SimpleStringProperty(total);
        this.cost = new SimpleStringProperty(cost);
    }

    public String getMonth() {
        return month.get();
    }

    public StringProperty monthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public String getTotal() {
        return total.get();
    }

    public StringProperty totalProperty() {
        return total;
    }

    public void setTotal(String total) {
        this.total.set(total);
    }

    public String getCost() {
        return cost.get();
    }

    public StringProperty costProperty() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost.set(cost);
    }
}
