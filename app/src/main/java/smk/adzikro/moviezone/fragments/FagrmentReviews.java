package smk.adzikro.moviezone.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.adapter.ListReviewAdapter;
import smk.adzikro.moviezone.objek.Movie;

/**
 * Created by server on 11/17/17.
 */

public class FagrmentReviews extends Fragment {
    public final static String KEY="nyieunReview";
    private static final String TAG ="FragmentReview" ;

    public static FagrmentReviews newInstance(Movie actor){
        Log.e(TAG,"Waktu di createView Review");
        FagrmentReviews fragment = new FagrmentReviews();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY,actor);
        fragment.setArguments(bundle);
        return fragment;
    }
    private RecyclerView recyclerView;
    private Movie movie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.detail_list_actor, group, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.list_actor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movie = getArguments().getParcelable(KEY);
        Log.e(TAG,"Jumlah Review "+movie.getReviews().size());
        if(movie.getReviews().size()>0) {
            Log.e(TAG,"Jumlah Review "+movie.getReviews().size());
            ListReviewAdapter listReviewAdapter = new ListReviewAdapter(getContext(), movie.getReviews());
            recyclerView.setAdapter(listReviewAdapter);
        }
        return view;
    }
}
