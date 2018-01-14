package smk.adzikro.moviezone.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.fragments.FragmentPersonPopular;
import smk.adzikro.moviezone.fragments.fragment_favorite;

/**
 * Created by server on 11/28/17.
 */

public class ViewActivity extends AppCompatActivity {

    public static final String KEY = "ViewActivity";

    @Override
    protected void onCreate(Bundle a){
        super.onCreate(a);
        setContentView(R.layout.layout_view_activity);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        int aksi = getIntent().getIntExtra(KEY, 1);
        if(aksi==1)
            FragmentPeople(); else FragmentFavorite();
    }

    public void FragmentPeople(){
        setTitle("Person Popular");
        FragmentPersonPopular fpp = FragmentPersonPopular.newInstance(1);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).
                replace(R.id.frame_container, fpp)
                .addToBackStack(null).commit();
    }
    public void FragmentFavorite(){
        setTitle("Favorite");
        fragment_favorite fpp = new fragment_favorite();
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).
                replace(R.id.frame_container, fpp)
                .addToBackStack(null).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //getMenuInflater().inflate(R.menu.menu_poster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int m = item.getItemId();
        if(m==android.R.id.home){
            finish();
        }
        return true;
    }
}
