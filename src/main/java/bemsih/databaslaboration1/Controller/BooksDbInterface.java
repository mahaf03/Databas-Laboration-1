package bemsih.databaslaboration1.Controller;

import bemsih.databaslaboration1.Model.*;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Interface for interacting with the Books database.
 * Provides methods for searching, managing books, authors, genres, and user data,
 * as well as handling ratings and reviews.
 */
public interface BooksDbInterface {

    public void initializeDefaultUser() throws BooksDbException;
    // Search operations
    public String getUserNameById(ObjectId userId) throws BooksDbException;

    /**
     * Retrieves a list of books based on their title.
     *
     * @param title the title of the book(s) to search for.
     * @return a list of books that match the given title.
     * @throws BooksDbException if an error occurs during the operation.
     */

    List<Book> getBooksByTitle(String title) throws BooksDbException;

    /**
     * Retrieves a list of books by a specific author.
     *
     * @param authorName the name of the author whose books to search for.
     * @return a list of books written by the specified author.
     * @throws BooksDbException if an error occurs during the operation.
     */
    List<Book> getBooksByAuthor(String authorName) throws BooksDbException;

    /**
     * Retrieves a list of books in a specific genre.
     *
     * @param genreName the name of the genre to search for books in.
     * @return a list of books in the specified genre.
     * @throws BooksDbException if an error occurs during the operation.
     */
    List<Book> getBooksByGenre(String genreName) throws BooksDbException;

    // Rating operations

    /**
     * Adds a rating to a book from a specific user.
     *
     * @param book the book being rated.
     * @param user the user providing the rating.
     * @param ratingValue the rating value assigned by the user.
     * @throws BooksDbException if an error occurs during the operation.
     */
    void addRating(Book book, User user, int ratingValue) throws BooksDbException;

    // Book operations

    /**
     * Adds a new book to the database along with its genres.
     *
     * @param book the book to be added.
     * @param genres the list of genres associated with the book.
     * @param user the user adding the book.
     * @throws BooksDbException if an error occurs during the operation.
     */
    void addBook(Book book, List<Genre> genres, User user) throws BooksDbException;

    /**
     * Adds a new book to the database along with its authors and genres.
     *
     * @param book the book to be added.
     * @param authors the list of authors associated with the book.
     * @param genres the list of genres associated with the book.
     * @param user the user adding the book.
     * @throws BooksDbException if an error occurs during the operation.
     */
    void addBookWithAuthors(Book book, List<Author> authors, List<Genre> genres, User user) throws BooksDbException;

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId the ID of the user to retrieve.
     * @return the user with the specified ID.
     * @throws BooksDbException if an error occurs during the operation.
     */
    User getUserById(int userId) throws BooksDbException;

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve.
     * @return the user with the specified username.
     * @throws BooksDbException if an error occurs during the operation.
     */
    User getUserByUsername(String username) throws BooksDbException;

    /**
     * Validates a user's login credentials.
     *
     * @param username the username to validate.
     * @param password the password to validate.
     * @return true if the credentials are valid, false otherwise.
     * @throws BooksDbException if an error occurs during the operation.
     */
    boolean validateUser(String username, String password) throws BooksDbException;

    /**
     * Adds a review for a specific book.
     *
     * @param book the book being reviewed.
     * @param user the user submitting the review.
     * @param reviewText the text content of the review.
     * @param ratingValue the rating value associated with the review.
     * @throws BooksDbException if an error occurs during the operation.
     */
    void addReview(Book book, User user, String reviewText, int ratingValue) throws BooksDbException;

    /**
     * Deletes a book from the database using its ID.
     *
     * @param bookId the ID of the book to delete.
     * @throws BooksDbException if an error occurs during the operation.
     */
    void deleteBookById(String bookId) throws BooksDbException;


    /**
     * Retrieves a list of reviews for a specific book by its ID.
     *
     * @param bookId the ID of the book to retrieve reviews for.
     * @return a list of reviews for the specified book.
     * @throws BooksDbException if an error occurs during the operation.
     */
    List<Review> getReviewsByBookId(String bookId) throws BooksDbException;
}
