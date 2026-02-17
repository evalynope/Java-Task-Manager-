package models;

import java.util.ArrayList;

public class Project {
    private int id;
    private String name;
    private int ownerId;
    private ArrayList<Task> tasks;


    public Project(int id, String name, int ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
//        this.tasks = new ArrayList<>();

    }

//    public void addTask(Task task){
//        tasks.add(task);
//    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getOwnerId() { return ownerId; }
//    public ArrayList<Task> getTasks() { return tasks; }


    @Override
    public String toString() {
        return "models.Project #" + id + ": " + name + " ( " + ownerId+ " ) ";
    }

}