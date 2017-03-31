/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
//import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
                implements LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USG_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query" +
                    "?format=geojson&starttime=2017-01-01&" +
                    "endtime=2017-01-31&minmag=7&limit=10";
    // https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2017-01-01&endtime=2017-01-01&minmag=2017-01-31&limit=10
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson";

    /*
        https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2017-01-01&endtime=2017-01-31&minmag=6&limit=10
     */

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private EarthquakeAdapter adapter;
    private ListView earthquakeListView;
    private TextView emptyStateView;
    private ProgressBar progressBarView;
    //private ViewGroup viewGroup;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
        //ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();
        // Find a reference to the {@link ListView} in the layou
        // t
        //viewGroup = (ViewGroup)findViewById(R.id.view_group);
        progressBarView = (ProgressBar)findViewById(R.id.progress_bar_view);
        earthquakeListView = (ListView) findViewById(R.id.list);
        emptyStateView = (TextView)findViewById(R.id.empty_state) ;
        earthquakeListView.setEmptyView(emptyStateView);

        // Create a new {@link ArrayAdapter} of earthquakes
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(
           //     this, android.R.layout.simple_list_item_1, earthquakes);


       /*earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentEarthquake = adapter.getItem(position);
                Uri currentUri = Uri.parse(currentEarthquake.getURL());
                startActivity(new Intent(Intent.ACTION_VIEW, currentUri));
            }
        });*/
        //updateUi(null);
        adapter = new EarthquakeAdapter(this,
                new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(adapter);
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            Log.e(LOG_TAG, "network access correct");
            //new EarthquakeAsyncTask().execute(USGS_REQUEST_URL);// doNecessary();
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null,this);
            return;
        }
        progressBarView.setVisibility(ProgressBar.GONE);
        emptyStateView.setText("No Internet Connection !!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.earthquake_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_refresh) {
            ConnectivityManager cm = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                Log.e(LOG_TAG, "network access correct");
                //new EarthquakeAsyncTask().execute(USGS_REQUEST_URL);// doNecessary();
                getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null,this);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUi(List<Earthquake> earthquakes)
    {
        adapter = new EarthquakeAdapter(this,
                earthquakes);
       // earthquakeListView.setAdapter(adapter);

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG, " onCreateLoader");
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        adapter.clear();

        if(earthquakes != null && !earthquakes.isEmpty())
        {
            adapter.addAll(earthquakes);
            Log.e(LOG_TAG, "add all on Load finished");
        }
        else {
            emptyStateView.setText("Nothing To Show Here");
            Log.e(LOG_TAG, "empty state");
        }
        progressBarView.setVisibility(ProgressBar.GONE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Earthquake>> loader) {
        Log.e(LOG_TAG, "on loader reset");
        adapter.clear();
    }
    /*private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>>
    {

        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes(urls[0]);
            if(earthquakes == null)
                Log.v(LOG_TAG, "null object");
            return earthquakes;
        }
        @Override
        protected void onPostExecute(List<Earthquake> earthquakes)
        {
            updateUi(earthquakes);
        }
    }*/
}
