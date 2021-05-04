package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class view_friend_streak extends AppCompatActivity {
    private TextView friends, highPriorityStreakPoints, mediumPriorityStreakPoints, lowPriorityStreakPoints
            , totalPriorityStreakPoints, fireStreak1, fireStreak2, fireStreak3;

    // Get User Credentials
    private FirebaseUser loggedInUser;
    private DatabaseReference reference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friend_streak);

        Intent get = getIntent();
        userId = get.getStringExtra("userId");

        friends = (TextView) findViewById(R.id.friend_streak_history);
        highPriorityStreakPoints =(TextView)findViewById(R.id.high_priority_streak_point_friends);
        mediumPriorityStreakPoints =(TextView) findViewById(R.id.medium_priority_streak_point_friends);
        lowPriorityStreakPoints =(TextView) findViewById(R.id.low_priority_streak_point_friends);
        totalPriorityStreakPoints = (TextView) findViewById(R.id.total_priority_streak_point_friends);
        fireStreak1 = (TextView) findViewById(R.id.fire_streak_sign_1_friends);
        fireStreak2 = (TextView) findViewById(R.id.fire_streak_sign_2_friends);
        fireStreak3 = (TextView) findViewById(R.id.fire_streak_sign_3_friends);



        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                Streak userStreak = userProfile.userStreaks;
                friends.setText("Streak Point of ".concat(userProfile.firstName));
                highPriorityStreakPoints.setText(userStreak.getHighPriorityStreak().toString());
                mediumPriorityStreakPoints.setText(userStreak.getMediumPriorityStreak().toString());
                lowPriorityStreakPoints.setText(userStreak.getLowPriorityStreak().toString());
                totalPriorityStreakPoints.setText(userStreak.totalStreak().toString());
                if (userStreak.getHighPriorityStreak()==0)
                    fireStreak1.setVisibility(View.INVISIBLE);
                if (userStreak.getMediumPriorityStreak()==0)
                    fireStreak2.setVisibility(View.INVISIBLE);
                if (userStreak.getLowPriorityStreak()==0)
                    fireStreak3.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view_friend_streak.this, "Error while retrieving streak history", Toast.LENGTH_SHORT).show();
            }
        });
    }
}