package models;

public class Task {
    // Fields
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private int projectId; // links task to a project

    // constructor
    public Task(int id, String title, String description, boolean completed, int projectId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = false;
        this.projectId = projectId;
    }


    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public int getProjectId() { return projectId; }

    //setters

    public void setCompleted(boolean completed) { this.completed = true; }

    //


    @Override
    public String toString() {
        return "Task #" + id + ": " + title + " [" + (completed ? "Completed" : "Pending") + "]";
    }
}

