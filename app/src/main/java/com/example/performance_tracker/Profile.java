package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
                    String streakPoint = userProfile.userStreaks.totalStreak().toString();
                    firstNameTextView.setText("Hi "+firstName+"!");
                    streakPoints.setText(streakPoint);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Error while accessing user account! Please retry", Toast.LENGTH_SHORT).show();
            }
        });


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