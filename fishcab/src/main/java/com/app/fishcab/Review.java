
package com.app.fishcab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Review extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(SelectDriverList.class.getResource("review.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 200);
        stage.setTitle("FishCab || Review");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
    }
}