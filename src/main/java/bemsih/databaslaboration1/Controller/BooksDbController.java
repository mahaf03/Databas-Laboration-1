package bemsih.databaslaboration1.Controller;

import bemsih.databaslaboration1.Model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Controller class for interacting with the Books database.
 * Provides methods to fetch, add, and delete books, authors, genres, reviews, and users.
 * This implementation uses a thread pool for asynchronous execution of tasks.
 */
public class BooksDbController implements BooksDbInterface {

    private final ExecutorService executorService;
    /**
     * Constructor that initializes the executor service with a cached thread pool.
     */
    public BooksDbController() {
        this.executorService = Executors.newCachedThreadPool();
    }

    /**
     * Retrieves a list of books that match the given title.
     *
     * @param title The title to search for.
     * @return A list of books matching the title.
     * @throws BooksDbException If an error occurs during database interaction.
     */

    @Override
    public List<Book> getBooksByTitle(String title) throws BooksDbException {
        Callable<List<Book>> task = () -> {
            String query = "SELECT book_id, title, publishingDate, ISBN FROM BOOK WHERE title LIKE ?";
            ArrayList<Book> books = new ArrayList<>();

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, "%" + title + "%");

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
                throw new BooksDbException("Error fetching books by title: " + title, e);
            }

            return books;
        };

        try {
            Future<List<Book>> future = executorService.submit(task);
            return future.get(); // Blocking call to wait for the result
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for fetching books by title.", e);
        }
    }

    /**
     * Retrieves a list of books authored by a person with the given name.
     *
     * @param authorName The author's name to search for.
     * @return A list of books by the given author.
     * @throws BooksDbException If an error occurs during database interaction.
     */

    @Override
    public List<Book> getBooksByAuthor(String authorName) throws BooksDbException {
        Callable<List<Book>> task = () -> {
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
                stmt.setString(1, "%" + authorName + "%");

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
                throw new BooksDbException("Error fetching books by author: " + authorName, e);
            }

            return books;
        };

        try {
            Future<List<Book>> future = executorService.submit(task);
            return future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for fetching books by author.", e);
        }
    }

    /**
     * Retrieves a list of books belonging to a specific genre.
     *
     * @param genreName The genre to search for.
     * @return A list of books in the given genre.
     * @throws BooksDbException If an error occurs during database interaction.
     */

    @Override
    public List<Book> getBooksByGenre(String genreName) throws BooksDbException {
        Callable<List<Book>> task = () -> {
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
                throw new BooksDbException("Error fetching books by genre: " + genreName, e);
            }

            return books;
        };

        try {
            Future<List<Book>> future = executorService.submit(task);
            return future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for fetching books by genre.", e);
        }
    }
    /**
     * Adds a rating for a specific book by a user.
     *
     * @param book The book to rate.
     * @param user The user adding the rating.
     * @param ratingValue The rating value to assign.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public void addRating(Book book, User user, int ratingValue) throws BooksDbException {
        Callable<Void> task = () -> {
            String query = "INSERT INTO RATING (book_id, user_id, rating_value) VALUES (?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, book.getBook_id());
                stmt.setInt(2, user != null ? user.getUserId() : 1);
                stmt.setInt(3, ratingValue);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new BooksDbException("Failed to add rating. No rows affected.");
                }
            } catch (SQLException e) {
                throw new BooksDbException("Error adding rating for book ID: " + book.getBook_id(), e);
            }
            return null;
        };

        try {
            Future<Void> future = executorService.submit(task);
            future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for adding rating.", e);
        }
    }
    /**
     * Adds a new book along with its genres.
     *
     * @param book The book to add.
     * @param genres A list of genres associated with the book.
     * @param user The user adding the book.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public void addBook(Book book, List<Genre> genres, User user) throws BooksDbException {
        Callable<Void> task = () -> {
            String bookInsertQuery = "INSERT INTO BOOK(title, publishingDate, ISBN, user_id) VALUES (?, ?, ?, ?)";
            String genreInsertQuery = "INSERT INTO GENRE(genreName) VALUES (?)";
            String bookGenreInsertQuery = "INSERT INTO BOOK_GENRE (book_id, genre_id) VALUES (?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement bookStmt = conn.prepareStatement(bookInsertQuery, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement genreStmt = conn.prepareStatement(genreInsertQuery, Statement.RETURN_GENERATED_KEYS)) {

                bookStmt.setString(1, book.getTitle());
                bookStmt.setDate(2, Date.valueOf(book.getDate()));
                bookStmt.setLong(3, book.getISBN());
                bookStmt.setInt(4, user.getUserId());
                bookStmt.executeUpdate();

                try (ResultSet generatedKeys = bookStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setBook_id(generatedKeys.getInt(1));
                    } else {
                        throw new BooksDbException("Failed to retrieve generated book ID.");
                    }
                }

                for (Genre genre : genres) {
                    genreStmt.setString(1, genre.getGenreName());
                    genreStmt.executeUpdate();

                    try (ResultSet generatedKeys = genreStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            genre.setGenreId(generatedKeys.getInt(1));
                        } else {
                            throw new BooksDbException("Failed to retrieve generated genre ID.");
                        }
                    }

                    try (PreparedStatement bookGenreStmt = conn.prepareStatement(bookGenreInsertQuery)) {
                        bookGenreStmt.setInt(1, book.getBook_id());
                        bookGenreStmt.setInt(2, genre.getGenreId());
                        bookGenreStmt.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                throw new BooksDbException("Error adding book and genres", e);
            }
            return null;
        };

        try {
            Future<Void> future = executorService.submit(task);
            future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for adding book.", e);
        }
    }
    /**
     * Adds a book with its authors and genres.
     *
     * @param book The book to add.
     * @param authors A list of authors associated with the book.
     * @param genres A list of genres associated with the book.
     * @param user The user adding the book.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public void addBookWithAuthors(Book book, List<Author> authors, List<Genre> genres, User user) throws BooksDbException {
        Callable<Void> task = () -> {
            addBook(book, genres, user);

            String authorInsertQuery = "INSERT INTO AUTHOR(firstName, lastName, dateOfBirth, user_id) VALUES (?, ?, ?, ?)";
            String bookAuthorInsertQuery = "INSERT INTO BOOK_AUTHOR (book_id, author_id) VALUES (?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement authorStmt = conn.prepareStatement(authorInsertQuery, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement bookAuthorStmt = conn.prepareStatement(bookAuthorInsertQuery)) {

                conn.setAutoCommit(false);

                for (Author author : authors) {
                    authorStmt.setString(1, author.getFirstName());
                    authorStmt.setString(2, author.getLastName());
                    authorStmt.setDate(3, author.getDateOfBirth() != null ? Date.valueOf(author.getDateOfBirth()) : null);
                    authorStmt.setInt(4, user.getUserId());
                    authorStmt.executeUpdate();

                    try (ResultSet generatedKeys = authorStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            author.setAuthor_id(generatedKeys.getInt(1));
                        } else {
                            throw new BooksDbException("Failed to retrieve generated author ID.");
                        }
                    }

                    bookAuthorStmt.setInt(1, book.getBook_id());
                    bookAuthorStmt.setInt(2, author.getAuthor_id());
                    bookAuthorStmt.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                throw new BooksDbException("Error adding authors and relations", e);
            }
            return null;
        };

        try {
            Future<Void> future = executorService.submit(task);
            future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for adding book with authors.", e);
        }
    }
    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to fetch.
     * @return The user object if found, or null if not.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public User getUserById(int userId) throws BooksDbException {
        Callable<User> task = () -> {
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
                throw new BooksDbException("Error fetching user by ID: " + userId, e);
            }
            return null;
        };

        try {
            Future<User> future = executorService.submit(task);
            return future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for fetching user by ID.", e);
        }
    }
    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to fetch.
     * @return The user object if found, or null if not.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public User getUserByUsername(String username) throws BooksDbException {
        Callable<User> task = () -> {
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
                throw new BooksDbException("Error fetching user by username: " + username, e);
            }
            return null;
        };

        try {
            Future<User> future = executorService.submit(task);
            return future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for fetching user by username.", e);
        }
    }
    /**
     * Validates a user's credentials.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @return True if the credentials are valid, false otherwise.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public boolean validateUser(String username, String password) throws BooksDbException {
        Callable<Boolean> task = () -> {
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
                throw new BooksDbException("Error validating user", e);
            }
            return false;
        };

        try {
            Future<Boolean> future = executorService.submit(task);
            return future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for validating user.", e);
        }
    }
    /**
     * Adds a review for a book by a user, along with a rating.
     *
     * @param book The book being reviewed.
     * @param user The user writing the review.
     * @param reviewText The text of the review.
     * @param ratingValue The rating value associated with the review.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public void addReview(Book book, User user, String reviewText, int ratingValue) throws BooksDbException {
        Callable<Void> task = () -> {
            String addRatingQuery = "INSERT INTO RATING (book_id, user_id, rating_value) VALUES (?, ?, ?)";
            String addReviewQuery = "INSERT INTO REVIEW (review_text, book_id, user_id, rating_id) VALUES (?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false);

                int ratingId;
                try (PreparedStatement ratingStmt = conn.prepareStatement(addRatingQuery, Statement.RETURN_GENERATED_KEYS)) {
                    ratingStmt.setInt(1, book.getBook_id());
                    ratingStmt.setInt(2, user.getUserId());
                    ratingStmt.setInt(3, ratingValue);
                    ratingStmt.executeUpdate();

                    try (ResultSet generatedKeys = ratingStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            ratingId = generatedKeys.getInt(1);
                        } else {
                            throw new BooksDbException("Failed to retrieve generated rating ID.");
                        }
                    }
                }

                try (PreparedStatement reviewStmt = conn.prepareStatement(addReviewQuery)) {
                    reviewStmt.setString(1, reviewText);
                    reviewStmt.setInt(2, book.getBook_id());
                    reviewStmt.setInt(3, user.getUserId());
                    reviewStmt.setInt(4, ratingId);
                    reviewStmt.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                throw new BooksDbException("Error adding review and rating", e);
            }
            return null;
        };

        try {
            Future<Void> future = executorService.submit(task);
            future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for adding review.", e);
        }
    }
    /**
     * Deletes a book and all associated data (reviews, ratings, authors, genres) by its ID.
     *
     * @param bookId The ID of the book to delete.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    public void deleteBookById(int bookId) throws BooksDbException {
        Callable<Void> task = () -> {
            String deleteReviewsQuery = "DELETE FROM REVIEW WHERE book_id = ?";
            String deleteRatingsQuery = "DELETE FROM RATING WHERE book_id = ?";
            String deleteBookAuthorsQuery = "DELETE FROM BOOK_AUTHOR WHERE book_id = ?";
            String deleteBookGenresQuery = "DELETE FROM BOOK_GENRE WHERE book_id = ?";
            String deleteBookQuery = "DELETE FROM BOOK WHERE book_id = ?";

            try (Connection conn = DatabaseConnection.getConnection()) {
                conn.setAutoCommit(false); // Starta en transaktion

                // Radera recensioner kopplade till boken
                try (PreparedStatement stmt = conn.prepareStatement(deleteReviewsQuery)) {
                    stmt.setInt(1, bookId);
                    stmt.executeUpdate();
                }

                // Radera betyg kopplade till boken
                try (PreparedStatement stmt = conn.prepareStatement(deleteRatingsQuery)) {
                    stmt.setInt(1, bookId);
                    stmt.executeUpdate();
                }

                // Radera författarkopplingar för boken
                try (PreparedStatement stmt = conn.prepareStatement(deleteBookAuthorsQuery)) {
                    stmt.setInt(1, bookId);
                    stmt.executeUpdate();
                }

                // Radera genrekopplingar för boken
                try (PreparedStatement stmt = conn.prepareStatement(deleteBookGenresQuery)) {
                    stmt.setInt(1, bookId);
                    stmt.executeUpdate();
                }

                // Radera själva boken
                try (PreparedStatement stmt = conn.prepareStatement(deleteBookQuery)) {
                    stmt.setInt(1, bookId);
                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected == 0) {
                        throw new BooksDbException("Ingen bok raderades. Kontrollera om bok-ID finns i databasen.");
                    }
                }

                conn.commit(); // Bekräfta transaktionen
            } catch (SQLException e) {
                throw new BooksDbException("Fel vid radering av bok med ID: " + bookId, e);
            }

            return null;
        };

        try {
            Future<Void> future = executorService.submit(task);
            future.get(); // Vänta på att bakgrundsprocessen slutförs
        } catch (Exception e) {
            throw new BooksDbException("Fel vid bakgrundsprocess för att radera bok.", e);
        }
    }

    /**
     * Retrieves a list of reviews for a book by its ID.
     *
     * @param bookId The ID of the book.
     * @return A list of reviews associated with the book.
     * @throws BooksDbException If an error occurs during database interaction.
     */

    @Override
    public List<Review> getReviewsByBookId(int bookId) throws BooksDbException {
        Callable<List<Review>> task = () -> {
            String query = """
            SELECT r.review_id, r.review_text, r.review_date, r.rating_id, u.user_id, u.userName,
                   b.book_id, b.title, b.publishingDate, b.ISBN
            FROM REVIEW r
            JOIN USER u ON r.user_id = u.user_id
            JOIN BOOK b ON r.book_id = b.book_id
            WHERE r.book_id = ?
        """;
            List<Review> reviews = new ArrayList<>();

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, bookId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Skapa en bokinstans
                        Book book = new Book(
                                rs.getInt("book_id"),
                                rs.getString("title"),
                                rs.getDate("publishingDate").toLocalDate(),
                                rs.getLong("ISBN")
                        );

                        // Skapa en användarinstans
                        User user = new User(
                                rs.getString("userName"),
                                rs.getInt("user_id"),
                                null // Lösenord hämtas inte här
                        );

                        // Skapa en recension
                        Review review = new Review(
                                rs.getInt("review_id"),
                                rs.getInt("rating_id"),
                                user,
                                book,
                                rs.getTimestamp("review_date").toLocalDateTime(),
                                rs.getString("review_text")
                        );

                        reviews.add(review);
                    }
                }
            } catch (SQLException e) {
                throw new BooksDbException("Error fetching reviews for book ID: " + bookId, e);
            }

            return reviews;
        };

        try {
            Future<List<Review>> future = executorService.submit(task);
            return future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for fetching reviews.", e);
        }
    }


    /**
     * Shuts down the executor service used for background tasks.
     */

    public void shutdown() {
        executorService.shutdown();
    }
}