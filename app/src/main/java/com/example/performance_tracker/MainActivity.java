package com.example.performance_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        private FirebaseAuth mAuth;

        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameString = ((EditText) usernameField).getText().toString();
                String passwordString = ((EditText) passwordField).getText().toString();
                if (usernameString.equals("prabesh") && passwordString.equals("prabesh")){
                        Snackbar.make(findViewById(R.id.login_page),
                            "Error Username", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}