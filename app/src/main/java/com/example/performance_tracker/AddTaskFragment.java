package com.example.performance_tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import templates_and_keys.Class;
import templates_and_keys.Keys;
import templates_and_keys.Priority;
import templates_and_keys.Task;
import templates_and_keys.User;

public class AddTaskFragment extends Fragment {
    public String selectedClassUid = null; // this stores the class UID value -> if the task is part of the class

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        // All button declaration goes in here
        Button addTask = view.findViewById(R.id.add_new_task);

        // All Task related content goes in here
        EditText newTask = view.findViewById(R.id.new_task);

        Spinner newTaskPriority = view.findViewById(R.id.new_task_priority);
        Priority[] items = new Priority[]{Priority.NONE, Priority.HIGH, Priority.MEDIUM, Priority.LOW};
        ArrayAdapter<Priority> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
        newTaskPriority.setAdapter(adapter);

        // For Spinner to add class if the task is related to the class
        Spinner newTaskClassRelated = view.findViewById(R.id.new_task_class_related);
        // null class as a spinner option
        Class nullClass = new Class(null,null,null);
        ArrayList<Class> classesName = new ArrayList<>();
        classesName.add(nullClass);
        ArrayAdapter<Class> classNameAdapter = new ArrayAdapter<Class>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,classesName);
        newTaskClassRelated.setAdapter(classNameAdapter);

        if (bundle!=null){
            User user = bundle.getParcelable(Keys.BUNDLE_USER);
            HashMap<String,Class> classes = user.classes;
            classesName.addAll(classes.values());
            classNameAdapter.notifyDataSetChanged();
        }
        // on item listener
        newTaskClassRelated.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Class selectedClass = (Class) parent.getSelectedItem();
                selectedClassUid = selectedClass.classUid;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedClassUid = null;
            }
        });


        DatePicker newTaskDeadline = view.findViewById(R.id.new_task_deadline);

        FirebaseUser loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        String loggedInUserId = loggedInUser.getUid();
        DocumentReference userProfile = FirebaseFirestore.getInstance().document(Keys.USERS.concat("/").concat(loggedInUserId));
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = newTask.getText().toString().trim();
                Priority priorityLevel = Priority.valueOf(newTaskPriority.getSelectedItem().toString());

                // Get Deadline
                String month;
                String day;
                if ((newTaskDeadline.getMonth() + 1) < 10) {
                    month = "0".concat(String.valueOf(newTaskDeadline.getMonth() + 1));
                } else {
                    month = String.valueOf(newTaskDeadline.getMonth() + 1);
                }
                if ((newTaskDeadline.getDayOfMonth()) < 10) {
                    day = "0".concat(String.valueOf(newTaskDeadline.getDayOfMonth()));
                } else {
                    day = String.valueOf(newTaskDeadline.getDayOfMonth());
                }
                String deadline = String.valueOf(newTaskDeadline.getYear()).concat("-").concat(month).concat("-").concat(day);
                if (deadline.isEmpty()) {
                    newTask.setError("Required Proper Date");
                    newTaskDeadline.requestFocus();
                    return;
                }

                // get task name
                if (taskName.isEmpty()) {
                    newTask.setError("Required task Name");
                    newTask.requestFocus();
                    return;
                }

                // get priority level
                if (priorityLevel == Priority.NONE) {
                    newTask.setError("Required priority level");
                    newTaskPriority.requestFocus();
                    return;
                }


                String uuid = UUID.randomUUID().toString().replace("-", "");
                Task userTask;
                if (selectedClassUid!=null){
                    userTask = new Task(uuid,taskName,deadline, priorityLevel,selectedClassUid);
                } else {
                    userTask = new Task(uuid, taskName, deadline, priorityLevel);
                }
                userProfile.update(Keys.TASK_LISTS.concat(".").concat(deadline).concat(".").concat(uuid), userTask);
            }
        });
        return view;
    }
}
