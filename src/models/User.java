package models;

import java.util.ArrayList;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private ArrayList<Project> projects;
//    private int nextProjectId = 1;


    public User(int id, String username, String email, String pw) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.projects = new ArrayList<>();
    }

    //getters and setters

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPw() { return password; }
    public ArrayList<Project> getProjects() { return projects; }

    //more//

    public void addProject(String name) {
        int newId = projects.size() + 1;
        Project project = new Project(newId, name, this.id);
        projects.add(project);
    }

    public Project getProjectByName(String name) {
        for (Project p : projects) {
            if (p.getName().equalsIgnoreCase(name)) {  // case-insensitive match
                return p;
            }
        }
        return null;
    }



    public boolean checkPassword(String inputPw) {
        return password.equals(inputPw);
    }

    @Override
    public String toString() {
        return "models.User #" + id + ": " + username + " ( " + email+ " ) ";
    }


}


