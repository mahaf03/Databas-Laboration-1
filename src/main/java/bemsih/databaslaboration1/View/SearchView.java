//package bemsih.databaslaboration1.View;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.scene.control.*;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//
//import java.awt.print.Book;
//
//public class SearchView {
//
//    private ComboBox<String> genreComboBox;
//    private TextField titleField;
//    private TextField authorField;
//    private TableView<Book> tableView;
//
//    public VBox getView() {
//        VBox searchView = new VBox(10);
//        searchView.setPadding(new Insets(10));
//
//        // Sökfält
//        HBox searchFields = new HBox(10);
//        genreComboBox = new ComboBox<>();
//        genreComboBox.setPromptText("Välj genre");
//        genreComboBox.setItems(FXCollections.observableArrayList("Fiction", "Non-Fiction", "Sci-Fi", "Fantasy"));
//
//        titleField = new TextField();
//        titleField.setPromptText("Sök efter titel");
//
//        authorField = new TextField();
//        authorField.setPromptText("Sök efter författare");
//
//        Button searchButton = new Button("Sök");
//        searchButton.setOnAction(e -> handleSearch());
//
//        searchFields.getChildren().addAll(genreComboBox, titleField, authorField, searchButton);
//
//        // Resultattabell
//        tableView = new TableView<>();
//        TableColumn<Book, String> titleColumn = new TableColumn<>("Titel");
//        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
//        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
//        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genresProperty());
//        TableColumn<Book, String> authorColumn = new TableColumn<>("Författare");
//        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorsProperty());
//        tableView.getColumns().addAll(titleColumn, genreColumn, authorColumn);
//
//        searchView.getChildren().addAll(searchFields, tableView);
//        return searchView;
//    }
//
//    private void handleSearch() {
//        // Placeholder för söklogik
//        AlertUtils.showInformation("Söker efter böcker...");
//    }
//}
//
