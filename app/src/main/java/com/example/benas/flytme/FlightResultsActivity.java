package com.example.benas.flytme;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class FlightResultsActivity extends Activity{

    Animation animSwipeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_results);
        //LinearLayout flightResultsPage = (LinearLayout) findViewById(R.id.flightResultsPage);
        ArrayList<FlightDetail> flightDetailList = (ArrayList<FlightDetail>) getIntent().getSerializableExtra("flightDetailList");

        //ArrayAdapter adapter = new ArrayAdapter<String>(this,
        //        R.layout.flight_results, (List)flightDetailList);


        // Create the adapter to convert the array to views
        FlytMeAdapter adapter = new FlytMeAdapter(this, flightDetailList);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.flightResultsPage);
        listView.setAdapter(adapter);
    }
}

