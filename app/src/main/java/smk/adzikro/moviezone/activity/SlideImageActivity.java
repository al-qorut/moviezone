package smk.adzikro.moviezone.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.adapter.ViewImagePagerAdapter;
import smk.adzikro.moviezone.custom.CirclePageIndicator;
import smk.adzikro.moviezone.fragments.FragmentPersonPopular;
import smk.adzikro.moviezone.net.SearchClient;

/**
 * Created by server on 11/19/17.
 */

public class SlideImageActivity extends AppCompatActivity {
    private List<String> images=new ArrayList<>();
    TextView info;
    ViewPager pager;
    ImageView imageView;
    public final static String KEY="simpan";
    private ActionBar ab;
    int pos=0;
    @Override
    protected void onCreate(Bundle saveInstanState){
        super.onCreate(saveInstanState);
        setContentView(R.layout.slide_picture);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        ab=getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        pager = (ViewPager)findViewById(R.id.pager);
      //  imageView = (ImageView)findViewById(R.id.image);
        info=(TextView)findViewById(R.id.info);
        if(saveInstanState!=null){
            images = saveInstanState.getStringArrayList(KEY);
        }else{
            images = getIntent().getStringArrayListExtra(KEY);
            pos = getIntent().getIntExtra("index",0);
        }
        ViewImagePagerAdapter adapter = new ViewImagePagerAdapter(getSupportFragmentManager(), images);
        pager.setAdapter(adapter);
        pager.setCurrentItem(pos);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int x = pager.getCurrentItem()+1;
                @SuppressLint("StringFormatMatches") String string = String.format(getString(R.string.slide_image), x, images.size());
                info.setText(string);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putStringArrayList(KEY, (ArrayList<String>) images);
        super.onSaveInstanceState(bundle);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_poster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int m = item.getItemId();
        if(m==android.R.id.home){
            onBackPressed();
        }else if(m==R.id.action_share){
            new setShare().execute();
        }else if(m==R.id.action_download){
            Toast.makeText(this,"download success..",Toast.LENGTH_SHORT).show();
            SearchClient.saveImage(this, images.get(pager.getCurrentItem()));
        }
        return true;
    }

    class setShare extends AsyncTask<Void, Integer, Void> {
        ProgressDialog dialog;
        String path= Environment.getExternalStorageDirectory()+"/Moviezone/gambar"+images.get(pager.getCurrentItem()) ;

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(SlideImageActivity.this);
            dialog.setMessage("Share...");
            Log.e("E-Info Share","PreExecute");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            SearchClient.saveImage(SlideImageActivity.this, images.get(pager.getCurrentItem()));
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Log.e("E-Info Share","DoInBackGround");
            SearchClient.share(SlideImageActivity.this,path);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();

        }
    }
}
