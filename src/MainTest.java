import models.Task;
import services.TaskManager;
import services.UserManager;
import models.User;

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

            // 2️⃣ Service layers
            UserManager userManager = new UserManager(conn);
            TaskManager taskManager = new TaskManager(conn);

            // 3️⃣ Create a unique username
            String uniqueUsername = "user" + System.currentTimeMillis();
            User newUser = userManager.registerUser(uniqueUsername, uniqueUsername + "@exddda.com", "d123");
            System.out.println("Registered user: " + newUser.getUsername() + " (id=" + newUser.getId() + ")");

            // 4️⃣ Create a project for the user (assume projects table exists)
            int projectId = 1; // replace with real project ID or create a method to insert a project
            System.out.println("Using project ID: " + projectId);

            // 5️⃣ Add tasks
            Task t1 = taskManager.addTask(projectId, "Task A", "Do something important");
            Task t2 = taskManager.addTask(projectId, "Task B", "Do something else");
            System.out.println("Added tasks:");
            System.out.println(t1);
            System.out.println(t2);

            // 6️⃣ List all tasks
            List<Task> allTasks = taskManager.getTasks(projectId, false);
            System.out.println("\nAll tasks for project " + projectId + ":");
            for (Task t : allTasks) {
                System.out.println(t);
            }

            // 7️⃣ Mark one complete
            taskManager.markComplete(projectId, "Task A");
            System.out.println("\nAfter marking Task A complete:");
            List<Task> pendingTasks = taskManager.getTasks(projectId, true);
            for (Task t : pendingTasks) {
                System.out.println(t);
            }

            // 8️⃣ Remove Task B
            taskManager.removeTask(projectId, "Task B");
            System.out.println("\nAfter removing Task B:");
            allTasks = taskManager.getTasks(projectId, false);
            for (Task t : allTasks) {
                System.out.println(t);
            }

        } catch (SQLException e) {
            System.err.println("Database error:");
            e.printStackTrace();
        }
    }
}
