package com.app.fishcab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Objects;

public class DriverRegisterController {

    @FXML
    private Button CancelButton;

    @FXML
    private TextField CapacityTextField;

    @FXML
    private Button ConfirmButton;

    @FXML
    private AnchorPane Pane;

    @FXML
    private TextField XTextField;

    @FXML
    private TextField YTextField;

    private Scene scene;
    private Stage stage;
    private Parent root;

    public void ConfirmClicked (ActionEvent event) throws IOException {

        String xCoor = XTextField.getText();
        String yCoor = YTextField.getText();
        String cap_input = CapacityTextField.getText();

        if(xCoor.equals("")||yCoor.equals("")||cap_input.equals("")){
            Alert incompleteInputAlert = new Alert(Alert.AlertType.ERROR);
            incompleteInputAlert.setTitle("Incomplete Input");
            incompleteInputAlert.setHeaderText("Please fill in all the information");
            incompleteInputAlert.setContentText("Missing values were detected");
            incompleteInputAlert.showAndWait();
            System.out.println("Error: Incomplete input");
            return;
        }

        int capacity = Integer.valueOf(cap_input);
        String coordinate = xCoor+","+yCoor;
        int status = 0;

        SelectedDriver sd = new SelectedDriver();
        sd.setCoordinate(coordinate);
        sd.setCapacity(capacity);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
            java.sql.Statement sqlStatement = conn.createStatement();
            String sql = "";
            sql = "INSERT INTO `fishcab`.`driver_pending` (`name`, `location`, `capacity`, `status`) VALUES ('"+sd.getName()+"', '"+sd.getCoordinate()+"', '"+sd.getCapacity()+"', 0);";
            System.out.println(sql);
            sqlStatement.executeUpdate(sql);

            System.out.println("Message: Driver info has been sent to pending");
            conn.close();
        } catch (Exception connectionError) {
            System.out.println("Error: class build failed");
            System.out.println(connectionError);
        }

        stage = (Stage) Pane.getScene().getWindow();
        stage.close();

    }

    public void CancelClicked (ActionEvent event) throws IOException {
        stage = (Stage) Pane.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Register.fxml")));
        stage.setTitle("FISHcab Registration");
        stage.setScene(new Scene(root));
        stage.show();
    }
}