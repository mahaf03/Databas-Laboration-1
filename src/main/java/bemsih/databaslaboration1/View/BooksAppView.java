package bemsih.databaslaboration1.View;

import bemsih.databaslaboration1.Controller.BooksDbController;
import bemsih.databaslaboration1.Model.*;
import bemsih.databaslaboration1.Controller.BooksDbException;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BooksAppView extends Application {
    private BooksDbController controller;
    private TableView<Book> tableView;
    private User loggedInUser;


    @Override
    public void start(Stage primaryStage) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            this.controller = new BooksDbController();
            this.tableView = new TableView<>();
            // Skapa UI-komponenter
            TextField searchField = new TextField();
            searchField.setPromptText("Sök efter böcker");

            ComboBox<String> searchTypeComboBox = new ComboBox<>();
            searchTypeComboBox.getItems().addAll("Titel", "Författare", "Genre");
            searchTypeComboBox.setValue("Titel"); // Default value

            Button searchButton = new Button("Sök");
            Button addBookButton = new Button("Lägg till bok");
            Button addRatingButton = new Button("Lägg till betyg");


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

        // Textfält för bokdetaljer
        TextField titleField = new TextField();
        titleField.setPromptText("Titel");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Författarens förnamn");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Författarens efternamn");

        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("Författarens födelsedatum");

        TextField genreField = new TextField();
        genreField.setPromptText("Genre");

        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");

        DatePicker publishDatePicker = new DatePicker();
        publishDatePicker.setPromptText("Publiceringsdatum");

        // Lista för extra författare
        List<Author> additionalAuthors = new ArrayList<>();

        // Knapp för att lägga till extra författare
        Button addExtraAuthorButton = new Button("Lägg till extra författare");
        addExtraAuthorButton.setOnAction(e -> {
            showAddAuthorDialog(additionalAuthors);
        });

        VBox vBox = new VBox(10, titleField, firstNameField, lastNameField, dobPicker, genreField, isbnField, publishDatePicker, addExtraAuthorButton);
        dialog.getDialogPane().setContent(vBox);

        ButtonType okButton = new ButtonType("Lägg till", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                String title = titleField.getText();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                LocalDate dob = dobPicker.getValue();
                String genre = genreField.getText();
                String isbn = isbnField.getText();
                LocalDate publishDate = publishDatePicker.getValue();

                // Kontrollera obligatoriska fält
                if (title.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || dob == null || genre.isEmpty() || isbn.isEmpty()) {
                    showAlert("Fel", "Ogiltiga detaljer", "Alla fält måste fyllas i.");
                    return null;
                }

                // Kontrollera ISBN-format
                if (!isbn.matches("\\d+")) {
                    showAlert("Fel", "Ogiltigt ISBN", "ISBN måste vara ett giltigt nummer.");
                    return null;
                }

                // Använd dagens datum som standard om inget publiceringsdatum valts
                if (publishDate == null) {
                    publishDate = LocalDate.now();
                }

                try {
                    // Skapa en lista med alla författare (inklusive den första)
                    List<Author> authors = new ArrayList<>();
                    authors.add(new Author(0, firstName, lastName, dob));
                    authors.addAll(additionalAuthors);

                    controller.addBookWithAuthors(
                            new Book(0, title, publishDate, Long.parseLong(isbn)),
                            authors,
                            List.of(new Genre(0, genre))
                    );
                    showAlert("Success", "Bok tillagd", "Boken har lagts till.");
                } catch (BooksDbException e) {
                    showAlert("Fel", "Bok kunde inte läggas till", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }


    private void showAddAuthorDialog(List<Author> authors) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Lägg till författare");
        dialog.setHeaderText("Fyll i författardetaljer");

        // Textfält för författardetaljer
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Förnamn");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Efternamn");

        DatePicker dobPicker = new DatePicker(); // Födelsedatum
        dobPicker.setPromptText("Födelsedatum");

        VBox vBox = new VBox(10, firstNameField, lastNameField, dobPicker);
        dialog.getDialogPane().setContent(vBox);

        ButtonType okButton = new ButtonType("Lägg till", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                LocalDate dob = dobPicker.getValue();

                // Kontrollera att alla obligatoriska fält är ifyllda
                if (firstName.isEmpty() || lastName.isEmpty() || dob == null) {
                    showAlert("Fel", "Ogiltiga detaljer", "Alla fält måste fyllas i.");
                    return null;
                }

                // Lägg till författaren till listan
                authors.add(new Author(0, firstName, lastName, dob));
                showAlert("Success", "Författare tillagd", "Författaren har lagts till.");
            }
            return null;
        });

        dialog.showAndWait();
    }




    private void showAddRatingDialog() {
        // Kontrollera om användaren är inloggad
        if (loggedInUser == null) {
            showLoginDialog();
            if (loggedInUser == null) {
                showAlert("Fel", "Ingen användare inloggad", "Du måste logga in för att lägga till ett betyg.");
                return;
            }
        }

        // Kontrollera om en bok är vald från tabellen
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Fel", "Ingen bok vald", "Välj en bok att betygsätta.");
            return;
        }

        // Skapa dialogfönster
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Lägg till betyg");
        dialog.setHeaderText("Ange betyg för: " + selectedBook.getTitle());
        dialog.setContentText("Betyg (1-5):");

        // Hantera användarens inmatning
        dialog.showAndWait().ifPresent(rating -> {
            try {
                int ratingValue = Integer.parseInt(rating);
                if (ratingValue < 1 || ratingValue > 5) {
                    showAlert("Fel", "Ogiltigt betyg", "Betyget ska vara mellan 1 och 5.");
                    return;
                }

                // Lägg till betyg i databasen
                controller.addRating(selectedBook, loggedInUser, ratingValue);
                showAlert("Success", "Betyg tillagt", "Betyget har lagts till för boken.");
            } catch (NumberFormatException ex) {
                showAlert("Fel", "Ogiltigt betyg", "Ange ett giltigt heltal.");
            } catch (BooksDbException ex) {
                showAlert("Fel", "Betyget kunde inte sparas", ex.getMessage());
            }
        });
    }
    private void showLoginDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Logga in");
        dialog.setHeaderText("Ange dina inloggningsuppgifter");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Användarnamn");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Lösenord");

        VBox vBox = new VBox(10, usernameField, passwordField);
        dialog.getDialogPane().setContent(vBox);

        ButtonType loginButton = new ButtonType("Logga in", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButton, cancelButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButton) {
                String username = usernameField.getText();
                String password = passwordField.getText();

                if (username.isEmpty() || password.isEmpty()) {
                    showAlert("Fel", "Ogiltiga detaljer", "Användarnamn och lösenord får inte vara tomma.");
                    return null;
                }

                try {
                    if (controller.validateUser(username, password)) {
                        loggedInUser = controller.getUserByUsername(username);
                        showAlert("Success", "Inloggning lyckades", "Välkommen, " + loggedInUser.getUserName() + "!");
                    } else {
                        showAlert("Fel", "Inloggning misslyckades", "Ogiltigt användarnamn eller lösenord.");
                    }
                } catch (BooksDbException e) {
                    showAlert("Fel", "Inloggning misslyckades", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
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
