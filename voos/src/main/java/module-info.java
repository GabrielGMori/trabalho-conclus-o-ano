module com.tca {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tca to javafx.fxml;
    exports com.tca;
}
