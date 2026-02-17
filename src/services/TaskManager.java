package services;
import models.Task;
import models.Project;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TaskManager {

    private Connection conn;

    public TaskManager(Connection conn) {
        this.conn = conn;
    }

    //methods

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
                return new Task(id, title, description, projectId);

            }
        }
    }

//list tasks in that project

    public List<Task> getTasks(int project_id, boolean onlyPending) throws SQLException {

        String sql = "SELECT * FROM tasks WHERE project_id = ?";
        if (onlyPending) sql += " AND completed = false";

        List<Task> tasks = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, project_id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(new Task(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
//                            rs.getBoolean("completed"),
                            rs.getInt("project_id")
                    ));
                }
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


    // REMOVE TASK

    public boolean removeTask(int project_id, String title) throws SQLException {
        String sql = "DELETE FROM tasks WHERE project_id = ? AND title = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, project_id);
            stmt.setString(2, title);
            int rows = stmt.executeUpdate();
            return rows > 0; // true if deleted
        }
    }

}















//* Add a task
//* List all tasks
//* Mark a task as complete
//* Remove a task
//* Filter tasks (optional)