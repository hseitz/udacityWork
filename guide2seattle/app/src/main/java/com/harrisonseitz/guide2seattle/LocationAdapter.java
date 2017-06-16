package com.harrisonseitz.guide2seattle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by harrisonseitz on 6/13/17.
 */

public class LocationAdapter extends ArrayAdapter<Location> {

    public LocationAdapter(Activity context, ArrayList<Location> locations) {
        super(context, 0, locations);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.location_item,
                    parent, false);
        }

        final Location currLocation = getItem(position);
        // Set Location name
        TextView locationNameTextView = (TextView) listItemView.findViewById(R.id.location_name);
        locationNameTextView.setText(currLocation.getLocationName());
        // Set Location Image drawable
        ImageView locationImageView = (ImageView) listItemView.findViewById(R.id.location_image);
        locationImageView.setImageResource(currLocation.getLocationImage());
        // Set Location Description
        TextView locationDescTextView =
                (TextView) listItemView.findViewById(R.id.location_description);
        locationDescTextView.setText(currLocation.getLocationDescription());
        // Set Hours Open for Location
        TextView locationHoursTextView = (TextView) listItemView.findViewById(R.id.location_hours);
        locationHoursTextView.setText(currLocation.getLocationHoursOpen());
        // Set Address for Location
        TextView locationAddress = (TextView) listItemView.findViewById(R.id.location_address);
        locationAddress.setText(currLocation.getLocationAddress());
        // If no reserve URL, set visibility on reservation button to GONE
        TextView reserveButton = (TextView) listItemView.findViewById(R.id.reserve_button);
        if (currLocation.getLocationReserveString() == null) {
            reserveButton.setVisibility(View.GONE);
        }
        else {
            reserveButton.setVisibility(View.VISIBLE);
        }

        TextView directionsButton = (TextView) listItemView.findViewById(R.id.directions_button);
        // Implementing click listeners for Reserve/Directions buttons here
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri geoLocation = Uri.parse("geo:0,0?q="
                        + Uri.encode(currLocation.getLocationAddress()));
                intent.setData(geoLocation);
                if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(intent);
                }
            }
        });
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri resPage = Uri.parse(currLocation.getLocationReserveString());
                Intent intent = new Intent(Intent.ACTION_VIEW, resPage);
                if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(intent);
                }

            }
        });
        return listItemView;
    }
}
