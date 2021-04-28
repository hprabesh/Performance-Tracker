package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.utilities.Tree;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Profile extends AppCompatActivity {
    // All Button Declaration Goes here
    private Button logOut;
    private Button addFriendButton;
    private Button addClassButton;
    private Button addTaskButton;
    private Button addProject;

    // All Listview goes in here
    private ListView listview;

    // All User Variables Goes in Here
    private FirebaseUser loggedInUser;
    private DatabaseReference reference;

    private String loggedInUserId;


    // View user streak history
    private TextView viewStreakHistory;
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // for the loggedInUser

        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        loggedInUserId = loggedInUser.getUid();

        // Home-Screen Content

        final TextView firstNameTextView = (TextView) findViewById(R.id.first_name); // the first name
        final TextView streakPoints = (TextView) findViewById(R.id.streak);
        reference.child(loggedInUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile!= null){
                    String firstName = userProfile.firstName;
                    firstNameTextView.setText("Hi "+firstName+"!");


                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String loggedInDate= currentDate.format(new Date());

                    TreeMap<String, Streak> streakHistory = new TreeMap<>(userProfile.streakHistory);
                    if (!streakHistory.containsKey(loggedInDate)){
                        Streak newStreakAdded = new Streak(loggedInDate);
                        newStreakAdded.setHighPriorityStreak(Objects.requireNonNull(streakHistory.lastEntry()).getValue().getHighPriorityStreak());
                        newStreakAdded.setMediumPriorityStreak(Objects.requireNonNull(streakHistory.lastEntry()).getValue().getMediumPriorityStreak());
                        newStreakAdded.setLowPriorityStreak(Objects.requireNonNull(streakHistory.lastEntry()).getValue().getLowPriorityStreak());
                        streakHistory.put(loggedInDate,newStreakAdded);
                        reference.child(loggedInUserId).child("streakHistory").setValue(streakHistory);
                        reference.child(loggedInUserId).child("userStreaks").setValue(newStreakAdded);
                    }
                    String streakPoint = userProfile.userStreaks.totalStreak().toString();
                    streakPoints.setText(streakPoint);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Error while accessing user account! Please retry", Toast.LENGTH_SHORT).show();
            }
        });

        // for pop up User Streak
        relativeLayout = (RelativeLayout) findViewById(R.id.streak_relative_layout);
        viewStreakHistory = (TextView) findViewById(R.id.view_full_streak);
        viewStreakHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, streak_history.class));
            }
        });

        // for the task list
        // for populating the pending task field
        listview = (ListView) findViewById(R.id.task_list);
        listview.setScrollContainer(false);


        String[] values = new String[] {
                "April 26, 2021",
                "   User Task 1       PRIORITY: High",
                "   User Task 2       PRIORITY: Medium",
                "   User Task 3       PRIORITY: Low",
                "   User Task 4       PRIORITY: High",
                "April 25, 2021",
                "   User Task 1       PRIORITY: High",
                "   User Task 2       PRIORITY: Low",
                "   User Task 3       PRIORITY: High",
                "   User Task 4       PRIORITY: High"}; // this should be the formatting for displaying the task


        final ArrayList<String> list = new ArrayList<String>(Arrays.asList(values));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(arrayAdapter);



        // All footer goes in here
        // Adding New Project
        addProject =(Button) findViewById(R.id.open_project_activity);
        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, AddProject.class));
            }
        });

        // Adding New Friends
        addFriendButton =(Button) findViewById(R.id.open_friend_activity);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, AddFriends.class));
            }
        });

        // Adding New Task
        addFriendButton =(Button) findViewById(R.id.open_task_activity);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, AddTask.class));
            }
        });

        // Adding New Class
        addFriendButton =(Button) findViewById(R.id.open_add_class_activity);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, AddFriends.class));
            }
        });

        // LogOut the Session
        logOut =(Button) findViewById(R.id.log_out);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, MainActivity.class));
            }
        });


    }


}