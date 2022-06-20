
package com.app.fishcab;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;

public class TravelBeginsController implements Initializable {
    @FXML
    public Label request_name_label = new Label();
    @FXML
    public Label request_pul_label = new Label();
    @FXML
    public Label request_dl_label = new Label();
    @FXML
    public Label request_capacity_label = new Label();
    @FXML
    public Label request_eat_label = new Label();
    @FXML
    public Label request_price_label = new Label();
    @FXML
    public Label driver_name_label = new Label();
    @FXML
    public Label driver_rating_label = new Label();
    @FXML
    public Label driver_capacity_label = new Label();
    @FXML
    public Label driver_eat_label = new Label();
    @FXML
    public Label status_label = new Label();
    @FXML
    public Label min_label = new Label();
    @FXML
    public Button arrivedBtn = new Button();
    @FXML
    public final Stage reviewStage = new Stage();

    @FXML
    public Label textLabel = new Label();
    @FXML
    public Button cancelBtn = new Button();

    public Stage stage = new Stage();

    public MyTimerTask wait_driver_to_arrive;

    public static Timer timer1;

    int total_rating;
    int rating_count;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        request_name_label.setText(Request.getName().toUpperCase());
        request_pul_label.setText(Request.getPul());
        request_dl_label.setText(Request.getDl());
        request_capacity_label.setText(String.valueOf(Request.getCapacity()));
        request_eat_label.setText(Request.getEat());
        request_price_label.setText("RM "+Request.getPrice());
        driver_name_label.setText(SelectedDriver.name.toUpperCase());
        driver_rating_label.setText(String.valueOf(SelectedDriver.rating));
        driver_capacity_label.setText(String.valueOf(SelectedDriver.capacity));
        driver_eat_label.setText(SelectedDriver.eat);
        status_label.setText("Waiting for driver");

        arrivedBtn.setDisable(true);

        int interval1 = SelectedDriver.getTimeDriverToCustomer();
        int interval2 = SelectedDriver.getTimeCustomerToDestination();
        min_label.setText(String.valueOf(interval1));

        timer1 = new Timer();
        wait_driver_to_arrive = new MyTimerTask(interval1, interval2, min_label, status_label, arrivedBtn, textLabel, cancelBtn);
        timer1.scheduleAtFixedRate(wait_driver_to_arrive, 1000, 1000);

    }

    public void arrived(ActionEvent event) throws Exception{
        System.out.println("Message: Arrived");
        Review reviewPage = new Review();
        reviewPage.start(reviewStage);

        //insert record into db

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
            java.sql.Statement sqlStatement = conn.createStatement();
            String sql = "";
            sql = "UPDATE `fishcab`.`customer_list` SET `status` = 'arrived' WHERE (`customer` = '"+Request.getName().toLowerCase()+"' AND `status` = 'picked up');";
            System.out.println(sql);
            sqlStatement.executeUpdate(sql);
            // read and update total rate
//            SELECT `total_rating`, `rating_count` FROM fishcab.driver_active WHERE `name` = 'weise';
            sql = "SELECT `total_rating`, `rating_count` FROM `fishcab`.`driver_active` WHERE `name` = '"+SelectedDriver.getName().toLowerCase()+"';";
            ResultSet results = sqlStatement.executeQuery(sql);
            System.out.println("sql get rating: "+sql);
            while (results.next()) {
                total_rating = results.getInt(1);
                rating_count = results.getInt(2);
                System.out.println("Operation: new total rating = "+total_rating+" + "+Request.getRate());
                System.out.println("Operation: new rating count = "+rating_count+" + 1");
                rating_count+=1;
                total_rating+=Request.getRate();
            }
            sql = "UPDATE `fishcab`.`driver_active` SET `location` = '"+Request.getDl()+"', `total_rating` = "+total_rating+", `rating_count` = "+rating_count+", `customer` = 'no', `status` = 1 WHERE (`name` = '"+SelectedDriver.getName().toLowerCase()+"');";
//            System.out.println("sql update : "+sql);
            System.out.println("Operation: the current location of driver "+SelectedDriver.getName()+" is updated to "+Request.getDl());
            sqlStatement.executeUpdate(sql);
            conn.close();
        } catch (Exception connectionError) {
            System.out.println("Error: class build failed");
            System.out.println(connectionError);
        }

        //

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    public void cancel(ActionEvent event) throws Exception{
        System.out.println("Operation: cancel");
        Alert cancelRequestAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelRequestAlert.setTitle("Cancel Request");
        cancelRequestAlert.setHeaderText("Confirmation:");
        cancelRequestAlert.setContentText("Are you sure you want to cancel the request?\nYou will only get half of your refund.");
        if(cancelRequestAlert.showAndWait().get()== ButtonType.OK){
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.close();

            MyTimerTask.timer.cancel();
            System.out.println("Operation: timer cancel");
            MyTimerTask.timer.purge();
            System.out.println("Operation: timer purge");

            wait_driver_to_arrive.stopTimer();
            timer1.cancel();
            timer1.purge();

            // update for records in db

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
                java.sql.Statement sqlStatement = conn.createStatement();
                String sql = "";
                // add into record
                sql = "UPDATE `fishcab`.`customer_list` SET `status` = 'canceled' WHERE (`customer` = '"+Request.getName().toLowerCase()+"' AND `status` = 'waiting');";
//            System.out.println("sql insert record: "+sql);
                sqlStatement.executeUpdate(sql);
                sql = "UPDATE `fishcab`.`driver_active` SET `customer` = 'no', `status` = 1 WHERE (`name` = '"+SelectedDriver.getName().toLowerCase()+"');";
                sqlStatement.executeUpdate(sql);
                conn.close();
            } catch (Exception connectionError) {
                System.out.println("Error: class build failed");
                System.out.println(connectionError);
            }

            //

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserMainMenu.fxml")));
            stage.setTitle("FISHcab");
            stage.setScene(new Scene(root));
            stage.show();
        }

    }
}