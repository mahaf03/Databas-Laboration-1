//package bemsih.databaslaboration1.View;
//
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
//
//public class AlertUtils {
//
//    public static void showError(String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Fel");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    public static void showInformation(String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Information");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    public static boolean showConfirmation(String message) {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Bekräftelse");
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        return alert.showAndWait().orElse(null) == ButtonType.OK;
//    }
//}
