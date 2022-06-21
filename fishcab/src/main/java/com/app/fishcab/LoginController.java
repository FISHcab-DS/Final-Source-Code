package com.app.fishcab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import java.sql.*;

public class LoginController implements Initializable {

    @FXML
    private ImageView logoIV;

    @FXML
    private Button LoginBtn;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private Button RegisterBtn;

    @FXML
    private ChoiceBox<String> RoleChoiceBox;

    @FXML
    private TextField UsernameTextField;

    @FXML
    private AnchorPane Pane;

    private Scene scene;
    private Stage stage;
    private Parent root;
    private String[] id = {"User", "Driver", "Administrator"};

    String role;
    String usernameInput;
    String username;
    String passwordInput;
    String password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RoleChoiceBox.getItems().addAll(id);
        Image fishcab = new Image("D:/intellij file/Final-Source-Code/fishcab/src/main/java/com/app/fishcab/fishcab.png");
        logoIV.setImage(fishcab);
    }

    public void LoginClicked (ActionEvent event) throws IOException {

        // check if username and password correct

        System.out.println("Operation: login");
        usernameInput = UsernameTextField.getText();
        System.out.println("username: "+usernameInput);
        passwordInput = PasswordField.getText();
        System.out.println("password: "+passwordInput);
        role = RoleChoiceBox.getValue();
        System.out.println("role: "+role);

        // ======================================

        if(role==null||usernameInput.equals("")||passwordInput.equals("")){
            System.out.println("Error: incomplete input");
            Alert emptyInputAlert = new Alert(Alert.AlertType.ERROR);
            emptyInputAlert.setTitle("Invalid Input");
            emptyInputAlert.setHeaderText("Please fill in information required");
            emptyInputAlert.setContentText("Please try again");
            emptyInputAlert.showAndWait();
        }else {

            if (role.equalsIgnoreCase("user")) {
                System.out.println("Opertion: login as user");
                // jdbc
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
                    java.sql.Statement sqlStatement = conn.createStatement();
                    String sql = "";
                    sql = "SELECT * FROM fishcab.user_info WHERE name  = \'" + usernameInput.toLowerCase() + "\'";
                    ResultSet results = sqlStatement.executeQuery(sql);
                    System.out.println("User information:");
                    while (results.next()) {
                        username = (String) results.getObject(2);
                        System.out.println("username in db: "+username);
                        password = (String) results.getObject(3);
                        System.out.println("password in db: "+password);
                    }
                    CurrentUserInfo user = new CurrentUserInfo("user", username, password);
                    conn.close();
                } catch (Exception connectionError) {
                    System.out.println("Error: class build failed");
                    System.out.println(connectionError);
                }
                // end jdbc

                if (passwordInput.equals(password)) {
                    // login successful
                    System.out.println("Operation: login for user");
                    stage = (Stage) Pane.getScene().getWindow();
                    stage.close();
                    // open user menu interface
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserMainMenu.fxml")));
                    stage.setTitle("FISHcab USER");
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    // wrong password
                    Alert wrongPasswordAlert = new Alert(Alert.AlertType.ERROR);
                    wrongPasswordAlert.setTitle("Wrong Password");
                    wrongPasswordAlert.setHeaderText("Incompatible password for user: " + usernameInput);
                    wrongPasswordAlert.setContentText("Please try again");
                    wrongPasswordAlert.showAndWait();
                    System.out.println("Error: wrong password");
                    PasswordField.setText("");
                }
            } else if (role.equalsIgnoreCase("driver")) {
                System.out.println("Operation: login as driver");
                // jdbc
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
                    java.sql.Statement sqlStatement = conn.createStatement();
                    String sql = "";
                    sql = "SELECT * FROM fishcab.driver_info WHERE name  = \'" + usernameInput.toLowerCase() + "\'";
                    ResultSet results = sqlStatement.executeQuery(sql);
                    System.out.println("Driver information:");
                    while (results.next()) {
                        username = (String) results.getObject(2);
                        System.out.println("username in db: "+username);
                        password = (String) results.getObject(3);
                        System.out.println("password in db: "+password);
                    }
                    CurrentUserInfo user = new CurrentUserInfo("driver", username, password);
                    conn.close();
                } catch (Exception connectionError) {
                    System.out.println("Error: class build failed");
                    System.out.println(connectionError);
                }
                // end jdbc

                if (passwordInput.equals(password)) {
                    // login successful
                    System.out.println("Operation: login for Driver");
                    stage = (Stage) Pane.getScene().getWindow();
                    stage.close();
                    // open user menu interface
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Driver.fxml")));
                    stage.setTitle("FISHcab DRIVER");
                    stage.setScene(new Scene(root));
                    stage.show();

                    stage = (Stage) Pane.getScene().getWindow();
                } else {
                    // wrong password
                    Alert wrongPasswordAlert = new Alert(Alert.AlertType.ERROR);
                    wrongPasswordAlert.setTitle("Wrong Password");
                    wrongPasswordAlert.setHeaderText("Incompatible password for driver: " + usernameInput);
                    wrongPasswordAlert.setContentText("Please try again");
                    wrongPasswordAlert.showAndWait();
                    System.out.println("Error: wrong password");
                    PasswordField.setText("");
                }
            } else if (role.equalsIgnoreCase("administrator")) {
            System.out.println("Operation: login as administrator");
            // jdbc
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
                java.sql.Statement sqlStatement = conn.createStatement();
                String sql = "";
                sql = "SELECT * FROM fishcab.admin_info WHERE name  = \'" + usernameInput.toLowerCase() + "\'";
                ResultSet results = sqlStatement.executeQuery(sql);
                System.out.println("Admin information:");
                while (results.next()) {
                    username = (String) results.getObject(2);
                    System.out.println("username in db: "+username);
                    password = (String) results.getObject(3);
                    System.out.println("password in db: "+password);
                }
                CurrentUserInfo user = new CurrentUserInfo("administrator", username, password);
                conn.close();
            } catch (Exception connectionError) {
                System.out.println("Error: class build failed");
                System.out.println(connectionError);
            }
            // end jdbc

            if (passwordInput.equals(password)) {
                // login successful
                System.out.println("Operation: login for Admin");
                stage = (Stage) Pane.getScene().getWindow();
                stage.close();
                // open admin menu interface

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdministratorMainMenu.fxml")));
                stage.setTitle("FISHcab ADMIN");
                stage.setScene(new Scene(root));
                stage.show();

                stage = (Stage) Pane.getScene().getWindow();
            } else {
                // wrong password
                Alert wrongPasswordAlert = new Alert(Alert.AlertType.ERROR);
                wrongPasswordAlert.setTitle("Wrong Password");
                wrongPasswordAlert.setHeaderText("Incompatible password for administrator: " + usernameInput);
                wrongPasswordAlert.setContentText("Please try again");
                wrongPasswordAlert.showAndWait();
                System.out.println("Error: wrong password");
                PasswordField.setText("");
            }
        }
        }
    }

    public void RegisterClicked (ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("Registration");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}
