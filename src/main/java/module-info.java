module bemsih.databaslaboration1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens bemsih.databaslaboration1.Model to javafx.fxml;

    exports bemsih.databaslaboration1.Model;

}
