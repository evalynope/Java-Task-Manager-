package services;
import database.TaskRepo;
import models.Project;
import java.sql.*;
import java.sql.SQLException;
import java.util.List;

public class ProjectManager {
    private TaskRepo repository;
    public ProjectManager(TaskRepo repository) {
        this.repository = repository;
    }

    //  Create a project for a user
    public Project createProject(int userId, String name) {
        if (name == null || name.isBlank()) {
            System.out.println("Task title cannot be empty.");
            return null;
        }

        try {
            return repository.createProject(userId, name);
        } catch (SQLException e) {
            System.err.println("Could not create project" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //check to see if it exists:

//    public boolean isProjectNameAvailable(int userId, String name) {
//        try {
//            Project existingProject = repository.getProjectByName(userId, name);
//            return existingProject == null; // true if available
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//        return false;
//    }

    public boolean isProjectNameAvailable(int userId, String name) {
        try {
            // Fetch all projects for the user
            List<Project> projects = getProjects(userId);

            // Check if any project has exactly the same name (case-sensitive)
            for (Project p : projects) {
                if (p.getName().equals(name)) { // equals is case-sensitive
                    return false; // name already exists
                }
            }

            return true; // name is available
        } catch (Exception e) {
            System.err.println("Error checking project name availability: " + e.getMessage());
            return false; // safe default: treat as unavailable if error occurs
        }
    }

    //getProjects

    public List<Project> getProjects(int userId) {
        try {
            return repository.getProjects(userId);
        } catch (Exception e) {
            System.err.println("Could not get projects." + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    //getProjectsByName (select one project)

    public Project getProjectsByName(int userId, String name){
        try {
            return repository.getProjectByName(userId, name);
        } catch (Exception e) {
            System.err.println("Could not get projects." + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
