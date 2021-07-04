package com.example.performance_tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import templates_and_keys.Keys;
import templates_and_keys.Task;

public class LoadTaskObject extends Activity {

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_load_task);

        // set the display width for the pop up window
        DisplayMetrics dm = new DisplayMetrics();

        // get the display parameter
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // display parameters
        int width = dm.widthPixels;
        int height = dm.heightPixels;


        // get the passed data from the main activity: TASK OBJECT
        Intent loadTaskObject = getIntent();
        Task clickedTask = (Task) loadTaskObject.getSerializableExtra(Keys.TASK_OBJECT);


        // get all the task parameters: see the Class TASK template
        TextView loadTaskName = findViewById(R.id.load_task_name);
        TextView loadTaskDueDate = findViewById(R.id.load_task_due_date);
        TextView loadTaskPriorityLevel = findViewById(R.id.load_task_priority_level);
        TextView loadTaskClassRelated = findViewById(R.id.load_task_class_related);

        String taskId = clickedTask.uuid;
        loadTaskName.setText(clickedTask.getTaskName());
        loadTaskDueDate.setText(clickedTask.getTaskDueDate());
        loadTaskPriorityLevel.setText(clickedTask.getTaskPriority().toString());
        if (clickedTask.getClassRelated() != null) {
            String className = loadTaskObject.getStringExtra(Keys.CLASS_NAME_OBJECT);
            loadTaskClassRelated.setText(className);
        } else {
            findViewById(R.id.linear_layout_class_related).setVisibility(View.INVISIBLE);
        }


        Button markTaskCompleted = findViewById(R.id.load_task_mark_completed);
        markTaskCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents = new Intent();
                intents.putExtra(Keys.TASK_OBJECT_ID, taskId);
                intents.putExtra(Keys.TASK_OBJECT_DATE, clickedTask.getTaskDueDate());
                setResult(Activity.RESULT_OK, intents);
                finish();
            }
        });
        Button cancelButton = findViewById(R.id.load_task_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.4));
    }
}
