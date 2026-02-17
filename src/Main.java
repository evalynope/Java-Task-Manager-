import services.TaskManager;
import models.Task;
import models.Project;
import models.User;
import services.UserManager;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        User currentUser = null;
        ArrayList<Project> projects = new ArrayList<>();
        Project currentProject = null;
        TaskManager t = new TaskManager(currentProject);
        Task task = new Task(1, "Task Title", "Task Description");


        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (currentUser == null) {
                System.out.println("* * * Authorization Menu * * *");
                System.out.println("[R] Register");
                System.out.println("[L] Login");
                System.out.println("[Q] Quit");

                String choice = scanner.nextLine().toLowerCase();

                if (choice.equals("r")) {
                    System.out.println("Username: ");
                    String username = scanner.nextLine();

                    System.out.println("Email: ");
                    String email = scanner.nextLine();

                    System.out.println("Password: ");
                    String password = scanner.nextLine();

                    userManager.registerUser(username, email, password);

                } else if (choice.equals("l")) {
                    System.out.println("Username: ");
                    String username = scanner.nextLine();

                    System.out.println("Password: ");
                    String password = scanner.nextLine();

                    currentUser = userManager.loginUser(username, password);

                    if (currentUser != null) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Invalid credentials.");
                    }

                } else if (choice.equals("q")) {
                    break;
                }
            } else if (currentProject == null) {
                while (true) {
                    System.out.println("* * * Project Menu * * *");
                    System.out.println("[C] Create Project");
                    System.out.println("[S] Select Project");
                    System.out.println("[L] List Projects");
                    System.out.println("[O] Logout");

                    String choice = scanner.nextLine().toLowerCase();


                    if (choice.equals("c")) {
                        System.out.print("Project name: ");
                        String name = scanner.nextLine();

                        currentUser.addProject(name);
                        System.out.println("Project created.");


                    } else if (choice.equals("s")) {
                        if (currentUser.getProjects().isEmpty()) {
                            System.out.println("No projects to select yet. Create one first!");
                        } else {
                            System.out.println("* * * My Projects * * *");
                            for (Project p : currentUser.getProjects()) {
                                System.out.println("- " + p.getName());
                            }

                            System.out.println("Please select a project to work on: ");
                            String name = scanner.nextLine();

                            currentProject = currentUser.getProjectByName(name);

                            if (currentProject != null) {
                                t = new TaskManager(currentProject);
                                System.out.println("Project selected!");

                                //task menu

                                while (true) {
                                    System.out.println( currentProject.getName() + " uncompleted tasks: ");
//                                    System.out.println(t.listTasks());
                                    System.out.println("[A]dd a task");
                                    System.out.println("[L]ist all tasks");
                                    System.out.println("[M]ark a task as complete");
                                    System.out.println("[R]emove a task");
                                    System.out.println("[B]ack to Projects");

                                    String input = scanner.nextLine().toLowerCase();

                                    if (input.trim().isEmpty()) {
                                        System.out.println("Please enter a valid option.");
                                        continue;
                                    } else if (input.equals("b")) {
                                        break;
                                    } else if (input.equals("a")) {
                                        System.out.println("Enter your task title below: ");
                                        String title = scanner.nextLine();
                                        if (title.trim().isEmpty()) {
                                            System.out.println("Task title cannot be empty.");
                                            return;
                                        } else {
                                            System.out.println("Enter task description:");
                                            String description = scanner.nextLine();
                                            t.addTask(title, description);
                                            System.out.println(title + " added to models.Task Manager!");
                                        }
                                    } else if (input.equals("l")) {
                                        t.listTasks();
                                    } else if (input.equals("m")) {
                                        System.out.println("Enter your task title below: ");
                                        String input2 = scanner.nextLine();
                                        if (input2.trim().isEmpty()) {
                                            System.out.println("Task title cannot be empty.");
                                            return;
                                        } else {
                                            t.markComplete(input2);
                                            System.out.println("Task marked as completed!");
                                        }

                                    } else if (input.equals("r")) {
                                        System.out.println("Enter your task title below: ");
                                        String input3 = scanner.nextLine();

                                        if (input3.trim().isEmpty()) {
                                            System.out.println("Task title cannot be empty.");
                                            return;
                                        }

                                        System.out.println("Are you sure you want to delete this task? Select [Y] or [N].");
                                        String input4 = scanner.nextLine();
                                        if (input4.equalsIgnoreCase("y")) {
                                            t.removeTask(input3);
                                            System.out.println("Task removed.");
                                        } else if (input4.equalsIgnoreCase("n")) {
                                            System.out.println("Task not removed.");
                                        } else {
                                            System.out.println("Invalid option.");
                                        }

                                    }
                                }

                            } else {
                                System.out.println("Project not found.");
                            }

                        }

                    } else if (choice.equals("l")) {
                        if (currentUser.getProjects().isEmpty()) {
                            System.out.println("No projects yet.");
                        } else {
                            System.out.println("* * * My Projects * * *");
                            for (Project p : currentUser.getProjects()) {
                                System.out.println(p.getName());
                            }
                        }

                    } else if (choice.equals("o")) {
                        currentUser = null;
                        System.out.println("Logged out. Returning to login menu...");
                        break;

                    } else if (choice.equals("q")) {
                        break;
                    }

                }
            }
        }
    }
}


