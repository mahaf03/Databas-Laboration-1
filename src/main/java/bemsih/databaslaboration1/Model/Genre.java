package bemsih.databaslaboration1.Model;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public class Genre {
    private ObjectId genreId; // Unique identifier for the genre
    private String genreName; // Name of the genre
    private ArrayList<ObjectId> bookIds; // List of book IDs associated with the genre

    /**
     * Constructor for creating a new Genre instance.
     *
     * @param genreName Name of the genre.
     */
    public Genre(String genreName) {
        this.genreId = new ObjectId(); // Generate a new ObjectId
        this.genreName = genreName;
        this.bookIds = new ArrayList<>();
    }

    /**
     * Gets the unique identifier of the genre.
     *
     * @return The genre ID.
     */
    public ObjectId getGenreId() {
        return genreId;
    }

    /**
     * Sets the unique identifier of the genre.
     *
     * @param genreId The genre ID to set.
     */
    public void setGenreId(ObjectId genreId) {
        this.genreId = genreId;
    }

    /**
     * Gets the name of the genre.
     *
     * @return The name of the genre.
     */
    public String getGenreName() {
        return genreName;
    }

    /**
     * Sets the name of the genre.
     *
     * @param genreName The name of the genre to set.
     */
    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    /**
     * Gets the list of book IDs associated with the genre.
     *
     * @return A list of book IDs belonging to the genre.
     */
    public ArrayList<ObjectId> getBookIds() {
        return bookIds;
    }

    /**
     * Sets the list of book IDs associated with the genre.
     *
     * @param bookIds The list of book IDs to set.
     */
    public void setBookIds(ArrayList<ObjectId> bookIds) {
        this.bookIds = bookIds;
    }

    /**
     * Adds a book ID to the genre.
     *
     * @param bookId The book ID to add.
     */
    public void addBookId(ObjectId bookId) {
        this.bookIds.add(bookId);
    }

    @Override
    public String toString() {
        return "Genre{" +
                "genreId=" + genreId +
                ", genreName='" + genreName + '\'' +
                ", bookIds=" + bookIds +
                '}';
    }
}
