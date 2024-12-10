package bemsih.databaslaboration1.Controller;

import bemsih.databaslaboration1.Model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BooksDbController implements BooksDbInterface {

    public BooksDbController() {

    }


    @Override
    public List<Book> getBooksByTitle(String title) throws BooksDbException {
        String query = "SELECT book_id, title, publishingDate, ISBN FROM BOOK WHERE title LIKE ?";
        ArrayList<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Lägg till wildcard runt titeln för delvis matchning
            stmt.setString(1, "%" + title + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Skapa bokobjekt från resultatet
                    Book book = new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getDate("publishingDate").toLocalDate(),
                            rs.getLong("ISBN")
                    );
                    books.add(book);
                }
            }

        } catch (SQLException e) {
            throw new BooksDbException("Error fetching books by title: " + title, e);
        }

        return books;
    }

    @Override
    public List<Book> getBooksByAuthor(String authorName) throws BooksDbException {
        List<Book> books = new ArrayList<>();
        String query = """
        SELECT b.book_id, b.title, b.publishingDate, b.ISBN
        FROM BOOK b
        JOIN BOOK_AUTHOR ba ON b.book_id = ba.book_id
        JOIN AUTHOR a ON ba.author_id = a.author_id
        WHERE CONCAT(a.firstName, ' ', a.lastName) LIKE ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Använd wildcard för att söka efter författarnamn som innehåller inputsträngen
            stmt.setString(1, "%" + authorName + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Skapa ett nytt Book-objekt för varje rad i resultatet
                    Book book = new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getDate("publishingDate").toLocalDate(),
                            rs.getLong("ISBN")
                    );
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            // Wrappa SQL-undantag i en BooksDbException
            throw new BooksDbException("Fel vid hämtning av böcker för författare: " + authorName, e);
        }

        return books;
    }

    @Override
    public List<Book> getBooksByGenre(String genreName) throws BooksDbException {
        String query = """
        SELECT DISTINCT b.book_id, b.title, b.publishingDate, b.ISBN
        FROM BOOK b
        JOIN BOOK_GENRE bg ON b.book_id = bg.book_id
        JOIN GENRE g ON bg.genre_id = g.genre_id
        WHERE g.genreName LIKE ? """;
        List<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + genreName + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getDate("publishingDate").toLocalDate(),
                            rs.getLong("ISBN")
                    );
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            throw new BooksDbException("Fel vid hämtning av böcker för genre: " + genreName, e);
        }

        return books;
    }

    @Override
    public void addRating(Book book, User user, int ratingValue) throws BooksDbException {
        String query = "INSERT INTO RATING (book_id, user_id, rating_value) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, book.getBook_id());
            stmt.setInt(2, user != null ? user.getUserId() : 1); // Använd en standardanvändare om null
            stmt.setInt(3, ratingValue);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new BooksDbException("Misslyckades att lägga till betyg. Ingen rad lades till.");
            }
        } catch (SQLException e) {
            throw new BooksDbException("Fel vid tillägg av betyg för bok ID: " + book.getBook_id(), e);
        }
    }

    @Override
    public void addBook(Book book, List<Genre> genres) throws BooksDbException {
        String bookInsertQuery = "INSERT INTO BOOK(title, publishingDate, ISBN) VALUES (?, ?, ?)";
        String genreInsertQuery = "INSERT INTO GENRE(genreName) VALUES (?)";
        String bookGenreInsertQuery = "INSERT INTO BOOK_GENRE (book_id, genre_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement bookStmt = conn.prepareStatement(bookInsertQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement genreStmt = conn.prepareStatement(genreInsertQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Lägg till boken
            bookStmt.setString(1, book.getTitle());
            bookStmt.setDate(2, Date.valueOf(book.getDate()));
            bookStmt.setLong(3, book.getISBN());
            bookStmt.executeUpdate();

            try (ResultSet generatedKeys = bookStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setBook_id(generatedKeys.getInt(1));
                } else {
                    throw new BooksDbException("Misslyckades att få genererat bok-ID.");
                }
            }

            // Lägg till genrer och kopplingar
            for (Genre genre : genres) {
                genreStmt.setString(1, genre.getGenreName());
                genreStmt.executeUpdate();

                try (ResultSet generatedKeys = genreStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        genre.setGenreId(generatedKeys.getInt(1));
                    } else {
                        throw new BooksDbException("Misslyckades att få genererat genre-ID.");
                    }
                }

                try (PreparedStatement bookGenreStmt = conn.prepareStatement(bookGenreInsertQuery)) {
                    bookGenreStmt.setInt(1, book.getBook_id());
                    bookGenreStmt.setInt(2, genre.getGenreId());
                    bookGenreStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new BooksDbException("Fel vid tillägg av bok och genrer", e);
        }
    }
    @Override
    public void addBookWithAuthors(Book book, List<Author> authors, List<Genre> genres) throws BooksDbException {
        // Lägg till boken och genrer först
        addBook(book, genres);

        String authorInsertQuery = "INSERT INTO AUTHOR(firstName, lastName, dateOfBirth) VALUES (?, ?, ?)";
        String bookAuthorInsertQuery = "INSERT INTO BOOK_AUTHOR (book_id, author_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement authorStmt = conn.prepareStatement(authorInsertQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement bookAuthorStmt = conn.prepareStatement(bookAuthorInsertQuery)) {

            conn.setAutoCommit(false); // Start transaktion

            for (Author author : authors) {
                // Lägg till författaren
                authorStmt.setString(1, author.getFirstName());
                authorStmt.setString(2, author.getLastName());
                authorStmt.setDate(3, author.getDateOfBirth() != null ? Date.valueOf(author.getDateOfBirth()) : null);
                authorStmt.executeUpdate();

                // Hämta det genererade ID:t för författaren
                try (ResultSet generatedKeys = authorStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        author.setAuthor_id(generatedKeys.getInt(1));
                    } else {
                        throw new BooksDbException("Misslyckades att få genererat author-ID.");
                    }
                }

                // Lägg till relationen mellan boken och författaren
                bookAuthorStmt.setInt(1, book.getBook_id());
                bookAuthorStmt.setInt(2, author.getAuthor_id());
                bookAuthorStmt.executeUpdate();
            }

            conn.commit(); // Bekräfta transaktion
        } catch (SQLException e) {
            throw new BooksDbException("Fel vid tillägg av författare och relationer", e);
        }
    }

    @Override
    public User getUserById(int userId) throws BooksDbException {
        String query = "SELECT user_id, userName, passWord FROM USER WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("userName"),
                            rs.getInt("user_id"),
                            rs.getString("passWord")
                    );
                }
            }
        } catch (SQLException e) {
            throw new BooksDbException("Kunde inte hämta användare med ID: " + userId, e);
        }
        return null;
    }


    @Override
    public User getUserByUsername(String username) throws BooksDbException {
        String query = "SELECT user_id, userName, passWord FROM USER WHERE userName = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("userName"),
                            rs.getInt("user_id"),
                            rs.getString("passWord")
                    );
                }
            }
        } catch (SQLException e) {
            throw new BooksDbException("Kunde inte hämta användare med användarnamn: " + username, e);
        }
        return null;
    }


    @Override
    public boolean validateUser(String username, String password) throws BooksDbException {
        String query = "SELECT COUNT(*) AS count FROM USER WHERE userName = ? AND passWord = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            throw new BooksDbException("Kunde inte validera användaren", e);
        }
        return false;
    }

}
