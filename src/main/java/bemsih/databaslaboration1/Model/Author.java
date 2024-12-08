package bemsih.databaslaboration1.Model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Author {
    private int author_id;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private ArrayList<Book> books;

    public Author(int author_id, String firstName, String lastName, LocalDate dateOfBirth) {
        this.author_id = author_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.books = new ArrayList<>();
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
}
