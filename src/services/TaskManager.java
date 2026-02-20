package services;
import models.*;

import java.sql.Connection;
import java.sql.SQLException;
import database.*;
import java.util.ArrayList;
import java.util.List;


//private Connection conn; //this line breaks the file - turns into compact.


public class TaskManager {
    private TaskRepoOnly repository;
    public TaskManager(TaskRepoOnly repository) {
        this.repository = repository;
    }

    //add task

    public Task addTask(int projectId, String title, String description) {
        if (title == null || title.isBlank()) {
            System.out.println("Task title cannot be empty.");
            return null;
        }

        try {
            return repository.addTask(projectId, title, description);
        } catch (Exception e) {
            System.err.println("Error adding task: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

//    check if name is available
    public boolean isTitleAvailable(int projectId, String title) {
        try {
            Task existingTask = repository.getTaskByTitle(projectId, title);
            return existingTask == null; // true if available
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    //get task by title

    public Task getTaskByTitle(int projectId, String title) {
        try {
            return repository.getTaskByTitle(projectId, title);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error fetching tasks.");
            return null;
        }
    }

    //list tasks
    public List<Task> getTasks(int projectId, boolean completed) {
        try {
            return repository.getTasks(projectId, false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error fetching tasks.");
            return null;
        }
    }

//markcomplete

    public boolean markComplete(int projectId, String title) {
        try {
            return repository.markComplete(projectId, title);
        } catch (Exception e) {
            System.err.println("Task could not be marked as complete.");
            e.printStackTrace();
            return false;
        }

    }

 //removetasks

    public boolean removeTask(int projectId, String title) {
        try {
            return repository.removeTask(projectId, title);
        } catch (Exception e) {
            System.err.println("Error removing task" + e.getMessage());
            e.printStackTrace();
            return false;

        }

    }

}
