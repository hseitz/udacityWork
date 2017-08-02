package com.harrisonseitz.beetsme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        // setup the buttons
        Button nowPlaying = (Button) findViewById(R.id.nowPlayingButton);
        Button explore = (Button) findViewById(R.id.exploreButton);
        Button library = (Button) findViewById(R.id.libraryButton);
        // create clickListeners for the buttons
        nowPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nowPlayingIntent = new Intent(LiveActivity.this, MainActivity.class);
                startActivity(nowPlayingIntent);
            }
        });
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exploreIntent = new Intent(LiveActivity.this, ExploreActivity.class);
                startActivity(exploreIntent);
            }
        });
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent libraryIntent = new Intent(LiveActivity.this, LibraryActivity.class);
                startActivity(libraryIntent);
            }
        });
    }
}
