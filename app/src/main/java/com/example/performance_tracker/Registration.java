package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmailAddress, editTextPhoneNumber, editTextStudentIdNumber, editTextPassword;

    private Button createNewAccount, cancelRegistration;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        editTextFirstName = (EditText) findViewById(R.id.first_name);
        editTextLastName = (EditText) findViewById(R.id.last_name);
        editTextEmailAddress = (EditText) findViewById(R.id.email);
        editTextPhoneNumber = (EditText) findViewById(R.id.phone);
        editTextStudentIdNumber = (EditText) findViewById(R.id.uuid);
        editTextPassword = (EditText) findViewById(R.id.new_password);

        createNewAccount = (Button) findViewById(R.id.create_new_account);

        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String emailAddress = editTextEmailAddress.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String studentIdNumber =editTextStudentIdNumber.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (firstName.isEmpty()){
                    editTextFirstName.setError("First Name is required!");
                    editTextFirstName.requestFocus();
                    return;
                }
                if (lastName.isEmpty()){
                    editTextLastName.setError("Last Name is required!");
                    editTextLastName.requestFocus();
                    return;
                }
                if (emailAddress.isEmpty()){
                    editTextEmailAddress.setError("Email Address is required!");
                    editTextEmailAddress.requestFocus();
                    return;
                }
                if (phoneNumber.isEmpty()){
                    editTextPhoneNumber.setError("Phone Number  is required!");
                    editTextPhoneNumber.requestFocus();
                    return;
                }

                if (studentIdNumber.isEmpty()){
                    editTextStudentIdNumber.setError("Student ID is required!");
                    editTextStudentIdNumber.requestFocus();
                    return;
                }
                if (password.isEmpty()){
                    editTextPassword.setError("Password Required");
                    editTextPassword.requestFocus();
                    return;
                }
                if (password.length()<10){
                    editTextPassword.setError("Password minimum length is 10");
                    editTextPassword.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(emailAddress, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    User user = new User(firstName,lastName,emailAddress,phoneNumber,studentIdNumber);

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "User has been Registered Successfully",Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        cancelRegistration = (Button) findViewById(R.id.cancel_registration);
        cancelRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }
}