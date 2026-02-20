
import models.*;
import services.*;
import database.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import database.DatabaseConnection;



public class Main {
    private static Connection conn = DatabaseConnection.getConnection();

    private static TaskRepoOnly taskRepo = new TaskRepoOnly(conn);
    private static TaskRepo mainRepo = new TaskRepo(conn);


    private static UserManager userManager = new UserManager(mainRepo);
    private static ProjectManager projectManager = new ProjectManager(mainRepo);
    private static TaskManager taskManager = new TaskManager(taskRepo);




    public static void main(String[] args) throws SQLException {
        User currentUser = null;
        List<Project> currentUsersProjects = null;
        Project currentProject = null;
        Scanner scanner = new Scanner(System.in);


        while (true) {
            if (currentUser == null) {
                System.out.println("* * * Authorization Menu * * *");
                System.out.println("[R] Register");
                System.out.println("[L] Login");
                System.out.println("[Q] Quit");

                String choice = scanner.nextLine().toLowerCase().trim();
                if (choice.isEmpty()) {
                    System.out.println("Please choose a valid option.");
                } else if (choice.equals("r")) {

                    String username;
                    while (true) {
                        System.out.print("Username (case-sensitive): ");
                        username = scanner.nextLine().trim();

                        if (username.isBlank()) {
                            System.out.println("Username cannot be blank.");
                            continue;
                        }

                        if (userManager.findByUsername(username) != null) {
                            System.out.println("Username already taken.");
                            continue;
                        }

                        break; // valid username
                    }

                    String email;
                    while (true) {
                        System.out.print("Email: ");
                        email = scanner.nextLine().trim();

                        if (email.isBlank()) {
                            System.out.println("Email cannot be blank.");
                            continue;
                        }
                        break;
                    }

                    String password;
                    while (true) {
                        System.out.print("Password: ");
                        password = scanner.nextLine().trim();

                        if (password.isBlank()) {
                            System.out.println("Password cannot be blank.");
                            continue;
                        }
                        break; // valid password
                    }
                    userManager.registerUser(username, email, password);
                    System.out.println("Registration successful!");

                } else if (choice.equals("l")) {
                    System.out.println("Username (case-sensitive): ");
                    String username = scanner.nextLine().trim();

                    if (username.isEmpty()) {
                        System.out.println("Username cannot be empty.");
                        continue; // go back to login menu
                    }

                    System.out.print("Password: ");
                    String password = scanner.nextLine().trim();

                    if (password.isEmpty()) {
                        System.out.println("Password cannot be empty.");
                        continue;
                    }
                    currentUser = userManager.loginUser(username, password);

                    if (currentUser != null) {
                        int userId = currentUser.getId();
                        currentUsersProjects = projectManager.getProjects(userId);
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }

                } else if (choice.equals("q")) {
                    break;
                } else {
                    System.out.println("Please choose a valid option.");
                }
        } else if (currentProject == null) {
                while (true) {
                    System.out.println("* * * Project Menu * * *");
                    System.out.println("[C] Create Project");
                    System.out.println("[S] Select Project");
                    System.out.println("[L] List Projects");
                    System.out.println("[O] Logout");

                    String choice = scanner.nextLine().toLowerCase().trim();


                    if (choice.equals("c")) {
                        System.out.print("Project name: ");
                        String name = scanner.nextLine();

//**************138
                        if (!projectManager.isProjectNameAvailable(currentUser.getId(), name)) {
                            System.out.println("Project name already exists! Please choose another one.");




                        } else {
//
                            Project newProject = projectManager.createProject(currentUser.getId(), name);

                            if (newProject != null) {
                                currentUsersProjects.add(newProject);
                                System.out.println("Project created.");
                            } else {
                                System.out.println("Project creation failed.");
                            }
                        }

                    } else if (choice.equals("s")) {
                        if (currentUsersProjects.isEmpty()) {
                            System.out.println("No projects to select yet. Create one first!");
                        } else {
                            System.out.println("* * * My Projects * * *");
                            for (Project p : currentUsersProjects) {
//                                System.out.println("- " + p.getName());
                                System.out.println("Project #"  + p.getId()  + ": " + p.getName());
                            }

                            System.out.println("To select a project, type the title below: ");
                            String name = scanner.nextLine().trim().toLowerCase();
                            for (Project p : currentUsersProjects) {
                                if (p.getName().equalsIgnoreCase(name)) {
                                    currentProject = p;
                                    break;
                                }
                            }


                            if (currentProject != null) {
                                System.out.println("Project selected!");

                                //task menu

                                while (true) {
                                    System.out.println("* * * Task Menu * * *");
                                    System.out.println("[A]dd a task");
                                    System.out.println("[L]ist all tasks");
                                    System.out.println("[M]ark a task as complete");
                                    System.out.println("[R]emove a task");
                                    System.out.println("[B]ack to Projects");

                                    String input = scanner.nextLine().toLowerCase().trim();

                                    if (input.isEmpty()) {
                                        System.out.println("Please enter a valid option.");
                                        continue;
                                    } else if (input.equals("b")) {
                                        break;
                                    } else if (input.equals("a")) {

                                        while (true) {

                                            System.out.println("Enter your task title below (or type 'back' to return):");
                                            String title = scanner.nextLine().trim();

                                            if (title.equalsIgnoreCase("back")) {
                                                break;
                                            }

                                            if (title.isEmpty()) {
                                                System.out.println("Task title cannot be empty.");
                                                continue;
                                            }

                                            if (!taskManager.isTitleAvailable(currentProject.getId(), title)) {
                                                System.out.println("Task name already exists! Please choose another one.");
                                                continue;
                                            }

                                            while (true) {
                                                System.out.println("Enter task description (optional, or type 'back' to cancel):");
                                                String description = scanner.nextLine().trim();

                                                if (description.equalsIgnoreCase("back")) {
                                                    break; // cancel task creation
                                                }

                                                // Optional: if empty, just store as empty string
                                                taskManager.addTask(currentProject.getId(), title, description);
                                                System.out.println(title + " added to Task Manager!");
                                                break; // exit loop after adding
                                            }

                                            break;
                                        }

                                    } else if (input.equals("l")) {
                                        List<Task> tasks = taskManager.getTasks(currentProject.getId(), false);
                                        if (tasks.isEmpty()) {
                                            System.out.println(("No tasks yet!"));
                                        } else {
                                            System.out.println("Tasks: ");
                                            int i = 1;
                                            for (Task task : tasks) {
                                                System.out.println(i + "." + task.getTitle() + " - " + task.getDescription());
                                                i++;
                                            }
                                        }
                                    } else if (input.equals("m")) {

                                        while (true) {
                                            System.out.println("Enter your task title below (or type 'back' to return):");
                                            String input2 = scanner.nextLine().trim();

                                            if (input2.equalsIgnoreCase("back")) {
                                                break; // return to task menu
                                            }

                                            if (input2.isEmpty()) {
                                                System.out.println("Task title cannot be empty.");
                                                continue;
                                            }

                                            Task task = taskManager.getTaskByTitle(currentProject.getId(), input2);

                                            if (task == null) {
                                                System.out.println("Task not found.");
                                                continue;
                                            }

                                            taskManager.markComplete(currentProject.getId(), input2);
                                            System.out.println("Task marked as completed!");
                                            break; // exit loop after success
                                        }
                                    } else if (input.equals("r")) {
                                            while (true) {
                                                System.out.println("Enter your task title below (or type 'back' to cancel):");
                                                String input3 = scanner.nextLine().trim();

                                                if (input3.equalsIgnoreCase("back")) {
                                                    break;
                                                }

                                                if (input3.isEmpty()) {
                                                    System.out.println("Task title cannot be empty.");
                                                    continue;
                                                }

                                                Task task = taskManager.getTaskByTitle(currentProject.getId(), input3);

                                                if (task == null) {
                                                    System.out.println("Task not found.");
                                                    continue;
                                                }

                                                // Confirmation loop
                                                while (true) {
                                                    System.out.println("Are you sure you want to delete this task? Select [Y] or [N].");
                                                    String input4 = scanner.nextLine().trim();

                                                    if (input4.equalsIgnoreCase("y")) {
                                                        taskManager.removeTask(currentProject.getId(), input3);
                                                        System.out.println("Task removed.");
                                                        break;
                                                    }
                                                    else if (input4.equalsIgnoreCase("n")) {
                                                        System.out.println("Task not removed.");
                                                        break;
                                                    }
                                                    else {
                                                        System.out.println("Invalid option.");
                                                    }
                                                }

                                                break; // main loop exit
                                            }

                                    } else {
                                        System.out.println("Please choose a valid option.");
                                    }
                                }

                            } else {
                                System.out.println("Project not found.");
                            }

                        }

                    } else if (choice.equals("l")) {
                        if (currentUsersProjects.isEmpty()) {
                            System.out.println("No projects yet.");
                        } else {
                            System.out.println("* * * My Projects * * *");
                            for (Project p : currentUsersProjects) {
                                System.out.println(p.getName());
                            }
                        }

                    } else if (choice.equals("o")) {
                        currentUser = null;
                        System.out.println("Logged out. Returning to login menu...");
                        break;

                    } else if (choice.equals("q")) {
                        break;
                    } else {
                        System.out.println("Please choose a valid option.");
                    }

                }
            }
        }
    }
}



