package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private Button resetPassword;
    private EditText resetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        resetEmail = (EditText) findViewById(R.id.forget_password_email);

        resetPassword = (Button) findViewById(R.id.reset_password);

        mAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String emailAddress = resetEmail.getText().toString().trim();
            if (emailAddress.isEmpty()){
                resetEmail.setError("Email is Required");
                resetEmail.requestFocus();
                return;
            }
            mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Snackbar.make(findViewById(R.id.reset_password_layout), "An email has been sent for password reset", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
            }
        });
    }
}