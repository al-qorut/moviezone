package smk.adzikro.moviezone.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.activity.DetailMovieActivity;
import smk.adzikro.moviezone.objek.Actor;
import smk.adzikro.moviezone.objek.CrewMovie;
import smk.adzikro.moviezone.objek.Movie;

/**
 * Created by server on 11/18/17.
 */

public class FragmentCrews extends Fragment {
    public final static String KEY="nyieun";
    private static final String TAG ="FragmentActors" ;
    private RecyclerView recyclerView;
    private Movie movie;

    public static FragmentCrews newInstance(Movie actor){
        Log.e(TAG,"Waktu di createView tah loba "+actor.getCrews().size());
        FragmentCrews fragment = new FragmentCrews();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY,actor);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.detail_list_actor, group, false);
        recyclerView = view.findViewById(R.id.list_actor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movie = getArguments().getParcelable(KEY);
        if(movie.getCrews().size()>0) {
            ListCrewAdapter listCrewAdapter = new ListCrewAdapter(getContext(), movie.getCrews());
            recyclerView.setAdapter(listCrewAdapter);
        }
        return view;
    }

    public class ListCrewAdapter extends RecyclerView.Adapter<ListCrewAdapter.Holder> {
        private static final String TAG ="ListActorAdapter" ;
        private List<CrewMovie> actors;
        private Context context;

        public ListCrewAdapter(Context context, List<CrewMovie> actors){
            this.context=context;
            this.actors = actors;
            //  Log.e(TAG,"on Create "+actors.get(0).getName());
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.detail_actor,parent,false);
            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            CrewMovie actor = actors.get(position);
            //  Log.e(TAG,"onBindView "+actor.getName());

            Glide.with(context).load(actor.getPhoto())
                    .thumbnail(0.7f)
                    .into(holder.photo);
            holder.nama.setText(actor.getName());
            holder.pemeran.setText(actor.getJob());
            holder.itemView.setTag(actor);
        }

        @Override
        public int getItemCount() {
            return (actors!=null?actors.size():0);
        }

        public class Holder extends RecyclerView.ViewHolder {
            CircleImageView photo;
            TextView nama, pemeran;
            public Holder(View itemView) {
                super(itemView);
                photo = itemView.findViewById(R.id.id_photo);
                nama = itemView.findViewById(R.id.nama_actor);
                pemeran = itemView.findViewById(R.id.peran);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Actor actor = new Actor(true);
                        actor.setmName(((CrewMovie)v.getTag()).getName());
                        actor.setPhoto(((CrewMovie)v.getTag()).getPhoto());
                        actor.setmId(((CrewMovie)v.getTag()).getId());
                        ((DetailMovieActivity)context).loadDetailActor(actor);
                    }
                });
            }
        }
    }
}
