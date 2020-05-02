package login;

import database.DatabaseConnection;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    public static boolean isLogin(String useName, String password, String departmentSelection){
        String sql = "select * from users where username = ? and password = ? and accesslevel = ?";

        try {
            Connection connection = DatabaseConnection.connection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, useName);
            statement.setString(2,password);
            statement.setString(3, departmentSelection);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                connection.close();
                return true;
            }

        } catch (SQLException | NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setContentText("Error logging in please recheck your credentials");
            alert.show();
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
