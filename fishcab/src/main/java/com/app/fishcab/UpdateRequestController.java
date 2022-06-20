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
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.ResourceBundle;

public class UpdateRequestController implements Initializable {

    @FXML
    private Button CancelBtn;

    @FXML
    private Spinner<Integer> CapacitySpinner;

    @FXML
    private TextField EATTextField;

    @FXML
    private AnchorPane Pane;

    @FXML
    public TextField PickupX;
    @FXML
    public TextField PickupY;
    @FXML
    public TextField DestX;
    @FXML
    public TextField DestY;

    private Scene scene;
    private Stage stage;
    private Parent root;
    private int capacity;

    @FXML
    private Label TimerLabel;

    static Calendar now = Calendar.getInstance();
    static long start = System.currentTimeMillis();

    public String time;
    public String EAT;

    @FXML
    public void update (ActionEvent event) throws IOException {
        ///
        if(PickupX.getText().equals("")||PickupY.getText().equals("")||DestX.getText().equals("")||DestY.getText().equals("")||EATTextField.getText().equals("")){
            Alert incompleteInfoAlert = new Alert(Alert.AlertType.WARNING);
            incompleteInfoAlert.setTitle("Update Request Fail");
            incompleteInfoAlert.setHeaderText("Error:");
            incompleteInfoAlert.setContentText("Please fill in all the required blanks");
            return;
        }
        ///

        Request.setPul(PickupX.getText()+","+PickupY.getText());
        Request.setDl(DestX.getText()+","+DestY.getText());
        Request.setEat(EATTextField.getText());
        Request.setCapacity(CapacitySpinner.getValue());
        System.out.println("Operation: requested updated");

        int num1 = Integer.parseInt(PickupX.getText());
        int num2 = Integer.parseInt(PickupY.getText());
        int num3 = Integer.parseInt(DestX.getText());
        int num4 = Integer.parseInt(DestY.getText());

        double distanceCustomer = Math.sqrt((num3 - num1) * (num3 - num1) + (num4 - num2) * (num4 - num2)) * 0.5 ;
        System.out.println("------------------------------------------------");
        System.out.printf("Output: updated distance of customer = %.2f units\n", distanceCustomer);
        System.out.println("------------------------------------------------");
        double sumOfPrice = distanceCustomer * 0.70;
        String price = String.format("%.2f", sumOfPrice);
        System.out.println("Output: price = RM "+price);
        EAT = EATTextField.getText();
        int timeCustomerToDestination = (int)(distanceCustomer * 0.08);

        Request.setDistanceCustomer(distanceCustomer);
        Request.setPrice(price);
        Request.setTimeCustomerToDestination(timeCustomerToDestination);

        stage = (Stage) Pane.getScene().getWindow();
        stage.close();

        /* next -- searching for driver
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(".fxml")));
        stage.setTitle("FISHcab");
        stage.setScene(new Scene(root));
        stage.show();
        */
    }

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

    public void CancelClicked (ActionEvent event) throws IOException {
        stage = (Stage) Pane.getScene().getWindow();
        stage.close();
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserMainMenu.fxml")));
//        stage.setTitle("FISHcab");
//        stage.setScene(new Scene(root));
//        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PickupX.setText(Request.getPul().split(",")[0]);
        PickupY.setText(Request.getPul().split(",")[1]);
        DestX.setText(Request.getDl().split(",")[0]);
        DestY.setText(Request.getDl().split(",")[1]);
        EATTextField.setText(Request.getEat());
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 6);
        valueFactory.setValue(1);
        CapacitySpinner.setValueFactory(valueFactory);
//        capacity = CapacitySpinner.getValue();
//        CapacitySpinner.valueProperty().addListener(new ChangeListener<Integer>() {
//            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
//                capacity = Request.getCapacity();
//            }
//        });
        CapacitySpinner.getValueFactory().setValue(Request.getCapacity());
        time = getCurrentTime();
        TimerLabel.setText(time);
    }
}