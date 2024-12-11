package bemsih.databaslaboration1.Model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents an author with personal details and their associated books.
 */
public class Author {
    private int author_id;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private ArrayList<Book> books;

    /**
     * Constructs an Author with the specified details.
     *
     * @param author_id  the unique identifier for the author
     * @param firstName  the first name of the author
     * @param lastName   the last name of the author
     * @param dateOfBirth the birthdate of the author
     */
    public Author(int author_id, String firstName, String lastName, LocalDate dateOfBirth) {
        this.author_id = author_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.books = new ArrayList<>();
    }

    /**
     * Gets the date of birth of the author.
     *
     * @return the author's date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Gets the last name of the author.
     *
     * @return the author's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the first name of the author.
     *
     * @return the author's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the unique identifier of the author.
     *
     * @return the author's ID
     */
    public int getAuthor_id() {
        return author_id;
    }

    /**
     * Sets the unique identifier of the author.
     *
     * @param author_id the new ID for the author
     */
    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    /**
     * Gets the list of books written by the author.
     *
     * @return a list of the author's books
     */
    public ArrayList<Book> getBooks() {
        return books;
    }

    /**
     * Sets the list of books associated with the author.
     *
     * @param books a list of books to associate with the author
     */
    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
}
