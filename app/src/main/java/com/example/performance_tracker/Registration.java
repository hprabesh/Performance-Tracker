package com.example.performance_tracker;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;

import templates_and_keys.Keys;
import templates_and_keys.User;

public class Registration extends AppCompatActivity {

    // For the User fields
    private EditText editTextFirstName, editTextLastName, editTextEmailAddress, editTextPhoneNumber, editTextStudentIdNumber, editTextPassword;

    // For the Authentication
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Get Authentication Instance
        mAuth = FirebaseAuth.getInstance();

        // Get the Form Field from Layout
        editTextFirstName = (EditText) findViewById(R.id.first_name);
        editTextLastName = (EditText) findViewById(R.id.last_name);
        editTextEmailAddress = (EditText) findViewById(R.id.email);
        editTextPhoneNumber = (EditText) findViewById(R.id.phone);
        editTextStudentIdNumber = (EditText) findViewById(R.id.uuid);
        editTextPassword = (EditText) findViewById(R.id.new_password);

        // Button to create new account
        Button createNewAccount = (Button) findViewById(R.id.create_new_account);

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String emailAddress = editTextEmailAddress.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String studentIdNumber = editTextStudentIdNumber.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (firstName.isEmpty()) {
                    editTextFirstName.setError("First Name is required!");
                    editTextFirstName.requestFocus();
                    return;
                }
                if (lastName.isEmpty()) {
                    editTextLastName.setError("Last Name is required!");
                    editTextLastName.requestFocus();
                    return;
                }
                if (emailAddress.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    editTextEmailAddress.setError("Valid Email Address is required!");
                    editTextEmailAddress.requestFocus();
                    return;
                }
                if (phoneNumber.isEmpty()) {
                    editTextPhoneNumber.setError("Phone Number  is required!");
                    editTextPhoneNumber.requestFocus();
                    return;
                }

                if (studentIdNumber.isEmpty()) {
                    editTextStudentIdNumber.setError("Student ID is required!");
                    editTextStudentIdNumber.requestFocus();
                    return;
                }
                if (password.isEmpty() || password.length() < 10) {
                    editTextPassword.setError("Valid Password (minimum length = 10) is Required");
                    editTextPassword.requestFocus();
                    return;
                }

                // Invoking the Google Firebase Authentication
                mAuth.createUserWithEmailAndPassword(emailAddress, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    // Create an instance of User class
                                    User user = new User(firstName, lastName, emailAddress, phoneNumber, studentIdNumber);

                                    FirebaseFirestore.getInstance().collection(Keys.USERS)
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), "Registration Success: ", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Registration Failed: ".concat(e.getMessage()), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                } else {
                                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                    Toast.makeText(getApplicationContext(), "Registration Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        Button cancelRegistration = (Button) findViewById(R.id.cancel_registration);
        cancelRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}