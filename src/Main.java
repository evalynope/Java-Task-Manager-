
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
                    System.out.println("Username: ");
                    String username = scanner.nextLine();
                    do {
                        System.out.println("Username:");
                        username = scanner.nextLine();

                        if (username == null || username.isBlank()) {
                            System.out.println("Username cannot be blank.");
                            continue;
                        }

                        if (userManager.findByUsername(username) != null) {
                            System.out.println("Username is already taken - please choose another one.");
                            username = null; // force loop to continue
                        }

                    } while (username == null || username.isBlank());

                    String email = scanner.nextLine();
                    do {
                        System.out.println("Email:");
                        email = scanner.nextLine();

                        if (email == null || email.isBlank()) {
                            System.out.println("Email cannot be blank.");
                        }

                    } while (email == null || email.isBlank());

                    String password = scanner.nextLine();
                    do {
                        System.out.println("Password:");
                        password = scanner.nextLine();

                        if (password == null || password.isBlank()) {
                            System.out.println("Password cannot be blank.");
                        }

                    } while (password == null || password.isBlank());

                    System.out.println("Registration successful!");
                    userManager.registerUser(username, email, password);

                } else if (choice.equals("l")) {
                    System.out.println("Username: ");
                    String username = scanner.nextLine();

                    System.out.println("Password: ");
                    String password = scanner.nextLine();

                    currentUser = userManager.loginUser(username, password);
                    int userId = currentUser.getId();
                    currentUsersProjects = projectManager.getProjects(userId);

                    if (currentUser != null) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Invalid credentials.");
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
                                System.out.println("- " + p.getName());
                                System.out.println("Project: " + p.getName() + " (ID: " + p.getId() + ")");
                                System.out.println("---------------------");
                            }

                            System.out.println("Please select a project to work on: ");
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
                                    System.out.println(currentProject.getName() + " uncompleted tasks: ");
                                    System.out.println(taskManager.getTasks(currentProject.getId(), false));
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
                                            System.out.println("Enter your task title below: ");
                                            String title = scanner.nextLine();

                                            if (title.trim().isEmpty()) {
                                                System.out.println("Task title cannot be empty.");
                                            } else if (!taskManager.isTitleAvailable(currentProject.getId(), title)) {
                                                System.out.println("Task name already exists! Please choose another one.");
                                            } else {
                                                System.out.println("Enter task description:");
                                                String description = scanner.nextLine();
                                                taskManager.addTask(currentProject.getId(), title, description);//
                                                System.out.println(title + " added to Task Manager!");
                                            }

                                    } else if (input.equals("l")) {
                                        taskManager.getTasks(currentProject.getId(), false);
                                        //CHECK ME
                                    } else if (input.equals("m")) {
                                        System.out.println("Enter your task title below: ");
                                        String input2 = scanner.nextLine();
                                        if (input2.trim().isEmpty()) {
                                            System.out.println("Task title cannot be empty.");
                                        } else {

                                            Task task = taskManager.getTaskByTitle(currentProject.getId(), input2);
                                            if (task == null) {
                                                System.out.println("Task not found.");
                                            } else {
                                                taskManager.markComplete(currentProject.getId(), input2);
                                                System.out.println("Task marked as completed!");
                                            }
                                        }

                                    } else if (input.equals("r")) {
                                        System.out.println("Enter your task title below: ");
                                        String input3 = scanner.nextLine();

                                        if (input3.trim().isEmpty()) {
                                            System.out.println("Task title cannot be empty.");
                                        } else {
                                            Task task = taskManager.getTaskByTitle(currentProject.getId(), input3);
                                            if (task == null) {
                                                System.out.println("Task not found.");
                                            } else {
                                                System.out.println("Are you sure you want to delete this task? Select [Y] or [N].");

                                                String input4 = scanner.nextLine();
                                                if (input4.equalsIgnoreCase("y")) {
                                                    taskManager.removeTask(currentProject.getId(), input3);
                                                    System.out.println("Task removed.");
                                                } else if (input4.equalsIgnoreCase("n")) {
                                                    System.out.println("Task not removed.");
                                                } else {
                                                    System.out.println("Invalid option.");
                                                }
                                            }
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



