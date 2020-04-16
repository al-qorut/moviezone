package smk.adzikro.moviezone.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import smk.adzikro.moviezone.fragments.FagrmentReviews;
import smk.adzikro.moviezone.fragments.FragmentActors;
import smk.adzikro.moviezone.fragments.FragmentCrews;
import smk.adzikro.moviezone.fragments.FragmentDetailInfo;
import smk.adzikro.moviezone.fragments.FragmentLinkDownload;
import smk.adzikro.moviezone.objek.Movie;

/**
 * Created by server on 11/15/17.
 */

public class PagerDetailMovie extends FragmentStatePagerAdapter {
    private static final String TAG ="PagerDEtailMovie" ;
    CharSequence title[];
    int TabCount;
    Movie movie;

    public PagerDetailMovie(FragmentManager fm,CharSequence mTitle[], int TabCount, Movie movie) {
        super(fm);
        this.title=mTitle;
        this.movie = movie;
        this.TabCount = TabCount;
//        Log.e(TAG,movie.getTitle());
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return FragmentDetailInfo.newInstance(movie);
        else if (position==1)
            return FragmentActors.newInstance(movie.getCasts());
        else if (position==2)
            return FagrmentReviews.newInstance(movie);
        else if (position==3)
            return FragmentCrews.newInstance(movie);
        else if (position==4)
            return FragmentLinkDownload.newInstance(movie.getTitle());
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
