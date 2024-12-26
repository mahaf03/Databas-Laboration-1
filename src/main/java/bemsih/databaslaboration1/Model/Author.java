package bemsih.databaslaboration1.Model;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class Author {
    private ObjectId authorId; // Unique identifier for the author
    private String firstName; // First name of the author
    private String lastName; // Last name of the author
    private Date dateOfBirth; // Birthdate of the author
    private ArrayList<ObjectId> bookIds; // List of book IDs written by the author

    /**
     * Constructs an Author with the specified details.
     *
     * @param firstName  the first name of the author
     * @param lastName   the last name of the author
     * @param dateOfBirth the birthdate of the author
     */
    public Author(String firstName, String lastName, LocalDate dateOfBirth) {
        this.authorId = new ObjectId(); // Generate a new ObjectId
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = Date.from(dateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.bookIds = new ArrayList<>();
    }

    /**
     * Gets the unique identifier of the author.
     *
     * @return the author's ID
     */
    public ObjectId getAuthorId() {
        return authorId;
    }

    /**
     * Sets the unique identifier of the author.
     *
     * @param authorId the new ID for the author
     */
    public void setAuthorId(ObjectId authorId) {
        this.authorId = authorId;
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
     * Sets the first name of the author.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
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
     * Sets the last name of the author.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the birthdate of the author.
     *
     * @return the author's birthdate
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Sets the birthdate of the author.
     *
     * @param dateOfBirth the birthdate to set
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = Date.from(dateOfBirth.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Gets the list of book IDs written by the author.
     *
     * @return a list of book IDs
     */
    public ArrayList<ObjectId> getBookIds() {
        return bookIds;
    }

    /**
     * Sets the list of book IDs written by the author.
     *
     * @param bookIds the list of book IDs to set
     */
    public void setBookIds(ArrayList<ObjectId> bookIds) {
        this.bookIds = bookIds;
    }

    /**
     * Adds a book ID to the author's list of books.
     *
     * @param bookId the book ID to add
     */
    public void addBookId(ObjectId bookId) {
        this.bookIds.add(bookId);
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", bookIds=" + bookIds +
                '}';
    }
}
