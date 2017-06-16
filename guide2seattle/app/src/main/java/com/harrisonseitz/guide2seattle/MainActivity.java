package com.harrisonseitz.guide2seattle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set On-click listeners for button categories
        TextView landmarks = (TextView) findViewById(R.id.landmarks);
        landmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent landmarksIntent = new Intent(MainActivity.this, LandmarksActivity.class);
                startActivity(landmarksIntent);
            }
        });

        TextView dining = (TextView) findViewById(R.id.dining);
        dining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent diningIntent = new Intent(MainActivity.this, DiningActivity.class);
                startActivity(diningIntent);
            }
        });

        TextView activities = (TextView) findViewById(R.id.activities);
        activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent activitiesIntent = new Intent(MainActivity.this, ActivitiesActivity.class);
                startActivity(activitiesIntent);
            }
        });

        TextView outdoors = (TextView) findViewById(R.id.outdoors);
        outdoors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent outdoorsIntent = new Intent(MainActivity.this, OutdoorsActivity.class);
                startActivity(outdoorsIntent);
            }
        });
    }
}
