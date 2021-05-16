package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import classes_template.Priority;
import classes_template.Task;
import classes_template.User;

public class AddTask extends AppCompatActivity {
    // All button declaration goes in here
    private Button mainMenu;
    private Button addTask;

    // All content edit text goes in here
    private EditText newTask;
    private Spinner newTaskPriority;
    private Spinner newTaskClassRelated;
    private DatePicker newTaskDeadline;

    // All user variables goes in here
    private FirebaseUser loggedInUser;
    private DatabaseReference reference;

    private String loggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // user login
        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        loggedInUserId = loggedInUser.getUid();


        // Getting the task
        newTask = (EditText) findViewById(R.id.new_task);


        // Task Level Priority
        newTaskPriority =(Spinner) findViewById(R.id.new_task_priority);

        Priority[] items = new Priority[]{Priority.NONE,Priority.HIGH, Priority.MEDIUM, Priority.LOW};
        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,items);
        newTaskPriority.setAdapter(adapter);



        // Class
            // Yet to be implemented

        // Deadline
        newTaskDeadline = (DatePicker) findViewById(R.id.new_task_deadline);
//        newTaskDeadline.(false);


        // Add Task
        addTask = (Button) (findViewById(R.id.add_new_task));
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = newTask.getText().toString().trim();
                Priority priorityLevel = Priority.valueOf(newTaskPriority.getSelectedItem().toString());

                // Get Deadline
                String month;
                String day;
                if ((newTaskDeadline.getMonth()+1)<10){
                    month = "0".concat(String.valueOf(newTaskDeadline.getMonth()+1));
                } else {
                    month = String.valueOf(newTaskDeadline.getMonth()+1);
                }
                if ((newTaskDeadline.getDayOfMonth())<10){
                    day = "0".concat(String.valueOf(newTaskDeadline.getDayOfMonth()));
                } else {
                    day = String.valueOf(newTaskDeadline.getDayOfMonth());
                }
                String deadline = String.valueOf(newTaskDeadline.getYear()).concat("-").concat(month).concat("-").concat(day);
                if (taskName.isEmpty()){
                    newTask.setError("Required task Name");
                    newTask.requestFocus();
                    return;
                }
                if (priorityLevel== Priority.NONE){
                    newTask.setError("Required priority level");
                    newTaskPriority.requestFocus();
                    return;
                }

                if (deadline.isEmpty()){
                    newTask.setError("Required Proper Date");
                    newTaskDeadline.requestFocus();
                    return;
                }

                String uuid = UUID.randomUUID().toString().replace("-","");
                Task userTask = new Task(uuid, taskName, deadline,priorityLevel);
                List<Task> previousTaskList = new ArrayList<Task>();

                reference.child(loggedInUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);

                        if (userProfile!= null){
                            if (userProfile.taskLists!=null){
                                Map<String, Object> taskToBeAdded = new HashMap<>();
                                taskToBeAdded.put(uuid, userTask);
                                reference.child(loggedInUserId).child("taskLists").child(deadline).updateChildren(taskToBeAdded);
                            } else {
                                reference.child(loggedInUserId)
                                        .child("taskLists")
                                        .child(deadline)
                                        .child(uuid)
                                        .setValue(userTask)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(getApplicationContext(), "Task has been added Successfully",Toast.LENGTH_LONG).show();
                                                } else {
                                                    FirebaseAuthException e= (FirebaseAuthException) task.getException();
                                                    Toast.makeText(getApplicationContext(), "Failed Adding Task: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddTask.this, "Error adding task", Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });

        // Close the activity
        mainMenu= (Button) findViewById(R.id.exit_task);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}