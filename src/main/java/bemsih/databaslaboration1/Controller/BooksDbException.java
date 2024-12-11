package bemsih.databaslaboration1.Controller;

/**
 * Represents a custom exception class for errors related to the Books database.
 * This exception is used to handle and propagate specific issues encountered
 * while interacting with the database.
 */
public class BooksDbException extends Exception {

  /**
   * Constructs a new BooksDbException with the specified detail message.
   *
   * @param message the detail message explaining the reason for the exception.
   */
  public BooksDbException(String message) {
    super(message);
  }

  /**
   * Constructs a new BooksDbException with the specified detail message and cause.
   *
   * @param message the detail message explaining the reason for the exception.
   * @param cause the underlying cause of the exception, typically another throwable.
   */
  public BooksDbException(String message, Throwable cause) {
    super(message, cause);
  }
}
