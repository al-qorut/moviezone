package smk.adzikro.moviezone.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.adapter.ViewPagerAdapter;
import smk.adzikro.moviezone.custom.SlidingTabLayout;
import smk.adzikro.moviezone.fragments.FragmentPersonPopular;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.objek.MovieFavorite;
import smk.adzikro.moviezone.preferences.SettingsPref;
import smk.adzikro.moviezone.provider.MovieFavorites;
import smk.adzikro.moviezone.services.ScheduleTask;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBar actionBar;
    ScheduleTask scheduleTask;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cekPermision();
        if (savedInstanceState != null) {

        }
        scheduleTask = new ScheduleTask(this);
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


    private void setting(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean all = preferences.getBoolean("allremaider",true);
        scheduleTask.scheduleUpdateWidget();
        if(all) {
            boolean daily = preferences.getBoolean("dailyremaider", false);
            Log.e(TAG, "Setting Remaider Harian " + (daily ? "Aktif " : "Tidak Aktif"));
            if (daily) {
                scheduleTask.createOneOfTask();
            } else {
                scheduleTask.cancelOneOff();
            }
            boolean upcoming = preferences.getBoolean("upcomingremaider", false);
            Log.e(TAG, "Setting Remaider upcoming " + (upcoming ? "Aktif " : "Tidak Aktif"));
            if (upcoming) {
                scheduleTask.scheduleRepeat();
            } else {
                scheduleTask.cancelRepeat();
            }
        }else{
            scheduleTask.cancelAll();
        }

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
