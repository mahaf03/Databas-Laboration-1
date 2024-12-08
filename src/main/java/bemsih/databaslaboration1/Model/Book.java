package bemsih.databaslaboration1.Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Book {
    private int book_id;
    private String title;
    private LocalDate date;
    private Long ISBN;
    private ArrayList<Author> authors;
    private ArrayList<Genre> genres;

    public Book (int book_id, String title, LocalDate date, Long ISBN)
    {
        this.book_id = book_id;
        this.title = title;
        this.date = date;
        this.ISBN = ISBN;
        this.authors = new ArrayList<>();
        this.genres = new ArrayList<>();
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getISBN() {
        return ISBN;
    }

    public void setISBN(Long ISBN) {
        this.ISBN = ISBN;
    }
}
