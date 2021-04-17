package com.example.performance_tracker;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String projectName;
    private User projectOwner;
    private List<User> teamMembers = new ArrayList<User>();
    private List<Task> taskLists = new ArrayList<Task>();

    private Project(){
        throw new AssertionError("Project Owner Needed");
    }

    public Project(User projectOwner){
        this("Unknown Project", projectOwner);
    }

    public Project(String projectName, User projectOwner){
        this.setProjectName(projectName);
        this.setProjectOwner(projectOwner);
    }

    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public User getProjectOwner() {
        return projectOwner;
    }
    private void setProjectOwner(User projectOwner){
        this.projectOwner = projectOwner;
    }


    public List<User> getTeamMembers() {
        return teamMembers;
    }
    public void setTeamMembers(List<User> teamMembers) {
        this.teamMembers = teamMembers;
    }
    public void addTeamMember(User newMember){
        this.teamMembers.add(newMember);
    }

    public void deleteTeamMember(User deleteMember){
        this.teamMembers.remove(deleteMember);
    }


    public List<Task> getTaskLists(){
        return taskLists;
    }
    public void setTaskLists(List<Task> taskLists){
        this.taskLists = taskLists;
    }

    public void addTaskList(Task newTask){
        this.taskLists.add(newTask);
    }
    public void deleteTaskList(Task deleteTask){
        this.taskLists.remove(deleteTask);
    }

}
