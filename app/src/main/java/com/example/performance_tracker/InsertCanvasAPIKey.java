package com.example.performance_tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import templates_and_keys.Keys;

public class InsertCanvasAPIKey extends Activity {

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_insert_api_class);

        // set the display width for the pop up window
        DisplayMetrics dm = new DisplayMetrics();

        // get the display parameter
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // display parameters
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // Edit Text params
        EditText canvasAPIKey = findViewById(R.id.api_key_edit_text);

        Button insertApiKey = findViewById(R.id.api_key_insert_button);
        insertApiKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents = new Intent();
                String canvasAPIKeygen = canvasAPIKey.getText().toString().trim();
                if (!canvasAPIKeygen.isEmpty()) {
                    intents.putExtra(Keys.CLASS_CANVAS_API_KEY, canvasAPIKeygen);
                    setResult(Activity.RESULT_OK, intents);
                    finish();
                }
            }
        });
        Button cancelButton = findViewById(R.id.api_key_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        getWindow().setLayout((int)(width*0.8), (int) (height*0.4));
    }
}
