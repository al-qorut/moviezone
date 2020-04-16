package smk.adzikro.moviezone.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import smk.adzikro.moviezone.fragments.FragmentBlank;
import smk.adzikro.moviezone.fragments.FragmentListMovie;
import smk.adzikro.moviezone.fragments.FragmentListTv;
import smk.adzikro.moviezone.fragments.fragment_favorite;
import smk.adzikro.moviezone.net.SearchClient;

/**
 * Created by server on 11/10/17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence title[];
    int TabCount;

    public ViewPagerAdapter(FragmentManager fm,
    CharSequence mTitle[], int TabCount) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.title = mTitle;
        this.TabCount = TabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (TabCount){
            case 5:
                if(position==0)
                    return FragmentListMovie.newInstance(SearchClient.NOWPLAYING,"");
                else if (position==1)
                    return FragmentListMovie.newInstance(SearchClient.UPCOMING,"");
                else if(position==2)
                    return FragmentListMovie.newInstance(SearchClient.POPULAR,"");
                else if(position==3)
                    return FragmentListMovie.newInstance(SearchClient.TOPRATE,"");
                else if(position==4)
                    return new fragment_favorite();
                break;
            case 4:
                if(position==0)
                    return FragmentListTv.newInstance(SearchClient.TVAIRINGTODAY,"");
                else if (position==1)
                    return FragmentListTv.newInstance(SearchClient.TVONTHEAIR,"");
                else if(position==2)
                    return FragmentListTv.newInstance(SearchClient.TVTOPRATE,"");
                else if(position==3)
                    return FragmentListTv.newInstance(SearchClient.TVPOPULAR,"");
                break;
            case 3:
                if(position==0)
                    return new FragmentBlank();
                else if (position==1)
                    return new FragmentBlank();
                else if (position==2)
                    return new FragmentBlank();
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
