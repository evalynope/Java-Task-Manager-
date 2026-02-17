package services;

import models.Project;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectManager {
    private Connection conn;

    public ProjectManager(Connection conn) {
        this.conn = conn;
    }

    // 1. Create a project for a user
    public Project createProject(int userId, String name) {
        String sql = "INSERT INTO projects (name, owner_id) VALUES (?, ?) RETURNING id";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                int id = rs.next() ? rs.getInt("id") : 0;
                return new Project(id, name, userId);
            }
        } catch (SQLException e){
            System.err.println("Error creating project" + e.getMessage());
            e.printStackTrace();
            return null;

        }
    }

    // 2. Get all projects for a user
    public List<Project> getProjects(int userId) {
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
            System.err.println("Error fetching projects" +e.getMessage());
            e.printStackTrace();
        }
        return projects;
    }

    // 3. Select a project by name
    public Project getProjectByName(int userId, String name) {
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
        } catch (SQLException e) {
            System.err.println("Error fetching projects" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
