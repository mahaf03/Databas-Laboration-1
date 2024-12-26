package bemsih.databaslaboration1.Controller;

import bemsih.databaslaboration1.Model.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Controller class for interacting with the Books database.
 * Provides methods to fetch, add, and delete books, authors, genres, reviews, and users.
 * This implementation uses a thread pool for asynchronous execution of tasks.
 */
public class BooksDbController implements BooksDbInterface {

    private final ExecutorService executorService;
    /**
     * Constructor that initializes the executor service with a cached thread pool.
     */
    public BooksDbController() {
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void initializeDefaultUser() throws BooksDbException {
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("Laboration1");
            var usersCollection = database.getCollection("users");

            // Kontrollera om standardanvändaren redan finns
            var existingUser = usersCollection.find(com.mongodb.client.model.Filters.eq("userName", "admin")).first();

            if (existingUser == null) {
                // Skapa standardanvändare
                var userDoc = new org.bson.Document()
                        .append("userName", "bmt127")
                        .append("password", "123"); // Använd en starkare standardlösning i en riktig applikation

                usersCollection.insertOne(userDoc);
                System.out.println("Default user 'admin' created with password 'admin123'.");
            } else {
                System.out.println("Default user 'admin' already exists.");
            }
        } catch (Exception e) {
            throw new BooksDbException("Error initializing default user.", e);
        }
    }

