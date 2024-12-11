module bemsih.databaslaboration1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens bemsih.databaslaboration1.Model to javafx.fxml;
    opens bemsih.databaslaboration1.View to javafx.fxml;
    opens bemsih.databaslaboration1.Controller to javafx.fxml;


    exports bemsih.databaslaboration1.Model;

    exports bemsih.databaslaboration1.View;

    exports bemsih.databaslaboration1.Controller;

}
