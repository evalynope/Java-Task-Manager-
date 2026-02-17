package services;
import models.Task;
import models.Project;
import java.util.ArrayList;


public class TaskManager {

    private Project project;
    private int nextId = 1;
    public TaskManager(Project project) {
        this.project = project;
    }
//    private ArrayList<Task> tasks = new ArrayList<>();



    //methods

    public boolean addTask(String title, String description) {
        if (title == null || title.isBlank()) return false;
        Task newTask = new Task(nextId, title, description);
        project.addTask(newTask);
        nextId++;
        return true;
    }


    public void listTasks() {
        for (Task task : project.getTasks()) {
            if (!task.isCompleted()) {
                System.out.println(task);
            }
        }

    }

    public void markComplete(String title) {
        for (Task task : project.getTasks()) {
            if (task.getTitle().equalsIgnoreCase(title)) {
                task.setCompleted(true);
                return;
            }
        }


    }

    public void removeTask(String title) {
        project.getTasks().removeIf(t -> t.getTitle().equalsIgnoreCase(title));
    }

}














//* Add a task
//* List all tasks
//* Mark a task as complete
//* Remove a task
//* Filter tasks (optional)