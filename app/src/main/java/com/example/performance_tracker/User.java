package com.example.performance_tracker;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class User {
    public String firstName, lastName, emailAddress,phoneNumber, studentIdNumber, accountOpenedDate;
    public Streak userStreaks;

    private List<User> friends;
    public HashMap<String,Streak> streakHistory;
    public HashMap<String, HashMap<String,Task>> taskLists;
    public Integer numberOfTasks;
    private Hashtable<String, String> classes;

    // All user variable goes in here
    private FirebaseUser loggedInUser;
    private DatabaseReference reference;
    private String loggedInUserId;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String firstName, String lastName, String emailAddress, String phoneNumber, String studentIdNumber){
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.studentIdNumber = studentIdNumber;
        this.numberOfTasks = 0;

        SimpleDateFormat currentDate= new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.getDefault());
        this.accountOpenedDate = currentDate.format(new Date());
        this.userStreaks = new Streak(this.accountOpenedDate);

        this.streakHistory = new HashMap<String, Streak>();
        this.streakHistory.put(accountOpenedDate,userStreaks);

        this.classes = new Hashtable<String, String>();

    }


    public void setStreakHistory(String date, Streak streak) {
        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        loggedInUserId = loggedInUser.getUid();
        this.streakHistory.put(date, streak);
        reference.child(loggedInUserId).child("StreakHistory").setValue(streakHistory);
    }
}
