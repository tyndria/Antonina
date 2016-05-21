package main.java.models;

/**
 * Created by Антонина on 29.04.16.
 */
public class User {

    String username;
    String password;
    String id;

    public User() {
        this.username = "";
        this.password = "";
        this.id = "";
    }

    public User(String username, String password, String id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }
}

