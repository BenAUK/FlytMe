package com.example.benas.flytme;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FlightResults extends AppCompatActivity {

    Animation animSwipeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_results);
        //LinearLayout flightResultsPage = (LinearLayout) findViewById(R.id.flightResultsPage);
        List<FlightDetail> flightDetailList = (List<FlightDetail>) getIntent().getSerializableExtra("flightDetailList");

        List<String> dateLandedArray = new ArrayList<String>();
        for(FlightDetail flight : flightDetailList){
            dateLandedArray.add(flight.arrivalDate);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.flight_results_list, dateLandedArray);

        ListView listView = (ListView) findViewById(R.id.flightResultsPage);
        listView.setAdapter(adapter);


        for(FlightDetail flight : flightDetailList)
        {
            //Add route
            TextView route = new TextView(this);
            route.setText(flight.route);
            //flightResultsPage.addView(route);

            TextView departureDate = new TextView(this);
            departureDate.setText(flight.departureDate);
            //flightResultsPage.addView(departureDate);

            TextView arrivalDate = new TextView(this);
            arrivalDate.setText(flight.arrivalDate);
            //flightResultsPage.addView(arrivalDate);



        }

    }
}

