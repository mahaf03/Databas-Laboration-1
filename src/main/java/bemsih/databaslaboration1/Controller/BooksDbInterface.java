package bemsih.databaslaboration1.Controller;

import bemsih.databaslaboration1.Model.*;

import java.util.List;

public interface BooksDbInterface {

    // Search operations
    List<Book> getBooksByTitle(String title) throws BooksDbException;
    List<Book> getBooksByAuthor(String authorName) throws BooksDbException;
    List<Book> getBooksByGenre(String genreName) throws BooksDbException;

    // Rating operations
    void addRating(int bookId, int userId, int ratingValue) throws BooksDbException;

    // Book operations
    void addBook(Book book) throws BooksDbException;
    void addBookWithAuthors(Book book, List<Author> authors) throws BooksDbException;
}

