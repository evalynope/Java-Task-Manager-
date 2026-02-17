package services;

import models.User;
import java.util.ArrayList;

public class UserManager {

    private ArrayList<User> users = new ArrayList<>(); //array list of users
    private int nextId = 1; //gives each user an ID automatically

    // register
    public void registerUser(String username, String email, String pw) {
        User newUser = new User(nextId++, username, email, pw);
        users.add(newUser);
        System.out.println("User registered: " + username); //this registers a  user. print line and inputs need to go in the main.
    }

    // Login
    public User loginUser(String username, String pw) {
        for (User u : users) { //for all users:
            if (u.getUsername().equals(username) && u.checkPassword(pw)) {
                System.out.println("Login successful: " + username);
                return u; //login if everything matches - case-sensitive no hash
            }
        }
        System.out.println("Login failed for: " + username);
        return null; //move to main
    }

    // Get all users
    public ArrayList<User> getUsers() {
        return users;
    }

    // Delete a user by username
    public boolean deleteUser(String username) { //takes in username and deletes
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) { //probably neededs lower case input validation
                users.remove(i);
                System.out.println("User deleted: " + username);
                return true;
            }
        }
        System.out.println("User not found: " + username);
        return false;
    }
}
