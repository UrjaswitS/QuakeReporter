package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UrJasWitK on 04-Mar-17.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>>{

    private String mUrl ;
    public static final String LOG_TAG = EarthquakeLoader.class.getName();

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected final  void onStartLoading()
    {
        forceLoad();
    }


    @Override
    public List<Earthquake> loadInBackground() {
        if (mUrl == null)
            return null;

        ArrayList<Earthquake> earthquakes = new ArrayList<>(
                QueryUtils.extractEarthquakes(getContext(), mUrl));
        if(earthquakes == null)
            Log.v(LOG_TAG, "null object");
        return earthquakes;
    }
}
