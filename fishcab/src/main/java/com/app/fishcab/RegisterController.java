package com.app.fishcab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {


    @FXML
    private Button CancelBtn;

    @FXML
    private AnchorPane Pane;

    @FXML
    private PasswordField PasswordTextField;

    @FXML
    private PasswordField ConfirmPasswordTextField;
    @FXML
    private ChoiceBox<String> RoleChoiceBox;

    @FXML
    private Button SignUpBtn;

    @FXML
    private TextField UsernameTextField;

    private Scene scene;
    private Stage stage = new Stage();
    private Parent root;
    private String[] id = {"User", "Driver"};

    String usernameInput;
    String passwordInput;
    String confirmPasswordInput;
    String role;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RoleChoiceBox.getItems().addAll(id);
    }

    public void CancelClicked (ActionEvent event) throws IOException {
        stage = (Stage) Pane.getScene().getWindow();
        stage.close();
    }

    public void SignUpClicked (ActionEvent event) throws IOException {

        System.out.println("Operation: register");
        role = RoleChoiceBox.getValue();
        System.out.println("role: " + role);
        usernameInput = UsernameTextField.getText();
        System.out.println("username: " + usernameInput);
        passwordInput = PasswordTextField.getText();
        System.out.println("password: " + passwordInput);
        confirmPasswordInput = ConfirmPasswordTextField.getText();
        System.out.println("confirm password: " + confirmPasswordInput);
        if (role == null || usernameInput.equals("") || passwordInput.equals("") || confirmPasswordInput.equals("")) {
            Alert incompleteInputAlert = new Alert(Alert.AlertType.ERROR);
            incompleteInputAlert.setTitle("Incomplete Input");
            incompleteInputAlert.setHeaderText("Please fill in all the information");
            incompleteInputAlert.setContentText("Missing values were detected");
            incompleteInputAlert.showAndWait();
            System.out.println("Error: Incomplete input");
        }else if(!passwordInput.equals(confirmPasswordInput)){
            Alert incompatiblePasswordAlert = new Alert(Alert.AlertType.ERROR);
            incompatiblePasswordAlert.setTitle("Incompatible password");
            incompatiblePasswordAlert.setHeaderText("Please make sure the passwords are the same");
            incompatiblePasswordAlert.setContentText("Enter your password again");
            incompatiblePasswordAlert.showAndWait();
            System.out.println("Error: Incompatible passwrod");
            UsernameTextField.setText("");
            PasswordTextField.setText("");
            ConfirmPasswordTextField.setText("");
        }else if (role.equalsIgnoreCase("user")) {
            System.out.println("Operation: register as user");
            // if user choose to register as user
            // jdbc
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
                java.sql.Statement sqlStatement = conn.createStatement();
                String sql = "";
                sql = "SELECT COUNT(name) FROM fishcab.user_info WHERE name = \'" + usernameInput.toLowerCase() + "\'";
                System.out.println(sql);
                ResultSet results = sqlStatement.executeQuery(sql);
                while (results.next()) {
                    Long count = (Long) results.getObject(1);
                    System.out.println(count);
                    if(count>0){
                        // the username already exist
                        Alert invalidUsernameAlert = new Alert(Alert.AlertType.ERROR);
                        invalidUsernameAlert.setTitle("Invalid Username");
                        invalidUsernameAlert.setHeaderText("The username " + usernameInput + " was already in use.");
                        invalidUsernameAlert.setContentText("Please try again");
                        invalidUsernameAlert.showAndWait();
                        System.out.println("Error: Invalid username");
                        UsernameTextField.setText("");
                        PasswordTextField.setText("");
                        ConfirmPasswordTextField.setText("");
                        break;
                    }else{
                        // the username doesn't exist in the db
                        sql = "INSERT INTO `fishcab`.`user_info` (`name`, `password`) VALUES (\'" + usernameInput.toLowerCase() + "\', \'"+passwordInput+"\')";
//                        System.out.println(sql);
                        sqlStatement.executeUpdate(sql);
                        System.out.println("Message: User egistered successfully");
                        Alert registeredAlert = new Alert(Alert.AlertType.INFORMATION);
                        registeredAlert.setTitle("Resgister Successful");
                        registeredAlert.setHeaderText("New user account created");
                        registeredAlert.setContentText("Username: " + usernameInput + "\nPassword:"+passwordInput);
                        registeredAlert.showAndWait();
                        stage = (Stage) Pane.getScene().getWindow();
                        stage.close();
                        break;
                    }
                }
                conn.close();
            } catch (Exception connectionError) {
                System.out.println("Error: class build failed");
                System.out.println(connectionError);
            }
            // end jdbc
        }else if (role.equalsIgnoreCase("driver")) {
            System.out.println("Operation: register as driver");
            // if user choose to register as driver
            // jdbc
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
                java.sql.Statement sqlStatement = conn.createStatement();
                String sql = "";
                sql = "SELECT COUNT(name) FROM fishcab.driver_info WHERE name = \'" + usernameInput.toLowerCase() + "\'";
                System.out.println(sql);
                ResultSet results = sqlStatement.executeQuery(sql);
                while (results.next()) {
                    Long count = (Long) results.getObject(1);
                    System.out.println(count);
                    if(count>0){
                        // the username already exist
                        Alert invalidUsernameAlert = new Alert(Alert.AlertType.ERROR);
                        invalidUsernameAlert.setTitle("Invalid Username");
                        invalidUsernameAlert.setHeaderText("The username " + usernameInput + " was already in use.");
                        invalidUsernameAlert.setContentText("Please try again");
                        invalidUsernameAlert.showAndWait();
                        System.out.println("Error: Invalid username");
                        UsernameTextField.setText("");
                        PasswordTextField.setText("");
                        ConfirmPasswordTextField.setText("");
                        break;
                    }else{
                        // the username doesn't exist in the db

                        SelectedDriver sd = new SelectedDriver();
                        sd.setName(usernameInput.toLowerCase());
                        sd.setPassword(passwordInput.toLowerCase());

                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DriverRegister.fxml")));
                        stage.setTitle("Driver Registration");
                        stage.setScene(new Scene(root));
                        stage.showAndWait();

                        // insert into driver login info
                        sql = "INSERT INTO `fishcab`.`driver_info` (`name`, `password`) VALUES (\'" + usernameInput.toLowerCase() + "\', \'"+passwordInput+"\')";
//                        System.out.println(sql);
                        sqlStatement.executeUpdate(sql);
                        System.out.println("Message: Driver registered successfully");
                        Alert registeredAlert = new Alert(Alert.AlertType.INFORMATION);
                        registeredAlert.setTitle("Resgister Successful");
                        registeredAlert.setHeaderText("New driver account created");
                        registeredAlert.setContentText("Username: " + usernameInput + "\nPassword:"+passwordInput+"\nYour request has been sent for approval");
                        registeredAlert.showAndWait();
                        stage = (Stage) Pane.getScene().getWindow();
                        stage.close();
                        break;
                    }
                }
                conn.close();
            } catch (Exception connectionError) {
                System.out.println("Error: class build failed");
                System.out.println(connectionError);
            }
        }
    }

}