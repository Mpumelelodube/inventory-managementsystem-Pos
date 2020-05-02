package employee;

import admin.*;
import animatefx.animation.FadeIn;
import animatefx.animation.Pulse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController extends AdminController /*implements Initializable*/ {

    @Override
    public void loggedUser() {
        super.loggedUser();
    }

    @Override
    public void logOut() throws IOException {
        super.logOut();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        low();

        loggedUser();

    }

    @Override
    public void loadIterms() {
        super.loadIterms();
    }

    @Override
    public void low() {
        super.low();
    }

    @Override
    public void decreamentQuantity(String id, int num) {
        super.decreamentQuantity(id, num);
    }

    ///////////////////SALES SECTION////////////////////SALES SECTION////////////////////////SALES SECTION//////////////////////SALES SECTION///////////////////SALES SECTION/


    @Override
    public void loadSales() {
        super.loadSales();
    }

    @Override
    public void makeSale() {
        super.makeSale();
    }

    @Override
    public boolean checkQuantity(String id, int quann) {
        return super.checkQuantity(id, quann);
    }

    @Override
    public void generatingReceipt(String itemId, String name, double price, int quantity, double total) {
        super.generatingReceipt(itemId, name, price, quantity, total);
    }

    @Override
    public void clearReceipt() {
        super.clearReceipt();
    }

    @Override
    public void validateSale() {
        super.validateSale();
    }

    @Override
    public String getDate() {
        return super.getDate();
    }

    ////////////////////////////////////////////////////PANE ARRANGEMENT////////////////////////////////////////////////

    @FXML
    private Pane paneSales;

    @FXML
    private Pane paneItems;

    @FXML
    private Pane paneDashboard;

    @FXML
    private Pane paneEditInventory;

    @FXML
    private Pane reportsPane;

    @FXML
    private Pane usersPane;

    @FXML
    private Pane filterSalesPane;

    @FXML
    private Button salesButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button addInventory;

    @FXML
    private Button dashboard;

    @FXML
    private Button reports;

    @FXML
    private Button userButton;

    @FXML
    private Button filterSalesButton;

    @FXML
    public void paneArrange(ActionEvent event){
        if (event.getSource().equals(salesButton)){
            new Pulse(salesButton).play();
            loadSales();
            paneSales.toFront();
            new FadeIn(paneSales).play();
        }
        else if (event.getSource().equals(inventoryButton)){
            new Pulse(inventoryButton).play();
            loadIterms();
            paneItems.toFront();
            new FadeIn(paneItems).play();
        }
        else if (event.getSource().equals(addInventory)){
//            new Pulse(addInventory).play();
//            paneEditInventory.toFront();
//            new FadeIn(paneEditInventory).play();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Stock Management System");
            alert.setContentText("You are not authorised to perform this action\nthis is action is for administrators only ");
            alert.show();
        }
        else if (event.getSource().equals(dashboard)){
            new Pulse(dashboard).play();
            paneDashboard.toFront();
            new FadeIn(paneDashboard).play();

        }
        else if (event.getSource().equals(reports)){
//            new Pulse(reports).play();
//            reportsPane.toFront();
//            new FadeIn(reportsPane).play();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Stock Management System");
            alert.setContentText("You are not authorised to perform this action\nthis is action is for administrators only ");
            alert.show();
        }
        else if (event.getSource().equals(userButton)){
//            new Pulse(userButton).play();
//            getUsers();
//            usersPane.toFront();
//            new FadeIn(usersPane).play();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Stock Management System");
            alert.setContentText("You are not authorised to perform this action\nthis is action is for administrators only ");
            alert.show();
        }
        else if (event.getSource().equals(filterSalesButton)){
            new Pulse(filterSalesButton).play();

            filterSalesPane.toFront();
            new FadeIn(filterSalesPane).play();
        }



    }

    ///////////////////////////////////////////// CALCULATOR ///////////////////////////////////////////////////////////

    @Override
    public void calculate(ActionEvent event) {
        super.calculate(event);
    }
}


