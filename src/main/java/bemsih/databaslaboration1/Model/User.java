package bemsih.databaslaboration1.Model;

import org.bson.types.ObjectId;

public class User {
    private ObjectId userId; // MongoDB ObjectId
    private String userName; // Username of the user
    private final String password; // Password for the user (immutable)

    /**
     * Constructor for creating a new User instance.
     *
     * @param userName The username of the user.
     * @param password The user's password.
     */
    public User(String userName, String password) {
        this.userId = new ObjectId(); // Generate a new ObjectId
        this.userName = userName;
        this.password = password;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return The user ID.
     */
    public ObjectId getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param userId The user ID to set.
     */
    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    /**
     * Gets the password of the user.
     * Note: The password is immutable and cannot be modified.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the username of the user.
     *
     * @return The user's username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username of the user.
     *
     * @param userName The username to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
