package com.example.performance_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

public class AddClass extends AppCompatActivity {
    private Button exitClass;

    private EditText getAPIKey;
    private TextView viewAPIKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        getAPIKey = (EditText) findViewById(R.id.find_class_search);
        String apiKey= getAPIKey.getText().toString().trim();

        HashMap<String, String> header = new HashMap<String,String>();
        header.put("Authorization", apiKey);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://uta.instructure.com/api/v1/courses?per_page=100&with_enrollments=1";





        exitClass= (Button) findViewById(R.id.exit_class);
        exitClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}