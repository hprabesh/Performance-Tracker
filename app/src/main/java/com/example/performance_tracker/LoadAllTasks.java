package com.example.performance_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import classes_template.Task;

public class LoadAllTasks extends AppCompatActivity {
    // for populating the task list
    private TextView taskDate;
    private ListView tasks;
    private ArrayList<String> values;
    private ArrayList<String> taskUid;
    private String taskDateToBeCompleted;
    private HashMap<String,HashMap<String, Task>> taskLists;
    private TreeMap<String, HashMap<String, Task>> sortedTaskLists;
    private HashMap<String, Task> task;
    private ArrayAdapter<String> arrayAdapter, arrayAdapter2;


    // pointer to the task
    private Integer positions = 0;

    // Button
    private Button next;
    private Button previous;

    private void updateListView(Integer position){

        this.tasks = (ListView) findViewById(R.id.first_day_task_list);
        this.taskDate = (TextView) findViewById(R.id.first_day_date);
        this.values = new ArrayList<String>();
        this.taskUid = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,values);
        arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskUid);

        tasks.setAdapter(arrayAdapter);


        if (sortedTaskLists!=null && (position>=0)){
            String[] date = sortedTaskLists.keySet().toArray(new String[sortedTaskLists.size()]);
            taskDateToBeCompleted = date[position];
            taskDate.setText(taskDateToBeCompleted);
            task =sortedTaskLists.get(date[position]);
            for (Map.Entry<String,Task> set: task.entrySet()){
                values.add(set.getValue().getTaskName());
                taskUid.add(set.getValue().uuid);
            }
            arrayAdapter.notifyDataSetChanged();
        }

        tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent n = new Intent(getApplicationContext(), MarkCompleteTask.class);
                String taskId = taskUid.get(position);
                positions = position;
                Bundle bundle = new Bundle();
                bundle.putString("taskId", taskId);
                bundle.putString("taskDate", taskDateToBeCompleted);
                n.putExtras(bundle);
//                values.remove(position);
//                taskUid.remove(position);
//                arrayAdapter.notifyDataSetChanged();
                startActivityForResult(n,1);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_all_tasks);
        Intent intent = getIntent();
        taskLists =(HashMap<String, HashMap<String, Task>> ) intent.getSerializableExtra("taskList");
        sortedTaskLists = new TreeMap<String, HashMap<String, Task>>(taskLists);


        next = (Button) findViewById(R.id.next_task_view);
        previous = (Button) findViewById(R.id.previous_task_view);
        updateListView(positions);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positions++;
                if (positions<(sortedTaskLists.size())){
                    updateListView(positions);
                } else {
                    Toast.makeText(LoadAllTasks.this, "EOF reached", Toast.LENGTH_SHORT).show();
                    positions = 0;
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positions--;
                if (positions>=0){
                    updateListView(positions);
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 ){
            HashMap<String,Task> taskRemoved = sortedTaskLists.get(taskDateToBeCompleted);
            taskRemoved.remove(taskUid.get(positions));
            sortedTaskLists.put(taskDateToBeCompleted,taskRemoved);
            this.updateListView(positions);
        }
    }
}