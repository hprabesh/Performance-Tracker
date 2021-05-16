package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameString = ((EditText) usernameField).getText().toString().trim();
                String passwordString = ((EditText) passwordField).getText().toString().trim();
                if (usernameString.isEmpty()){
                    usernameField.setError("Email is required to login!");
                    usernameField.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(usernameString).matches()){
                    usernameField.setError("Please enter valid Email!");
                    usernameField.requestFocus();
                    return;
                }

                if (passwordString.isEmpty()){
                    passwordField.setError("Please enter the password!");
                    passwordField.requestFocus();
                    return;
                }
                if (passwordString.length()<10){
                    passwordField.setError("Password minimum length is 10!!");
                    passwordField.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(usernameString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user.isEmailVerified()){
                                startActivity(new Intent(MainActivity.this, Profile.class));
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
        });


        Button registerButton = findViewById(R.id.register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationPage = new Intent(MainActivity.this, Registration.class);
                MainActivity.this.startActivityForResult(registrationPage, 100);
            }
        });

        Button forgetPassword = findViewById(R.id.forgot_password);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetPasswordPage = new Intent(MainActivity.this, ForgetPassword.class);
                MainActivity.this.startActivityForResult(forgetPasswordPage, 100);
            }
        });
    }
}