package bemsih.databaslaboration1.Controller;

import bemsih.databaslaboration1.Model.Author;
import bemsih.databaslaboration1.Model.Book;

import java.sql.Connection;
import java.util.List;

public class BooksDbController implements BooksDbInterface{

    private final Connection connection;

    public BooksDbController(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Book> getBooksByTitle(String title) throws BooksDbException {

        return List.of();
    }

    @Override
    public List<Book> getBooksByAuthor(String authorName) throws BooksDbException {
        return List.of();
    }

    @Override
    public List<Book> getBooksByGenre(String genreName) throws BooksDbException {
        return List.of();
    }

    @Override
    public void addRating(int bookId, int userId, int ratingValue) throws BooksDbException {

    }

    @Override
    public void addBook(Book book) throws BooksDbException {

    }

    @Override
    public void addBookWithAuthors(Book book, List<Author> authors) throws BooksDbException {

    }
}
