package login;

import animatefx.animation.FadeIn;
import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private ObservableList values;

    @FXML
    private ComboBox departmentSelection;

    private ObservableList<String> comboData = FXCollections.observableArrayList("Admin","User");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        departmentSelection.setItems(comboData);
    }

    public void loggedUser(){
        try {
            Connection connection = DatabaseConnection.connection();
            String sql = "update logged set username = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, useName.getText());

            statement.execute();

            sql = "insert into loginhistory(username,date,status)values(?,?,'loggedIn')";

            statement = connection.prepareStatement(sql);
            statement.setString(1,useName.getText());
            statement.setString(2,getDate());
            statement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TextField useName;

    @FXML
    private TextField password;

    @FXML
    private Button loginButton;

    @FXML
    private Label label;


    @FXML
    public void login(){
        try {

            if (LoginModel.isLogin(useName.getText(),password.getText(),departmentSelection.getValue().toString())){
                Stage stage = (Stage)this.loginButton.getScene().getWindow();
                stage.close();

                loggedUser();

                switch (departmentSelection.getValue().toString()){
                    case "Admin":
                        adminLogin();
                        break;
                    case "User":
                        userLogin();
                        break;
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login");
                alert.setContentText("Error logging in please recheck your credentials");
                alert.show();
            }
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setContentText("Error logging in please recheck your credentials");
            alert.show();
        }
    }



    public void adminLogin(){
        try {
            Stage stage = new Stage();

            Parent root = (Parent) FXMLLoader.load(getClass().getResource("/admin/Admin.fxml"));

            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getResource("/admin/admin.css").toExternalForm());

            stage.setScene(scene);

            stage.setResizable(false);

            stage.setTitle("STOCK MANAGEMENT SYSTEM");

            stage.show();

            new FadeIn(root).play();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void userLogin(){
        try {
            Stage stage = new Stage();

            Parent root = (Parent) FXMLLoader.load(getClass().getResource("/employee/User.fxml"));

            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getResource("/admin/admin.css").toExternalForm());

            stage.setScene(scene);

            stage.setResizable(false);

            stage.setTitle("STOCK MANAGEMENT SYSTEM");

            stage.show();

            new FadeIn(root).play();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getDate(){

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM-dd-YYYY, hh:mm a");

        System.out.println(dtf.format(localDateTime));
        return  dtf.format(localDateTime);
    }
}
