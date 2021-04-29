package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

public class MarkCompleteTask extends AppCompatActivity {

    // Text view
    private TextView taskName;
    private TextView taskStatus;
    private TextView taskDeadline;
    private TextView taskPriority;


    // ImageView : Button

    private ImageView markCompleted;


    // All user Variables goes in here
    private FirebaseUser loggedInUser;
    private DatabaseReference reference;

    private String loggedInUserId;

    // Received User Task Id
    private String taskId;
    private String taskDate;
    private TreeMap<String, Streak> streakHistory;
    private HashMap<String,HashMap<String, Task>> taskContent;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_complete_task);

        // text view
        taskName = (TextView) findViewById(R.id.task_name_to_be_completed);
        taskStatus =(TextView) findViewById(R.id.task_status_to_be_completed);
        taskDeadline =(TextView) findViewById(R.id.task_deadline_to_be_completed);
        taskPriority =(TextView) findViewById(R.id.task_priority_to_be_completed);
        //Button
        markCompleted = (ImageView) findViewById(R.id.mark_task_completed);

        // for logged in user
        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");


        loggedInUserId = loggedInUser.getUid();


        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            taskId = bundle.getString("taskId");
            taskDate = bundle.getString("taskDate");
        }
        reference.child(loggedInUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile!=null){

                    taskContent = userProfile.taskLists;
                    task = taskContent.get(taskDate).get(taskId);

                    String taskNameCollect = task.getTaskName();
                    String taskDeadlineDateCollect = task.getTaskDueDate();

                    String taskPriorityCollect;
                    if (task.getTaskPriority()== Priority.HIGH){
                        taskPriorityCollect= "High";
                    } else if (task.getTaskPriority()== Priority.MEDIUM){
                        taskPriorityCollect= "Medium";
                    } else {
                        taskPriorityCollect = "Low";
                    }

                    String taskStatusCollect = (!task.getTaskStatus())?"Incomplete": "Completed";
                    taskName.setText(taskNameCollect);
//                    taskName.setText(taskId);
                    taskStatus.setText(taskStatusCollect);
//                    taskStatus.setText(taskDate);
                    taskDeadline.setText(taskDeadlineDateCollect);
                    taskPriority.setText(taskPriorityCollect);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MarkCompleteTask.this, "Error while accessing user task! Please retry", Toast.LENGTH_SHORT).show();
            }
        });

        markCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(loggedInUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);
                        if (userProfile!=null){

                            reference.child(loggedInUserId).child("taskLists").child(taskDate).child(taskId).removeValue();
                            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String loggedInDate= currentDate.format(new Date());
                            streakHistory = new TreeMap<>(userProfile.streakHistory);

                            if (task.getTaskPriority()== Priority.HIGH){
                                Streak updatedStreak = streakHistory.get(loggedInDate);
                                assert updatedStreak != null;
                                updatedStreak.addHighStreak();
                                streakHistory.put(loggedInDate, updatedStreak);

                            } else if (task.getTaskPriority()== Priority.MEDIUM){
                                Streak updatedStreak = streakHistory.get(loggedInDate);
                                assert updatedStreak != null;
                                updatedStreak.addMediumStreak();
                                streakHistory.put(loggedInDate, updatedStreak);
                            } else {
                                Streak updatedStreak = streakHistory.get(loggedInDate);
                                assert updatedStreak != null;
                                updatedStreak.addLowStreak();
                                streakHistory.put(loggedInDate, updatedStreak);
                            }
                            reference.child(loggedInUserId).child("streakHistory").setValue(streakHistory);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MarkCompleteTask.this, "Error while accessing user task! Please retry", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



    }
}