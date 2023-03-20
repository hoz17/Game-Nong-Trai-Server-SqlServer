package Model;

public class User {
    private int user_ID;
    private String username;
    private String password;

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(int user_ID, String username, String password) {
        this.user_ID = user_ID;
        this.username = username;
        this.password = password;
    }
}
