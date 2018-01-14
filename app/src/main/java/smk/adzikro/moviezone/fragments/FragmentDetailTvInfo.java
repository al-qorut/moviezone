package smk.adzikro.moviezone.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.List;
import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.activity.DetailTvActivity;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.SimilarTv;
import smk.adzikro.moviezone.objek.Trailer;
import smk.adzikro.moviezone.objek.TrailerTv;
import smk.adzikro.moviezone.objek.Tv;

/**
 * Created by server on 11/20/17.
 */

public class FragmentDetailTvInfo extends Fragment {
    private static final String KEY="FragmentDetailTvInfo";

    public static FragmentDetailTvInfo newInstance(Tv tv){
        FragmentDetailTvInfo info = new FragmentDetailTvInfo();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY,tv);
        info.setArguments(bundle);
        return info;
    }
    private Tv tv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle){
        super.onCreateView(inflater,parent,bundle);
        final View view = inflater.inflate(R.layout.detail_tv_info,parent,false);
        if(getArguments()!=null){
            tv = getArguments().getParcelable(KEY);
        }
        //Log.e(KEY,"Waktu di createView "+movie.getTitle());
        init(view);
        putValue();
        return view;
    }
    TextView tx_rate, tx_review, tx_firstDate, tx_lastDate,tx_network,
            tx_created, tx_type, tx_status, tx_similar, tx_other;
    private RecyclerView trailer, similar;

    private void init(View view){
        tx_rate = (TextView)view.findViewById(R.id.tx_rating);
        tx_review = (TextView)view.findViewById(R.id.tx_review);
        tx_firstDate= (TextView)view.findViewById(R.id.tx_first_date);
        tx_lastDate = (TextView)view.findViewById(R.id.tx_last_date);
        tx_network = (TextView)view.findViewById(R.id.tx_networks);
        tx_created = (TextView)view.findViewById(R.id.tx_created);
        tx_type = (TextView)view.findViewById(R.id.tx_type);
        tx_status = (TextView)view.findViewById(R.id.tx_status);
        tx_similar = (TextView)view.findViewById(R.id.tx_similar);
        tx_other = (TextView)view.findViewById(R.id.other_from);
        tx_other.setVisibility(View.GONE);
        tx_similar.setText("Similar TV Shows");
        trailer = (RecyclerView)view.findViewById(R.id.list_movie);
        similar = (RecyclerView)view.findViewById(R.id.list_similar);
        trailer.setHasFixedSize(true);
        trailer.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        similar.setHasFixedSize(true);
        similar.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
    }

    private void putValue(){
        tx_rate.setText(tv.getRating());
        tx_review.setText(tv.getPlot());
        tx_firstDate.setText(tv.getReleaseDate());
        tx_lastDate.setText(tv.getLastDate());
        tx_created.setText(tv.getCreateBy());
        tx_type.setText(tv.getType());
        tx_status.setText(tv.getStatus());
        tx_network.setText(tv.getNetwork());
        ListTrailerTvAdapter adapter = new ListTrailerTvAdapter(getContext(), tv.getTrailerTvs());
        trailer.setAdapter(adapter);
        if(tv.getSimilarTvList()!=null) {
            ListSimilarTv listSimilarTv = new ListSimilarTv(getContext(), tv.getSimilarTvList());
            similar.setAdapter(listSimilarTv);
        }
    }

    private class ListTrailerTvAdapter extends
            RecyclerView.Adapter<ListTrailerTvAdapter.Holder>{
        Context context;
        private List<TrailerTv> listTraMovie;
        public ListTrailerTvAdapter(Context context, List<TrailerTv> trailerTvs) {
            this.context = context;
            this.listTraMovie = trailerTvs;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            Holder viewHolder = new Holder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final TrailerTv p = listTraMovie.get(position);
            holder.txTitle.setText(p.getName());
            holder.itemView.setTag(p);
            String url = "https://img.youtube.com/vi/" + p.getSource() +"/0.jpg";
            Log.e(KEY,url);
            Glide.with(context).load(url)
                    .thumbnail(0.5f)
                    .into(holder.videoView);
        }

        @Override
        public int getItemCount() {
            return listTraMovie!=null?listTraMovie.size():0;
        }

        public class Holder extends RecyclerView.ViewHolder {
            ImageView videoView;
            TextView txTitle;
            public Holder(View itemView) {
                super(itemView);
                videoView = (ImageView) itemView.findViewById(R.id.video);
                txTitle = (TextView)itemView.findViewById(R.id.title_video);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String link = ((Trailer)v.getTag()).getSource();
                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + link));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + link));
                        try {
                            context.startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            context.startActivity(webIntent);
                        }
                    }
                });
            }
        }
    }

    private class ListSimilarTv extends RecyclerView.Adapter<ListSimilarTv.Holder>{
        Context context;
        List<SimilarTv> similarTvs;

        public ListSimilarTv(Context context, List<SimilarTv> list){
            this.context =context;
            this.similarTvs = list;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_similar_movie, parent, false);
            Holder viewHolder = new Holder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            SimilarTv p = similarTvs.get(position);
            holder.txTitle.setText(p.getTitle());
            holder.itemView.setTag(p);
            Glide.with(context).load(SearchClient.getImagePath(context)+p.getPoster())
                    .thumbnail(0.5f)
                    .into(holder.videoView);
        }

        @Override
        public int getItemCount() {
            return similarTvs!=null?similarTvs.size():0;
        }

        public class Holder extends RecyclerView.ViewHolder {

            ImageView videoView;
            TextView txTitle;
            public Holder(View itemView) {
                super(itemView);
                videoView = (ImageView) itemView.findViewById(R.id.video);
                txTitle = (TextView)itemView.findViewById(R.id.title_video);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimilarTv similarTv = (SimilarTv)v.getTag();
                        ((DetailTvActivity)context).loadNewTv(similarTv.getTv());

                    }
                });
            }
        }
    }
}
