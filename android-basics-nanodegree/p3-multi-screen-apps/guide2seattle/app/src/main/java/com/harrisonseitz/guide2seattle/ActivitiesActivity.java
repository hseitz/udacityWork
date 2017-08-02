package com.harrisonseitz.guide2seattle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by harrisonseitz on 6/13/17.
 */

public class ActivitiesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list);

        final ArrayList<Location> locations = new ArrayList<Location>();

        // Activities (e.g. Tours, structured events) will contain all fields + reservation strings
        // Seattle in One Day Tour
        locations.add(new Location(getString(R.string.loc_name_si1d),
                getString(R.string.loc_desc_si1d),
                R.drawable.si1d,
                getString(R.string.loc_hours_si1d),
                getString(R.string.loc_addr_si1d),
                getString(R.string.loc_res_si1d)));
        // Great Wheel
        locations.add(new Location(getString(R.string.loc_name_gw),
                getString(R.string.loc_desc_gw),
                R.drawable.gw,
                getString(R.string.loc_hours_gw),
                getString(R.string.loc_addr_gw),
                getString(R.string.loc_res_gw)));
        // Pacific Science Center
        locations.add(new Location(getString(R.string.loc_name_psc),
                getString(R.string.loc_desc_psc),
                R.drawable.psc,
                getString(R.string.loc_hours_psc),
                getString(R.string.loc_addr_psc),
                getString(R.string.loc_res_psc)));
        // Museum of Flight
        locations.add(new Location(getString(R.string.loc_name_mof),
                getString(R.string.loc_desc_mof),
                R.drawable.mof,
                getString(R.string.loc_hours_mof),
                getString(R.string.loc_addr_mof),
                getString(R.string.loc_res_mof)));

        LocationAdapter adapter = new LocationAdapter(this, locations);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
}
