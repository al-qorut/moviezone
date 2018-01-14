package smk.adzikro.moviezone.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.adapter.PagerDetailMovie;
import smk.adzikro.moviezone.adapter.ViewBoongan;
import smk.adzikro.moviezone.adapter.ViewImagePagerAdapter;
import smk.adzikro.moviezone.custom.CirclePageIndicator;
import smk.adzikro.moviezone.custom.SlidingTabLayout;
import smk.adzikro.moviezone.databasehelper.MovieDatabase;
import smk.adzikro.moviezone.loader.GetDetailMovieTaskLoader;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Actor;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.widget.TopImageView;

/**
 * Created by server on 10/19/17.
 */

public class DetailMovieActivity extends AppCompatActivity
implements
        LoaderManager.LoaderCallbacks<Movie>,
        View.OnClickListener{
    private static final String KEY_ON_SAVED ="tah_simpan_datana";
    public static final String KEY_KIRIMAN ="film";
    private static final String EXTRA_IMAGE = "percobaan";
    TextView txInfo, txType, title;
    Movie movie ;
    ViewPager pagerBawah, viewpagerImage;
    TopImageView gbroll;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView photo;
  //  Toolbar toolbar;
    LinearLayout mTitleContainer;
    SlidingTabLayout slidingTabLayout;
    PagerDetailMovie pagerAdapter;
    CirclePageIndicator circlePageIndicator;
    ViewBoongan adapterViewImage;
    boolean isfavorite;

    public static void runDetail(AppCompatActivity activity, View transitionImage, Movie movie){
        try {
            Intent intent = new Intent(activity, DetailMovieActivity.class);
            intent.putExtra(KEY_KIRIMAN, movie);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, transitionImage, movie.getCover());
            ActivityCompat.startActivity(activity, intent, options.toBundle());
            Log.e("DetailActivity", "runDetail ");
        }catch (Exception e) {
            Log.e("DetailActivity", "runDetail "+e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.detail_movie);
        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(KEY_ON_SAVED);
        }else {
            movie = getIntent().getExtras().getParcelable(KEY_KIRIMAN);
        }
        MovieDatabase movieDatabase = new MovieDatabase(this);
        isfavorite=movieDatabase.isMovie(movie.getId());
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie",movie);
        getSupportLoaderManager().initLoader(1001,bundle,this);
        init();
    }
    private void loadImage(final ImageView imageView, final ProgressBar progressBar, int posisi){
        Glide.with(this).load(SearchClient.getImagePathBesar(this)+movie.getListBackDrop().get(posisi))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        imageView.setImageDrawable(resource);
                        imageView.setDrawingCacheEnabled(true);
                    }
                });
    }
    CharSequence TitleTab[];
    ProgressBar loading;

    private void init(){
        TitleTab=new CharSequence[]{
                getString(R.string.tab_info),
                getString(R.string.tab_cast),
                getString(R.string.tab_review),
                getString(R.string.tab_crews),
                getString(R.string.tab_link_donlot)
        };
        ViewCompat.setTransitionName(findViewById(R.id.appbar), EXTRA_IMAGE);
        supportPostponeEnterTransition();
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photo = (ImageView)findViewById(R.id.photo); //photo kotak leutik
        gbroll = (TopImageView)findViewById(R.id.slidingImage); //background
        txInfo = (TextView)findViewById(R.id.info);
        txType = (TextView)findViewById(R.id.type);
        title = (TextView)findViewById(R.id.titleBold);
        collapsingToolbarLayout =(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        onFutValue();
        mTitleContainer = (LinearLayout) findViewById(R.id.infoMovie);
        pagerBawah = (ViewPager)findViewById(R.id.pager);
        slidingTabLayout = (SlidingTabLayout)findViewById(R.id.tabs);
        loading = (ProgressBar)findViewById(R.id.progress);
        viewpagerImage = (ViewPager)findViewById(R.id.imageshow);
        circlePageIndicator = (CirclePageIndicator)findViewById(R.id.imageindicator);
        viewpagerImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadImage(gbroll, loading, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailMovieActivity.this, SlideImageActivity.class);
                intent.putStringArrayListExtra(SlideImageActivity.KEY, (ArrayList<String>) movie.getListPoster());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }
    String TAG="DetailMovieActivity";

    private void onFutValue() {
       Glide.with(this)
                    .load(SearchClient.getImagePath(this)+movie.getCover())
                    .thumbnail(0.5f)
                    .override(100, 130)
                    .into(photo);
        Glide.with(this)
                .load(SearchClient.getImagePathBesar(this)+movie.getBackdrop())
                .into(gbroll);
        collapsingToolbarLayout.setTitle(movie.getTitle());
        txType.setText(movie.getGenres());
        title.setText(movie.getTitle());
        txInfo.setText(movie.getYear());
    }

    private String[] getPhoto(){

         String path = Environment.getExternalStorageDirectory().toString()+"/Moviezone/gambar";
        //  Log.d(TAG, "Path: " + path);
          File directory = new File(path);
          File[] files = directory.listFiles();
          Log.d(TAG, "Size: "+ files.length);
        String[] hasil = new String[files.length];
          for (int i = 0; i < files.length; i++)
          {
            Log.e(TAG, "FileName:" + files[i].getName());
            hasil[i]=path+"/"+files[i].getName();
           // mBit.add(i, BitmapFactory.decodeFile(path+"/"+files[i].getName()));
          }
          return hasil;
    }
    String hasilImage[];


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_detail_movie, menu);
        MenuItem item = menu.findItem(R.id.action_favorite);
        item.setIcon(isfavorite?R.drawable.ic_favorite_black_24dp:R.drawable.ic_heart_outline_white_24dp);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.e(TAG,"onClickmenu");
        int m = item.getItemId();
        if(m==android.R.id.home){
            onBackPressed();
        }else if(m==R.id.action_share){
          //  Toast.makeText(this,"Share \n"+R.string.drawer_header_text,Toast.LENGTH_SHORT).show();
            SearchClient.shareLink(this, "movie/"+movie.getId());
        }else if(m==R.id.action_favorite){
            SearchClient.addMovieFavorite(this, movie);
        }
    return true;
    }

    private void onFinishLoad(){
        loading.setVisibility(View.GONE);
        pagerBawah.setVisibility(View.VISIBLE);
        pagerAdapter = new PagerDetailMovie(getSupportFragmentManager(),TitleTab,5,movie);
        pagerBawah.setAdapter(pagerAdapter);
        slidingTabLayout.setViewPager(pagerBawah);
        onFutValue();
        adapterViewImage = new ViewBoongan(getSupportFragmentManager(), movie.getListBackDrop());
        viewpagerImage.setAdapter(adapterViewImage);
        circlePageIndicator.setViewPager(viewpagerImage);
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        Log.e(TAG,"onLoaderCreate ");
        Movie idm = args.getParcelable("movie");
        return new GetDetailMovieTaskLoader(this,idm);
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
//        Log.e(TAG,"onLoadFinished Jumlah Actor "+data.getActors().size());
        this.movie = data;
    //    Log.e(TAG,"onLoadFinished Jumlah Actor dari movie "+movie.getActors().size());
        onFinishLoad();
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }

    public void loadNewMovie(Movie movie){
        loading.setVisibility(View.VISIBLE);
        pagerBawah.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie",movie);
        getSupportLoaderManager().restartLoader(1001,bundle,this);
    }
    public void loadDetailActor(Actor actor){
        Bundle bundle = new Bundle();
        bundle.putParcelable("actor",actor);
        Intent intent = new Intent(this, DetailPeopleActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_ON_SAVED, movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.photo){
            Intent intent = new Intent(DetailMovieActivity.this, SlideImageActivity.class);
            intent.putStringArrayListExtra(SlideImageActivity.KEY, (ArrayList<String>) movie.getListPoster());
            startActivity(intent);
        }else if(v.getId()==R.id.slidingImage){
            Intent intent = new Intent(DetailMovieActivity.this, SlideImageActivity.class);
            intent.putStringArrayListExtra(SlideImageActivity.KEY, (ArrayList<String>) movie.getListBackDrop());
            startActivity(intent);
        }
    }
}
