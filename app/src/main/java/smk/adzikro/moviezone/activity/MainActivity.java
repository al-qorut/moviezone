package smk.adzikro.moviezone.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;


import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.adapter.ViewPagerAdapter;
import smk.adzikro.moviezone.custom.SlidingTabLayout;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.preferences.SettingsPref;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cekPermision();
        if (savedInstanceState != null) {

        }
        init();
    }
    SlidingTabLayout tab;
    ViewPager pager;
    ViewPagerAdapter pagerAdapter;
    CharSequence TitleMovie[];
    CharSequence TitleTV[];
    public void init(){

        TitleMovie=new CharSequence[]{
                getString(R.string.tab_playing),
                getString(R.string.tab_upcoming),
                getString(R.string.tab_popular),
                getString(R.string.tab_toprated),
                getString(R.string.navb_favorite)
            };
        TitleTV=new CharSequence[]{
                getString(R.string.tab_airingtoday),
                getString(R.string.tab_ontheair),
                getString(R.string.tab_popular),
                getString(R.string.tab_toprated)
        };
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        tab = (SlidingTabLayout)findViewById(R.id.tabs);
        pager = (ViewPager)findViewById(R.id.pager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), TitleMovie,5);
        pager.setAdapter(pagerAdapter);
        tab.setViewPager(pager);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                if (item.getTitle().equals(getString(R.string.tab_favorite))) {
                } else if ((item.getTitle().equals(getString(R.string.tab_searching)))) {
                    onSearchRequested();
                    Log.e("Main","tab seaching");
                } else if ((item.getTitle().equals(getString(R.string.home)))) {
                   new login().execute();
                }else if ((item.getTitle().equals(getString(R.string.nav_sub_menu_about)))) {
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                }else if ((item.getTitle().equals(getString(R.string.nav_item_setting)))) {
                    startActivity(new Intent(MainActivity.this, SettingsPref.class));
                    Log.e("Main","setting");
                }

                return true;
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getTitle().equals(getString(R.string.navb_movie))) {
                    setActive(true);
                    movie();
                } else if ((item.getTitle().equals(getString(R.string.navb_tv)))) {
                    setActive(false);
                    tv();
                } else if ((item.getTitle().equals(getString(R.string.navb_discovery)))) {
                    startActivity(new Intent(MainActivity.this, DiscoverActivity.class));
                }else if ((item.getTitle().equals(getString(R.string.navb_favorite)))) {
                    Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                    intent.putExtra(ViewActivity.KEY,0);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private void movie(){
        pagerAdapter = null;
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), TitleMovie,5);
        pager.setAdapter(pagerAdapter);
        tab.setViewPager(pager);
    }
    private void tv(){
        pagerAdapter = null;
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), TitleTV,4);
        pager.setAdapter(pagerAdapter);
        tab.setViewPager(pager);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_cari);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG,"pencarian di enter");
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra(SearchActivity.KEY_QUERY,query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onResume(){
        Log.e(TAG,"onResume");
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_cari:
                Toast.makeText(this, "wkwkwkwkwkw :)", Toast.LENGTH_LONG).show();
                return true;
            }
        return super.onOptionsItemSelected(item);
    }





    final private int WRITE_ESCDARD=1;
    private void cekPermision(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "checkSelfPermission ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
             Log.e(TAG, "show request permision ");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_ESCDARD);
            } else {

                Log.e(TAG, "tidak butuh show request permision ");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_ESCDARD);
                  }
        } else {
            Log.e(TAG, "cek self fermision false");

        }
    }
 public boolean isInternetAda(){
     ConnectivityManager connectivityManager =
             (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
     NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
     return networkInfo != null && networkInfo.isConnectedOrConnecting();
 }



 public void setView(boolean view){
     SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
     SharedPreferences.Editor edit = preference.edit();
     edit.putBoolean("view",view);
     edit.apply();
 }
 public void setActive(boolean view){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = preference.edit();
        edit.putBoolean("video",view);
        edit.apply();
    }

 public void showDetail(Movie movie, View view){
     Intent intent = new Intent(this, DetailMovieActivity.class);
     intent.putExtra("film",movie);
     startActivity(intent);
 }

 String token;
 private class login extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
           String url ="https://api.themoviedb.org/3/authentication/token/new?api_key="+SearchClient.API_KEY;
           JSONObject object = SearchClient.getJSONObject(MainActivity.this, url);
           try {
            token = object.getString("request_token");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    return null;
            }
                @Override
                protected void onPostExecute(Void d){
                    if(!token.equals("")) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/authenticate/" + token + "?redirect_to=smk.adzikro.moviezone")));
                    }
                }
            }


}
