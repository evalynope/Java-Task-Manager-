import models.Task;
import services.TaskManager;
import services.UserManager;
import models.User;
import models.Project;
import services.ProjectManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {
        // 1️⃣ DB connection
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "evalynbassett"; // replace
        String password = ""; // Postgres.app default

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to database!");


            // Assume you already have a Connection and a user
            UserManager userManager = new UserManager(conn);
            ProjectManager projectManager = new ProjectManager(conn);

            // Register a test user
            User user1 = userManager.registerUser("testuser_pm", "pm@example.com", "password123");

           // Create a project for this user
            Project project = projectManager.createProject(user1.getId(), "Test Project A");

            System.out.println("Created project: " + project.getName() + " (id=" + project.getId() + ")");


            } catch (SQLException e) {
            System.err.println("Error getting database connection: " + e.getMessage());
            e.printStackTrace();

        }
    }
}