package bemsih.databaslaboration1.Model;

import org.bson.types.ObjectId;

public class Rating {
    private ObjectId ratingId; // Unique identifier for the rating
    private ObjectId bookId; // Reference to the book being rated
    private ObjectId userId; // Reference to the user who gave the rating
    private int ratingValue; // The value of the rating (e.g., 1-5)

    /**
     * Constructor for creating a new Rating instance.
     *
     * @param ratingValue Value of the rating (e.g., 1-5).
     * @param userId The user who gave the rating.
     * @param bookId The book being rated.
     */
    public Rating(int ratingValue, ObjectId userId, ObjectId bookId) {
        if (ratingValue < 1 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating value must be between 1 and 5.");
        }
        this.ratingId = new ObjectId(); // Generate a new ObjectId
        this.ratingValue = ratingValue;
        this.userId = userId;
        this.bookId = bookId;
    }

    // Getter and Setter for ratingId
    public ObjectId getRatingId() {
        return ratingId;
    }

    public void setRatingId(ObjectId ratingId) {
        this.ratingId = ratingId;
    }

    // Getter and Setter for ratingValue
    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        if (ratingValue < 1 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating value must be between 1 and 5.");
        }
        this.ratingValue = ratingValue;
    }

    // Getter and Setter for bookId
    public ObjectId getBookId() {
        return bookId;
    }

    public void setBookId(ObjectId bookId) {
        this.bookId = bookId;
    }

    // Getter and Setter for userId
    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ratingId=" + ratingId +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", ratingValue=" + ratingValue +
                '}';
    }
}
