//package bemsih.databaslaboration1.View;
//
//import javafx.collections.FXCollections;
//import javafx.geometry.Insets;
//import javafx.scene.control.*;
//import javafx.scene.layout.GridPane;
//
//public class AddBookView {
//
//    public GridPane getView() {
//        GridPane form = new GridPane();
//        form.setPadding(new Insets(10));
//        form.setHgap(10);
//        form.setVgap(10);
//
//        // Fält för att lägga till böcker
//        TextField titleField = new TextField();
//        titleField.setPromptText("Titel");
//
//        DatePicker publishingDatePicker = new DatePicker();
//
//        TextField isbnField = new TextField();
//        isbnField.setPromptText("ISBN");
//
//        TextField authorField = new TextField();
//        authorField.setPromptText("Författare (kommaseparerade)");
//
//        ComboBox<String> genreComboBox = new ComboBox<>();
//        genreComboBox.setPromptText("Välj genre");
//        genreComboBox.setItems(FXCollections.observableArrayList("Fiction", "Non-Fiction", "Sci-Fi", "Fantasy"));
//
//        Button addButton = new Button("Lägg till");
//        addButton.setOnAction(e -> handleAddBook(titleField, publishingDatePicker, isbnField, authorField, genreComboBox));
//
//        form.add(new Label("Titel:"), 0, 0);
//        form.add(titleField, 1, 0);
//        form.add(new Label("Publiceringsdatum:"), 0, 1);
//        form.add(publishingDatePicker, 1, 1);
//        form.add(new Label("ISBN:"), 0, 2);
//        form.add(isbnField, 1, 2);
//        form.add(new Label("Författare:"), 0, 3);
//        form.add(authorField, 1, 3);
//        form.add(new Label("Genre:"), 0, 4);
//        form.add(genreComboBox, 1, 4);
//        form.add(addButton, 1, 5);
//
//        return form;
//    }
//
//    private void handleAddBook(TextField titleField, DatePicker datePicker, TextField isbnField, TextField authorField, ComboBox<String> genreComboBox) {
//        // Placeholder för tilläggslogik
//        AlertUtils.showInformation("Lägger till bok...");
//    }
//}
