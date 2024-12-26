package bemsih.databaslaboration1.Model;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Review {
    private ObjectId reviewId; // MongoDB ObjectId
    private String reviewText; // The content of the review
    private Date reviewDate; // The date and time the review was created
    private ObjectId bookId; // Reference to the book being reviewed
    private ObjectId userId; // Reference to the user who wrote the review
    private ObjectId ratingId; // Reference to the associated rating

    // Constructor for creating a new Review
    public Review(String reviewText, LocalDateTime reviewDate, ObjectId bookId, ObjectId userId, ObjectId ratingId) {
        this.reviewId = new ObjectId(); // Generate a new ObjectId
        this.reviewText = reviewText;
        this.reviewDate = Date.from(reviewDate.atZone(ZoneId.systemDefault()).toInstant());
        this.bookId = bookId;
        this.userId = userId;
        this.ratingId = ratingId;
    }

    // Getter and Setter for reviewId
    public ObjectId getReviewId() {
        return reviewId;
    }

    public void setReviewId(ObjectId reviewId) {
        this.reviewId = reviewId;
    }

    // Getter and Setter for reviewText
    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    // Getter and Setter for reviewDate
    public LocalDateTime getReviewDate() {
        return reviewDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = Date.from(reviewDate.atZone(ZoneId.systemDefault()).toInstant());
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

    // Getter and Setter for ratingId
    public ObjectId getRatingId() {
        return ratingId;
    }

    public void setRatingId(ObjectId ratingId) {
        this.ratingId = ratingId;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", reviewText='" + reviewText + '\'' +
                ", reviewDate=" + reviewDate +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", ratingId=" + ratingId +
                '}';
    }
}
