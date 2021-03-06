package smk.adzikro.moviezone.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.activity.DetailMovieActivity;
import smk.adzikro.moviezone.custom.GridAutofitLayoutManager;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Actor;
import smk.adzikro.moviezone.objek.Crew;
import smk.adzikro.moviezone.objek.Movie;

/**
 * Created by server on 11/18/17.
 */

public class FragmentPeopleCrew extends Fragment {
    public final static String KEY="nyieun";
    private static final String TAG ="FragmentPeopleCrew" ;
    private Actor actor;

    public static FragmentPeopleCrew newInstance(Actor actor){
        Log.e(TAG,"Waktu di createView tah loba ");
        FragmentPeopleCrew fragment = new FragmentPeopleCrew();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY,actor);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.upcoming_layout, group, false);
        actor = getArguments().getParcelable(KEY);
        RecyclerView recyclerView = view.findViewById(R.id.list_movie);
        recyclerView.setLayoutManager(new GridAutofitLayoutManager(getContext(),0));
        actor = getArguments().getParcelable(KEY);
        TampilGridCrew adapter = new TampilGridCrew(getContext(), actor.getCrews());
        recyclerView.setAdapter(adapter);
        return view;
    }


    public class TampilGridCrew extends RecyclerView.Adapter<TampilGridCrew.Holder>{
        Context context;
        List<Crew> list;

        public TampilGridCrew(Context context, List<Crew> castings){
            this.context = context;
            this.list = castings;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.grid_layout,parent,false);
            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final Crew actor = list.get(position);
            Glide.with(context).load(SearchClient.getImagePath(context)+actor.getPoster())
                    .thumbnail(0.7f)
                    .into(holder.photo);
            String t[] = actor.getRelease_date().split("-");
            holder.nama.setText(actor.getTitle()+"\n"+t[0]);
            holder.tahun.setText(actor.getJob());
            holder.itemView.setTag(actor);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie = new Movie(true);
                    movie.setId(Integer.valueOf(actor.getId()));
                    movie.setTitle(actor.getTitle());
                    movie.setCover(actor.getPoster());
                    Intent intent = new Intent(getContext(), DetailMovieActivity.class);
                    intent.putExtra(DetailMovieActivity.KEY_KIRIMAN,movie);
                    startActivity(intent);
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }



        public class Holder extends RecyclerView.ViewHolder{
            ImageView photo;
            TextView nama, tahun;
            public Holder(View itemView) {
                super(itemView);
                photo = itemView.findViewById(R.id.image);
                nama = itemView.findViewById(R.id.title);
                tahun = itemView.findViewById(R.id.tahun);
            }
        }
    }

}
