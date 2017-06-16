package com.harrisonseitz.guide2seattle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by harrisonseitz on 6/13/17.
 */

public class DiningActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list);

        final ArrayList<Location> locations = new ArrayList<Location>();
        // Dining Locations will have all fields and *sometimes* reservation link strings
        // Dick's
        locations.add(new Location(getString(R.string.loc_name_dicks),
                getString(R.string.loc_desc_dicks),
                R.drawable.dicks,
                getString(R.string.loc_hours_dicks),
                getString(R.string.loc_addr_dicks)));
        // El Gaucho
        locations.add(new Location(getString(R.string.loc_name_eg),
                getString(R.string.loc_desc_eg),
                R.drawable.eg,
                getString(R.string.loc_hours_eg),
                getString(R.string.loc_addr_eg),
                getString(R.string.loc_res_eg)));
        // Salumi
        locations.add(new Location(getString(R.string.loc_name_salumi),
                getString(R.string.loc_desc_salumi),
                R.drawable.salumi,
                getString(R.string.loc_hours_salumi),
                getString(R.string.loc_addr_salumi)));
        // The Pink Door
        locations.add(new Location(getString(R.string.loc_name_pd),
                getString(R.string.loc_desc_pd),
                R.drawable.pd,
                getString(R.string.loc_hours_pd),
                getString(R.string.loc_addr_pd),
                getString(R.string.loc_res_pd)));
        LocationAdapter adapter = new LocationAdapter(this, locations);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
}
