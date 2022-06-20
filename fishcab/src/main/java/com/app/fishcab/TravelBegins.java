
package com.app.fishcab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TravelBegins extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SelectDriverList.class.getResource("travel-begins.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 500);
        stage.setTitle("FishCab || Travel Begins");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}