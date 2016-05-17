package main.java.models;

/**
 * Created by Антонина on 29.04.16.
 */
public class User {

    String name;
    String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
