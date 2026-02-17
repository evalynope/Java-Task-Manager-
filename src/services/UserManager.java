package services;

import models.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private Connection conn;

    // Constructor:
    public UserManager(Connection conn) {
        this.conn = conn;
    }

    // register
    public User registerUser(String username, String email, String password) throws SQLException {

        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?) RETURNING id";

        try (PreparedStatement stmnt = conn.prepareStatement(sql)) {
            stmnt.setString( 1, username);
            stmnt.setString(2, email);
            stmnt.setString(3, password);

            try (ResultSet rs = stmnt.executeQuery()) {
                int id = 0;
                if (rs.next()) {
                    id = rs.getInt("id");
                }
                return new User(id, username, email, password);
            }
        }
    }


    // Login

      public User loginUser(String username, String password) throws SQLException {
          String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

          try (PreparedStatement stmt = conn.prepareStatement(sql)) {
              stmt.setString(1, username);
              stmt.setString(2, password);

              try (ResultSet rs = stmt.executeQuery()){
                  if (rs.next()) {
                      return new User(rs.getInt("id"),
                              rs.getString("username"),
                              rs.getString("email"),
                              rs.getString("password")
                      );
                  } else { System.out.println("Login failed for " + username);
                      return null;
                  }
              }
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


    // DELETE USER BY USERNAME
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

}


