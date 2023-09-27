module com.mycompany.audiosign {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.audiosign to javafx.fxml;
    exports com.mycompany.audiosign;
}
