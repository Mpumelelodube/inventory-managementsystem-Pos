package dataModels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserData {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty userName;
    private final StringProperty accessLevel;
    private final StringProperty lastLogin;

    public UserData(String firstName, String lastName, String userName, String accessLevel, String lastLogin) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.userName = new SimpleStringProperty(userName);
        this.accessLevel = new SimpleStringProperty(accessLevel);
        this.lastLogin = new SimpleStringProperty(lastLogin);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getAccessLevel() {
        return accessLevel.get();
    }

    public StringProperty accessLevelProperty() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel.set(accessLevel);
    }

    public String getLastLogin() {
        return lastLogin.get();
    }

    public StringProperty lastLoginProperty() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin.set(lastLogin);
    }
}
