package com.app.fishcab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewExistingDriverController implements Initializable {

    private Stage stage = new Stage();

    //Table
    @FXML
    private TableView tableView;

    //Columns
    @FXML
    private TableColumn<PendingDriver, String> driverCol;

    @FXML
    private TableColumn<PendingDriver, String> coordinateCol;

    @FXML
    private TableColumn<PendingDriver, Integer> capacityCol;

    @FXML
    private TableColumn<PendingDriver, String> statusCol;

    @FXML
    private TableColumn<PendingDriver, String> ratingCol;

    String name;
    String coordinate;
    int capacity;
    int statusInt;
    String status;
    double totalRating;
    int ratingCount;
    String rating;

    @FXML
    private AnchorPane Pane;

    private ObservableList<PendingDriver> existingDriverObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        driverCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        coordinateCol.setCellValueFactory(new PropertyValueFactory<>("coordinate"));
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        // jdbc start
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
            java.sql.Statement sqlStatement = conn.createStatement();
            String sql = "";
            sql = "SELECT * FROM fishcab.driver_active;";
            System.out.println(sql);
            ResultSet results = sqlStatement.executeQuery(sql);
            while (results.next()) {
                // start grabbing data
                name = results.getString(2).toUpperCase();
                System.out.print(name+" ");
                coordinate = results.getString(3);
                System.out.print(coordinate+" ");
                capacity = results.getInt(4);
                System.out.print(capacity+" ");
                statusInt = results.getInt(5);
                if(statusInt==0){
                    status = "Pending";
                }else{
                    status = "Available";
                }
                System.out.print(status+" ");
                totalRating = results.getDouble(6);
                System.out.print(totalRating+" ");
                ratingCount = results.getInt(7);
                System.out.print(ratingCount+" ");
                rating = String.format("%.2f", totalRating/ratingCount);
                System.out.println(rating);
                // end grabbing data
                existingDriverObservableList.add(new PendingDriver(name, coordinate, capacity, status, rating));
            }
            tableView.setItems(existingDriverObservableList);
            conn.close();
        } catch (Exception connectionError) {
            System.out.println("Error: class build failed");
            System.out.println(connectionError);
        }
    }

    // for existing driver page
    @FXML
    public void viewPendingDriver(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AdministratorMainMenu.fxml")));
        stage.setTitle("FISHcab ADMIN");
        stage.setScene(new Scene(root));
        stage.show();
        System.out.println("Operation: view existing drivers");
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    @FXML
    public void removeExistingDriver(ActionEvent event)throws IOException{
        System.out.println("Operation: remove existing driver");
        PendingDriver selectedDriver = (PendingDriver) tableView.getSelectionModel().getSelectedItem();
        ///
        Alert removeDriverAlert = new Alert(Alert.AlertType.CONFIRMATION);
        removeDriverAlert.setTitle("Remove Driver");
        removeDriverAlert.setHeaderText("Confirmation:");
        removeDriverAlert.setContentText("Are you sure you want to remove the driver's account named "+selectedDriver.getName()+" from the database?");
        if(removeDriverAlert.showAndWait().get()== ButtonType.OK){
            // jdbc
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
                java.sql.Statement sqlStatement = conn.createStatement();
                String sql = "";
                // remove driver from pending list
                //DELETE FROM `fishcab`.`driver_pending` WHERE (`id` = '4');
                sql = "DELETE FROM `fishcab`.`driver_active` WHERE (`name` = '"+selectedDriver.getName().toLowerCase()+"');";
                sqlStatement.executeUpdate(sql);
                // remove the driver from login system
                sql = "DELETE FROM `fishcab`.`driver_info` WHERE (`name` = '"+selectedDriver.getName().toLowerCase()+"');";
                sqlStatement.executeUpdate(sql);
                updateTable();
                Alert successfulAlert = new Alert(Alert.AlertType.INFORMATION);
                successfulAlert.setTitle("Removed successfully");
                successfulAlert.setHeaderText("You have successfully remove a driver");
                successfulAlert.setContentText("Name : "+selectedDriver.getName());
                successfulAlert.showAndWait();
                conn.close();
            } catch (Exception connectionError) {
                System.out.println("Error: class build failed");
                System.out.println(connectionError);
            }
        }
    }

    public void updateTable(){
        driverCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        coordinateCol.setCellValueFactory(new PropertyValueFactory<>("coordinate"));
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        existingDriverObservableList.clear();
        // jdbc start
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
            java.sql.Statement sqlStatement = conn.createStatement();
            String sql = "";
            sql = "SELECT * FROM fishcab.driver_active;";
            System.out.println(sql);
            ResultSet results = sqlStatement.executeQuery(sql);
            while (results.next()) {
                // start grabbing data
                name = results.getString(2).toUpperCase();
                System.out.print(name+" ");
                coordinate = results.getString(3);
                System.out.print(coordinate+" ");
                capacity = results.getInt(4);
                System.out.print(capacity+" ");
                statusInt = results.getInt(5);
                if(statusInt==0){
                    status = "Pending";
                }else{
                    status = "Available";
                }
                System.out.print(status+" ");
                totalRating = results.getDouble(6);
                System.out.print(totalRating+" ");
                ratingCount = results.getInt(7);
                System.out.print(ratingCount+" ");
                rating = String.format("%.2f", totalRating/ratingCount);
                System.out.println(rating);
                // end grabbing data
                existingDriverObservableList.add(new PendingDriver(name, coordinate, capacity, status, rating));
            }
            tableView.setItems(existingDriverObservableList);
            conn.close();
        } catch (Exception connectionError) {
            System.out.println("Error: class build failed");
            System.out.println(connectionError);
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        System.out.println("Operation: log out");
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        stage.setTitle("FISHcab");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
