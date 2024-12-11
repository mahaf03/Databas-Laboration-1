package bemsih.databaslaboration1.Model;

import java.util.ArrayList;

/**
 * Represents a genre that groups books sharing similar characteristics or themes.
 */
public class Genre {
    private int genreId; // Unique identifier for the genre
    private String genreName; // Name of the genre
    private ArrayList<Book> books; // List of books associated with the genre

    /**
     * Constructor for creating a new Genre instance.
     *
     * @param genreId Unique identifier for the genre.
     * @param genreName Name of the genre.
     */
    public Genre(int genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.books = new ArrayList<>();
    }

    /**
     * Gets the unique identifier of the genre.
     *
     * @return The genre ID.
     */
    public int getGenreId() {
        return genreId;
    }

    /**
     * Sets the unique identifier of the genre.
     *
     * @param genreId The genre ID to set.
     */
    public void setGenreId(int genreId) {
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
     * Gets the list of books associated with the genre.
     *
     * @return A list of books belonging to the genre.
     */
    public ArrayList<Book> getBooks() {
        return books;
    }

    /**
     * Sets the list of books associated with the genre.
     *
     * @param books The list of books to set.
     */
    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
}
