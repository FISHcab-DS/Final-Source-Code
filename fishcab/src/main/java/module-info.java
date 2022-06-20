module com.app.fishcab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.app.fishcab to javafx.fxml;
    exports com.app.fishcab;
}