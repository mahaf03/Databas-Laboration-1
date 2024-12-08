package bemsih.databaslaboration1.Model;

public class Rating {
    private int ratingId;
    private Book book; // Koppling till bok
    private User user;
    private int ratingValue;

    public Rating(int ratingId, int ratingValue, User user, Book book) {
        this.ratingId = ratingId;
        this.ratingValue = ratingValue;
        this.user = user;
        this.book = book;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
