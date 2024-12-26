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
import org.bson.types.ObjectId;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BooksAppView extends Application {
    private BooksDbController controller;
    private TableView<Book> tableView;
    private User loggedInUser;
    private User user; // Lägg till detta om det saknas
    private ObjectId userId; // Om du redan har detta


    @Override
    public void start(Stage primaryStage) {
        try {
            this.controller = new BooksDbController();
            controller.initializeDefaultUser();// Initiera MongoDB-kontrollern
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
            Button addReviewButton = new Button("Lägg till recension");
            Button reviewButton = new Button("Visa recension");
            Button deleteBookButton = new Button("Radera bok");
            Button loginButton = new Button("Logga in");
            Button logoutButton = new Button("Logga ut");

            TableColumn<Book, String> titleColumn = new TableColumn<>("Titel");
            titleColumn.setCellValueFactory(param ->
                    new SimpleStringProperty(param.getValue().getTitle())); // Använd getter-metod för titel

            tableView.getColumns().addAll(titleColumn);

            // Layout för sökfält och knappar
            HBox searchBox = new HBox(10, searchField, searchTypeComboBox, searchButton);
            HBox actionButtons = new HBox(10, loginButton, logoutButton, addBookButton, addRatingButton, addReviewButton, reviewButton, deleteBookButton);
            VBox vBox = new VBox(10, searchBox, actionButtons, tableView);
            vBox.setPrefWidth(800);
            vBox.setPrefHeight(600);

            reviewButton.setOnAction(e -> {
                Book selectedBook = tableView.getSelectionModel().getSelectedItem();
                if (selectedBook == null) {
                    showAlert("Fel", "Ingen bok vald", "Välj en bok för att visa recensioner.");
                    return;
                }

                try {
                    List<Review> reviews = controller.getReviewsByBookId(selectedBook.getBookId());
                    if (reviews.isEmpty()) {
                        showAlert("Info", "Inga recensioner", "Den valda boken har inga recensioner.");
                    } else {
                        showReviewsDialog(reviews);
                    }
                } catch (BooksDbException ex) {
                    showAlert("Fel", "Misslyckades att hämta recensioner", ex.getMessage());
                }
            });

            // Event-handler för knappar
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

            loginButton.setOnAction(e -> showLoginDialog());

            logoutButton.setOnAction(e -> {
                loggedInUser = null; // Nollställ den inloggade användaren
                showAlert("Success", "Utloggning lyckades", "Du är nu utloggad.");
            });

            addBookButton.setOnAction(e -> {
                if (loggedInUser == null) {
                    showAlert("Fel", "Ingen användare inloggad", "Du måste logga in för att lägga till böcker.");
                } else {
                    showAddBookDialog();
                }
            });

            addRatingButton.setOnAction(e -> showAddRatingDialog());
            addReviewButton.setOnAction(e -> showAddReviewDialog());

            // Radera bok-knappens event-handler
            deleteBookButton.setOnAction(e -> deleteSelectedBook());


            // Visa scenen
            Scene scene = new Scene(vBox);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Bokdatabas");
            primaryStage.show();
        } catch (Exception e) {
            showAlert("Fel", "Databasanslutning misslyckades", e.getMessage());
        }
    }


    private void deleteSelectedBook() {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Fel", "Ingen bok vald", "Välj en bok att radera.");
            return;
        }

        if (loggedInUser == null) {
            showAlert("Fel", "Ingen användare inloggad", "Du måste logga in för att radera böcker.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Bekräfta radering");
        confirmationAlert.setHeaderText("Är du säker på att du vill radera boken?");
        confirmationAlert.setContentText("Bok: " + selectedBook.getTitle());

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    controller.deleteBookById(selectedBook.getBookId());

                    // Uppdatera tabellen
                    tableView.getItems().remove(selectedBook);
                    tableView.refresh();

                    showAlert("Success", "Bok raderad", "Boken har raderats.");
                } catch (BooksDbException e) {
                    e.printStackTrace(); // För att se detaljer i konsolen
                    showAlert("Fel", "Misslyckades att radera bok", e.getMessage());
                }
            }
        });
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
                    authors.add(new Author(firstName, lastName, dob));
                    authors.addAll(additionalAuthors);
                    controller.addBookWithAuthors(new Book(title, publishDate, Long.parseLong(isbn)), authors, List.of(new Genre(genre)), loggedInUser);
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
                authors.add(new Author(firstName, lastName, dob));
                showAlert("Success", "Författare tillagd", "Författaren har lagts till.");
            }
            return null;
        });

        dialog.showAndWait();
    }




    private void showAddRatingDialog() {
        // Kontrollera om användaren är inloggad
        if (loggedInUser == null) {
            showAlert("Fel", "Ingen användare inloggad", "Du måste logga in för att lägga till ett betyg.");
            return;
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

    private void showReviewsDialog(List<Review> reviews) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Recensioner");
        dialog.setHeaderText("Recensioner för boken");

        VBox vBox = new VBox(10);
        for (Review review : reviews) {
            String userName = "Okänd användare"; // Default om användaren inte kan hittas
            try {
                userName = controller.getUserNameById(review.getUserId()); // Hämta användarnamn från databasen
            } catch (BooksDbException e) {
                e.printStackTrace(); // Logga fel för felsökning
            }

            Label userLabel = new Label("Användare: " + userName);
            Label dateLabel = new Label("Datum: " + review.getReviewDate().toString());
            Label ratingLabel = new Label("Betyg: " + review.getRatingId());
            Label reviewTextLabel = new Label("Recension: " + review.getReviewText());
            vBox.getChildren().addAll(userLabel, dateLabel, ratingLabel, reviewTextLabel, new Separator());
        }

        dialog.getDialogPane().setContent(vBox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }



    private void showAddReviewDialog() {
        // Kontrollera om användaren är inloggad
        if (loggedInUser == null) {
            showAlert("Fel", "Ingen användare inloggad", "Du måste logga in för att skriva en recension.");
            return;
        }

        // Kontrollera om en bok är vald från tabellen
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Fel", "Ingen bok vald", "Välj en bok att recensera.");
            return;
        }

        // Skapa dialogfönster för att skriva recension och ange betyg
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Lägg till recension");
        dialog.setHeaderText("Skriv din recension och ange ett betyg");

        TextArea reviewTextArea = new TextArea();
        reviewTextArea.setPromptText("Skriv din recension här...");

        TextField ratingField = new TextField();
        ratingField.setPromptText("Betyg (1-5)");

        VBox vBox = new VBox(10, reviewTextArea, ratingField);
        dialog.getDialogPane().setContent(vBox);

        ButtonType okButton = new ButtonType("Lägg till", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Avbryt", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                String reviewText = reviewTextArea.getText();
                String ratingInput = ratingField.getText();

                // Validera inmatning
                if (reviewText.isEmpty() || ratingInput.isEmpty()) {
                    showAlert("Fel", "Ogiltiga detaljer", "Alla fält måste fyllas i.");
                    return null;
                }

                int ratingValue;
                try {
                    ratingValue = Integer.parseInt(ratingInput);
                    if (ratingValue < 1 || ratingValue > 5) {
                        showAlert("Fel", "Ogiltigt betyg", "Betyget ska vara mellan 1 och 5.");
                        return null;
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Fel", "Ogiltigt betyg", "Ange ett giltigt heltal.");
                    return null;
                }

                // Lägg till recension och betyg i databasen
                try {
                    controller.addReview(selectedBook, loggedInUser, reviewText, ratingValue);
                    showAlert("Success", "Recension tillagd", "Recensionen har lagts till för boken.");
                } catch (BooksDbException e) {
                    showAlert("Fel", "Recension kunde inte sparas", e.getMessage());
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
