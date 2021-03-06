package smk.adzikro.moviezone.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import smk.adzikro.moviezone.fragments.FragmentPeopleCrew;
import smk.adzikro.moviezone.fragments.FragmentPeopleInfo;
import smk.adzikro.moviezone.fragments.FragmentPeopleMovie;
import smk.adzikro.moviezone.fragments.FragmentPeopleOnTv;
import smk.adzikro.moviezone.objek.Actor;

/**
 * Created by server on 11/18/17.
 */

public class PagerDetailPeople extends FragmentStatePagerAdapter {
    private static final String TAG ="PagerDEtailPeople" ;
    CharSequence title[];
    int TabCount;
    Actor actor;

    public PagerDetailPeople(FragmentManager fm, CharSequence mTitle[], int TabCount, Actor actor) {
        super(fm);
        this.title=mTitle;
        this.actor= actor;
        this.TabCount = TabCount;
    }


    @Override
    public CharSequence getPageTitle(int p){
        return title[p];
    }
    @Override
    public int getCount() {
        return TabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return FragmentPeopleInfo.newInstance(actor);
        else if (position==1)
            return FragmentPeopleMovie.newInstance(actor);
        else if (position==2)
            return FragmentPeopleCrew.newInstance(actor);
        else if (position==3)
            return FragmentPeopleOnTv.newInstance(actor);
        return null;
    }
}
