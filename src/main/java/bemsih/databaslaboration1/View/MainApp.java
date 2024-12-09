//package bemsih.databaslaboration1.View;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
//
//public class MainApp extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            BorderPane root = new BorderPane();
//
//            // Lägg till sökgränssnittet överst
//            SearchView searchView = new SearchView();
//            root.setTop(searchView.getView());
//
//            // Lägg till formulär för att lägga till böcker nederst
//            AddBookView addBookView = new AddBookView();
//            root.setBottom(addBookView.getView());
//
//            Scene scene = new Scene(root, 800, 600);
//            primaryStage.setTitle("Bokhanteringssystem");
//            primaryStage.setScene(scene);
//            primaryStage.show();
//        } catch (Exception e) {
//            AlertUtils.showError("Fel vid start av applikationen: " + e.getMessage());
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
