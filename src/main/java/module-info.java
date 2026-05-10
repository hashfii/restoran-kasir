module com.restoran.kasir {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.restoran.kasir to javafx.fxml;
    opens com.restoran.kasir.controller to javafx.fxml;
    opens com.restoran.kasir.model to javafx.base;

    exports com.restoran.kasir;
}
