module com.mycompany.audiosign {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires charm.glisten;


    opens com.mycompany.audiosign to javafx.fxml;
    exports com.mycompany.audiosign;
}
