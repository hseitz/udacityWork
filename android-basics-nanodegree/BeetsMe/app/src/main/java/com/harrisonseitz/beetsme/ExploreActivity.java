package com.harrisonseitz.beetsme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExploreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        // setup the buttons
        Button nowPlaying = (Button) findViewById(R.id.nowPlayingButton);
        Button live = (Button) findViewById(R.id.liveButton);
        Button library = (Button) findViewById(R.id.libraryButton);
        // create clickListeners for the buttons
        nowPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nowPlayingIntent = new Intent(ExploreActivity.this, MainActivity.class);
                startActivity(nowPlayingIntent);
            }
        });
        live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent liveIntent = new Intent(ExploreActivity.this, LiveActivity.class);
                startActivity(liveIntent);
            }
        });
        library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent libraryIntent = new Intent(ExploreActivity.this, LibraryActivity.class);
                startActivity(libraryIntent);
            }
        });
    }
}
