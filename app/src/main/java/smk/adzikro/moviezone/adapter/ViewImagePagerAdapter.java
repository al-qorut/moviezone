package smk.adzikro.moviezone.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.activity.SlideImageActivity;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.IconPagerAdapter;


/**
 * Created by server on 11/11/17.
 */

public class ViewImagePagerAdapter extends FragmentPagerAdapter implements
        IconPagerAdapter {
    List<String> IMAGE_CONTENT = new ArrayList<>();
//    private int mCount = IMAGE_CONTENT.length;

    public ViewImagePagerAdapter(FragmentManager fm, List<String> data) {
        super(fm);
        IMAGE_CONTENT = data;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentImage.newInstance(IMAGE_CONTENT.get(position));
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
        private String link;

        public static FragmentImage newInstance(String s){
            FragmentImage fragmentImage = new FragmentImage();
            fragmentImage.link=s;
            return fragmentImage;
        }
        private String mContent="????";

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
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_slide_picture,group,false);
            ProgressBar progressBar = view.findViewById(R.id.progress);
            ImageView imageView = view.findViewById(R.id.image);
            loadImage(imageView,progressBar);
            return view;
        }
        private void loadImage(final ImageView imageView, final ProgressBar progressBar){
            Glide.with(getContext()).load(SearchClient.getImagePathBesar(getContext())+link)
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
    }
}
