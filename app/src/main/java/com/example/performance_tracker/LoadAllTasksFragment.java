package com.example.performance_tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import templates_and_keys.Keys;
import templates_and_keys.Priority;
import templates_and_keys.Task;
import templates_and_keys.User;

public class LoadAllTasksFragment extends Fragment {

    private User user;
    private HashMap<String, HashMap<String, Task>> taskLists;
    private View view;

    private Button nextDate;
    private Button previousDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_load_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        Bundle bundle = getArguments();

        if (bundle != null) {
            user = bundle.getParcelable(Keys.BUNDLE_USER);
            taskLists = user.taskLists;

            //  load all the task is viewed on the main home pages
            String taskDueDate = bundle.getString(Keys.TASK_OBJECT_DATE);
            updateTaskListView(taskLists, taskDueDate);

            ArrayList<String> taskDueDates = new ArrayList<>(taskLists.keySet());
            Collections.sort(taskDueDates);
            final int[] currentPosition = {taskDueDates.indexOf(taskDueDate)};

            nextDate = (Button) view.findViewById(R.id.next_date);
            nextDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!taskLists.isEmpty()) {
                        currentPosition[0]++;
                        if (currentPosition[0] >= taskDueDates.size())
                            currentPosition[0] = 0;
                        updateTaskListView(taskLists, taskDueDates.get(currentPosition[0]));
                    }
                }
            });
            previousDate = (Button) view.findViewById(R.id.previous_date);
            previousDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!taskLists.isEmpty()) {
                        currentPosition[0]--;
                        if (currentPosition[0] < 0)
                            currentPosition[0] = taskDueDates.size() - 1;
                        updateTaskListView(taskLists, taskDueDates.get(currentPosition[0]));
                    }
                }
            });
        }


    }


    private void updateTaskListView(HashMap<String, HashMap<String, Task>> taskLists, String taskDueDate) {

        TextView todayDate = view.findViewById(R.id.load_all_task_date_header);
        todayDate.setText(taskDueDate);

        // for filling the task list
        ListView todayTask = view.findViewById(R.id.load_all_task_list_view);
        todayTask.setScrollContainer(false);

        // for today's task Adapter
        ArrayList<String> taskName = new ArrayList<>();
        ArrayList<String> taskUid = new ArrayList<>();
        ArrayAdapter<String> taskNameAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, taskName);
        ArrayAdapter<String> taskUidAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, taskUid);
        todayTask.setAdapter(taskNameAdapter);

        if (taskLists.containsKey(taskDueDate)) {
            taskNameAdapter.clear();
            taskUidAdapter.clear();
            HashMap<String, Task> currentTask = taskLists.get(taskDueDate);
            assert currentTask != null;
            for (Map.Entry<String, Task> set : currentTask.entrySet()) {
                // only add the non completed task :
                if (!set.getValue().getTaskStatus()) {
                    taskName.add(set.getValue().getTaskName());
                    taskUid.add(set.getValue().uuid);
                }
            }
        }

        taskNameAdapter.notifyDataSetChanged();
        taskUidAdapter.notifyDataSetChanged();


        // on today's task / item click listener
        todayTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create a new intent to show the pop up window: Call the LoadTaskObject class
                Intent loadTaskObject = new Intent(getContext(), LoadTaskObject.class);
                String taskId = taskUid.get(position); // captures the uid of the task object
                loadTaskObject.putExtra(Keys.TASK_OBJECT, taskLists.get(taskDueDate).get(taskId)); // pass the task object to the class LoadTaskObject
                if (user.taskLists.get(taskDueDate).get(taskId).getClassRelated() != null) {
                    String className =user.classes.get(user.taskLists.get(taskDueDate).get(taskId).getClassRelated()).className;
                    loadTaskObject.putExtra(Keys.CLASS_NAME_OBJECT, className);
                }
                activityLauncher.launch(loadTaskObject); // launches the activity through the custom activity launcher
            }
        });

    }

    // this activity launcher overrides if the task / assignment is marked completed in LoadTaskObject
    private final ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if the task is marked as completed, the return value is RESULT_OK = -1 from the class LoadTask Object
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        //if the task is mark completed, then get all the task information from the intent
                        String uuid = result.getData().getStringExtra(Keys.TASK_OBJECT_ID);
                        String taskDueDate = result.getData().getStringExtra(Keys.TASK_OBJECT_DATE);

                        Task completedTask = taskLists.get(taskDueDate).get(uuid);
                        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String completedDate = currentDate.format(new Date());


                        // update the taskLists with the completedTask and the listview as well
                        user.taskLists.get(taskDueDate).get(uuid)
                                .setTaskStatus(true);
                        user.taskLists.get(taskDueDate).get(uuid)
                                .setTaskCompletedDate(completedDate);
                        // first update the user streak
                        if (completedTask.getTaskPriority() == Priority.HIGH)
                            user.userStreaks.addHighStreak();
                        else if (completedTask.getTaskPriority() == Priority.MEDIUM)
                            user.userStreaks.addMediumStreak();
                        else
                            user.userStreaks.addLowStreak();

                        // second update user streak history
                        user.streakHistory.put(completedDate, user.userStreaks);


                        taskLists.get(taskDueDate).put(uuid, completedTask);
                        updateTaskListView(taskLists, taskDueDate);

                        // invokes the firebase API to update the completed task
                        FirebaseUser loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
                        String loggedInUserId = loggedInUser.getUid();
                        String path = Keys.USERS + "/" + loggedInUserId;
                        DocumentReference userProfile = FirebaseFirestore.getInstance().document(path);
                        userProfile.set(user);

                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(getContext(), "Task Update Cancelled", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );
}
