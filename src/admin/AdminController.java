package admin;

import animatefx.animation.*;
import dataModels.*;
import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private Label loggedInAs;

    public String userName;

    public void loggedUser() {
        String sql = "select * from logged";
        try {
            Connection connection = DatabaseConnection.connection();

            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            this.userName = resultSet.getString(1);

            sql = "select firstname, lastname from users where username = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, this.userName);

            resultSet = statement.executeQuery();

            String name = null;
            String surname = null;
            while (resultSet.next()) {
                name = resultSet.getString(1);
                surname = resultSet.getString(2);
            }

            this.loggedInAs.setText(name + " " + surname);

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private Button logoutButton;

    public void logOut() throws IOException {
        new Pulse(this.logoutButton).play();

        String sql = "insert into loginhistory(username,date,status)values(?,?,'loggedOut')";
        try {
            Connection connection = DatabaseConnection.connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.userName);
            preparedStatement.setString(2, getDate());
            preparedStatement.execute();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) this.logoutButton.getScene().getWindow();
        stage.close();

        Parent root = (Parent) FXMLLoader.load(getClass().getResource("/login/LoginFXML.fxml"));

        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/login/Login.css").toExternalForm());

        stage.setScene(scene);

        stage.setTitle("STOCK MANAGEMENT SYSTEM");


        stage.show();

        new ZoomIn(root).play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String sql = "select * from category";

        try {
            Connection connection = DatabaseConnection.connection();

            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                this.combodetails.addAll(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.categoryField.setItems(this.combodetails);

        this.accessLevelCombo.setItems(this.accessComboData);

        low();

        loggedUser();

    }


    @FXML
    private TableView<ProductData> items;

    @FXML
    private TableColumn<ProductData, String> id;

    @FXML
    private TableColumn<ProductData, String> name;

    @FXML
    private TableColumn<ProductData, String> category;

    @FXML
    private TableColumn<ProductData, String> brandColumn;

    @FXML
    private TableColumn<ProductData, String> quantity;

    @FXML
    private TableColumn<ProductData, String> costPrice;

    @FXML
    private TableColumn<ProductData, String> markup;

    @FXML
    private TableColumn<ProductData, String> sellingPrice;

    @FXML
    private TableColumn<ProductData, String> damaged;

    private ObservableList<ProductData> productData;

    @FXML
    public void loadIterms() {
        try {
            this.productData = FXCollections.observableArrayList();
            Connection connection = DatabaseConnection.connection();
            String sql = "select * from iterms";

            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            System.out.println("statement");

            while (resultSet.next()) {
                this.productData.add(new ProductData(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getDouble(6), resultSet.getDouble(7), resultSet.getDouble(8), resultSet.getInt(9)));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.id.setCellValueFactory(new PropertyValueFactory<ProductData, String>("id"));
        this.name.setCellValueFactory(new PropertyValueFactory<ProductData, String>("name"));
        this.category.setCellValueFactory(new PropertyValueFactory<ProductData, String>("category"));
        this.quantity.setCellValueFactory(new PropertyValueFactory<ProductData, String>("quantity"));
        this.costPrice.setCellValueFactory(new PropertyValueFactory<ProductData, String>("costPrice"));
        this.markup.setCellValueFactory(new PropertyValueFactory<ProductData, String>("markup"));
        this.sellingPrice.setCellValueFactory(new PropertyValueFactory<ProductData, String>("sellingPrice"));
        this.damaged.setCellValueFactory(new PropertyValueFactory<ProductData, String>("damaged"));
        this.brandColumn.setCellValueFactory(new PropertyValueFactory<ProductData, String>("brand"));

        items.setItems(productData);
    }

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox categoryField;

    @FXML
    private TextField brand;


    @FXML
    private TextField quantityField;

    @FXML
    private TextField costPriceField;

    @FXML
    private TextField markupField;

    @FXML
    private TextField sellingPriceField;

    @FXML
    private Label addProductStatusLabel;

    private ObservableList<String> combodetails = FXCollections.observableArrayList();


    @FXML
    public void addIterm() {
        try {
            String sql = "insert into iterms(itermid ,name ,category ,brand, quantity ,costPrice ,markup ,sellingPrice)values(?,?,?,?,?,?,?,?)";
            Connection connection = DatabaseConnection.connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, this.idField.getText());
            preparedStatement.setString(2, this.nameField.getText());
            preparedStatement.setString(3, this.categoryField.getValue().toString());
            preparedStatement.setString(4, this.brand.getText());
            preparedStatement.setInt(5, Integer.parseInt(this.quantityField.getText()));
            preparedStatement.setDouble(6, Double.parseDouble(this.costPriceField.getText()));
            preparedStatement.setInt(7, Integer.parseInt(this.markupField.getText()));
            preparedStatement.setDouble(8, Double.parseDouble(this.sellingPriceField.getText()));

            preparedStatement.execute();


//            Dialog<ButtonType> dialog = new Dialog<>();
//            dialog.setTitle("Product Added");
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Inventory Management");
            al.setHeaderText("New Product");
            al.setContentText("Product Id:" + this.idField.getText() + "\nproduct Name: " + this.nameField.getText() + "\nCategory: " + this.categoryField.getValue().toString()
                    + "\nProduct Brand: " + this.brand.getText() + "\nQuantity: " + this.quantityField.getText() + "\nCost Price: " + this.costPriceField.getText() + "\nMark Up: " + markupField.getText()
                    + "\nSelling Price: " + this.sellingPriceField.getText());
            al.showAndWait();

            this.idField.clear();
            this.nameField.clear();
            this.brand.clear();
            this.quantityField.clear();
            this.costPriceField.clear();
            this.markupField.clear();
            this.sellingPriceField.clear();

            connection.close();

        } catch (SQLException | NumberFormatException e) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Inventory Management");
            al.setHeaderText("Error");
            al.setContentText("Error Adding the new product please\nrecheck your input data ");
            al.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private TextField deleteItemField;

    public void deleteItem(){
        String sql = "delete from iterms where itermid = ? ";

        try {
            Connection connection = DatabaseConnection.connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, this.deleteItemField.getText());

            preparedStatement.execute();

            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Inventory Management");
            al.setHeaderText("Deleted");
            al.setContentText("Product successfully deleted from database");
            al.showAndWait();

            this.deleteItemField.clear();

        } catch (SQLException | NullPointerException e) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Inventory Management");
            al.setHeaderText("Error");
            al.setContentText("Error deleting the product please\nrecheck your input data ");
            al.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private TextField stockItermId;
    @FXML
    private TextField stockQuantity;

    @FXML
    public void newStock() {
        String value = null;
        String sql = "select quantity from iterms where itermid = '" + this.stockItermId.getText() + "'";
        try {
            Connection connection = DatabaseConnection.connection();

            ResultSet resultSet = connection.createStatement().executeQuery(sql);


            value = resultSet.getString(1);


            String sql2 = "update iterms set quantity = ? where itermid = ?";
            int temp = Integer.parseInt(value) + Integer.parseInt(this.stockQuantity.getText());
            PreparedStatement statement = connection.prepareStatement(sql2);
            System.out.println(temp);
            statement.setInt(1, temp);
            statement.setString(2, this.stockItermId.getText());

            statement.execute();

            String sql3 = "insert into newstock(date,itermid,quantity) values(?,?,?)";

            statement = connection.prepareStatement(sql3);

            statement.setString(1, getDate());
            statement.setString(2, this.stockItermId.getText());
            statement.setInt(3, Integer.parseInt(this.stockQuantity.getText()));

            statement.execute();

            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Inventory Management");
            al.setHeaderText("New Stock Information");
            al.setContentText("Successfully added the new stock\n\nProduct Id: " + this.stockItermId.getText() + "\nPrevious stock quantity: " + value
                    + "\nQuantity added: " + this.stockQuantity.getText() + "\nNew Quantity Available: " + temp);
            al.showAndWait();
            this.stockItermId.clear();
            this.stockQuantity.clear();


            connection.close();


        } catch (SQLException | NumberFormatException e) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Inventory Management");
            al.setHeaderText("Error");
            al.setContentText("Error Adding new stock please\nrecheck your input data ");
            al.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private TextField drawingsIdField;
    @FXML
    private TextField drawingsQuantityField;

    @FXML
    public void damagesDrawings() {
        String value = null;
        String sql = "select damaged from iterms where itermid = '" + this.drawingsIdField.getText() + "'";
        try {
            Connection connection = DatabaseConnection.connection();

            ResultSet resultSet = connection.createStatement().executeQuery(sql);


            value = resultSet.getString(1);


            String sql2 = "update iterms set damaged = ? where itermid = ?";
            int temp = Integer.parseInt(value) + Integer.parseInt(this.drawingsQuantityField.getText());
            PreparedStatement statement = connection.prepareStatement(sql2);
            System.out.println(temp);
            statement.setInt(1, temp);
            statement.setString(2, this.drawingsIdField.getText());

            statement.execute();

            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Inventory Management");
            al.setHeaderText("Stock Update");
            al.setContentText("Update successfully Applied");
            al.showAndWait();

            connection.close();

            decreamentQuantity(this.drawingsIdField.getText(), Integer.parseInt(this.drawingsQuantityField.getText()));

            this.drawingsIdField.clear();
            this.drawingsQuantityField.clear();


        } catch (SQLException | NumberFormatException e) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Inventory Management");
            al.setHeaderText("Error");
            al.setContentText("Error Updating Damages/Drawings  please\nrecheck your input data ");
            al.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private TextField changeSellingPrice;

    @FXML
    private TextField changeSellingPriceId;

    @FXML
    public void setSellingPrice() {
        try {
            Connection connection = DatabaseConnection.connection();

            String sql2 = "update iterms set sellingprice = ? where itermid = ?";

            PreparedStatement statement = connection.prepareStatement(sql2);

            statement.setDouble(1, Double.parseDouble(this.changeSellingPrice.getText()));
            statement.setString(2, this.changeSellingPriceId.getText());

            statement.execute();

            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Inventory Management");
            al.setHeaderText("Set Selling Price");
            al.setContentText("Selling Price Successfully Updated");
            al.showAndWait();
            this.changeSellingPrice.clear();
            this.changeSellingPriceId.clear();

            connection.close();

        } catch (SQLException | NumberFormatException e) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Inventory Management");
            al.setHeaderText("Error");
            al.setContentText("Error Updating Selling Price  please\nrecheck your input data ");
            al.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private TextField changeCostPrice;

    @FXML
    private TextField changeCostPriceId;

    @FXML
    public void setCostPrice() {
        try {
            Connection connection = DatabaseConnection.connection();

            String sql2 = "update iterms set costprice = ? where itermid = ?";

            PreparedStatement statement = connection.prepareStatement(sql2);

            statement.setDouble(1, Double.parseDouble(this.changeCostPrice.getText()));
            statement.setString(2, this.changeCostPriceId.getText());

            statement.execute();

            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Inventory Management");
            al.setHeaderText("Set Cost Price");
            al.setContentText("Successfully Updated Cost Price");
            al.showAndWait();

            this.changeCostPrice.clear();
            this.changeCostPriceId.clear();

            connection.close();

        } catch (SQLException | NumberFormatException e) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Inventory Management");
            al.setHeaderText("Error");
            al.setContentText("Error Updating Cost Price  please\nrecheck your input data ");
            al.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private TextField addCategoryField;

    @FXML
    public void addCategory(ActionEvent event) {
        try {
            Connection connection = DatabaseConnection.connection();

            String sql2 = "insert into category(categoryname)values (?)";

            PreparedStatement statement = connection.prepareStatement(sql2);

            statement.setString(1, this.addCategoryField.getText());

            statement.execute();

            this.combodetails.clear();
            String sql = "select * from category";

            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                this.combodetails.addAll(resultSet.getString(1));
            }

            this.categoryField.setItems(this.combodetails);

            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Inventory Management");
            al.setHeaderText("New Category");
            al.setContentText("Successfully Added new Category: " + this.addCategoryField.getText());
            al.showAndWait();
            this.addCategoryField.clear();

            connection.close();

        } catch (SQLException | NumberFormatException e) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Inventory Management");
            al.setHeaderText("Error");
            al.setContentText("Error adding new Category  please\nrecheck your input data ");
            al.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private Label productsRunningLow;

    public void low() {
        String sql = "select count(itermid) from iterms where quantity < 20";
        int count = 0;
        try {
            Connection connection = DatabaseConnection.connection();
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            count = resultSet.getInt(1);

            this.productsRunningLow.setText(String.valueOf(count));

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void decreamentQuantity(String id, int num) {
        String sql = "select quantity from iterms where itermid = ?";
        int quantity = 0;
        try {
            Connection connection = DatabaseConnection.connection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                quantity = resultSet.getInt(1);
            }
            sql = "update iterms set quantity = ? where itermid = ?";

            System.out.println(quantity + "  " + num);

            statement = connection.prepareStatement(sql);

            statement.setString(1, String.valueOf(quantity - num));
            statement.setString(2, id);

            statement.execute();

            connection.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

///////////////////SALES SECTION////////////////////SALES SECTION////////////////////////SALES SECTION//////////////////////SALES SECTION///////////////////SALES SECTION/

    @FXML
    private TableView<SalesData> salesTable;

    @FXML
    private TableColumn<SalesData, String> salesDate;

    @FXML
    private TableColumn<SalesData, Integer> salesId;

    @FXML
    private TableColumn<SalesData, String> itermId;

    @FXML
    private TableColumn<SalesData, Integer> quan;

    @FXML
    private TableColumn<SalesData, String> columnSalesItemName;

    @FXML
    private TableColumn<SalesData, String> columnSalesCategory;

    @FXML
    private TableColumn<SalesData, String> columnSalesSellingPrice;

    @FXML
    private TableColumn<SalesData, String> columnSalestotal;

    @FXML
    private TableColumn<SalesData, String> columnReceiptNumber;

    @FXML
    public void loadSales() {
        ObservableList<SalesData> salesData = FXCollections.observableArrayList();
        String sql = "select sales.salesdate, sales.salesid,sales.itermid,iterms.name,sales.quantity,iterms.category,iterms.sellingprice,sales.total,sales.receiptnumber from sales,iterms where sales.itermid == iterms.itermid";
        try {
            Connection connection = DatabaseConnection.connection();
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                salesData.add(new SalesData(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4),
                        resultSet.getInt(5), resultSet.getString(6), resultSet.getDouble(7), resultSet.getDouble(8), resultSet.getInt(9)));
            }

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.salesDate.setCellValueFactory(new PropertyValueFactory<SalesData, String>("salesDate"));
        this.salesId.setCellValueFactory(new PropertyValueFactory<SalesData, Integer>("salesId"));
        this.itermId.setCellValueFactory(new PropertyValueFactory<SalesData, String>("itemId"));
        this.quan.setCellValueFactory(new PropertyValueFactory<SalesData, Integer>("quantity"));
        this.columnSalesCategory.setCellValueFactory(new PropertyValueFactory<SalesData, String>("category"));
        this.columnSalesItemName.setCellValueFactory(new PropertyValueFactory<SalesData, String>("itemName"));
        this.columnSalesSellingPrice.setCellValueFactory(new PropertyValueFactory<SalesData, String>("sellingPrice"));
        this.columnSalestotal.setCellValueFactory(new PropertyValueFactory<SalesData, String>("total"));
        this.columnReceiptNumber.setCellValueFactory(new PropertyValueFactory<SalesData, String>("receiptNumber"));


        this.salesTable.setItems(salesData);


    }

    @FXML
    private Label nameItem;

    @FXML
    private Label priceItem;

    @FXML
    private Label saleTotal;

    @FXML
    private Label saleQuantity;

    @FXML
    private Label calTotal;

    @FXML
    private TextField makeSaleItemId;

    @FXML
    private TextField makeSaleQuantity;

    double total;
    String itemid = null;
    int quann = 0;
    String nameX = null;
    String priceX = null;
    private double receiptTotal = 0;

    @FXML
    public void makeSale() {
        try {
            if (checkQuantity(this.makeSaleItemId.getText(), Integer.parseInt(this.makeSaleQuantity.getText()))) {
                try {
                    Connection connection = DatabaseConnection.connection();

                    String sql = "select name, sellingprice,itermid from iterms where itermid = '" + this.makeSaleItemId.getText() + "'";

                    ResultSet resultSet = connection.createStatement().executeQuery(sql);


                    this.quann = Integer.parseInt(this.makeSaleQuantity.getText());


                    while (resultSet.next()) {
                        this.nameX = resultSet.getString(1);
                        this.priceX = resultSet.getString(2);
                        this.itemid = resultSet.getString(3);
                    }

                    System.out.println(nameX + priceX);

                    total = Double.parseDouble(this.priceX) * Integer.parseInt(this.makeSaleQuantity.getText());
                    this.receiptTotal = this.receiptTotal + (Double.parseDouble(priceX) * Integer.parseInt(this.makeSaleQuantity.getText()));

                    this.nameItem.setText(nameX);
                    this.priceItem.setText(priceX);
                    this.saleQuantity.setText(this.makeSaleQuantity.getText());
                    this.saleTotal.setText(String.valueOf(this.total));
                    this.calTotal.setText(String.valueOf(this.receiptTotal));

                    connection.close();

                    generatingReceipt(this.makeSaleItemId.getText(), nameX, Double.parseDouble(priceX), Integer.parseInt(this.makeSaleQuantity.getText()), total);

                    this.makeSaleQuantity.clear();
                    this.makeSaleItemId.clear();

                } catch (NullPointerException | NumberFormatException | SQLException e) {
                    Alert al = new Alert(Alert.AlertType.ERROR);
                    al.setTitle("Inventory Management");
                    al.setHeaderText("Error");
                    al.setContentText("Error Processing the transaction  please\nrecheck your input data ");
                    al.showAndWait();
                    e.printStackTrace();
                }
            } else {
                Alert al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Inventory Management");
                al.setHeaderText("Error");
                al.setContentText("Not Enough Stock left To process this transaction\nProduct :" + nameX + "\nStock left :" + this.itermQuan);
                al.showAndWait();
            }
        } catch (NullPointerException | NumberFormatException e) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Inventory Management");
            al.setHeaderText("Error");
            al.setContentText("Error Processing the transaction  please\nrecheck your input data ");
            al.showAndWait();
            e.printStackTrace();
        }

    }

    int itermQuan = 0;

    public boolean checkQuantity(String id, int quann) {
        String sql = "select quantity,name from iterms where itermid = ?";

        try {
            Connection connection = DatabaseConnection.connection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();

            this.itermQuan = resultSet.getInt(1);
            this.nameX = resultSet.getString(2);

            connection.close();

            if (this.itermQuan - quann >= 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException | NullPointerException e) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Inventory Management");
            al.setHeaderText("Error");
            al.setContentText("Error Processing please\nrecheck your input data");
            al.showAndWait();
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    private TableView<ReceiptData> receiptDataTableView;

    @FXML
    private TableColumn<ReceiptData, String> receiptItemName;

    @FXML
    private TableColumn<ReceiptData, String> receiptItemPrice;

    @FXML
    private TableColumn<ReceiptData, String> receiptItemQuantity;

    @FXML
    private TableColumn<ReceiptData, String> receiptItemTotal;

    private ObservableList<ReceiptData> receiptData;


    public void generatingReceipt(String itemId, String name, double price, int quantity, double total) {

        this.receiptData = FXCollections.observableArrayList();

        String sql = "insert into receipt(receiptid,itermid,itermname,price,quantity,total)values(?,?,?,?,?,?)";

        try {
            Connection connection = DatabaseConnection.connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            int receiptNumber = 0;
            preparedStatement.setString(1, String.valueOf(receiptNumber));
            preparedStatement.setString(2, itemId);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, String.valueOf(price));
            preparedStatement.setString(5, String.valueOf(quantity));
            preparedStatement.setString(6, String.valueOf(total));

            preparedStatement.execute();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select itermid,itermname,price,quantity,total from receipt";

        try {
            Connection connection = DatabaseConnection.connection();
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                receiptData.add(new ReceiptData(resultSet.getString(1), resultSet.getString(2),
                        Double.parseDouble(resultSet.getString(3)), Integer.parseInt(resultSet.getString(4)),
                        Double.parseDouble(resultSet.getString(5))));
            }

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.receiptItemName.setCellValueFactory(new PropertyValueFactory<ReceiptData, String>("itemName"));
        this.receiptItemQuantity.setCellValueFactory(new PropertyValueFactory<ReceiptData, String>("price"));
        this.receiptItemPrice.setCellValueFactory(new PropertyValueFactory<ReceiptData, String>("quantity"));
        this.receiptItemTotal.setCellValueFactory(new PropertyValueFactory<ReceiptData, String>("total"));

        this.receiptDataTableView.setItems(receiptData);

    }

    public void clearReceipt() {
        this.receiptData.clear();
        this.makeSaleQuantity.clear();
        this.makeSaleItemId.clear();
        this.calTotal.setText("0.00000000");
        this.nameItem.setText("");
        this.priceItem.setText("");
        this.saleQuantity.setText("");
        this.saleTotal.setText("");
        this.receiptTotal = 0;

        try {
            Connection connection = DatabaseConnection.connection();
            String sql = "delete  from receipt where receiptid > -1";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void validateSale() {

        String receiptNumber = null;
        String sql = "select id from rr";
        try {
            Connection connection = DatabaseConnection.connection();
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {

                receiptNumber = resultSet.getString(1);

            }
            connection.close();
            System.out.println(receiptNumber + "  receipt number");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        sql = "insert into sales(salesdate,itermid,quantity,total,receiptnumber)values(?,?,?,?,?)";

        try {
            Connection connection = DatabaseConnection.connection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < this.receiptData.size(); i++) {
                ReceiptData receiptData = this.receiptData.get(i);
                preparedStatement.setString(1, getDate());
                preparedStatement.setString(2, receiptData.getItemId());
                preparedStatement.setString(3, String.valueOf(receiptData.getQuantity()));
                preparedStatement.setString(4, String.valueOf(receiptData.getTotal()));
                preparedStatement.setString(5, String.valueOf(Integer.parseInt(receiptNumber) + 1));
                preparedStatement.execute();
                decreamentQuantity(receiptData.getItemId(), receiptData.getQuantity());
            }

            sql = "insert into rr(data)values(?)";

            String[] receipt = new String[this.receiptData.size()+1];

            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < this.receiptData.size(); i++) {
                ReceiptData a = this.receiptData.get(i);
                receipt[i] = a.getItemName() + "   x" + a.getQuantity() + "   @$" + a.getPrice() + "   T$" + a.getTotal() + "\n";
            }
            receipt[this.receiptData.size()] = "receipt number: "+ (Integer.parseInt(receiptNumber)+1);


            preparedStatement.setString(1, Arrays.toString(receipt));

            preparedStatement.execute();

            this.receiptData.clear();

            sql = "delete  from receipt where receiptid > -1";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.execute();
            connection.close();

            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Inventory Management");
            al.setHeaderText("Sale Processed");
            al.setContentText("Sucessifuly sold: \n" + Arrays.toString(receipt));
            al.showAndWait();

            this.calTotal.setText("0.00000000");
            this.receiptTotal = 0;
            this.nameItem.setText("");
            this.priceItem.setText("");
            this.saleQuantity.setText("");
            this.saleTotal.setText("");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDate() {

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM-dd-YYYY hh:mm a");

        System.out.println(dtf.format(localDateTime));
        return dtf.format(localDateTime);
    }


    @FXML
    private TableView<SalesData> salesTable2;

    @FXML
    private TableColumn<SalesData, String> salesDate2;

    @FXML
    private TableColumn<SalesData, Integer> salesId2;

    @FXML
    private TableColumn<SalesData, String> itermId2;

    @FXML
    private TableColumn<SalesData, Integer> quan2;

    @FXML
    private TableColumn<SalesData, String> columnSalesItemName2;

    @FXML
    private TableColumn<SalesData, String> columnSalesCategory2;

    @FXML
    private TableColumn<SalesData, String> columnSalesSellingPrice2;

    @FXML
    private TableColumn<SalesData, String> columnSalestotal2;


    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField searchItemId;

    @FXML
    private ComboBox salesByComboBox;

    @FXML
    private RadioButton checkStartDate;

    @FXML
    private RadioButton checkEndDate;

    @FXML
    private RadioButton checkItemId;

    @FXML
    private RadioButton checkSalesBy;

    @FXML
    public void filterSales() {
        endDate.setConverter(new LocalDateStringConverter());
        ObservableList<SalesData> salesData2 = FXCollections.observableArrayList();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        if (checkStartDate.isSelected()) {
            String sql = "select sales.salesdate, sales.salesid,sales.itermid,iterms.name,sales.quantity,iterms.category," +
                    "iterms.sellingprice,sales.total,sales.receiptnumber from sales,iterms where sales.itermid == iterms.itermid" +
                    " and sales.salesdate > ?";

            try {
                Connection connection = DatabaseConnection.connection();

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, start.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println(startDate.getEditor().getText());

                while (resultSet.next()) {
                    salesData2.add(new SalesData(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4),
                            resultSet.getInt(5), resultSet.getString(6), resultSet.getDouble(7), resultSet.getDouble(8), resultSet.getInt(9)));
                }

                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            this.salesDate2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("salesDate"));
            this.salesId2.setCellValueFactory(new PropertyValueFactory<SalesData, Integer>("salesId"));
            this.itermId2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("itemId"));
            this.quan2.setCellValueFactory(new PropertyValueFactory<SalesData, Integer>("quantity"));
            this.columnSalesCategory2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("category"));
            this.columnSalesItemName2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("itemName"));
            this.columnSalesSellingPrice2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("sellingPrice"));
            this.columnSalestotal2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("total"));


            this.salesTable2.setItems(salesData2);

        } else if (checkEndDate.isSelected()) {
            String sql = "select sales.salesdate, sales.salesid,sales.itermid,iterms.name,sales.quantity,iterms.category,iterms.sellingprice,sales.total,sales.receiptnumber from sales,iterms where sales.itermid == iterms.itermid and sales.salesdate > ? and sales.salesdate < ?";

            try {
                Connection connection = DatabaseConnection.connection();

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, start.toString());
                preparedStatement.setString(2, end.toString());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    salesData2.add(new SalesData(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4),
                            resultSet.getInt(5), resultSet.getString(6), resultSet.getDouble(7), resultSet.getDouble(8), resultSet.getInt(9)));
                }

                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            this.salesDate2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("salesDate"));
            this.salesId2.setCellValueFactory(new PropertyValueFactory<SalesData, Integer>("salesId"));
            this.itermId2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("itemId"));
            this.quan2.setCellValueFactory(new PropertyValueFactory<SalesData, Integer>("quantity"));
            this.columnSalesCategory2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("category"));
            this.columnSalesItemName2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("itemName"));
            this.columnSalesSellingPrice2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("sellingPrice"));
            this.columnSalestotal2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("total"));


            this.salesTable2.setItems(salesData2);

        } else if (checkItemId.isSelected()) {
            String sql = "select sales.salesdate, sales.salesid,sales.itermid,iterms.name,sales.quantity,iterms.category,iterms.sellingprice,sales.total,sales.receiptnumber from sales,iterms " +
                    "where sales.itermid == iterms.itermid and sales.salesdate > ? and sales.salesdate < ? and sales.itermid = ?";

            try {
                Connection connection = DatabaseConnection.connection();

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, start.toString());
                preparedStatement.setString(2, end.toString());
                preparedStatement.setString(3, searchItemId.getText());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    salesData2.add(new SalesData(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4),
                            resultSet.getInt(5), resultSet.getString(6), resultSet.getDouble(7), resultSet.getDouble(8), resultSet.getInt(9)));
                }

                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            this.salesDate2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("salesDate"));
            this.salesId2.setCellValueFactory(new PropertyValueFactory<SalesData, Integer>("salesId"));
            this.itermId2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("itemId"));
            this.quan2.setCellValueFactory(new PropertyValueFactory<SalesData, Integer>("quantity"));
            this.columnSalesCategory2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("category"));
            this.columnSalesItemName2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("itemName"));
            this.columnSalesSellingPrice2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("sellingPrice"));
            this.columnSalestotal2.setCellValueFactory(new PropertyValueFactory<SalesData, String>("total"));


            this.salesTable2.setItems(salesData2);

        }


    }

    @FXML
    private Label costs;

    @FXML
    private Label sumSales;

    @FXML
    private Label grossProfit;

    @FXML
    private Label totalItems;

    public void totalSalesAndCosts() {
        String sql = "select sum(costprice) from  (select sales.itermid,iterms.costprice from sales,iterms where sales.itermid == iterms.itermid)";
        double totalCost = 0;
        double totalSales = 0;
        int Items = 0;
        try {
            Connection connection = DatabaseConnection.connection();

            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                totalCost = resultSet.getDouble(1);
            }
            System.out.println(totalCost);

            this.costs.setText(String.valueOf(totalCost));

            sql = "select sum(total) from sales";

            resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                totalSales = resultSet.getDouble(1);
            }

            this.sumSales.setText(String.valueOf(totalSales));

            this.grossProfit.setText(String.valueOf(totalSales - totalCost));

            sql = "select sum(quantity) from sales";

            resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                Items = resultSet.getInt(1);
            }

            this.totalItems.setText(String.valueOf(Items));

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    private Pane piePane;

    @FXML
    private Pane lineChartPane;

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
    private Button loadPieChart;

    @FXML
    private Button loadLineChart;

    @FXML
    public void paneArrange(ActionEvent event) {
        if (event.getSource().equals(this.salesButton)) {
            new Pulse(this.salesButton).play();
            loadSales();
            this.paneSales.toFront();
            new FadeIn(this.paneSales).play();

        } else if (event.getSource().equals(this.inventoryButton)) {
            new Pulse(this.inventoryButton).play();
            loadIterms();
            this.paneItems.toFront();
            new FadeIn(this.paneItems).play();

        } else if (event.getSource().equals(this.addInventory)) {
            new Pulse(this.addInventory).play();
            this.paneEditInventory.toFront();
            new FadeIn(this.paneEditInventory).play();

        } else if (event.getSource().equals(this.dashboard)) {
            new Pulse(this.dashboard).play();
            this.paneDashboard.toFront();
            new FadeIn(this.paneDashboard).play();

        } else if (event.getSource().equals(this.reports)) {
            new Pulse(this.reports).play();
            this.reportsPane.toFront();
            new FadeIn(this.reportsPane).play();
            totalSalesAndCosts();
            setPieChartData();

        } else if (event.getSource().equals(this.userButton)) {
            new Pulse(this.userButton).play();
            getUsers();
            getLoginData();
            this.usersPane.toFront();
            new FadeIn(this.usersPane).play();

        } else if (event.getSource().equals(this.filterSalesButton)) {
            new Pulse(this.filterSalesButton).play();

            this.filterSalesPane.toFront();
            new FadeIn(this.filterSalesPane).play();

        } else if (event.getSource().equals(this.loadPieChart)) {
            new Pulse(this.loadPieChart).play();
            this.piePane.toFront();
            new FadeIn(this.piePane).play();
        }else if (event.getSource().equals(this.loadLineChart)) {
            new Pulse(this.loadLineChart).play();
            this.lineChartPane.toFront();
            new FadeIn(this.lineChartPane).play();
        }
    }

    //////////////////////////////////////////////////////////   CHARTs  //////////////////////////////////////////////////
    @FXML
    private PieChart pieChart;

    public void setPieChartData() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        try {
            List<String> categories = FXCollections.observableArrayList();
            List<Double> values = FXCollections.observableArrayList();
            String sql = "select * from category";
            Connection connection = DatabaseConnection.connection();

            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                categories.add(resultSet.getString(1));
                System.out.println(resultSet.getString(1));
            }

            sql = "select sum(total) from (select iterms.category ,sales.itermid,sales.total from iterms,sales where" +
                    " iterms.itermid == sales.itermid) where category= ?";

            PreparedStatement statement = connection.prepareStatement(sql);


            for (int i = 0; i < categories.size(); i++) {
                statement.setString(1, categories.get(i));
                resultSet = statement.executeQuery();
                values.add(resultSet.getDouble(1));
            }
            for (int i = 0; i < categories.size(); i++) {
                data.add(new PieChart.Data(categories.get(i), values.get(i)));
            }

            this.pieChart.setData(data);

            connection.close();

        } catch (SQLException | NumberFormatException | NullPointerException e) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Inventory Management");
            al.setHeaderText("Error");
            al.setContentText("Error gathering pieChart data");
            al.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private LineChart<String, Number> lineChart;
    private ObservableList<LineChartData> lineChartData;

    public void lineChart(ActionEvent event) {

        String month[] = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};

        lineChartData = FXCollections.observableArrayList();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        XYChart.Series<String, Number> seriesCost = new XYChart.Series<>();
        XYChart.Series<String, Number> seriesProfit = new XYChart.Series<>();
        lineChart.getData().clear();


        String sql2 = "select sum(sales.total),sum(iterms.costprice) from sales,iterms where salesdate like ? and sales.itermid == iterms.itermid";
            try {
                Connection connection = DatabaseConnection.connection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql2);
                ResultSet resultSet;

                for (int i = 0; i < month.length; i++){
                    preparedStatement.setString(1,"%"+month[i]+"%");
                    resultSet = preparedStatement.executeQuery();
                    lineChartData.add(new LineChartData(month[i],resultSet.getString(1),resultSet.getString(2)));
                    System.out.println(resultSet.getString(1)+"......"+resultSet.getString(2)+"......"+month[i]);
                }

                for (LineChartData l: lineChartData){
                    if (l.getTotal() != null)series.getData().add(new XYChart.Data<>(l.getMonth(), Double.parseDouble(l.getTotal())));
                    if (l.getCost() != null)seriesCost.getData().add(new XYChart.Data<>(l.getMonth(), Double.parseDouble(l.getCost())));
                    if (l.getCost() != null && l.getTotal() != null)seriesProfit.getData().add(new XYChart.Data<>(l.getMonth(), Double.parseDouble(l.getTotal())-Double.parseDouble(l.getCost())));

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        series.setName("Sales");
        seriesProfit.setName("Profit");
        seriesCost.setName("COGS");
        this.lineChart.getData().addAll(series,seriesProfit,seriesCost);
    }

    ////////////////////////////////////////////////////////// USERS ///////////////////////////////////////////////////////
    @FXML
    private TableView<UserData> userTable;

    @FXML
    private TableColumn<UserData, String> userDataNameColumn;

    @FXML
    private TableColumn<UserData, String> userDataSurnameColumn;

    @FXML
    private TableColumn<UserData, String> userDataUsernameColumn;

    @FXML
    private TableColumn<UserData, String> userDataAccessLevelColumn;

    @FXML
    private TableColumn<UserData, String> userDataLastLoginColumn;


    public void getUsers() {
        ObservableList<UserData> userData = FXCollections.observableArrayList();
        String sql = "select firstname,lastname,username,accesslevel,lastlogin from users";
        try {
            Connection connection = DatabaseConnection.connection();
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                userData.add(new UserData(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
            }

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.userDataNameColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("firstName"));
        this.userDataSurnameColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("lastName"));
        this.userDataUsernameColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("userName"));
        this.userDataAccessLevelColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("accessLevel"));
        this.userDataLastLoginColumn.setCellValueFactory(new PropertyValueFactory<UserData, String>("lastLogin"));

        this.userTable.setItems(userData);
    }

    @FXML
    private TextField createUserNameField;

    @FXML
    private TextField createUserLastNameField;

    @FXML
    private TextField createUserUserNameField;

    @FXML
    private TextField createUserPasswordField;

    @FXML
    private ComboBox accessLevelCombo;

    private ObservableList<String> accessComboData = FXCollections.observableArrayList("Admim", "User");

    public void createUser() {
        String sql = "insert into users(firstname,lastname,username,password,accesslevel) values (?,?,?,?,?)";
        try {
            Connection connection = DatabaseConnection.connection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, this.createUserNameField.getText());
            statement.setString(2, this.createUserLastNameField.getText());
            statement.setString(3, this.createUserUserNameField.getText());
            statement.setString(4, this.createUserPasswordField.getText());
            statement.setString(5, this.accessLevelCombo.getValue().toString());

            statement.execute();

            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("User Management");
            al.setHeaderText("Creating User");
            al.setContentText("Successfully created user \nUser Name: " + this.createUserNameField.getText() + "\nLast Name: " + this.createUserLastNameField.getText()
                    + "\nUser Name: " + this.createUserUserNameField.getText() + "Access level: " + this.accessLevelCombo.getValue().toString());
            al.showAndWait();

            this.createUserUserNameField.clear();
            this.createUserNameField.clear();
            this.createUserLastNameField.clear();
            this.createUserPasswordField.clear();

            connection.close();

        } catch (SQLException | NumberFormatException e) {
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("User Management");
            al.setHeaderText("Error");
            al.setContentText("Error creating user please\nrecheck your input data ");
            al.showAndWait();
            e.printStackTrace();
        }
        getUsers();
    }

    @FXML
    private TextField deleteUserField;

    @FXML
    public void deleteUser() {
        String sql = "delete from users where username = ?";
        try {
            Connection connection = DatabaseConnection.connection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, this.deleteUserField.getText());

            statement.execute();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        getUsers();
        this.deleteUserField.clear();

    }

    @FXML
    private TableView<LoginData> loginTable;

    @FXML
    private TableColumn<LoginData, String> logedUser;

    @FXML
    private TableColumn<LoginData, String> logedDate;

    @FXML
    private TableColumn<LoginData, String> status;

    public void getLoginData() {
        ObservableList<LoginData> loginData = FXCollections.observableArrayList();
        String sql = "select * from Loginhistory";
        try {
            Connection connection = DatabaseConnection.connection();
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                loginData.add(new LoginData(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
            }

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.logedUser.setCellValueFactory(new PropertyValueFactory<LoginData, String>("userName"));
        this.logedDate.setCellValueFactory(new PropertyValueFactory<LoginData, String>("date"));
        this.status.setCellValueFactory(new PropertyValueFactory<LoginData, String>("status"));

        this.loginTable.setItems(loginData);
    }

    ///////////////////////////////////////////// CALCULATOR ///////////////////////////////////////////////////////////
    @FXML
    private Button one;

    @FXML
    private Button two;

    @FXML
    private Button three;

    @FXML
    private Button four;

    @FXML
    private Button five;

    @FXML
    private Button six;

    @FXML
    private Button seven;

    @FXML
    private Button eight;

    @FXML
    private Button nine;

    @FXML
    private Button zero;

    @FXML
    private Button plus;

    @FXML
    private Button minus;

    @FXML
    private Button divide;

    @FXML
    private Button multiply;

    @FXML
    private Button comma;

    @FXML
    private Button clear;

    @FXML
    private Button equall;

    @FXML
    private Label answerLabel;

    @FXML
    private Label numberLabel;
    double number = 0.0, number2 = 0.0;
    String operator;

    public void calculate(ActionEvent event) {
        if (event.getSource().equals(one)) {
            String num = numberLabel.getText();
            numberLabel.setText(num + "1");
        } else if (event.getSource().equals(two)) {
            String num = numberLabel.getText();
            numberLabel.setText(num + "2");
        } else if (event.getSource().equals(three)) {
            String num = numberLabel.getText();
            numberLabel.setText(num + "3");
        } else if (event.getSource().equals(four)) {
            String num = numberLabel.getText();
            numberLabel.setText(num + "4");
        } else if (event.getSource().equals(five)) {
            String num = numberLabel.getText();
            numberLabel.setText(num + "5");
        } else if (event.getSource().equals(six)) {
            String num = numberLabel.getText();
            numberLabel.setText(num + "6");
        } else if (event.getSource().equals(seven)) {
            String num = numberLabel.getText();
            numberLabel.setText(num + "7");
        } else if (event.getSource().equals(eight)) {
            String num = numberLabel.getText();
            numberLabel.setText(num + "8");
        } else if (event.getSource().equals(nine)) {
            String num = numberLabel.getText();
            numberLabel.setText(num + "9");
        } else if (event.getSource().equals(zero)) {
            String num = numberLabel.getText();
            numberLabel.setText(num + "0");
        } else if (event.getSource().equals(comma)) {
            String num = numberLabel.getText();
            numberLabel.setText(num + ".");
        } else if (event.getSource().equals(plus)) {
            try {
                number = Double.parseDouble(numberLabel.getText());
                operator = "+";
                String num = numberLabel.getText();
                numberLabel.setText(num + "+");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (event.getSource().equals(minus)) {
            number = Double.parseDouble(numberLabel.getText());
            operator = "-";
            String num = numberLabel.getText();
            numberLabel.setText(num + "-");
        } else if (event.getSource().equals(divide)) {
            number = Double.parseDouble(numberLabel.getText());
            operator = "/";
            String num = numberLabel.getText();
            numberLabel.setText(num + "/");
        } else if (event.getSource().equals(multiply)) {
            number = Double.parseDouble(numberLabel.getText());
            operator = "*";
            String num = numberLabel.getText();
            numberLabel.setText(num + "x");
        } else if (event.getSource().equals(clear)) {
            answerLabel.setText("");
            numberLabel.setText("");
        } else if (event.getSource().equals(equall)) {
//            //number2 = Double.parseDouble(numberLabel.getText());
//            operator = "+";
//            String num =numberLabel.getText();
//            numberLabel.setText(num+"+");


            switch (operator) {
                case "+":
                    String temp = numberLabel.getText();
                    int subString = temp.lastIndexOf("+");
                    number2 = Double.parseDouble(temp.substring(subString + 1));
                    answerLabel.setText(String.valueOf(number + number2));

                    break;
                case "-":
                    String temp1 = numberLabel.getText();
                    int subString1 = temp1.lastIndexOf("-");
                    number2 = Double.parseDouble(temp1.substring(subString1 + 1));
                    answerLabel.setText(String.valueOf(number - number2));
                    break;
                case "/":
                    String temp2 = numberLabel.getText();
                    int subString2 = temp2.lastIndexOf("/");
                    number2 = Double.parseDouble(temp2.substring(subString2 + 1));
                    answerLabel.setText(String.valueOf(number / number2));
                    break;
                case "*":
                    String temp3 = numberLabel.getText();
                    int subString3 = temp3.lastIndexOf("x");
                    number2 = Double.parseDouble(temp3.substring(subString3 + 1));
                    answerLabel.setText(String.valueOf(number * number2));
                    break;
            }
        }
    }
}


