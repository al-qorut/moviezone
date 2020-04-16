package smk.adzikro.moviezone.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.adapter.PagerDetailPeople;
import smk.adzikro.moviezone.adapter.ViewImagePagerAdapter;
import smk.adzikro.moviezone.custom.CirclePageIndicator;
import smk.adzikro.moviezone.custom.SlidingTabLayout;
import smk.adzikro.moviezone.loader.GetDetailPeopleLoader;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Actor;
import smk.adzikro.moviezone.widget.TopImageView;

/**
 * Created by server on 11/18/17.
 */

public class DetailPeopleActivity extends AppCompatActivity
implements LoaderManager.LoaderCallbacks<Actor>{

    public static final String KEY_SAVE ="actor" ;
    String TAG = "DetailPeopleActivity";
    CharSequence TitleTab[];
    ProgressBar loading;
    ActionBar actionBar;
    CircleImageView photo;
    TopImageView gbroll;
    TextView title;
    AppBarLayout appBarLayout;
    ViewPager pager, pagerBawah;
    CirclePageIndicator circlePageIndicator;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    SlidingTabLayout slidingTabLayout;
    PagerDetailPeople pagerAdapter;
    ViewImagePagerAdapter adapter;
    private Actor actor;

    @Override
    protected void onCreate(Bundle c){
        super.onCreate(c);
        initActivityTransitions();
        setContentView(R.layout.detail_people);
        init();
        if(c!=null){
            actor = c.getParcelable(KEY_SAVE);
        }else{
            actor = getIntent().getParcelableExtra(KEY_SAVE);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SAVE, actor);
       // getSupportLoaderManager().initLoader(1002,bundle,this);
        isiData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_SAVE, actor);
        super.onSaveInstanceState(outState);
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void init(){
        TitleTab=new CharSequence[]{
                getString(R.string.tab_info),
                getString(R.string.tab_movie),
                getString(R.string.tab_crews),
                getString(R.string.tab_tv_show)
        };
        photo = findViewById(R.id.photo);
        gbroll = findViewById(R.id.slidingImage);
        title = findViewById(R.id.titleBold);
        appBarLayout = findViewById(R.id.appbar);
        pager = findViewById(R.id.imageshow);
        circlePageIndicator = findViewById(R.id.imageindicator);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        pagerBawah = findViewById(R.id.pager);
        slidingTabLayout = findViewById(R.id.tabs);
        loading = findViewById(R.id.progress);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadImage(gbroll,loading,position);
               // Glide.with(getBaseContext()).load(SearchClient.getImagePath(DetailPeopleActivity.this)+actor.getImages().get(position))
               //        .into(gbroll);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPeopleActivity.this, SlideImageActivity.class);
                intent.putStringArrayListExtra(SlideImageActivity.KEY, (ArrayList<String>) actor.getImages());
                startActivity(intent);
            }
        });
    }

    private void loadImage(final ImageView imageView, final ProgressBar progressBar, int posisi){
        Glide.with(this).load(SearchClient.getImagePathBesar(this)+actor.getImages().get(posisi))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                        imageView.setDrawingCacheEnabled(true);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void isiData(){
        title.setText(actor.getmName());
        Glide.with(getBaseContext()).load(SearchClient.getImagePath(this)+actor.getPhoto())
               .into(photo);
        collapsingToolbarLayout.setTitle(actor.getmName());
    }
/*
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        loading.setVisibility(View.VISIBLE);
        pagerBawah.setVisibility(View.GONE);
        actor = args.getParcelable(KEY_SAVE);
     //   Log.e(TAG,actor.getmId());
        return new GetDetailPeopleLoader(this, actor);
    }

    @Override
    public void onLoadFinished(Loader<Actor> loader, Actor data) {

    }

    @Override
    public void onLoaderReset(Loader<Actor> loader) {

    }

*/
    private void onFinishLoad(){
       // Log.e(TAG,"onFinishLoad "+actor.getBiography());
        loading.setVisibility(View.GONE);
        pagerBawah.setVisibility(View.VISIBLE);
        pagerAdapter = new PagerDetailPeople(getSupportFragmentManager(),TitleTab,4,actor);
        pagerBawah.setAdapter(pagerAdapter);
        slidingTabLayout.setViewPager(pagerBawah);
        adapter = new ViewImagePagerAdapter(getSupportFragmentManager(), actor.getImages());
        pager.setAdapter(adapter);
        circlePageIndicator.setViewPager(pager);
        //pager.setCurrentItem(1);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_detail_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int m = item.getItemId();
        if(m==android.R.id.home){
            onBackPressed();
        }else if(m==R.id.action_share){
           // Toast.makeText(this,"Share \n"+actor.getmName(),Toast.LENGTH_SHORT).show();
            SearchClient.shareLink(this, "person/"+actor.getmId());
        }else if(m==R.id.action_download){
            Toast.makeText(this,"download \n"+actor.getmName(),Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public Loader<Actor> onCreateLoader(int id, Bundle args) {
        loading.setVisibility(View.VISIBLE);
        pagerBawah.setVisibility(View.GONE);
        actor = args.getParcelable(KEY_SAVE);
        //   Log.e(TAG,actor.getmId());
       // return new GetDetailPeopleLoader(this, actor);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Actor> loader, Actor data) {
        onFinishLoad();
    }

    @Override
    public void onLoaderReset(Loader<Actor> loader) {

    }
}
