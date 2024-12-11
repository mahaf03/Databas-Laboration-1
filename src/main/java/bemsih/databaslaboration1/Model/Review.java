package bemsih.databaslaboration1.Model;

import java.time.LocalDateTime;

/**
 * Represents a review given by a user for a specific book.
 * The review includes text, a timestamp, and a link to a rating.
 */
public class Review {
    private int reviewId; // Unique identifier for the review
    private String reviewText; // The content of the review
    private LocalDateTime reviewDate; // The date and time the review was created
    private Book book; // The book being reviewed
    private User user; // The user who wrote the review
    private int ratingId; // The identifier of the associated rating

    /**
     * Constructor for creating a new Review instance.
     *
     * @param reviewId Unique identifier for the review.
     * @param ratingId Identifier of the associated rating.
     * @param user The user who wrote the review.
     * @param book The book being reviewed.
     * @param reviewDate The date and time the review was created.
     * @param reviewText The content of the review.
     */
    public Review(int reviewId, int ratingId, User user, Book book, LocalDateTime reviewDate, String reviewText) {
        this.reviewId = reviewId;
        this.ratingId = ratingId;
        this.user = user;
        this.book = book;
        this.reviewDate = reviewDate;
        this.reviewText = reviewText;
    }

    /**
     * Gets the unique identifier of the review.
     *
     * @return The review ID.
     */
    public int getReviewId() {
        return reviewId;
    }

    /**
     * Sets the unique identifier of the review.
     *
     * @param reviewId The review ID to set.
     */
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    /**
     * Gets the identifier of the associated rating.
     *
     * @return The rating ID.
     */
    public int getRatingId() {
        return ratingId;
    }

    /**
     * Sets the identifier of the associated rating.
     *
     * @param ratingId The rating ID to set.
     */
    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    /**
     * Gets the user who wrote the review.
     *
     * @return The user who wrote the review.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who wrote the review.
     *
     * @param user The user to set.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the book associated with the review.
     *
     * @return The book being reviewed.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Sets the book associated with the review.
     *
     * @param book The book to set.
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * Gets the date and time the review was created.
     *
     * @return The review date and time.
     */
    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    /**
     * Sets the date and time the review was created.
     *
     * @param reviewDate The review date and time to set.
     */
    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    /**
     * Gets the content of the review.
     *
     * @return The review text.
     */
    public String getReviewText() {
        return reviewText;
    }

    /**
     * Sets the content of the review.
     *
     * @param reviewText The review text to set.
     */
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
