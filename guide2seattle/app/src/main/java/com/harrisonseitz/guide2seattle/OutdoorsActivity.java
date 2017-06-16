package com.harrisonseitz.guide2seattle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by harrisonseitz on 6/13/17.
 */

public class OutdoorsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list);

        final ArrayList<Location> locations = new ArrayList<Location>();
        // Outdoors Locations will have all fields except reservation strings
        // Snoqualmie Falls
        locations.add(new Location(getString(R.string.loc_name_snofalls),
                getString(R.string.loc_desc_snofalls),
                R.drawable.snofalls,
                getString(R.string.loc_hours_snofalls),
                getString(R.string.loc_addr_snofalls)
        ));
        // North Cascades
        locations.add(new Location(getString(R.string.loc_name_ncascades),
                getString(R.string.loc_desc_ncascades),
                R.drawable.ncascades,
                getString(R.string.loc_hours_ncascades),
                getString(R.string.loc_addr_ncascades)

        ));
        // Gasworks Park
        locations.add(new Location(getString(R.string.loc_name_gwp),
                getString(R.string.loc_desc_gwp),
                R.drawable.gwp,
                getString(R.string.loc_hours_gwp),
                getString(R.string.loc_addr_gwp)
        ));
        LocationAdapter adapter = new LocationAdapter(this, locations);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
}
