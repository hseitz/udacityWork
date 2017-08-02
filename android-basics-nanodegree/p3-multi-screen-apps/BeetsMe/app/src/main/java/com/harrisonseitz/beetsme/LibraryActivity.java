package com.harrisonseitz.beetsme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        // setup the buttons
        Button nowPlaying = (Button) findViewById(R.id.nowPlayingButton);
        Button live = (Button) findViewById(R.id.liveButton);
        Button explore = (Button) findViewById(R.id.exploreButton);
        // create clickListeners for the buttons
        nowPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nowPlayingIntent = new Intent(LibraryActivity.this, MainActivity.class);
                startActivity(nowPlayingIntent);
            }
        });
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent liveIntent = new Intent(LibraryActivity.this, LiveActivity.class);
                startActivity(liveIntent);
            }
        });
        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exploreIntent = new Intent(LibraryActivity.this, ExploreActivity.class);
                startActivity(exploreIntent);
            }
        });
    }
}
