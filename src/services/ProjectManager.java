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

            if ("23505".equals(e.getSQLState())) {  // unique violation
                System.out.println("Project name already exists.");
                return null;
            }

            System.out.println("Error creating project.");
            return null;
        }
    }

    public boolean isProjectNameAvailable(int userId, String name) {
        try {
            List<Project> projects = getProjects(userId);

            for (Project p : projects) {
                if (p.getName().equals(name)) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            System.err.println("Error checking project name availability: " + e.getMessage());
            return false;
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
