package bemsih.databaslaboration1.Model;

import java.util.ArrayList;

public class Genre {
    private int genreId;
    private String genreName;
    private ArrayList<Book> books;


    public Genre(int genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.books = new ArrayList<>();
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
}
