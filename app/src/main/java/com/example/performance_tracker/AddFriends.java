package com.example.performance_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class AddFriends extends AppCompatActivity {

    // Button
    private Button mainMenu;
    private Button search;

    // Edit Text for friend search
    private EditText findFriendsEmail;

    // Get User Credentials
    private FirebaseUser loggedInUser;
    private DatabaseReference reference;
    private String loggedInUserId;

    // for populating the friendsList
    private ListView friendsList;
    private ArrayList<String> friendsName;
    private ArrayList<String> friendsUid;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> arrayAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        // for the loggedInUser
        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        loggedInUserId = loggedInUser.getUid();

        // for getting text
        findFriendsEmail = (EditText) findViewById(R.id.enter_email_of_friends);


        // For getting the friendsList
        friendsList= (ListView) findViewById(R.id.friends_list_view);
        this.friendsName = new ArrayList<String >();
        this.friendsUid = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,friendsName);
        arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,friendsUid);
        friendsList.setAdapter(arrayAdapter);

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent n = new Intent(getApplicationContext(), view_friend_streak.class);
                n.putExtra("userId",friendsUid.get(position));
                startActivity(n);
            }
        });



        Intent intent = getIntent();
        HashMap<String, String> friendsList = (HashMap<String, String>) intent.getSerializableExtra("friendsList");

        if (friendsList!=null){
            for (Map.Entry<String,String>userFriend: friendsList.entrySet()){
                friendsName.add(userFriend.getValue());
                friendsUid.add(userFriend.getKey());
            }
            arrayAdapter.notifyDataSetChanged();
        }






        // for adding person to your friends list0
        search = (Button) findViewById(R.id.search_friends);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addFriendEmail =findFriendsEmail.getText().toString().trim();
                if (addFriendEmail.isEmpty()){
                    findFriendsEmail.setError("Enter the email please");
                    findFriendsEmail.requestFocus();
                    return;
                }
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshots) {
                        User userProfile = snapshots.child(loggedInUserId).getValue(User.class);
                        for (DataSnapshot snapShot: snapshots.getChildren()){
                            User user = snapShot.getValue(User.class);
                            if (user.emailAddress.toLowerCase().equals(addFriendEmail.toLowerCase()) && !(user.emailAddress.toLowerCase().equals(userProfile.emailAddress.toLowerCase()))){
                                Hashtable<String, String> friends = new Hashtable<String, String>();
                                reference.child(snapShot.getKey()).child("friends").child(loggedInUserId).setValue(userProfile.firstName.concat(" ").concat(userProfile.lastName));
                                reference.child(loggedInUserId).child("friends").child(snapShot.getKey()).setValue(user.firstName.concat(" ").concat(user.lastName));
                                friendsName.add(user.firstName.concat(" ").concat(user.lastName));
                                arrayAdapter.notifyDataSetChanged();
                                Toast.makeText(AddFriends.this, "Added to your friends list!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddFriends.this, "Couldn't find the friend - please ask them to create a new account", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        // go back
        mainMenu = (Button) findViewById(R.id.exit_friends);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}