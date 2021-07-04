package com.example.performance_tracker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import templates_and_keys.Keys;
import templates_and_keys.Streak;
import templates_and_keys.User;

public class StreakHistory extends Fragment {
    // user streak part goes in here
    private TextView highPriorityStreakPoint;
    private TextView mediumPriorityStreakPoint;
    private TextView lowPriorityStreakPoint;
    private TextView totalPriorityStreakPoint;
    private View view;


    //Graphing
    LineChart mpLineChart;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_streak_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view = view;
        Bundle bundle = getArguments();

        // streakpoint
        highPriorityStreakPoint = view.findViewById(R.id.high_priority_streak_point);
        mediumPriorityStreakPoint = view.findViewById(R.id.medium_priority_streak_point);
        lowPriorityStreakPoint = view.findViewById(R.id.low_priority_streak_point);
        totalPriorityStreakPoint = view.findViewById(R.id.total_priority_streak_point);

        // graphing
        mpLineChart = view.findViewById(R.id.streak_history_plot);
        if (bundle != null) {
            User userProfile = bundle.getParcelable(Keys.BUNDLE_USER);
            Streak userStreak;
            HashMap<String, Streak> unSortedStreakHistory;

            if (userProfile != null) {
                userStreak = userProfile.userStreaks;
                String highPriorStreakPoint = userStreak.getHighPriorityStreak().toString();
                String mediumPriorStreakPoint = userStreak.getMediumPriorityStreak().toString();
                String lowPriorStreakPoint = userStreak.getLowPriorityStreak().toString();
                String totalPriorStreakPoint = userStreak.totalStreak().toString();
                if (userStreak.getHighPriorityStreak() == 0)
                    view.findViewById(R.id.fire_streak_sign_1).setVisibility(View.INVISIBLE);
                if (userStreak.getMediumPriorityStreak() == 0)
                    view.findViewById(R.id.fire_streak_sign_2).setVisibility(View.INVISIBLE);
                if (userStreak.getLowPriorityStreak() == 0)
                    view.findViewById(R.id.fire_streak_sign_3).setVisibility(View.INVISIBLE);

                highPriorityStreakPoint.setText(highPriorStreakPoint);
                mediumPriorityStreakPoint.setText(mediumPriorStreakPoint);
                lowPriorityStreakPoint.setText(lowPriorStreakPoint);
                totalPriorityStreakPoint.setText(totalPriorStreakPoint);

                unSortedStreakHistory = userProfile.streakHistory;
                Map<String, Streak> streakHistory = new TreeMap<>(unSortedStreakHistory);
                // Hashmap <String, Streak>
                final ArrayList<String> xAxisLabel = new ArrayList<>();
                final ArrayList<Entry> yHighAxisLabel = new ArrayList<>();
                final ArrayList<Entry> yMediumAxisLabel = new ArrayList<>();
                final ArrayList<Entry> yLowAxisLabel = new ArrayList<>();
                final ArrayList<Entry> yTotalAxisLabel = new ArrayList<>();
                Integer xAxisPosition = 0;
                for (Map.Entry<String, Streak> entry : streakHistory.entrySet()) {
                    String[] dateArray = entry.getKey().split("-", 4);
                    String date = dateArray[1].concat("-").concat(dateArray[2]);
                    xAxisLabel.add(date);
                    Streak dateStreak = entry.getValue();
                    yHighAxisLabel.add(new Entry(xAxisPosition, dateStreak.getHighPriorityStreak()));
                    yMediumAxisLabel.add(new Entry(xAxisPosition, dateStreak.getMediumPriorityStreak()));
                    yLowAxisLabel.add(new Entry(xAxisPosition, dateStreak.getLowPriorityStreak()));
                    yTotalAxisLabel.add(new Entry(xAxisPosition, dateStreak.totalStreak()));
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
                mpLineChart.invalidate();
                ;
            }
        }

    }


}
