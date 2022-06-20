
package com.app.fishcab;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask{
    public static int interval1;
    public static int interval2;
    @FXML
    public static Label min_label;
    @FXML
    public static Label status_label;
    @FXML
    public static Label textLabel;
    @FXML
    public static Button arrivedBtn;
    @FXML
    public static Button cancelBtn;
    public static Timer timer = new Timer();

    public boolean update = true;

    public MyTimerTask(int interval1, int interval2, Label min_label, Label status_label, Button arrivedBtn, Label textLabel, Button cancelBtn) {
        MyTimerTask.interval1 = interval1;
        MyTimerTask.interval2 = interval2 + 1;
        MyTimerTask.min_label = min_label;
        MyTimerTask.status_label = status_label;
        MyTimerTask.arrivedBtn = arrivedBtn;
        MyTimerTask.textLabel = textLabel;
        MyTimerTask.cancelBtn = cancelBtn;
    }

    @Override
    public void run() {
        if(interval1+interval2 > 0){
            if(interval1 > 0){
                Platform.runLater(() -> min_label.setText(String.valueOf(interval1)));
                interval1--;
            }else{
                Platform.runLater(() -> status_label.setText("Your driver has arrived"));
                Platform.runLater(() -> min_label.setText(String.valueOf(interval2)));
                Platform.runLater(() -> textLabel.setText("You will be Arriving in"));
                cancelBtn.setDisable(true);
                interval2--;
                //
                if(update){
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fishcab", "root", "20020407");
                        java.sql.Statement sqlStatement = conn.createStatement();
                        String sql = "";
                        sql = "UPDATE `fishcab`.`customer_list` SET `status` = 'picked up' WHERE (`customer` = '"+Request.getName().toLowerCase()+"' AND `status` = 'waiting');";
                        System.out.println(sql);
                        sqlStatement.executeUpdate(sql);
                        conn.close();
                    } catch (Exception connectionError) {
                        System.out.println("Error: class build failed");
                        System.out.println(connectionError);
                    }
                    update = false;
                }
                //
            }
        }else{
            timer.cancel();
            timer.purge();
            TravelBeginsController.timer1.cancel();
            TravelBeginsController.timer1.purge();
            Platform.runLater(() -> status_label.setText("You have reached the destination"));
            arrivedBtn.setDisable(false);
        }
    }

    public void stopTimer(){
        MyTimerTask.timer.cancel();
        MyTimerTask.timer.purge();
        System.out.println("Operation: stop timer from inside");
    }
}