package com.app.fishcab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;

public class NewRequestController implements Initializable {

    @FXML
    private Button CancelBtn;

    @FXML
    private Spinner<Integer> CapacitySpinner;

    @FXML
    private TextField PickUpX;

    @FXML
    private TextField PickUpY;

    @FXML
    private TextField DestX;

    @FXML
    private TextField DestY;

    @FXML
    private TextField EATTextField;

    @FXML
    private AnchorPane Pane;

    @FXML
    private Button SearchBtn;


    @FXML
    private Label TimerLabel;

    private Scene scene;
    private Stage stage;
    private Parent root;
    private int capacity;

    public String time;
    public String EAT;
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

    public int getTotalMinutes(String a) {
        String[] time_arr = a.split(":");
        int hour_1 = Integer.parseInt(time_arr[0]);
        int min_1 = Integer.parseInt(time_arr[1]);

        min_1 += hour_1 * 60;

        return min_1;
    }

    public int getTimeTaken(int CT, int EAT) {
        return EAT - CT;
    }

    public void SearchClicked (ActionEvent event) throws IOException {

        if(PickUpX.getText().equals("")||PickUpY.getText().equals("")||DestX.getText().equals("")||DestY.getText().equals("")||EATTextField.getText().equals("")){
            Alert incompleteInfoAlert = new Alert(Alert.AlertType.WARNING);
            incompleteInfoAlert.setTitle("Request Fail");
            incompleteInfoAlert.setHeaderText("Error:");
            incompleteInfoAlert.setContentText("Please fill in all the required blanks");
            return;
        }
        try{
            int num1 = Integer.parseInt(PickUpX.getText());
            int num2 = Integer.parseInt(PickUpY.getText());
            int num3 = Integer.parseInt(DestX.getText());
            int num4 = Integer.parseInt(DestY.getText());

            double distanceCustomer = Math.sqrt((num3 - num1) * (num3 - num1) + (num4 - num2) * (num4 - num2)) * 0.5 ;
            System.out.println("------------------------------------------------");
            System.out.printf("Output: distance of customer = %.2f units\n", distanceCustomer);
            System.out.println("------------------------------------------------");
            double sumOfPrice = distanceCustomer * 0.25;
            String price = String.format("%.2f", sumOfPrice);
            System.out.println("Output: price = RM "+price);
            EAT = EATTextField.getText();
            int timeCustomerToDestination = (int)(distanceCustomer * 0.08);

            Request.setName(CurrentUserInfo.getUsername());
            Request.setPul(num1+","+num2);
            Request.setDl(num3+","+num4);
            Request.setEat(EAT);
            Request.setDistanceCustomer(distanceCustomer);
            Request.setCapacity(CapacitySpinner.getValue());
            Request.setPrice(price);
            Request.setTimeCustomerToDestination(timeCustomerToDestination);

            stage = (Stage) Pane.getScene().getWindow();
            stage.close();

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("select-driver-list.fxml")));
            stage.setTitle("Pick your driver");
            stage.setScene(new Scene(root));
            stage.show();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void CancelClicked (ActionEvent event) throws IOException {
        stage = (Stage) Pane.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserMainMenu.fxml")));
        stage.setTitle("FISHcab");
        stage.setScene(new Scene(root));
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 6);
        valueFactory.setValue(1);
        CapacitySpinner.setValueFactory(valueFactory);
        capacity = CapacitySpinner.getValue();
        CapacitySpinner.valueProperty().addListener(new ChangeListener<Integer>() {
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                capacity = CapacitySpinner.getValue();
            }
        });

        time = getCurrentTime();
        TimerLabel.setText(time);
        Time.setCurrent_time(time);
    }
}