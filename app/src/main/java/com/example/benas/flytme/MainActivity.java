package com.example.benas.flytme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity{
    EditText flightNumberInput;
    String flightNumber;
    Button submitButton;
    String result;
    List<FlightDetail> flightDetailList = new ArrayList<FlightDetail>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the value of the input box
        flightNumberInput   = (EditText)findViewById(R.id.flightNumber);
        submitButton = (Button)findViewById(R.id.button);
        submitButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        flightNumber = flightNumberInput.getText().toString();
                        //Instantiate new instance of our class
                        ScrapeData scrapeData = new ScrapeData();
                        //Perform the doInBackground method, passing in our url
                        try {
                            result = scrapeData.execute(flightNumber).get();
                            //Process Data Extraction
                            List<String> flightData = ExtractFlightDetails(result);
                            //Cast to FlightDetail object
                            flightDetailList = CreateFlightDetailObjectList(flightData, flightDetailList);




                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private List<FlightDetail> CreateFlightDetailObjectList(List<String> rawData, List<FlightDetail> flightDetailList) {

        List<String> formattedList = new ArrayList<String>();
        for (int i = 0; i < rawData.size(); i++) {
            String[] flightDetailPart = rawData.get(i).split(" ");
            FlightDetail singleFlight = new FlightDetail();
            for(int j = 0; j < flightDetailPart.length; j++) {
                Pattern pattern = Pattern.compile("\"([A-Za-z0-9+:?/-]+)\"?");
                Matcher matcher = pattern.matcher(flightDetailPart[j]);
                while (matcher.find()) {
                    formattedList.add(matcher.group(1));
                }
            }

            singleFlight.arrivalDelay = formattedList.get(0);
            singleFlight.arrivalDate = formattedList.get(1);
            singleFlight.departureDelay = formattedList.get(2);
            singleFlight.departureDate = formattedList.get(3);
            singleFlight.flightNumber = formattedList.get(4);
            singleFlight.hasArrived = formattedList.get(5);
            singleFlight.hasDeparted = formattedList.get(6);
            singleFlight.scheduleOnly = formattedList.get(7);
            singleFlight.route = formattedList.get(8);
            singleFlight.status = formattedList.get(9);
            singleFlight.terminals = formattedList.get(10);

            //RESET LIST
            formattedList.clear();

            this.flightDetailList.add(singleFlight);
        }

        return this.flightDetailList;
    }

    public class ScrapeData extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        private String resp;
        Context context;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = "https://www.google.co.uk/search?q="+params[0];
            String result = null;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Intent newIntent = new Intent();
            newIntent.setClass(getApplicationContext(),FlightResults.class);
            newIntent.putExtra("flightDetailList", (Serializable) flightDetailList);
            startActivity(newIntent);
            overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);

        }


        @Override
        protected void onPreExecute() {

        }
    }

    public List<String> ExtractFlightDetails(String rawData){
        List<String> flightDetails = new ArrayList<String>();
        Matcher m = Pattern.compile("(?:data-arrival_delay=)\"([0-9-+]*)\" (?:data-arrival_time=)\"([A-Z0-9-:+]+)\" " +
                "(?:data-departure_delay=)\"([0-9-+]*)\" (?:data-departure_time=)\"([A-Z0-9-:+]+)\" " +
                "(?:data-flight_number=)\"([A-Z0-9]+)\" (?:data-has_arrived=)\"([A-Za-z]+)\" (?:data-has_departed=)\"([A-Za-z]+)\" " +
                "(?:data-is_schedule_only=)\"([A-Za-z]+)\" (?:data-route=)\"([A-Za-z-]+)\" (?:data-status=)\"([A-Za-z]+)\" " +
                "(?:data-terminals=)\"([A-Z-\\/?\\s0-9]+)\"")
                .matcher(rawData);
        while (m.find()) {
            flightDetails.add(m.group());
        }
        return flightDetails;
    }

}
