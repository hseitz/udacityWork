package com.harrisonseitz.guide2seattle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by harrisonseitz on 6/13/17.
 */

public class LandmarksActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list);

        final ArrayList<Location> locations = new ArrayList<Location>();
        // Landmarks will have Name, Image, Description, Hours, and Address. No Reserve String.
        // Space Needle
        locations.add(new Location(getString(R.string.loc_name_space_needle),
                getString(R.string.loc_desc_space_needle),
                R.drawable.spaceneedle,
                getString(R.string.loc_hours_space_needle),
                getString(R.string.loc_addr_space_needle)));
        // Pike Place Market
        locations.add(new Location(getString(R.string.loc_name_pike_place),
                getString(R.string.loc_desc_pike_place),
                R.drawable.pikeplace,
                getString(R.string.loc_hours_pike_place),
                getString(R.string.loc_addr_pike_place)));
        // Chihuly Garden
        locations.add(new Location(getString(R.string.loc_name_chihuly),
                getString(R.string.loc_desc_chihuly),
                R.drawable.chihuly,
                getString(R.string.loc_hours_chihuly),
                getString(R.string.loc_addr_chihuly)));
        // Fremont Troll
        locations.add(new Location(getString(R.string.loc_name_troll),
                getString(R.string.loc_desc_troll),
                R.drawable.troll,
                getString(R.string.loc_hours_troll),
                getString(R.string.loc_addr_troll)));

        LocationAdapter adapter = new LocationAdapter(this, locations);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
}
