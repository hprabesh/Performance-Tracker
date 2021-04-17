package com.example.performance_tracker;

import java.util.Hashtable;
import java.util.List;

public class User {
    public String firstName, lastName, emailAddress,phoneNumber, studentIdNumber;

    private List<User> friends;
    private Hashtable<String, String> classes;
    private List<Task> taskLists;

    public User(){

    }
    public User(String firstName, String lastName, String emailAddress, String phoneNumber, String studentIdNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.studentIdNumber = studentIdNumber;
    }
}
