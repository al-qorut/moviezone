package smk.adzikro.moviezone.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.adapter.ListActorAdapter;
import smk.adzikro.moviezone.objek.CastMovie;

/**
 * Created by server on 11/16/17.
 */

public class FragmentActors extends Fragment {
    public final static String KEY="nyieun";
    private static final String TAG ="FragmentActors" ;
    private RecyclerView recyclerView;
    private ArrayList<CastMovie> movie;

    public static FragmentActors newInstance(ArrayList<CastMovie> actor){
        //Log.e(TAG,"Waktu di createView "+actor.getTitle());
        FragmentActors fragment = new FragmentActors();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY, actor);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.detail_list_actor, group, false);
        recyclerView = view.findViewById(R.id.list_actor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movie = getArguments().getParcelableArrayList(KEY);
        if(movie.size()>0) {
            ListActorAdapter listActorAdapter = new ListActorAdapter(getContext(), movie);
            recyclerView.setAdapter(listActorAdapter);
        }
        return view;
    }
}
