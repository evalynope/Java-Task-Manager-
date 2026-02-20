package database;
import models.Project;
import models.Task;
import models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//TASK REPO CONTAINS ALL PROJECT AND USER METHODS

import static database.DatabaseConnection.conn;

public class TaskRepo {

    private Connection conn;

    public TaskRepo(Connection conn) {
        this.conn = conn;
    }

    // ************* USER METHODS *************

    //find by username

    public User findByUsername(String username) {

        String sql = "SELECT * FROM users WHERE username =?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String user = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                return new User(id, email, user, password);
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Username is already taken - please choose another one.");
            } else {
                System.out.println("Error registering user: " + e.getMessage());
            }
        }
        return null;
    }

    // registerUser

    public User registerUser(String username, String email, String password) {

        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?) RETURNING id";

        try (PreparedStatement stmnt = conn.prepareStatement(sql)) {
            stmnt.setString(1, username);
            stmnt.setString(2, email);
            stmnt.setString(3, password);

            try (ResultSet rs = stmnt.executeQuery()) {
                int id = 0;
                if (rs.next()) {
                    id = rs.getInt("id");
                }
                return new User(id, username, email, password);
            }
        } catch (SQLException e) {
//            System.out.println("Username is already taken - please choose another one.");
        }
        return null;
    }
    // loginUser

    public User loginUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                } else {
                    System.out.println("Login failed for " + username);
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    // Get all users
    public List<User> getUsers() throws SQLException {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        }

        return users;
    }

    // deleteUser by username
    public boolean deleteUser(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User deleted: " + username);
                return true;
            } else {
                System.out.println("User not found: " + username);
                return false;
            }
        }
    }


// ************ PROJECT METHODS *************


    //  Create a project for a user
    public Project createProject(int userId, String name) throws SQLException {
        String sql = "INSERT INTO projects (name, owner_id) VALUES (?, ?) RETURNING id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                int id = rs.next() ? rs.getInt("id") : 0;
                return new Project(id, name, userId);
            }
        } catch (SQLException e) {
            System.out.println("Error creating project");
            return null;

        }
    }


    // Get all projects for a user
    public List<Project> getProjects(int userId) throws SQLException {
        String sql = "SELECT * FROM projects WHERE owner_id = ?";
        List<Project> projects = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    projects.add(new Project(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("owner_id")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching projects" + e.getMessage());
            e.printStackTrace();
        }
        return projects;
    }

    //  Select a project by name
    public Project getProjectByName(int userId, String name) throws SQLException {
        String sql = "SELECT * FROM projects WHERE owner_id = ? AND name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Project(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("owner_id")
                    );
                }
            }
            catch (Exception ex) {
                System.out.println("Error here");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching project: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }






}




