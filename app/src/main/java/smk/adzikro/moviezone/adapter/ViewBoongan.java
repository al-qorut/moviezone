package smk.adzikro.moviezone.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import smk.adzikro.moviezone.objek.IconPagerAdapter;

/**
 * Created by server on 11/23/17.
 */

public class ViewBoongan extends FragmentPagerAdapter implements
        IconPagerAdapter {
    ArrayList<String> IMAGE_CONTENT = new ArrayList<>();

    public ViewBoongan(FragmentManager fm, ArrayList<String> data) {
        super(fm);
        IMAGE_CONTENT = data;
    }

    @Override
    public Fragment getItem(int position) {
        return ViewImagePagerAdapter.FragmentImage.newInstance(IMAGE_CONTENT.get(position));
    }

    @Override
    public int getIconResId(int index) {
        return 0;
    }

    @Override
    public int getCount() {
        return IMAGE_CONTENT.size();
    }

    public static class FragmentImage extends Fragment {
        private static final String KEY = "FragmentImage:Content" ;
        private String mContent = "????";

        public static ViewImagePagerAdapter.FragmentImage newInstance(String s){
            ViewImagePagerAdapter.FragmentImage fragmentImage = new ViewImagePagerAdapter.FragmentImage();
            return fragmentImage;
        }

        @Override
        public void onCreate(Bundle s){
            super.onCreate(s);
            if((s!=null) && s.containsKey(KEY)){
                mContent = s.getString(KEY);
            }
        }
        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString(KEY, mContent);
        }
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup group, Bundle bundle){
            View view = new View(getContext());
            return view;
        }

    }
}
