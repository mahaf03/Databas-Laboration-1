package bemsih.databaslaboration1.View;

import bemsih.databaslaboration1.Controller.BooksDbController;
import bemsih.databaslaboration1.Model.Author;
import bemsih.databaslaboration1.Model.Book;
import bemsih.databaslaboration1.Controller.BooksDbException;
import bemsih.databaslaboration1.Model.DatabaseConnection;
import bemsih.databaslaboration1.Model.Genre;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class BooksAppView extends Application {
    private BooksDbController controller;

    @Override
    public void start(Stage primaryStage) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            BooksDbController controller = new BooksDbController();

            // Skapa UI-komponenter
            TextField searchField = new TextField();
            searchField.setPromptText("Sök efter böcker");

            ComboBox<String> searchTypeComboBox = new ComboBox<>();
            searchTypeComboBox.getItems().addAll("Titel", "Författare", "Genre");
            searchTypeComboBox.setValue("Titel"); // Default value

            Button searchButton = new Button("Sök");
            Button addBookButton = new Button("Lägg till bok");
            Button addRatingButton = new Button("Lägg till betyg");

            TableView<Book> tableView = new TableView<>();
            TableColumn<Book, String> titleColumn = new TableColumn<>("Titel");
            titleColumn.setCellValueFactory(param ->
                    new SimpleStringProperty(param.getValue().getTitle())); // Använd getter-metod för titel



            tableView.getColumns().addAll(titleColumn);

            // Layout för sökfält, combobox och knappar
            HBox searchBox = new HBox(10, searchField, searchTypeComboBox, searchButton);
            VBox vBox = new VBox(10, searchBox, addBookButton, addRatingButton, tableView);
            vBox.setPrefWidth(600);
            vBox.setPrefHeight(400);

            // Sökfunktion
            searchButton.setOnAction(e -> {
                String query = searchField.getText();
                String selectedSearchType = searchTypeComboBox.getValue();
                List<Book> books = null;

                try {
                    switch (selectedSearchType) {
                        case "Titel":
                            books = controller.getBooksByTitle(query);
                            break;
                        case "Författare":
                            books = controller.getBooksByAuthor(query);
                            break;
                        case "Genre":
                            books = controller.getBooksByGenre(query);
                            break;
                    }

                    tableView.getItems().clear();
                    tableView.getItems().addAll(books);

                } catch (BooksDbException ex) {
                    showAlert("Fel", "Sökningen misslyckades", ex.getMessage());
                }
            });

            // Lägg till bok (använd Dialog)
            addBookButton.setOnAction(e -> showAddBookDialog());

            // Lägg till betyg (ytterligare funktionalitet behövs här)
            addRatingButton.setOnAction(e -> showAddRatingDialog());

            // Visa scenen
            Scene scene = new Scene(vBox);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Bokdatabas");
            primaryStage.show();
        } catch (Exception e) {
            showAlert("Fel", "Databasanslutning misslyckades", e.getMessage());
        }
    }

    private void showAddBookDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Lägg till bok");
        dialog.setHeaderText("Fyll i bokdetaljer");

        // Skapa fält för titel, författare, genre
        TextField titleField = new TextField();
        titleField.setPromptText("Titel");

        TextField authorField = new TextField();
        authorField.setPromptText("Författare");

        TextField genreField = new TextField();
        genreField.setPromptText("Genre");

        // Lägg till fält i layout
        VBox vBox = new VBox(10, titleField, authorField, genreField);
        dialog.getDialogPane().setContent(vBox);

        // Lägg till OK- och Avbryt-knappar
        ButtonType okButton = new ButtonType("Lägg till", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        // Hantera knapptryckning
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                String title = titleField.getText();
                String author = authorField.getText();
                String genre = genreField.getText();

                // Skapa bok och försök att lägga till den
                try {
                    List<Author> authors = new ArrayList<>();
                    authors.add(new Author(0, author.split(" ")[0], author.split(" ")[1], null)); // Förenklad författarehantering
                    List<Genre> genres = new ArrayList<>();
                    genres.add(new Genre(0, genre));

                    controller.addBookWithAuthors(new Book(0, title, null, 0L), authors, genres);
                    showAlert("Success", "Bok tillagd", "Boken har lagts till.");
                } catch (BooksDbException e) {
                    showAlert("Fel", "Bok kunde inte läggas till", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }


    private void showAddRatingDialog() {
        // Exempel för att lägga till betyg till bok (förbättra vid behov)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lägg till betyg");
        dialog.setHeaderText("Ange betyg för bok (1-5):");
        dialog.showAndWait().ifPresent(rating -> {
            try {
                int ratingValue = Integer.parseInt(rating);
                if (ratingValue < 1 || ratingValue > 5) {
                    showAlert("Fel", "Ogiltigt betyg", "Betyget ska vara mellan 1 och 5.");
                    return;
                }
                // Lägg till betyg i databasen, använd rätt metod från controller
                // controller.addRating(book, user, ratingValue); // Anpassa vid behov
                showAlert("Success", "Betyg tillagt", "Betyget har lagts till.");
            } catch (NumberFormatException ex) {
                showAlert("Fel", "Ogiltigt betyg", "Ange ett giltigt heltal.");
            }
        });
    }



    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