    public String getUserNameById(ObjectId userId) throws BooksDbException {
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("Laboration1");
            var usersCollection = database.getCollection("users");

            var userDoc = usersCollection.find(Filters.eq("_id", userId)).first();
            if (userDoc != null) {
                return userDoc.getString("userName");
            } else {
                throw new BooksDbException("Användare kunde inte hittas med ID: " + userId);
            }
        } catch (Exception e) {
            throw new BooksDbException("Fel vid hämtning av användarnamn", e);
        }
    }


    /**
     * Retrieves a list of books that match the given title.
     *
     * @param title The title to search for.
     * @return A list of books matching the title.
     * @throws BooksDbException If an error occurs during database interaction.
     */

    @Override
    public List<Book> getBooksByTitle(String title) throws BooksDbException {
        Callable<List<Book>> task = () -> {
            List<Book> books = new ArrayList<>();
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                var database = mongoClient.getDatabase("Laboration1");
                var collection = database.getCollection("books");

                // Använd en regex-sökning för att matcha titeln
                var query = com.mongodb.client.model.Filters.regex("title", ".*" + title + ".*", "i");
                var results = collection.find(query);

                for (var doc : results) {
                    books.add(new Book(
                            doc.getString("title"),
                            doc.getDate("publishingDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                            doc.getLong("ISBN")
                    ));
                }
            } catch (Exception e) {
                throw new BooksDbException("Error fetching books by title: " + title, e);
            }
            return books;
        };

        try {
            Future<List<Book>> future = executorService.submit(task);
            return future.get(); // Vänta tills resultatet är tillgängligt
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for fetching books by title.", e);
        }
    }


    /**
     * Retrieves a list of books authored by a person with the given name.
     *
     * @param authorName The author's name to search for.
     * @return A list of books by the given author.
     * @throws BooksDbException If an error occurs during database interaction.
     */

    @Override
    public List<Book> getBooksByAuthor(String authorName) throws BooksDbException {
        Callable<List<Book>> task = () -> {
            List<Book> books = new ArrayList<>();
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                var database = mongoClient.getDatabase("Laboration1");
                var booksCollection = database.getCollection("books");
                var authorsCollection = database.getCollection("authors");

                // Hitta författare vars namn matchar
                var authorQuery = com.mongodb.client.model.Filters.regex(
                        "fullName", ".*" + authorName + ".*", "i"
                );
                var authors = authorsCollection.find(authorQuery);

                // Extrahera deras ObjectId
                List<ObjectId> authorIds = new ArrayList<>();
                for (var doc : authors) {
                    authorIds.add(doc.getObjectId("_id"));
                }

                // Hitta böcker kopplade till dessa författare
                var bookQuery = com.mongodb.client.model.Filters.in("authorIds", authorIds);
                var results = booksCollection.find(bookQuery);

                for (var doc : results) {
                    books.add(new Book(
                            doc.getString("title"),
                            doc.getDate("publishingDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                            doc.getLong("ISBN")
                    ));
                }
            } catch (Exception e) {
                throw new BooksDbException("Error fetching books by author: " + authorName, e);
            }
            return books;
        };

        try {
            Future<List<Book>> future = executorService.submit(task);
            return future.get(); // Vänta tills resultatet är tillgängligt
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for fetching books by author.", e);
        }
    }



    /**
     * Retrieves a list of books belonging to a specific genre.
     *
     * @param genreName The genre to search for.
     * @return A list of books in the given genre.
     * @throws BooksDbException If an error occurs during database interaction.
     */

    @Override
    public List<Book> getBooksByGenre(String genreName) throws BooksDbException {
        Callable<List<Book>> task = () -> {
            List<Book> books = new ArrayList<>();
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                var database = mongoClient.getDatabase("Laboration1");
                var booksCollection = database.getCollection("books");

                // Filtrera böcker med det givna genrenamnet
                var query = com.mongodb.client.model.Filters.regex("genres", ".*" + genreName + ".*", "i");
                var results = booksCollection.find(query);

                for (var doc : results) {
                    books.add(new Book(
                            doc.getString("title"),
                            doc.getDate("publishingDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                            doc.getLong("ISBN")
                    ));
                }
            } catch (Exception e) {
                throw new BooksDbException("Error fetching books by genre: " + genreName, e);
            }
            return books;
        };

        try {
            Future<List<Book>> future = executorService.submit(task);
            return future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for fetching books by genre.", e);
        }
    }


    /**
     * Adds a rating for a specific book by a user.
     *
     * @param book The book to rate.
     * @param user The user adding the rating.
     * @param ratingValue The rating value to assign.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public void addRating(Book book, User user, int ratingValue) throws BooksDbException {
        Callable<Void> task = () -> {
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                var database = mongoClient.getDatabase("Laboration1");
                var ratingsCollection = database.getCollection("ratings");

                // Skapa en rating-dokument
                var ratingDoc = new org.bson.Document()
                        .append("bookId", book.getBookId()) // Använd rätt metod eller fält från Book-klassen
                        .append("userId", user.getUserId())
                        .append("ratingValue", ratingValue);

                ratingsCollection.insertOne(ratingDoc);
            } catch (Exception e) {
                throw new BooksDbException("Error adding rating for book ID: " + book.getBookId(), e);
            }
            return null;
        };

        try {
            Future<Void> future = executorService.submit(task);
            future.get(); // Vänta tills operationen slutförs
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for adding rating.", e);
        }
    }


    /**
     * Adds a new book along with its genres.
     *
     * @param book The book to add.
     * @param genres A list of genres associated with the book.
     * @param user The user adding the book.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public void addBook(Book book, List<Genre> genres, User user) throws BooksDbException {
        Callable<Void> task = () -> {
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                var database = mongoClient.getDatabase("Laboration1");
                var booksCollection = database.getCollection("books");
                var genresCollection = database.getCollection("genres");

                // Skapa genrer i databasen om de inte redan finns
                List<String> genreIds = new ArrayList<>();
                for (Genre genre : genres) {
                    var existingGenre = genresCollection.find(com.mongodb.client.model.Filters.eq("name", genre.getGenreName())).first();
                    if (existingGenre == null) {
                        var genreDoc = new org.bson.Document().append("name", genre.getGenreName());
                        genresCollection.insertOne(genreDoc);
                        genreIds.add(genreDoc.getObjectId("_id").toHexString());
                    } else {
                        genreIds.add(existingGenre.getObjectId("_id").toHexString());
                    }
                }

                // Lägg till boken i databasen
                var bookDoc = new org.bson.Document()
                        .append("title", book.getTitle())
                        .append("publishingDate", java.util.Date.from(book.getPublicationDate().atStartOfDay(ZoneId.systemDefault()).toInstant())) // Ändrat till getPublicationDate
                        .append("ISBN", book.getISBN())
                        .append("userId", user.getUserId())
                        .append("genres", genreIds);

                booksCollection.insertOne(bookDoc);
            } catch (Exception e) {
                throw new BooksDbException("Error adding book and genres.", e);
            }
            return null;
        };

        try {
            Future<Void> future = executorService.submit(task);
            future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for adding book.", e);
        }
    }


    /**
     * Adds a book with its authors and genres.
     *
     * @param book The book to add.
     * @param authors A list of authors associated with the book.
     * @param genres A list of genres associated with the book.
     * @param user The user adding the book.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public void addBookWithAuthors(Book book, List<Author> authors, List<Genre> genres, User user) throws BooksDbException {
        Callable<Void> task = () -> {
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                var database = mongoClient.getDatabase("Laboration1");
                var booksCollection = database.getCollection("books");
                var authorsCollection = database.getCollection("authors");

                // Lägg till boken
                var bookDoc = new org.bson.Document()
                        .append("title", book.getTitle())
                        .append("publishingDate", java.util.Date.from(book.getPublicationDate().atStartOfDay(ZoneId.systemDefault()).toInstant())) // Ändrat till getPublicationDate
                        .append("ISBN", book.getISBN())
                        .append("userId", user.getUserId())
                        .append("genres", genres.stream().map(Genre::getGenreName).toList());

                booksCollection.insertOne(bookDoc);
                var bookId = bookDoc.getObjectId("_id");

                // Lägg till författare och koppla till boken
                for (Author author : authors) {
                    var authorDoc = new org.bson.Document()
                            .append("firstName", author.getFirstName())
                            .append("lastName", author.getLastName())
                            .append("dateOfBirth", java.util.Date.from(author.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                            .append("bookIds", List.of(bookId));

                    authorsCollection.insertOne(authorDoc);
                }
            } catch (Exception e) {
                throw new BooksDbException("Error adding book with authors.", e);
            }
            return null;
        };

        try {
            Future<Void> future = executorService.submit(task);
            future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for adding book with authors.", e);
        }
    }


    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to fetch.
     * @return The user object if found, or null if not.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public User getUserById(int userId) throws BooksDbException {
        Callable<User> task = () -> {
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                var database = mongoClient.getDatabase("Laboration1");
                var usersCollection = database.getCollection("users");

                var query = com.mongodb.client.model.Filters.eq("userId", userId);
                var userDoc = usersCollection.find(query).first();

                if (userDoc != null) {
                    return new User(
                            userDoc.getString("userName"),
                            userDoc.getString("password") // Endast två parametrar används här
                    );
                }
            } catch (Exception e) {
                throw new BooksDbException("Error fetching user by ID: " + userId, e);
            }
            return null;
        };

        try {
            Future<User> future = executorService.submit(task);
            return future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for fetching user by ID.", e);
        }
    }


    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to fetch.
     * @return The user object if found, or null if not.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public User getUserByUsername(String username) throws BooksDbException {
        Callable<User> task = () -> {
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                var database = mongoClient.getDatabase("Laboration1");
                var usersCollection = database.getCollection("users");

                var query = com.mongodb.client.model.Filters.eq("userName", username);
                var userDoc = usersCollection.find(query).first();

                if (userDoc != null) {
                    return new User(
                            userDoc.getString("userName"),
                            userDoc.getString("password") // Endast två parametrar
                    );
                }
            } catch (Exception e) {
                throw new BooksDbException("Error fetching user by username: " + username, e);
            }
            return null;
        };

        try {
            Future<User> future = executorService.submit(task);
            return future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for fetching user by username.", e);
        }
    }

    /**
     * Validates a user's credentials.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @return True if the credentials are valid, false otherwise.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public boolean validateUser(String username, String password) throws BooksDbException {
        Callable<Boolean> task = () -> {
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                var database = mongoClient.getDatabase("Laboration1");
                var usersCollection = database.getCollection("users");

                var query = com.mongodb.client.model.Filters.and(
                        com.mongodb.client.model.Filters.eq("userName", username),
                        com.mongodb.client.model.Filters.eq("password", password)
                );

                return usersCollection.find(query).first() != null;
            } catch (Exception e) {
                throw new BooksDbException("Error validating user credentials.", e);
            }
        };

        try {
            Future<Boolean> future = executorService.submit(task);
            return future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for validating user credentials.", e);
        }
    }

    /**
     * Adds a review for a book by a user, along with a rating.
     *
     * @param book The book being reviewed.
     * @param user The user writing the review.
     * @param reviewText The text of the review.
     * @param ratingValue The rating value associated with the review.
     * @throws BooksDbException If an error occurs during database interaction.
     */
    @Override
    public void addReview(Book book, User user, String reviewText, int ratingValue) throws BooksDbException {
        Callable<Void> task = () -> {
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                var database = mongoClient.getDatabase("Laboration1");
                var reviewsCollection = database.getCollection("reviews");
                var ratingsCollection = database.getCollection("ratings");

                // Lägg till betyg
                var ratingDoc = new org.bson.Document()
                        .append("bookId", book.getBookId()) // Använd rätt metod för att hämta bokens ID
                        .append("userId", user.getUserId())
                        .append("ratingValue", ratingValue);
                ratingsCollection.insertOne(ratingDoc);
                var ratingId = ratingDoc.getObjectId("_id");

                // Lägg till recension
                var reviewDoc = new org.bson.Document()
                        .append("reviewText", reviewText)
                        .append("bookId", book.getBookId()) // Använd rätt metod för att hämta bokens ID
                        .append("userId", user.getUserId())
                        .append("ratingId", ratingId);
                reviewsCollection.insertOne(reviewDoc);
            } catch (Exception e) {
                throw new BooksDbException("Error adding review and rating.", e);
            }
            return null;
        };

        try {
            Future<Void> future = executorService.submit(task);
            future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for adding review.", e);
        }
    }


    /**
     * Deletes a book and all associated data (reviews, ratings, authors, genres) by its ID.
     *
     * @param bookId The ID of the book to delete.
     * @throws BooksDbException If an error occurs during database interaction.
     */

    @Override
    public void deleteBookById(String bookId) throws BooksDbException {
        Callable<Void> task = () -> {
            try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                var database = mongoClient.getDatabase("Laboration1");
                var booksCollection = database.getCollection("books");
                var reviewsCollection = database.getCollection("reviews");
                var ratingsCollection = database.getCollection("ratings");
                var authorsCollection = database.getCollection("authors");
                var genresCollection = database.getCollection("genres");

                // Konvertera bookId till ObjectId
                var objectId = new org.bson.types.ObjectId(bookId);

                // Radera recensioner kopplade till boken
                var reviewQuery = com.mongodb.client.model.Filters.eq("bookId", objectId);
                reviewsCollection.deleteMany(reviewQuery);

                // Radera betyg kopplade till boken
                var ratingQuery = com.mongodb.client.model.Filters.eq("bookId", objectId);
                ratingsCollection.deleteMany(ratingQuery);

                // Ta bort författarkopplingar för boken
                var authorQuery = com.mongodb.client.model.Filters.elemMatch("bookIds", com.mongodb.client.model.Filters.eq(objectId));
                var updateAuthor = new org.bson.Document("$pull", new org.bson.Document("bookIds", objectId));
                authorsCollection.updateMany(authorQuery, updateAuthor);

                // Radera genrekopplingar för boken
                var genreQuery = com.mongodb.client.model.Filters.elemMatch("bookIds", com.mongodb.client.model.Filters.eq(objectId));
                var updateGenre = new org.bson.Document("$pull", new org.bson.Document("bookIds", objectId));
                genresCollection.updateMany(genreQuery, updateGenre);

                // Radera själva boken
                var bookQuery = com.mongodb.client.model.Filters.eq("_id", objectId);
                booksCollection.deleteOne(bookQuery);
            } catch (Exception e) {
                throw new BooksDbException("Error deleting book with ID: " + bookId, e);
            }
            return null;
        };

        try {
            Future<Void> future = executorService.submit(task);
            future.get();
        } catch (Exception e) {
            throw new BooksDbException("Error executing background task for deleting book.", e);
        }
    }

    @Override
    public List<Review> getReviewsByBookId(String bookId) throws BooksDbException {
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            var database = mongoClient.getDatabase("Laboration1");
            var reviewsCollection = database.getCollection("reviews");

            // Konvertera bookId från String till ObjectId
            var objectId = new org.bson.types.ObjectId(String.valueOf(bookId));

            var query = com.mongodb.client.model.Filters.eq("bookId", objectId);
            var reviewDocs = reviewsCollection.find(query);

            List<Review> reviews = new ArrayList<>();
            for (var reviewDoc : reviewDocs) {
                reviews.add(new Review(
                        reviewDoc.getString("reviewText"),
                        reviewDoc.getDate("reviewDate").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        reviewDoc.getObjectId("bookId"),
                        reviewDoc.getObjectId("userId"),
                        reviewDoc.getObjectId("ratingId")
                ));
            }
            return reviews;
        } catch (Exception e) {
            throw new BooksDbException("Error fetching reviews for book ID: " + bookId, e);
        }
    }


    /**
     * Retrieves a list of reviews for a book by its ID.
     *
     * @param bookId The ID of the book.
     * @return A list of reviews associated with the book.
     * @throws BooksDbException If an error occurs during database interaction.
     */




    /**
     * Shuts down the executor service used for background tasks.
     */

    public void shutdown() {
        executorService.shutdown();
    }
}