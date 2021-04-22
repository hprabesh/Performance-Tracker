package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    // All Button Declaration Goes here
    private Button logOut;
    private Button addFriendButton;
    private Button addProject;

    // All User Variables Goes in Here
    private FirebaseUser loggedInUser;
    private DatabaseReference reference;

    private String loggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        loggedInUserId = loggedInUser.getUid();

        // Home-Screen Content

        final TextView firstNameTextView = (TextView) findViewById(R.id.first_name);
        reference.child(loggedInUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile!= null){
                    String firstName = userProfile.firstName;
                    firstNameTextView.setText("Hi "+firstName+"!");
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