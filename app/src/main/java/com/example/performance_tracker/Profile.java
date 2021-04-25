package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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

import java.util.Hashtable;

public class Profile extends AppCompatActivity {
    // All Button Declaration Goes here
    private Button logOut;
    private Button addFriendButton;
    private Button addProject;

    // All User Variables Goes in Here
    private FirebaseUser loggedInUser;
    private DatabaseReference reference;

    private String loggedInUserId;


    public void loadTasks() {
        TableLayout stk = (TableLayout) findViewById(R.id.task_table);
        stk.setColumnStretchable(1, true);
        stk.setColumnStretchable(2, true);
        stk.setColumnStretchable(3, true);
        stk.setStretchAllColumns(true);


        for (int i = 0; i < 25; i++) {
            TableRow tbrow = new TableRow(this);

            TextView t1v = new TextView(this);
            t1v.setText("This position ");
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.LEFT);
            tbrow.addView(t1v);

            TextView t2v = new TextView(this);
            t2v.setText("High | Medium | Low " );
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);

            TextView t3v = new TextView(this);
            t3v.setText("MM-DD-YYYY" );
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.RIGHT);
            tbrow.addView(t3v);
            stk.addView(tbrow);

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loadTasks(); //load all the tasks

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
        addProject =(Button) findViewById(R.id.project);
        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, AddProject.class));
            }
        });

        // Adding New Friends
        addFriendButton =(Button) findViewById(R.id.friends);
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