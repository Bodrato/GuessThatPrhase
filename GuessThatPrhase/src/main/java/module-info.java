module com.example.guessthatprhase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires java.desktop;


    opens com.example.guessthatprhase to javafx.fxml;
    exports com.example.guessthatprhase;
    exports com.example.guessthatprhase.business to com.google.gson;
    exports com.example.guessthatprhase.presentation;
    opens com.example.guessthatprhase.presentation to javafx.fxml;
}