package smk.adzikro.moviezone.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.adapter.ViewBoongan;
import smk.adzikro.moviezone.custom.CirclePageIndicator;
import smk.adzikro.moviezone.custom.SlidingTabLayout;
import smk.adzikro.moviezone.fragments.FragmentActors;
import smk.adzikro.moviezone.fragments.FragmentDetailTvInfo;
import smk.adzikro.moviezone.fragments.FragmentSeason;
import smk.adzikro.moviezone.loader.GetDetailTvTaskLoader;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Tv;
import smk.adzikro.moviezone.widget.TopImageView;

/**
 * Created by server on 11/20/17.
 *
 *
 */

public class DetailTvActivity extends AppCompatActivity
    implements
        LoaderManager.LoaderCallbacks<Tv>{
    public final static String DETAIL_TV="detailtv";
    CharSequence TitleTab[];
    ProgressBar loading;
    ImageView photo;
    TopImageView gbroll;
    TextView txInfo, txType, title;
    ViewPager pager, pagerBawah;
    CirclePageIndicator circlePageIndicator;
    CollapsingToolbarLayout collapsingToolbarLayout;
    LinearLayout mTitleContainer;
    SlidingTabLayout slidingTabLayout;
    private Tv tv;

    @Override
    protected void onCreate(Bundle save){
        super.onCreate(save);
        setContentView(R.layout.detail_movie);
        if(save!=null){
            tv = save.getParcelable(DETAIL_TV);
        }else{
            tv = getIntent().getParcelableExtra(DETAIL_TV);
        }
        init();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DETAIL_TV,tv);
        getSupportLoaderManager().initLoader(1002,bundle,this);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putParcelable(DETAIL_TV, tv);
        super.onSaveInstanceState(bundle);
    }

    private void init(){
        TitleTab=new CharSequence[]{
                getString(R.string.tab_info),
                getString(R.string.tab_actors),
                getString(R.string.tab_seasons)
        };
        //ViewCompat.setTransitionName(findViewById(R.id.appbar), EXTRA_IMAGE);
        supportPostponeEnterTransition();
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photo = findViewById(R.id.photo); //photo kotak leutik
        gbroll = findViewById(R.id.slidingImage); //background
        txInfo = findViewById(R.id.info);
        txType = findViewById(R.id.type);
        title = findViewById(R.id.titleBold);
        pager = findViewById(R.id.imageshow);
        circlePageIndicator = findViewById(R.id.imageindicator);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(tv.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mTitleContainer = findViewById(R.id.infoMovie);
        pagerBawah = findViewById(R.id.pager);
        slidingTabLayout = findViewById(R.id.tabs);
        loading = findViewById(R.id.progress);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
             // Glide.with(getBaseContext()).load(SearchClient.getImagePath(DetailTvActivity.this)+tv.getImagesBack().get(position))//hasilImage[position])
            //  .thumbnail(0.7f)
             // .into(gbroll);
                loadImage(gbroll,loading,position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        onFutValue(tv);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailTvActivity.this, SlideImageActivity.class);
                intent.putStringArrayListExtra(SlideImageActivity.KEY, tv.getListposter());
                startActivity(intent);
            }
        });
    }
    private void loadImage(final ImageView imageView, final ProgressBar progressBar, int posisi){
        Glide.with(this).load(SearchClient.getImagePathBesar(this)+tv.getImagesBack().get(posisi))
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
    private void onFutValue(Tv tv1) {
       // Glide.with(getBaseContext()).load(SearchClient.getImagePathBesar(DetailTvActivity.this)+tv1.getBackdrop())//hasilImage[position])
       //         .thumbnail(0.7f)
       //         .into(gbroll);
        Glide.with(this)
                    .load(SearchClient.getImagePath(DetailTvActivity.this)+tv1.getPoster())
                    .thumbnail(0.5f)
                    .override(100, 120)
                    .into(photo);
        txType.setText(tv1.getGenres());
        title.setText(tv1.getTitle());
        txInfo.setText(tv1.getReleaseDate());
    }

    private void onFinishLoadData(){
        loading.setVisibility(View.GONE);
        pagerBawah.setVisibility(View.VISIBLE);

        PagerDetailTV adapterPager = new PagerDetailTV(getSupportFragmentManager(),TitleTab,3,tv);
        pagerBawah.setAdapter(adapterPager);
        slidingTabLayout.setViewPager(pagerBawah);

        ViewBoongan adapter = new ViewBoongan(getSupportFragmentManager(), tv.getImagesBack());
        pager.setAdapter(adapter);
        circlePageIndicator.setViewPager(pager);
        pager.setCurrentItem(tv.getImagesBack().size());
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
            //Toast.makeText(this,"Share \n"+R.string.drawer_header_text,Toast.LENGTH_SHORT).show();
            SearchClient.shareLink(this, "tv/"+tv.getId());
        }else if(m==R.id.action_favorite){
            Toast.makeText(this,"Favorite \n"+R.string.drawer_header_text,Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    //===========LOADER==============
    @Override
    public Loader<Tv> onCreateLoader(int id, Bundle args) {
        Tv tvx = args.getParcelable(DETAIL_TV);
        return new GetDetailTvTaskLoader(this, tvx);
    }
    public void loadNewTv(Tv tv){
        onFutValue(tv);
        loading.setVisibility(View.VISIBLE);
        pagerBawah.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DETAIL_TV,tv);
        getSupportLoaderManager().restartLoader(1002,bundle,this);
    }
    @Override
    public void onLoadFinished(Loader<Tv> loader, Tv data) {
        this.tv=data;
        onFinishLoadData();
    }

    @Override
    public void onLoaderReset(Loader<Tv> loader) {

    }
    //=============end of Loader==================

    private class PagerDetailTV extends FragmentStatePagerAdapter{
        private static final String TAG ="PagerDEtailTV" ;
        CharSequence title[];
        int TabCount;
        Tv tv;
        public PagerDetailTV(FragmentManager fm, CharSequence mTitle[],int tabCount, Tv tv) {
            super(fm);
            this.title = mTitle;
            this.TabCount =tabCount;
            this.tv = tv;
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return FragmentDetailTvInfo.newInstance(tv);
            }else if (position==1){
                return FragmentActors.newInstance(tv.getCastTvList());
            }else if(position==2){
                return FragmentSeason.newIntance(tv.getSeason());
            }
            return null;
        }
        @Override
        public CharSequence getPageTitle(int p){
            return title[p];
        }
        @Override
        public int getCount() {
            return TabCount;
        }
    }
}
