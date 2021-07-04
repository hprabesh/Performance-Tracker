package com.example.performance_tracker;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import templates_and_keys.Keys;
import templates_and_keys.User;

public class Profile extends AppCompatActivity {


    private DocumentReference userProfile;

    // User Object
    public User user;

    // Home Fragment: This fragment stores the current fragment: default is home
    private Fragment home;

    // from XML

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // for the loggedInUser
        // Database References to the Firebase
        FirebaseUser loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        String loggedInUserId = loggedInUser.getUid();
        userProfile = FirebaseFirestore.getInstance().document(Keys.USERS.concat("/").concat(loggedInUserId));

        // setting up the home (default) fragment when application is loaded
        home = new HomeProfileFragment();

        userProfile.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    user = value.toObject(User.class);
                    // update the last logged in session
                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    user.lastLoggedInDate = currentDate.format(new Date());
                    userProfile.update(
                            "lastLoggedInDate", user.lastLoggedInDate
                    );
                    // Updating the fragments
                    Bundle bundle = new Bundle(); // to pass the user references
                    bundle.putParcelable(Keys.BUNDLE_USER, user);
                    home.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
                } else {
                    Toast.makeText(Profile.this, "Document not found", Toast.LENGTH_LONG).show();
                }
            }
        });

        // for the navigation bar
        BottomNavigationView footerView = findViewById(R.id.footer_bar);
        footerView.setOnNavigationItemSelectedListener(footerListener);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener footerListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.fragment_home_page:
                    home = new HomeProfileFragment();
                    break;
                case R.id.fragment_add_task_page:
                    home = new AddTaskFragment();
                    break;
                case R.id.fragment_add_class_page:
                    home = new AddClassFragment();
                    break;
            }
            Bundle bundle = new Bundle(); // to pass the user references
            bundle.putParcelable(Keys.BUNDLE_USER, user);
            home.setArguments(bundle); // this is to do so that when one fragment updates the data
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    home).commit();
            return true;
        }
    };


}