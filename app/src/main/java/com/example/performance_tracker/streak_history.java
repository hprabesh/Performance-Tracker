package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class streak_history extends AppCompatActivity {
    // user streak part goes in here
    private TextView highPriorityStreakPoint;
    private TextView mediumPriorityStreakPoint;
    private TextView lowPriorityStreakPoint;
    private TextView totalPriorityStreakPoint;


    //Graphing
    LineChart mpLineChart;

    // All user variables goes in here
    private FirebaseUser loggedInUser;
    private DatabaseReference reference;
    private String loggedInUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streak_history);

        // loggedInUser

        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        loggedInUserId = loggedInUser.getUid();

        // streakpoint
        highPriorityStreakPoint = (TextView) findViewById(R.id.high_priority_streak_point);
        mediumPriorityStreakPoint= (TextView) findViewById(R.id.medium_priority_streak_point);
        lowPriorityStreakPoint = (TextView) findViewById(R.id.low_priority_streak_point);
        totalPriorityStreakPoint = (TextView) findViewById(R.id.total_priority_streak_point);

        // graphing
        mpLineChart = (LineChart) findViewById(R.id.streak_history_plot);




        // get the current streak
        reference.child(loggedInUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                    Streak userStreak;
                    HashMap<String,Streak> streakHistory = new HashMap<String, Streak>();

                if (userProfile != null){
                    userStreak = (Streak) userProfile.userStreaks;
                    streakHistory = userProfile.streakHistory;
                    String highPriorStreakPoint = userStreak.getHighPriorityStreak().toString();
                    String mediumPriorStreakPoint = userStreak.getMediumPriorityStreak().toString();
                    String lowPriorStreakPoint = userStreak.getLowPriorityStreak().toString();
                    String totalPriorStreakPoint = userStreak.totalStreak().toString();
                    if (userStreak.getHighPriorityStreak()==0)
                        findViewById(R.id.fire_streak_sign_1).setVisibility(View.INVISIBLE);
                    if (userStreak.getMediumPriorityStreak()==0)
                        findViewById(R.id.fire_streak_sign_1).setVisibility(View.INVISIBLE);
                    if (userStreak.getLowPriorityStreak()==0)
                        findViewById(R.id.fire_streak_sign_1).setVisibility(View.INVISIBLE);

                    highPriorityStreakPoint.setText(highPriorStreakPoint);
                    mediumPriorityStreakPoint.setText(mediumPriorStreakPoint);
                    lowPriorityStreakPoint.setText(lowPriorStreakPoint);
                    totalPriorityStreakPoint.setText(totalPriorStreakPoint);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(streak_history.this, "Error while retrieving streak history", Toast.LENGTH_SHORT).show();
            }
        });

    }
}