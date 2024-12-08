package bemsih.databaslaboration1.Model;

import java.time.LocalDateTime;

public class Review {
    private int reviewId;
    private String reviewText;
    private LocalDateTime reviewDate;
    private Book book; // Koppling till bok
    private User user;
    private int ratingId;

    public Review(int reviewId, int ratingId, User user, Book book, LocalDateTime reviewDate, String reviewText) {
        this.reviewId = reviewId;
        this.ratingId = ratingId;
        this.user = user;
        this.book = book;
        this.reviewDate = reviewDate;
        this.reviewText = reviewText;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
