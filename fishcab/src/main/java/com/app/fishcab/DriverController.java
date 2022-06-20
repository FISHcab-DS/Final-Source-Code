package com.app.fishcab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class DriverController implements Initializable {
    @FXML
    private TableView  dl_tableView;
    @FXML
    private TableView  cl_tableView;

    @FXML
    private TableColumn<PendingDriver, String> dl_driverCol;
    @FXML
    private TableColumn<PendingDriver, String> dl_statusCol;
    @FXML
    private TableColumn<PendingDriver, Integer> dl_capacityCol;
    @FXML
    private TableColumn<PendingDriver, String> dl_coordinateCol;
    @FXML
    private TableColumn<PendingDriver, String> dl_ratingCol;
    @FXML
    private TableColumn<PendingDriver, String> dl_customerCol;
    ////////// CL ////////////
    @FXML
    private TableColumn<Customer, String> cl_customerCol;
    @FXML
    private TableColumn<Customer, String> cl_statusCol;
    @FXML
    private TableColumn<Customer, Integer> cl_capacityCol;
    @FXML
    private TableColumn<Customer, String> cl_eatCol;
    @FXML
    private TableColumn<Customer, String> cl_pulCol;
    @FXML
    private TableColumn<Customer, String> cl_destinationCol;
    @FXML
    private TableColumn<Customer, String> cl_priceCol;
    /////////////////////
    @FXML
    private Button btCustomer;

    @FXML
    private HBox banner;

    @FXML
    private Label list;
    @FXML
    private Button btDriver;

    @FXML
    private GridPane pnCustomer;

    @FXML
    private GridPane pnDriver;

//    private Scene scene;
    private Stage stage = new Stage();
//    private Parent root;

//    @FXML
//    private AnchorPane Pane;

//    @FXML
//    private Button driverLogoutBtn;

    public void handleCLicks(ActionEvent event){
        if(event.getSource() == btDriver){
            list.setText("Driver List");
            banner.setBackground(new Background(new BackgroundFill(Color.rgb(159, 118, 255), CornerRadii.EMPTY, Insets.EMPTY)));
            pnDriver.toFront();

        } else if (event.getSource()==btCustomer) {
            list.setText("Customer List");
            banner.setBackground(new Background(new BackgroundFill(Color.rgb(42, 28, 66), CornerRadii.EMPTY, Insets.EMPTY)));
            pnCustomer.toFront();
        }

    }

    public void logout(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        stage.setTitle("FISHcab");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private ObservableList<PendingDriver> driverObservableList = FXCollections.observableArrayList();
    private ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

    String name;
    int statusInt;
    String status;
    int capacity;
    String coordinate;
    double totalRating;
    int ratingCount;
    String rating;
    String customer;

    String eat;
    String pickup;
    String destination;
    String price;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // display driver list
        dl_driverCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dl_statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dl_capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        dl_coordinateCol.setCellValueFactory(new PropertyValueFactory<>("coordinate"));
        dl_ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        dl_customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        // jdbc start
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
            java.sql.Statement sqlStatement = conn.createStatement();
            String sql = "";
            sql = "SELECT * FROM fishcab.driver_active;";
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
                    status = "Driving";
                }else{
                    status = "Available";
                }
                System.out.print(status+" ");
                totalRating = results.getDouble(6);
                System.out.print(totalRating+" ");
                ratingCount = results.getInt(7);
                System.out.print(ratingCount+" ");
                rating = String.format("%.2f", totalRating/ratingCount);
                System.out.print(rating+" ");
                customer = results.getString(8);
                System.out.println(customer);
                // end grabbing data
                driverObservableList.add(new PendingDriver(name, status, capacity, coordinate, rating, customer));
            }
            dl_tableView.setItems(driverObservableList);
            conn.close();
        } catch (Exception connectionError) {
            System.out.println("Error: class build failed");
            System.out.println(connectionError);
        }

        // display customer list
        cl_customerCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        cl_statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        cl_capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        cl_eatCol.setCellValueFactory(new PropertyValueFactory<>("eat"));
        cl_pulCol.setCellValueFactory(new PropertyValueFactory<>("pickup"));
        cl_destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        cl_priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        // jdbc start
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
            java.sql.Statement sqlStatement = conn.createStatement();
            String sql = "";
            sql = "SELECT * FROM fishcab.customer_list;";
            ResultSet results = sqlStatement.executeQuery(sql);
            while (results.next()) {
                // start grabbing data
                name = results.getString(2).toUpperCase();
                System.out.print(name+" ");
                status = results.getString(3);
                System.out.print(status+" ");
                capacity = results.getInt(4);
                System.out.print(capacity+" ");
                eat = results.getString(5);
                System.out.print(eat+" ");
                pickup = results.getString(6);
                System.out.print(pickup+" ");
                destination = results.getString(7);
                System.out.print(destination+" ");
                price = results.getString(8);
                System.out.println(price);
                // end grabbing data
                customerObservableList.add(new Customer(name, status, capacity, eat, pickup, destination, price));
            }
            cl_tableView.setItems(customerObservableList);
            conn.close();
        } catch (Exception connectionError) {
            System.out.println("Error: class build failed");
            System.out.println(connectionError);
        }
    }
}