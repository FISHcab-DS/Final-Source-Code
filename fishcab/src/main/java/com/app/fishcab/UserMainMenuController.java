package com.app.fishcab;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class UserMainMenuController implements Initializable{

    @FXML
    private Button LogOutBtn;

    @FXML
    private Button NewRequestBtn;

    @FXML
    private AnchorPane Pane;

    @FXML
    private Label username_tag;


    @FXML
    private Label TimerLabel;

    private Scene scene;
    private Stage stage;
    private Parent root;

    static Calendar now = Calendar.getInstance();
    static long start = System.currentTimeMillis();

    public String getCurrentTime() {
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        long hour=now.get(Calendar.HOUR_OF_DAY);
        long min =(now.get(Calendar.MINUTE)+(timeElapsed/1000));
        while(min>=60){
            min-=60;
            hour++;
        }
        DecimalFormat twodigits= new DecimalFormat("00");
        return hour + ":" + twodigits.format(min);
    }

    Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    e -> {
                        TimerLabel.setText(getCurrentTime());
                    }));

    public void NewRequestClicked (ActionEvent event) throws IOException {
        stage = (Stage) Pane.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("NewRequest.fxml")));
        stage.setTitle("FISHcab");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void LogOutClicked (ActionEvent event) throws IOException {
        stage = (Stage) Pane.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        stage.setTitle("FISHcab");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TimerLabel.setText(getCurrentTime());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}

    /*
    public void NewRequestClicked (ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewRequest.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("Edit Food Menu!");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
    */