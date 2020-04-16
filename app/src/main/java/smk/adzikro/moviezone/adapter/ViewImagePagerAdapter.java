package smk.adzikro.moviezone.adapter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import smk.adzikro.moviezone.R;
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
        private String mContent = "????";

        public static FragmentImage newInstance(String s){
            FragmentImage fragmentImage = new FragmentImage();
            fragmentImage.link=s;
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
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_slide_picture,group,false);
            ProgressBar progressBar = view.findViewById(R.id.progress);
            ImageView imageView = view.findViewById(R.id.image);
            loadImage(imageView,progressBar);
            return view;
        }
        private void loadImage(final ImageView imageView, final ProgressBar progressBar){
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(getContext()).load(SearchClient.getImagePathBesar(getContext())+link).apply(options)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }
}
