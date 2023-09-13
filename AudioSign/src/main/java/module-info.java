module com.mycompany.audiosign {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.audiosign to javafx.fxml;
    exports com.mycompany.audiosign;
}
