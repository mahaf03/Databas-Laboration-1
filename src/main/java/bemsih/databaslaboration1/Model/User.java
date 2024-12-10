package bemsih.databaslaboration1.Model;

public class User {
    private int userId;
    private String userName;
    private final String password;


    public User(String userName, int userId, String password) {
        this.userName = userName;
        this.userId = userId;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}