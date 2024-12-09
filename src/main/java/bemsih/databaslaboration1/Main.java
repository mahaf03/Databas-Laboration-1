//package bemsih.databaslaboration1;
//
//import bemsih.databaslaboration1.Controller.BooksDbController;
//import bemsih.databaslaboration1.Controller.BooksDbException;
//import bemsih.databaslaboration1.Model.*;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        try (Connection connection = DatabaseConnection.getConnection()) {
//            BooksDbController controller = new BooksDbController(DatabaseConnection.getConnection());
//            Scanner scanner = new Scanner(System.in);
//            boolean running = true;
//
//            while (running) {
//                System.out.println("\nVälj ett alternativ:");
//                System.out.println("1. Sök efter böcker baserat på titel");
//                System.out.println("2. Sök efter böcker baserat på författare");
//                System.out.println("3. Sök efter böcker baserat på genre");
//                System.out.println("4. Lägg till en ny bok");
//                System.out.println("5. Lägg till en ny bok med författare");
//                System.out.println("6. Lägg till ett betyg för en bok");
//                System.out.println("7. Avsluta");
//
//                int choice = scanner.nextInt();
//                scanner.nextLine(); // Konsumera radbrytning
//
//                switch (choice) {
//                    case 1 -> {
//                        System.out.print("Ange titel: ");
//                        String title = scanner.nextLine();
//                        try {
//                            List<Book> books = controller.getBooksByTitle(title);
//                            books.forEach(book -> System.out.println("Bok: " + book.getTitle()));
//                        } catch (BooksDbException e) {
//                            System.err.println("Fel: " + e.getMessage());
//                        }
//                    }
//                    case 2 -> {
//                        System.out.print("Ange författarnamn: ");
//                        String authorName = scanner.nextLine();
//                        try {
//                            List<Book> books = controller.getBooksByAuthor(authorName);
//                            books.forEach(book -> System.out.println("Bok: " + book.getTitle()));
//                        } catch (BooksDbException e) {
//                            System.err.println("Fel: " + e.getMessage());
//                        }
//                    }
//                    case 3 -> {
//                        System.out.print("Ange genre: ");
//                        String genreName = scanner.nextLine();
//                        try {
//                            List<Book> books = controller.getBooksByGenre(genreName);
//                            books.forEach(book -> System.out.println("Bok: " + book.getTitle()));
//                        } catch (BooksDbException e) {
//                            System.err.println("Fel: " + e.getMessage());
//                        }
//                    }
//                    case 4 -> {
//                        System.out.print("Ange titel: ");
//                        String title = scanner.nextLine();
//                        System.out.print("Ange publiceringsdatum (YYYY-MM-DD): ");
//                        LocalDate date = LocalDate.parse(scanner.nextLine());
//                        System.out.print("Ange ISBN: ");
//                        long isbn = scanner.nextLong();
//                        scanner.nextLine(); // Konsumera radbrytning
//
//                        Book book = new Book(0, title, date, isbn);
//                        List<Genre> genres = new ArrayList<>();
//
//                        System.out.print("Ange antal genrer: ");
//                        int numGenres = scanner.nextInt();
//                        scanner.nextLine();
//
//                        for (int i = 0; i < numGenres; i++) {
//                            System.out.print("Ange genre: ");
//                            String genreName = scanner.nextLine();
//                            genres.add(new Genre(0, genreName));
//                        }
//
//                        try {
//                            controller.addBook(book, genres);
//                            System.out.println("Bok med genrer tillagd.");
//                        } catch (BooksDbException e) {
//                            System.err.println("Fel: " + e.getMessage());
//                        }
//                    }
//                    case 5 -> {
//                        System.out.print("Ange titel: ");
//                        String title = scanner.nextLine();
//                        System.out.print("Ange publiceringsdatum (YYYY-MM-DD): ");
//                        LocalDate date = LocalDate.parse(scanner.nextLine());
//                        System.out.print("Ange ISBN: ");
//                        long isbn = scanner.nextLong();
//                        scanner.nextLine(); // Konsumera radbrytning
//
//                        Book book = new Book(0, title, date, isbn);
//                        List<Author> authors = new ArrayList<>();
//                        List<Genre> genres = new ArrayList<>();
//
//                        System.out.print("Ange antal författare: ");
//                        int numAuthors = scanner.nextInt();
//                        scanner.nextLine();
//
//                        for (int i = 0; i < numAuthors; i++) {
//                            System.out.print("Ange författarens förnamn: ");
//                            String firstName = scanner.nextLine();
//                            System.out.print("Ange författarens efternamn: ");
//                            String lastName = scanner.nextLine();
//                            System.out.print("Ange författarens födelsedatum (YYYY-MM-DD): ");
//                            LocalDate dob = LocalDate.parse(scanner.nextLine());
//                            authors.add(new Author(0, firstName, lastName, dob));
//                        }
//
//                        System.out.print("Ange antal genrer: ");
//                        int numGenres = scanner.nextInt();
//                        scanner.nextLine();
//
//                        for (int i = 0; i < numGenres; i++) {
//                            System.out.print("Ange genre: ");
//                            String genreName = scanner.nextLine();
//                            genres.add(new Genre(0, genreName));
//                        }
//
//                        try {
//                            controller.addBookWithAuthors(book, authors, genres);
//                            System.out.println("Bok med författare och genrer tillagd.");
//                        } catch (BooksDbException e) {
//                            System.err.println("Fel: " + e.getMessage());
//                        }
//                    }
//                    case 6 -> {
//                        System.out.print("Ange bok-ID: ");
//                        int bookId = scanner.nextInt();
//                        scanner.nextLine(); // Konsumera radbrytning
//                        System.out.print("Ange användar-ID: ");
//                        int userId = scanner.nextInt();
//                        scanner.nextLine(); // Konsumera radbrytning
//                        System.out.print("Ange betyg (1-5): ");
//                        int ratingValue = scanner.nextInt();
//                        scanner.nextLine(); // Konsumera radbrytning
//
//                        try {
//                            Book book = new Book(bookId, "", null, 0L);
//                            User user = new User("", "", "", "", userId, "");
//                            controller.addRating(book, user, ratingValue);
//                            System.out.println("Betyg tillagt.");
//                        } catch (BooksDbException e) {
//                            System.err.println("Fel: " + e.getMessage());
//                        }
//                    }
//                    case 7 -> {
//                        System.out.println("Avslutar programmet.");
//                        running = false;
//                    }
//                    default -> System.out.println("Ogiltigt val. Försök igen.");
//                }
//            }
//
//            scanner.close();
//        } catch (SQLException e) {
//            System.err.println("Databasanslutning misslyckades: " + e.getMessage());
//        }
//    }
//}