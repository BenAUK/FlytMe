package com.example.benas.flytme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by benas on 04/06/2017.
 */

public class FlytMeAdapter extends ArrayAdapter<FlightDetail> {

    public FlytMeAdapter(Context context, ArrayList<FlightDetail> flights) {
        super(context, 0, flights);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FlightDetail flight = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.flight_results_list, parent, false);
        }
        // Lookup view for data population
        //TextView departureDate = (TextView) convertView.findViewById(R.id.departureDate);
        TextView arrivalDate = (TextView) convertView.findViewById(R.id.arrivalDate);
        TextView status = (TextView) convertView.findViewById(R.id.status);
        TextView route = (TextView) convertView.findViewById(R.id.route);
        // Populate the data into the template view using the data object
        //departureDate.setText(flight.departureDate);


        // Format the date ranges
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        Date result = null;
        SimpleDateFormat sdf = null;
        try {
            result = df.parse(flight.arrivalDate);
            sdf = new SimpleDateFormat("EEE, d MMM yyyy");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        arrivalDate.setText(sdf.format(result));
        status.setText(flight.status);
        route.setText(flight.route);
        // Return the completed view to render on screen
        return convertView;
    }

}