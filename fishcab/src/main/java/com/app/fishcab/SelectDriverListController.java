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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class SelectDriverListController implements Initializable {
    @FXML
    private final Stage travelBeginsStage = new Stage();
    @FXML
    public Label name_label = new Label();
    @FXML
    public Label pul_label = new Label();
    @FXML
    public Label dl_label = new Label();
    @FXML
    public Label eat_label = new Label();
    @FXML
    public Label capacity_label = new Label();
    @FXML
    public Label price_label = new Label();
    @FXML
    public TableView<Driver> selectDriverTable = new TableView();
    @FXML
    public TableColumn<Driver, String> driver_name_col = new TableColumn<>();
    @FXML
    public TableColumn<Driver, Integer> capacity_col = new TableColumn<>();
    @FXML
    public TableColumn<Driver, String> eat_col = new TableColumn<>();
    @FXML
    public TableColumn<Driver, Double> rating_col = new TableColumn<>();
    private ObservableList<Driver> driver_list = FXCollections.observableArrayList();

    private Stage stage = new Stage();

    String name;
    String coordinate;
    int driver_x;
    int driver_y;
    int capacity;
    int statusInt;
    String status;
    double totalRating;
    int ratingCount;
    String rating;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name_label.setText(Request.getName().toUpperCase());
        pul_label.setText(Request.getPul());
        dl_label.setText(Request.getDl());
        eat_label.setText(Request.getEat());
        capacity_label.setText(String.valueOf(Request.getCapacity()));
        price_label.setText("RM "+Request.getPrice());

        driver_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        capacity_col.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        eat_col.setCellValueFactory(new PropertyValueFactory<>("eat"));
        rating_col.setCellValueFactory(new PropertyValueFactory<>("rating"));

        String[] user_coor = Request.getPul().split(",");
        int user_x = Integer.parseInt(user_coor[0]);
        int user_y = Integer.parseInt(user_coor[1]);

        /// CONNECT TO DB
        // jdbc start
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
            java.sql.Statement sqlStatement = conn.createStatement();
            String sql = "";
            sql = "SELECT * FROM fishcab.driver_active;";
            ResultSet results = sqlStatement.executeQuery(sql);

            /// LOOP THROUGH ALL DRIVERS AND CALCULATE TOTAL DISTANCE AND ESTIMATED ARRIVAL TIME
            while (results.next()) {
                // start grabbing data
                System.out.println("--------------------------------------------------------");
                name = results.getString(2).toUpperCase();
                System.out.print(name+" ");
                coordinate = results.getString(3);
                System.out.print("("+coordinate+") ");
                capacity = results.getInt(4);
                System.out.println("capacity: "+capacity);/////////

                driver_x = Integer.parseInt(coordinate.split(",")[0]);
                driver_y = Integer.parseInt(coordinate.split(",")[1]);

                /// start complicated calculation here

                double distanceDriver = Math.sqrt((driver_x - user_x) * (driver_x - user_x) + (driver_y - user_y) * (driver_y - user_y));
                double totalDistance = distanceDriver + Request.getDistanceCustomer();
                double total_time = totalDistance * 0.08;
                Request.setTimeDriverToCustomer((int)total_time-Request.getTimeCustomerToDestination());
                System.out.print("Total time needed : "+(int)total_time+" minutes ");
                System.out.println("("+Request.getTimeDriverToCustomer()+"+"+Request.getTimeCustomerToDestination()+")");
                if(capacity>=Request.getCapacity()&&onTime(total_time, Request.getEat())){
                    // if system expected time before user expected time
                    System.out.println("system expected time before user expected time");
                    statusInt = results.getInt(5);
                    if(statusInt==0){
                        status = "Not Available";
                    }else{
                        status = "Available";
                    }
                    System.out.print("status: "+status+" ");
                    totalRating = results.getDouble(6);
//                    System.out.print(totalRating+" ");
                    ratingCount = results.getInt(7);
//                    System.out.print(ratingCount+" ");
                    rating = String.format("%.2f", totalRating/ratingCount);
                    System.out.println("rating: "+rating);
                    // end grabbing data
                    driver_list.add(new Driver(name, capacity, calEat(total_time), rating, Request.getTimeDriverToCustomer(), Request.getTimeCustomerToDestination()));
                }
            }
            selectDriverTable.setItems(driver_list);
            conn.close();
            System.out.println("--------------------------------------------------------");
        } catch (Exception connectionError) {
            System.out.println("Error: class build failed");
            System.out.println(connectionError);
        }

    }

    int total_rating;
    int rating_count;

    @FXML
    protected void confirm(ActionEvent event) throws Exception {
        System.out.println("Operation: confirm");
        Driver selectedDriver = (Driver) selectDriverTable.getSelectionModel().getSelectedItem();
        SelectedDriver.setName(selectedDriver.getName().toLowerCase());
        SelectedDriver.setCapacity(selectedDriver.getCapacity());
        SelectedDriver.setEat(selectedDriver.getEat());
        SelectedDriver.setRating(selectedDriver.getRating());
        SelectedDriver.setTimeDriverToCustomer(selectedDriver.getTimeDriverToCustomer());
        SelectedDriver.setTimeCustomerToDestination(selectedDriver.getTimeCustomerToDestination());
//        SelectedDriver driver = new SelectedDriver(selectedDriver.getName(), selectedDriver.getCapacity(),selectedDriver.getEat(),selectedDriver.getRating(), selectedDriver.getTimeDriverToCustomer(), selectedDriver.getTimeCustomerToDestination());
        System.out.println("Message: driver selected "+SelectedDriver.getName());

        // update on driver active which customer he / she is fetching

        // UPDATE `fishcab`.`driver_active` SET `customer` = 'ching yen' WHERE (`id` = '2');
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
            java.sql.Statement sqlStatement = conn.createStatement();
            String sql = "";
            // update who is driver fetching
            sql = "UPDATE `fishcab`.`driver_active` SET `customer` = '"+Request.getName()+"', `status` = 0 WHERE (`name` = '"+SelectedDriver.getName().toLowerCase()+"');";
            sqlStatement.executeUpdate(sql);

            // create new record in db
            sql = "INSERT INTO `fishcab`.`customer_list` (`customer`, `status`, `capacity`, `eat`, `pickup`, `destination`, `price`) VALUES ('"+Request.getName()+"', 'waiting', "+Request.getCapacity()+", '"+Request.getEat()+"', '"+Request.getPul()+"', '"+Request.getDl()+"', '"+Request.getPrice()+"');";
            sqlStatement.executeUpdate(sql);
            //

            conn.close();
        } catch (Exception connectionError) {
            System.out.println("Error: class build failed");
            System.out.println(connectionError);
        }

        TravelBegins travelBeginsPage = new TravelBegins();
        travelBeginsPage.start(travelBeginsStage);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    @FXML
    protected void cancel(ActionEvent event) throws Exception {
        System.out.println("Operation: cancel");
        // open user menu interface
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserMainMenu.fxml")));
        stage.setTitle("FISHcab USER");
        stage.setScene(new Scene(root));
        stage.show();
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    @FXML
    public void update(ActionEvent event) throws IOException {
        System.out.println("Operation: update request");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UpdateRequest.fxml")));
        stage.setTitle("FISHcab || Update Request");
        stage.setScene(new Scene(root));
        stage.showAndWait();

        driver_list.clear();

        name_label.setText(Request.getName().toUpperCase());
        pul_label.setText(Request.getPul());
        dl_label.setText(Request.getDl());
        eat_label.setText(Request.getEat());
        capacity_label.setText(String.valueOf(Request.getCapacity()));
        price_label.setText("RM "+Request.getPrice());

        driver_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        capacity_col.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        eat_col.setCellValueFactory(new PropertyValueFactory<>("eat"));
        rating_col.setCellValueFactory(new PropertyValueFactory<>("rating"));

        String[] user_coor = Request.getPul().split(",");
        int user_x = Integer.parseInt(user_coor[0]);
        int user_y = Integer.parseInt(user_coor[1]);



        /// CONNECT TO DB
        // jdbc start
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
            java.sql.Statement sqlStatement = conn.createStatement();
            String sql = "";
            sql = "SELECT * FROM fishcab.driver_active;";
            ResultSet results = sqlStatement.executeQuery(sql);

            /// LOOP THROUGH ALL DRIVERS AND CALCULATE TOTAL DISTANCE AND ESTIMATED ARRIVAL TIME
            while (results.next()) {
                // start grabbing data
                System.out.println("--------------------------------------------------------");
                name = results.getString(2).toUpperCase();
                System.out.print(name+" ");
                coordinate = results.getString(3);
                System.out.print("("+coordinate+") ");
                capacity = results.getInt(4);
                System.out.println("capacity: "+capacity);/////////

                driver_x = Integer.parseInt(coordinate.split(",")[0]);
                driver_y = Integer.parseInt(coordinate.split(",")[1]);

                /// start complicated calculation here

                double distanceDriver = Math.sqrt((driver_x - user_x) * (driver_x - user_x) + (driver_y - user_y) * (driver_y - user_y));
                double totalDistance = distanceDriver + Request.getDistanceCustomer();
                double total_time = totalDistance * 0.08;
                Request.setTimeDriverToCustomer((int)total_time-Request.getTimeCustomerToDestination());
                System.out.print("Total time needed : "+(int)total_time+" minutes ");
                System.out.println("("+Request.getTimeDriverToCustomer()+"+"+Request.getTimeCustomerToDestination()+")");
                if(capacity>=Request.getCapacity()&&onTime(total_time, Request.getEat())){
                    // if system expected time before user expected time
                    System.out.println("system expected time before user expected time");
                    statusInt = results.getInt(5);
                    if(statusInt==0){
                        status = "Not Available";
                    }else{
                        status = "Available";
                    }
                    System.out.print("status: "+status+" ");
                    totalRating = results.getDouble(6);
//                    System.out.print(totalRating+" ");
                    ratingCount = results.getInt(7);
//                    System.out.print(ratingCount+" ");
                    rating = String.format("%.2f", totalRating/ratingCount);
                    System.out.println("rating: "+rating);
                    // end grabbing data
                    driver_list.add(new Driver(name, capacity, calEat(total_time), rating, Request.getTimeDriverToCustomer(), Request.getTimeCustomerToDestination()));
                }
            }
            selectDriverTable.setItems(driver_list);
            conn.close();
        } catch (Exception connectionError) {
            System.out.println("Error: class build failed");
            System.out.println(connectionError);
        }

    }

    public boolean onTime(double total_time, String eat){
        String system_expected_time = calEat(total_time);
        int new_hours = Integer.parseInt(system_expected_time.split(":")[0]);
        int new_minutes = Integer.parseInt(system_expected_time.split(":")[1]);

        int e_hours = Integer.parseInt(eat.split(":")[0]);
        int e_minutes = Integer.parseInt(eat.split(":")[1]);

        System.out.println("user expected time : "+eat);
        if(new_minutes<10){
            System.out.println("system expected time : "+new_hours+":0"+new_minutes);
        }else{
            System.out.println("system expected time : "+new_hours+":"+new_minutes);
        }

        if(e_hours==new_hours && e_minutes>=new_minutes)return true;
        else if(e_hours>new_hours)return true;
        else return false;
    }

    public String calEat(double total_time){
        total_time = (int)total_time;
        String currentTime = Time.getCurrentTime();
        int c_hours = Integer.parseInt(currentTime.split(":")[0]);
        int c_minutes = Integer.parseInt(currentTime.split(":")[1]);
        int additional_hours = (int)(total_time/60);
        int additional_minutes = (int)(total_time%60);

        int new_hours = c_hours + additional_hours;
        int new_minutes = c_minutes + additional_minutes;

        if(new_minutes>=60){
            new_hours+=1;
            new_minutes-=60;
        }

        if(new_minutes<10)return new_hours+":0"+new_minutes;
        return new_hours+":"+new_minutes;

    }

}
