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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import templates_and_keys.Class;
import templates_and_keys.Keys;
import templates_and_keys.Priority;
import templates_and_keys.Task;
import templates_and_keys.User;

public class AddClassFragment extends Fragment {
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_class, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // getting the data from the Profile Activity as a bundle
        Bundle bundle = getArguments();

        // this part connects to the firebase database
        FirebaseUser loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        String loggedInUserId = loggedInUser.getUid();
        DocumentReference userProfile = FirebaseFirestore.getInstance().document(Keys.USERS.concat("/").concat(loggedInUserId));

        // for accessing the class list and updating the classes listview
        ListView newTaskClassRelated = view.findViewById(R.id.classes_list);
        ArrayList<Class> classesName = new ArrayList<>();
        ArrayAdapter<Class> classNameAdapter = new ArrayAdapter<Class>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, classesName);
        newTaskClassRelated.setAdapter(classNameAdapter);

        if (bundle != null) {
            user = bundle.getParcelable(Keys.BUNDLE_USER);
            HashMap<String, Class> classes = user.classes;
            if (classes != null) {
                classesName.addAll(classes.values());
                classNameAdapter.notifyDataSetChanged();
            }
        }

        // for manual add class
        EditText className = view.findViewById(R.id.canvas_class);
        Button addClass = view.findViewById(R.id.add_class);
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classNameValue = className.getText().toString().trim();
                if (classNameValue.isEmpty()) {
                    className.setError("Class Name shouldn't be empty");
                    return;
                }
                Class userClass = new Class(className.getText().toString().trim());
                userProfile.update(Keys.CLASS_LISTS_KEY.concat(".").concat(userClass.classUid), userClass);
                classesName.add(userClass);
                classNameAdapter.notifyDataSetChanged();
            }
        });

        Button importClass = view.findViewById(R.id.import_class_from_canvas);
        importClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.canvasAPIKey==null) {
                    // create a new intent to show the pop up window: Call the InsertCanvasAPIKey class
                    openLauchBar();
                } else {
                    // this part finds all the classes information from CANVAS using the Canvas REST API
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    JsonArrayRequest getAllCanvasClassesRequest = new JsonArrayRequest(Request.Method.GET, Keys.CANVAS_CLASS_JSON_URL, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String className = jsonObject.getString("name");
                                    String classUid = jsonObject.getString("id");
                                    String classUUid = jsonObject.getString("uuid");
                                    Class newCanvasClass = new Class(className, classUUid, classUid);
                                    userProfile.update(Keys.CLASS_LISTS_KEY.concat(".").concat(classUUid), newCanvasClass);
                                    if (!classesName.contains(newCanvasClass)) {
                                        user.classes.put(classUUid, newCanvasClass);
                                        classesName.add(newCanvasClass);
                                        classNameAdapter.notifyDataSetChanged();
                                        Toast.makeText(getContext(), className, Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("Content-Type", "application/json");
                            params.put("Authorization", "Bearer " + user.canvasAPIKey);
                            return params;
                        }
                    };

                    // first add the request to the queue that is processed in separate thread using Volley
                    queue.add(getAllCanvasClassesRequest);


                    // now we need to get all the assignment from that class: so we need to send another GET request
                    if (!user.classes.isEmpty()) {
                        for (Class canvasClass : user.classes.values()) {
                            JsonArrayRequest assignmentRequest = new JsonArrayRequest(Request.Method.GET, Keys.GET_CLASS_ASSIGNMENTS_URL(canvasClass.courseId), null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            JSONObject jsonObject = response.getJSONObject(i);
                                            // this part captures whether the assignment has been already submitted or not
                                            Boolean assignmentStatus = jsonObject.getBoolean("has_submitted_submissions");

                                            if (jsonObject != null && !assignmentStatus) {
                                                // see the CANVAS LMS to understand why we use Hardcoded string
                                                String taskName = jsonObject.getString("name");
                                                Priority priorityLevel; // generally we put class stuffs as high priority task
                                                if (jsonObject.getBoolean("is_quiz_assignment"))
                                                    priorityLevel = Priority.HIGH;
                                                else
                                                    priorityLevel = Priority.MEDIUM;

                                                String taskDueDate = jsonObject.getString("due_at").split("T")[0];
                                                if (taskDueDate == null || taskDueDate == "null") {
                                                    taskDueDate = "No Deadline";
                                                    priorityLevel = Priority.LOW;
                                                }
                                                String classUid = jsonObject.getString("course_id");

                                                Task newTask = new Task(taskName, taskDueDate, priorityLevel, canvasClass.classUid);

                                                userProfile.update(Keys.TASK_LISTS.concat(".").concat(taskDueDate).concat(".").concat(newTask.uuid), newTask);

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("Content-Type", "application/json");
                                    params.put("Authorization", "Bearer " + user.canvasAPIKey);
                                    return params;
                                }
                            };
                            queue.add(assignmentRequest);
                        }
                    }
                }
            }


        });



        // on item listener
        newTaskClassRelated.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                Class selectedClass = (Class) parent.getSelectedItem();
                String selectedClassUid = selectedClass.classUid;
                Toast.makeText(getContext(), selectedClass.className + " ->" + selectedClass.classUid, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        String canvasKeygen = result.getData().getStringExtra(Keys.CLASS_CANVAS_API_KEY);
                        // invokes the firebase API to update the completed task
                        FirebaseUser loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
                        String loggedInUserId = loggedInUser.getUid();
                        String path = Keys.USERS + "/" + loggedInUserId;
                        user.canvasAPIKey = canvasKeygen;
                        DocumentReference userProfile = FirebaseFirestore.getInstance().document(path);
                        userProfile.update(Keys.CLASS_CANVAS_API_KEY, canvasKeygen);

                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(getContext(), "Task Update Cancelled", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );
    private void openLauchBar() {
        Intent insertAPIKeyActivity = new Intent(getContext(), InsertCanvasAPIKey.class);
        activityLauncher.launch(insertAPIKeyActivity); // launches the activity through the custom activity launcher
    }


}

