package com.example.android.quakereport;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs_general);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_start_date_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_end_date_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_magnitude_key)));
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Set the preference summaries
        setPreferenceSummary(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();
        // For other preferences, set the summary to the value's simple string representation.
        preference.setSummary(stringValue);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        setPreferenceSummary(preference, newValue);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ( key.equals(getString(R.string.pref_start_date_key)) ) {
            // we've changed the location
            // first clear locationStatus
            Preference locationPreference = findPreference(getString(R.string.pref_start_date_key));
            bindPreferenceSummaryToValue(locationPreference);
            //SunshineSyncAdapter.syncImmediately(this);
        } else if ( key.equals(getString(R.string.pref_magnitude_key)) ) {
            // units have changed. update lists of weather entries accordingly
            Preference locationPreference = findPreference(getString(R.string.pref_magnitude_key));
            bindPreferenceSummaryToValue(locationPreference);
        } else if ( key.equals(getString(R.string.pref_end_date_key)) ) {
            // our location status has changed.  Update the summary accordingly
            Preference locationPreference = findPreference(getString(R.string.pref_end_date_key));
            bindPreferenceSummaryToValue(locationPreference);
        }
    }
}
