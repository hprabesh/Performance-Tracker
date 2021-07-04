package com.example.performance_tracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import templates_and_keys.SessionManagement;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText userEmailField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = ((EditText) userEmailField).getText().toString().trim();
                String passwordString = ((EditText) passwordField).getText().toString().trim();
                if (userEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    userEmailField.setError("Please enter valid email address!");
                    userEmailField.requestFocus();
                    return;
                } // email validation
                if (passwordString.length() < 10) {
                    passwordField.setError("Please enter the valid password!");
                    passwordField.requestFocus();
                    return;
                } // password validation
                firebaseAuthInvocation(userEmail, passwordString); // firebase call
            }
        });

        // new user registration
        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationPage = new Intent(MainActivity.this, Registration.class);
                MainActivity.this.startActivityForResult(registrationPage, 100);
            }
        });

        // forgot password
        Button forgetPassword = findViewById(R.id.forgot_password);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetPasswordPage = new Intent(MainActivity.this, ForgetPassword.class);
                MainActivity.this.startActivityForResult(forgetPasswordPage, 100);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check for user logged in session
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        String[] userInfo = sessionManagement.getSession(); // first string : user email && second string: user password
        if (userInfo[0]!=null && userInfo[1]!=null){
            openProfileActivity();
        }
    }

    private void firebaseAuthInvocation(String usernameString, String passwordString) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(usernameString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user.isEmailVerified()) {
                        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
                        sessionManagement.saveSession(usernameString, passwordString); // save the user session
                        openProfileActivity(); // open the activity
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Please check your email for verification!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Login Failed! Username/Password Incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openProfileActivity() {
        Intent intent = new Intent(MainActivity.this, Profile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear the intent
        startActivity(intent);
    }
}