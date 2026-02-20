package services;
import database.TaskRepo;
import models.User;
import java.sql.SQLException;
import java.util.List;

public class UserManager {
    private TaskRepo repository;
    public UserManager(TaskRepo repository) {
        this.repository = repository;
    }

    // *************** Service Methods ****************

    // Register user
    public User registerUser(String username, String email, String password) throws SQLException {
        try {
            return repository.registerUser(username, email, password);
        } catch (Exception e) {
            System.out.println("Error registering user.");
            e.printStackTrace();
            return null;
        }
    }

    //find by username for validation

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    // Login user

    public User loginUser(String username, String password) throws SQLException {
        try {
            return repository.loginUser(username, password);
        } catch (SQLException e) {
            System.err.println("Error logging in user: " + e.getMessage());
            return null;
        }
    }

    // Get all users
    public List<User> getUsers() {
        try {
            return repository.getUsers();
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
//            e.printStackTrace();
            return null;
        }
    }

    // Delete user by username
    public boolean deleteUser(String username) {
        try {
            return repository.deleteUser(username);
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}



