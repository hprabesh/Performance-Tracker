package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
                    HashMap<String,Streak> unSortedStreakHistory = new HashMap<String, Streak>();

                if (userProfile != null){
                    userStreak = (Streak) userProfile.userStreaks;
                    String highPriorStreakPoint = userStreak.getHighPriorityStreak().toString();
                    String mediumPriorStreakPoint = userStreak.getMediumPriorityStreak().toString();
                    String lowPriorStreakPoint = userStreak.getLowPriorityStreak().toString();
                    String totalPriorStreakPoint = userStreak.totalStreak().toString();
                    if (userStreak.getHighPriorityStreak()==0)
                        findViewById(R.id.fire_streak_sign_1).setVisibility(View.INVISIBLE);
                    if (userStreak.getMediumPriorityStreak()==0)
                        findViewById(R.id.fire_streak_sign_2).setVisibility(View.INVISIBLE);
                    if (userStreak.getLowPriorityStreak()==0)
                        findViewById(R.id.fire_streak_sign_3).setVisibility(View.INVISIBLE);

                    highPriorityStreakPoint.setText(highPriorStreakPoint);
                    mediumPriorityStreakPoint.setText(mediumPriorStreakPoint);
                    lowPriorityStreakPoint.setText(lowPriorStreakPoint);
                    totalPriorityStreakPoint.setText(totalPriorStreakPoint);

                    unSortedStreakHistory = userProfile.streakHistory;
                    Map<String,Streak> streakHistory = new TreeMap<>(unSortedStreakHistory);
                    // Hashmap <String, Streak>
                    final ArrayList<String> xAxisLabel = new ArrayList<>();
                    final ArrayList<Entry> yHighAxisLabel = new ArrayList<>();
                    final ArrayList<Entry> yMediumAxisLabel = new ArrayList<>();
                    final ArrayList<Entry> yLowAxisLabel = new ArrayList<>();
                    final ArrayList<Entry> yTotalAxisLabel = new ArrayList<>();
                    Integer xAxisPosition =0;
                    for (Map.Entry<String, Streak> entry: streakHistory.entrySet()){
                        String[] dateArray = entry.getKey().split("-",4);
                        String date = dateArray[1].concat("-").concat(dateArray[2]);
                        xAxisLabel.add(date);
                        Streak dateStreak = entry.getValue();
                        yHighAxisLabel.add(new Entry(xAxisPosition,dateStreak.getHighPriorityStreak()));
                        yMediumAxisLabel.add(new Entry(xAxisPosition,dateStreak.getMediumPriorityStreak()));
                        yLowAxisLabel.add(new Entry(xAxisPosition,dateStreak.getLowPriorityStreak()));
                        yTotalAxisLabel.add(new Entry(xAxisPosition,dateStreak.totalStreak()));
                        xAxisPosition++;
                    }

                    XAxis xAxis = mpLineChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));

                    LineDataSet highPriorityLineStreak = new LineDataSet(yHighAxisLabel, "High Priority");
                    highPriorityLineStreak.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    highPriorityLineStreak.setCubicIntensity(0.2f);
                    highPriorityLineStreak.setLineWidth(4f);
                    highPriorityLineStreak.setColor(Color.parseColor("#ff1111"));
                    highPriorityLineStreak.setCircleColor(Color.parseColor("#ff1111"));

                    LineDataSet mediumPriorityLineStreak = new LineDataSet(yMediumAxisLabel, "Medium Priority");
                    mediumPriorityLineStreak.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    mediumPriorityLineStreak.setCubicIntensity(0.2f);
                    mediumPriorityLineStreak.setLineWidth(4f);
                    mediumPriorityLineStreak.setColor(Color.parseColor("#800080"));
                    mediumPriorityLineStreak.setCircleColor(Color.parseColor("#800080"));

                    LineDataSet lowPriorityLineStreak = new LineDataSet(yLowAxisLabel, "Low Priority");
                    lowPriorityLineStreak.setColor(Color.parseColor("#11ff11"));
                    lowPriorityLineStreak.setCircleColor(Color.parseColor("#ff1111"));
                    lowPriorityLineStreak.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    lowPriorityLineStreak.setCubicIntensity(0.2f);
                    lowPriorityLineStreak.setLineWidth(4f);

                    LineDataSet totalPriorityLineStreak = new LineDataSet(yTotalAxisLabel, "Total Point");
                    totalPriorityLineStreak.setLineWidth(4f);

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(highPriorityLineStreak);
                    dataSets.add(mediumPriorityLineStreak);
                    dataSets.add(lowPriorityLineStreak);
                    dataSets.add(totalPriorityLineStreak);

                    LineData data = new LineData(dataSets);
//                    mpLineChart.setT
                    mpLineChart.getXAxis().setAxisMinimum(0f);
                    mpLineChart.setDescription(null);
//                    mpLineChart.getLegend().setEnabled(false);
                    mpLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    mpLineChart.getAxisLeft().setDrawGridLines(false);
                    mpLineChart.getAxisRight().setDrawGridLines(false);
                    mpLineChart.getXAxis().setDrawGridLines(false);
                    mpLineChart.getAxisLeft().setDrawAxisLine(false);
                    mpLineChart.setData(data);
                    mpLineChart.invalidate();;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(streak_history.this, "Error while retrieving streak history", Toast.LENGTH_SHORT).show();
            }
        });

    }



}