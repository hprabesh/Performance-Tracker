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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import templates_and_keys.Keys;
import templates_and_keys.Priority;
import templates_and_keys.Task;
import templates_and_keys.User;

public class HomeProfileFragment extends Fragment {
    private SimpleDateFormat currentDate;
    private HashMap<String, HashMap<String, Task>> taskLists;
    private User user;
    private View view;
    private TextView streakPoint;
    private TextView userName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view = view;
        Bundle bundle = getArguments();
        userName =(TextView) view.findViewById(R.id.first_name);
        streakPoint = (TextView)view.findViewById(R.id.streak);


        // to get the user information from the main profile
        if (bundle != null) {
            user = bundle.getParcelable(Keys.BUNDLE_USER);

            String userMessage = "Hi ".concat(user.firstName).concat("!");
            userName.setText(userMessage);

            // this method updates the streak point displayed on CardView
            updateStreakPointValue();


            String taskDueDate = user.lastLoggedInDate; // since today is the due date, and the last loggedInDate is today, we can just use it instead of creating a new class
            taskLists = user.taskLists;

            // for updating the  listview
            updateTaskListView(taskLists, taskDueDate);

            // to view the streak history
            TextView loadStreakHistory = view.findViewById(R.id.load_streak_history);
            loadStreakHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment viewStreakHistory = new StreakHistory();

                    Bundle bundle = new Bundle(); // to pass the user references
                    bundle.putParcelable(Keys.BUNDLE_USER, user);
                    viewStreakHistory.setArguments(bundle); // this is to do so that when one fragment updates the data

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,viewStreakHistory,"View Streak History");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            // to load all the task
            Button loadAllTasks = view.findViewById(R.id.view_more_task);
            loadAllTasks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment loadAllTasksFragmentLauncher = new LoadAllTasksFragment();

                    Bundle bundle = new Bundle(); // to pass the user references
                    bundle.putParcelable(Keys.BUNDLE_USER, user);
                    bundle.putString(Keys.TASK_OBJECT_DATE,taskDueDate);
                    loadAllTasksFragmentLauncher.setArguments(bundle); // this is to do so that when one fragment updates the data

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,loadAllTasksFragmentLauncher,"Load All Tasks");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

        }


    }

    private void updateStreakPointValue() {
        String streakValues = user.userStreaks.totalStreak().toString();
        streakPoint.setText(streakValues);
    }

    private void updateTaskListView(HashMap<String, HashMap<String, Task>> taskLists, String taskDueDate) {
        // for filling the task list
        ListView todayTask = view.findViewById(R.id.task_list);
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
                if (user.taskLists.get(taskDueDate).get(taskId).getClassRelated() != null) {
                    String className =user.classes.get(user.taskLists.get(taskDueDate).get(taskId).getClassRelated()).className;
                    loadTaskObject.putExtra(Keys.CLASS_NAME_OBJECT, className);
                }
                loadTaskObject.putExtra(Keys.TASK_OBJECT, taskLists.get(taskDueDate).get(taskId)); // pass the task object to the class LoadTaskObject
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
                    // if the task is marked as completed, the return value is RESULT_OK = -1 from the class LoadTask Object
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        //if the task is mark completed, then get all the task information from the intent
                        String uuid = result.getData().getStringExtra(Keys.TASK_OBJECT_ID);
                        String taskDueDate = result.getData().getStringExtra(Keys.TASK_OBJECT_DATE);

                        Task completedTask = taskLists.get(taskDueDate).get(uuid);
                        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String completedDate = currentDate.format(new Date());

                        if (user.streakHistory.containsKey(completedDate))
                            user.userStreaks = user.streakHistory.get(completedDate);


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

                        // update the streak point on card view as well
                        updateStreakPointValue();


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
