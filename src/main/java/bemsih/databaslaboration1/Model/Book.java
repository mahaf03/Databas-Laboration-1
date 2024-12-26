package bemsih.databaslaboration1.Model;

import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class Book {
    private ObjectId bookId; // Unique identifier for the book
    private String title; // Title of the book
    private Date publicationDate; // Publication date of the book
    private Long ISBN; // ISBN number of the book
    private ArrayList<ObjectId> authorIds; // List of author IDs
    private ArrayList<ObjectId> genreIds; // List of genre IDs

    /**
     * Constructor for creating a new Book instance.
     *
     * @param title Title of the book.
     * @param publicationDate Publication date of the book.
     * @param ISBN ISBN number of the book.
     */
    public Book(String title, LocalDate publicationDate, Long ISBN) {
        this.bookId = new ObjectId(); // Generate a new ObjectId
        this.title = title;
        this.publicationDate = Date.from(publicationDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.ISBN = ISBN;
        this.authorIds = new ArrayList<>();
        this.genreIds = new ArrayList<>();
    }

    /**
     * Gets the unique identifier of the book.
     *
     * @return The book ID.
     */
    public String getBookId() {
        return bookId.toHexString();
    }

    /**
     * Sets the unique identifier of the book.
     *
     * @param bookId The book ID to set.
     */
    public void setBookId(ObjectId bookId) {
        this.bookId = bookId;
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
    public LocalDate getPublicationDate() {
        return publicationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Sets the publication date of the book.
     *
     * @param publicationDate The publication date to set.
     */
    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = Date.from(publicationDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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

    /**
     * Gets the list of author IDs associated with the book.
     *
     * @return A list of author IDs.
     */
    public ArrayList<ObjectId> getAuthorIds() {
        return authorIds;
    }

    /**
     * Sets the list of author IDs associated with the book.
     *
     * @param authorIds The list of author IDs to set.
     */
    public void setAuthorIds(ArrayList<ObjectId> authorIds) {
        this.authorIds = authorIds;
    }

    /**
     * Gets the list of genre IDs associated with the book.
     *
     * @return A list of genre IDs.
     */
    public ArrayList<ObjectId> getGenreIds() {
        return genreIds;
    }

    /**
     * Sets the list of genre IDs associated with the book.
     *
     * @param genreIds The list of genre IDs to set.
     */
    public void setGenreIds(ArrayList<ObjectId> genreIds) {
        this.genreIds = genreIds;
    }

    /**
     * Adds an author ID to the book.
     *
     * @param authorId The author ID to add.
     */
    public void addAuthorId(ObjectId authorId) {
        this.authorIds.add(authorId);
    }

    /**
     * Adds a genre ID to the book.
     *
     * @param genreId The genre ID to add.
     */
    public void addGenreId(ObjectId genreId) {
        this.genreIds.add(genreId);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", publicationDate=" + publicationDate +
                ", ISBN=" + ISBN +
                ", authorIds=" + authorIds +
                ", genreIds=" + genreIds +
                '}';
    }
}
