package com.example.performance_tracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

public class User {
    public String firstName, lastName, emailAddress,phoneNumber, studentIdNumber, accountOpenedDate;
    private List<User> friends;
    private Hashtable<String, String> classes;
    private List<Task> taskLists;
    private List<Streak> streakHistory;
    private Streak userStreaks;
    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String firstName, String lastName, String emailAddress, String phoneNumber, String studentIdNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.studentIdNumber = studentIdNumber;

        SimpleDateFormat currentDate= new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.getDefault());
        this.accountOpenedDate = currentDate.format(new Date());
        this.userStreaks = new Streak(this.accountOpenedDate);
    }

    public Streak getUserStreaks() {

        return userStreaks;
    }
}
