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
    void addBook(Book book, List<Genre> genres) throws BooksDbException;
    void addBookWithAuthors(Book book, List<Author> authors, List<Genre> genres) throws BooksDbException;
}