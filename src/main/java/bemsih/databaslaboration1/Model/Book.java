package bemsih.databaslaboration1.Model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a book with details such as book ID, title, publication date, ISBN, authors, and genres.
 */
public class Book {
    private int book_id; // Unique identifier for the book
    private String title; // Title of the book
    private LocalDate date; // Publication date of the book
    private Long ISBN; // ISBN number of the book
    private ArrayList<Author> authors; // List of authors who wrote the book
    private ArrayList<Genre> genres; // List of genres the book belongs to

    /**
     * Constructor for creating a new Book instance.
     *
     * @param book_id Unique identifier for the book.
     * @param title Title of the book.
     * @param date Publication date of the book.
     * @param ISBN ISBNnumber of the book.
     */
    public Book(int book_id, String title, LocalDate date, Long ISBN) {
        this.book_id = book_id;
        this.title = title;
        this.date = date;
        this.ISBN = ISBN;
        this.authors = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    /**
     * Gets the unique identifier of the book.
     *
     * @return The book ID.
     */
    public int getBook_id() {
        return book_id;
    }

    /**
     * Sets the unique identifier of the book.
     *
     * @param book_id The book ID to set.
     */
    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    /**
     * Gets the title of the book.
     *
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the publication date of the book.
     *
     * @return The publication date of the book.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the publication date of the book.
     *
     * @param date The publication date to set.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the ISBN of the book.
     *
     * @return The ISBN of the book.
     */
    public Long getISBN() {
        return ISBN;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param ISBN The ISBN to set.
     */
    public void setISBN(Long ISBN) {
        this.ISBN = ISBN;
    }
}
