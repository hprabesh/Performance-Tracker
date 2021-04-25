package com.example.performance_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFriends extends AppCompatActivity {
    // All content are collected here
    private EditText findFriendsSearch;

    // All Button Declaration Goes in Here
    private Button mainMenu;
    private Button searchFriends;
    // All User Variables Goes in Here
    private FirebaseUser loggedInUser;
    private DatabaseReference reference;
    private String loggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        findFriendsSearch = (EditText) findViewById(R.id.find_friends_search);
        searchFriends= (Button) findViewById(R.id.search_friends);

        searchFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendEmail = findFriendsSearch.getText().toString().trim().toLowerCase();
                if (friendEmail.isEmpty()){
                    findFriendsSearch.setError("Email Address is required");
                    findFriendsSearch.requestFocus();
                }

            }
        });






        mainMenu = (Button) findViewById(R.id.exit_friends);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}