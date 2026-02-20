package database;
import models.Project;
import models.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//TASK REPO ONLY CONTAINS ONLY TASK METHODS

public class TaskRepoOnly {

    private Connection conn;
    public TaskRepoOnly(Connection conn) {
        this.conn = conn;
    }

// ***************** TASK METHODS ****************

//add task

    public Task addTask(int projectId, String title, String description) throws SQLException {
        if (title == null || title.isBlank()) return null;

        String sql = "INSERT INTO tasks( title, description, project_id, completed) VALUES (?, ?, ?, false) RETURNING id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setInt(3, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                int id = 0;
                if (rs.next()) id = rs.getInt("id");
                return new Task(id, title, description, rs.getBoolean("completed"), projectId);
            }
        }
        catch (SQLException e) {

            return null;
        }
    }

    //get task by name

    public Task getTaskByTitle(int projectId, String title) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE project_id = ? AND title = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, projectId);
            stmt.setString(2, title);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Task(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getBoolean("completed"),
                            rs.getInt("project_id")
                    );
                }
            }
        }

        return null; // no task found
    }
//    public Task getTaskByTitle(int userId, String title) throws SQLException {
//        String sql = "SELECT * FROM tasks WHERE userId = ? AND title = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setInt(1, userId);
//            stmt.setString(2, title);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return new Task(
//                            rs.getInt("id"),
//                            rs.getString("title"),
//                            rs.getString("description"),
//                            rs.getBoolean("completed"),
//                            rs.getInt("projectId")
//                    );
//                }
//            }
//            catch (Exception ex) {
//                System.out.println("Error here");
//                return null;
//            }
//        } catch (SQLException e) {
//            System.err.println("Error fetching task: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return null;
//    }


    //list tasks in that project

    public List<Task> getTasks(int project_id, boolean completed) throws SQLException {

        String sql = "SELECT * FROM tasks WHERE project_id = ? AND completed = ?";
        List<Task> tasks = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, project_id);
            stmt.setBoolean(2, completed);

            ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tasks.add(new Task(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getBoolean("completed"),
                            rs.getInt("project_id")

                    ));
                }
            }
        return tasks;
    }

    //mark complete/done

    public boolean markComplete(int project_id, String title) throws SQLException {
        String sql = "UPDATE tasks SET completed = true WHERE project_id = ? AND title = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, project_id);
            stmt.setString(2, title);
            int rows = stmt.executeUpdate();
            return rows > 0; // true if at least one row updated
        }
    }




    public boolean removeTask(int project_id, String title) throws SQLException {
        String sql = "DELETE FROM tasks WHERE project_id = ? AND LOWER(title) = LOWER(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, project_id);
            stmt.setString(2, title.trim());
            int rows = stmt.executeUpdate();
//            System.out.println("Project ID: " + project_id);
//            System.out.println("Title: [" + title + "]");
            System.out.println("Deleted rows: " + rows);

            return rows > 0; // true if deleted
        }
    }
}
