package smk.adzikro.moviezone.preferences;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.preference.Preference;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;

import smk.adzikro.moviezone.R;

/**
 * Created by server on 10/21/17.
 */

public class SettingsPref extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_settings);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fm = getFragmentManager();
        Fragment fragment1 = fm.findFragmentById(R.id.content);
        if(fragment1==null) {
            fm.beginTransaction()
            .replace(R.id.content, new PrefsFragment())
            .commit();
        }
    }

    @SuppressLint("ValidFragment")
    private static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.sharedprefen);
//            getView().setBackgroundColor(getResources().getColor(R.color.nav_bottom));
            Preference myPref = (Preference) findPreference("local");
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                    return true;
                }
            });
        }
    }


}
