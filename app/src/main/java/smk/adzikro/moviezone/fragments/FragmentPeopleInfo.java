package smk.adzikro.moviezone.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.activity.SlideImageActivity;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Actor;

/**
 * Created by server on 11/18/17.
 */

public class FragmentPeopleInfo extends Fragment {

    private static final String KEY_ACTOR ="key_actor" ;
    static String TAG = "FragmentPoepleInfo";
    Actor actor;
    TextView lahir, tmplahir, biography;
    RecyclerView recyclerView;

    public static FragmentPeopleInfo newInstance(Actor actor) {
        Log.e(TAG,"onCreate");
        FragmentPeopleInfo fragmentPeopleInfo = new FragmentPeopleInfo();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_ACTOR,actor);
        fragmentPeopleInfo.setArguments(bundle);
        return fragmentPeopleInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle){
        super.onCreateView(inflater,group,bundle);
        final View view = inflater.inflate(R.layout.detail_list_info_people,group,false);
        if(getArguments()!=null){
            actor  = getArguments().getParcelable(KEY_ACTOR);
        }
        init(view);
        return view;
    }

    private void init(View view){
        biography = view.findViewById(R.id.tx_biografi);
        lahir = view.findViewById(R.id.tx_born);
        tmplahir = view.findViewById(R.id.tx_birthplace);
        recyclerView = view.findViewById(R.id.list_photo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        biography.setText(actor.getBiography());
        lahir.setText(actor.getBirthday());
        tmplahir.setText(actor.getPlacebirth());
        ImageList ad = new ImageList(getContext(), actor.getImages());
        recyclerView.setAdapter(ad);
    }

    private class ImageList extends RecyclerView.Adapter<ImageList.Holder> {
        Context context;
        private List<String> img = new ArrayList<>();
        public ImageList(Context context, List<String> images){
            this.context = context;
            this.img = images;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
            Holder viewHolder = new Holder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
            String gb = img.get(position);
            holder.itemView.setTag(position);
            Glide.with(context).load(SearchClient.getImagePath(context)+gb)
                    .thumbnail(0.7f)
                    .into(holder.photo);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), SlideImageActivity.class);
                    intent.putStringArrayListExtra(SlideImageActivity.KEY, (ArrayList<String>) img);
                    intent.putExtra("index",position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return img!=null?img.size():0;
        }

        public class Holder extends RecyclerView.ViewHolder {
            ImageView photo;
            public Holder(View itemView) {
                super(itemView);
                photo = itemView.findViewById(R.id.photo);

            }
        }
    }

}
