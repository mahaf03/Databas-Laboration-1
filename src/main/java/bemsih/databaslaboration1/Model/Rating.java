package bemsih.databaslaboration1.Model;

/**
 * Represents a rating given by a user for a specific book.
 */
public class Rating {
    private int ratingId; // Unique identifier for the rating
    private Book book; // The book being rated
    private User user; // The user who gave the rating
    private int ratingValue; // The value of the rating (e.g., 1-5)

    /**
     * Constructor for creating a new Rating instance.
     *
     * @param ratingId Unique identifier for the rating.
     * @param ratingValue Value of the rating (e.g., 1-5).
     * @param user The user who gave the rating.
     * @param book The book being rated.
     */
    public Rating(int ratingId, int ratingValue, User user, Book book) {
        this.ratingId = ratingId;
        this.ratingValue = ratingValue;
        this.user = user;
        this.book = book;
    }

    /**
     * Gets the unique identifier of the rating.
     *
     * @return The rating ID.
     */
    public int getRatingId() {
        return ratingId;
    }

    /**
     * Sets the unique identifier of the rating.
     *
     * @param ratingId The rating ID to set.
     */
    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    /**
     * Gets the value of the rating.
     *
     * @return The rating value.
     */
    public int getRatingValue() {
        return ratingValue;
    }

    /**
     * Sets the value of the rating.
     *
     * @param ratingValue The rating value to set (e.g., 1-5).
     */
    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    /**
     * Gets the book associated with the rating.
     *
     * @return The book being rated.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the book associated with the rating.
     *
     * @param book The book to associate with the rating.
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Gets the user who gave the rating.
     *
     * @return The user who provided the rating.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who gave the rating.
     *
     * @param user The user to associate with the rating.
     */
    public void setUser(User user) {
        this.user = user;
    }
}
