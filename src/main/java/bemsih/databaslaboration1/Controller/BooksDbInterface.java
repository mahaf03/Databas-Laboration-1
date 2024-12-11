package bemsih.databaslaboration1.Controller;

import bemsih.databaslaboration1.Model.*;

import java.util.List;

public interface BooksDbInterface {

    // Search operations
    List<Book> getBooksByTitle(String title) throws BooksDbException;
    List<Book> getBooksByAuthor(String authorName) throws BooksDbException;
    List<Book> getBooksByGenre(String genreName) throws BooksDbException;

    // Rating operations
    void addRating(Book book, User user, int ratingValue) throws BooksDbException;

    // Book operations
    void addBook(Book book, List<Genre> genres, User user) throws BooksDbException;
    void addBookWithAuthors(Book book, List<Author> authors, List<Genre> genres, User user) throws BooksDbException;
    User getUserById(int userId) throws BooksDbException; // Hämta användare med ID
    User getUserByUsername(String username) throws BooksDbException; // Hämta användare med användarnamn
    boolean validateUser(String username, String password) throws BooksDbException; // Validera användaruppgifter
    public void addReview(Book book, User user, String reviewText, int ratingValue) throws BooksDbException;
    public void deleteBookById(int bookId) throws BooksDbException;
    List<Review> getReviewsByBookId(int bookId) throws BooksDbException;

}