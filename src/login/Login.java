package login;

import animatefx.animation.ZoomIn;
import database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;


public class Login extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = (Parent) FXMLLoader.load(getClass().getResource("/login/LoginFXML.fxml"));

        Scene scene = new Scene(root);

//        scene.getStylesheets().add(getClass().getResource("/admin/admin.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/login/Login.css").toExternalForm());

        stage.setScene(scene);

        stage.setTitle("STOCK MANAGEMENT SYSTEM");

        Connection connection = DatabaseConnection.connection();

        stage.show();

        new ZoomIn(root).play();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
