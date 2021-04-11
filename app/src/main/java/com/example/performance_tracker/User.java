package com.example.performance_tracker;

public class User {
    public String firstName, lastName, emailAddress,phoneNumber, studentIdNumber;

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
