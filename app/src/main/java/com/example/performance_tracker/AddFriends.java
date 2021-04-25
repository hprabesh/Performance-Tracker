package com.example.performance_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;

public class AddFriends extends AppCompatActivity {
    // All Button Declaration Goes in Here
    private Button mainMenu;

    // All User Variables Goes in Here
    private FirebaseUser loggedInUser;
    private
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        mainMenu = (Button) findViewById(R.id.exit_friends);

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}