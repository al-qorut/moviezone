package smk.adzikro.moviezone.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Episode;
import smk.adzikro.moviezone.objek.Season;

/**
 * Created by server on 11/20/17.
 */

public class FragmentSeason extends Fragment {
    private static final String KEY ="password" ;
    private Season season;
    public  static FragmentSeason newIntance(Season season){
        FragmentSeason fragmentSeason = new FragmentSeason();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY, season);
        fragmentSeason.setArguments(bundle);
        return fragmentSeason;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle bundle){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.season_layout,group,false);
        season = getArguments().getParcelable(KEY);
        TextView tx_season = view.findViewById(R.id.season);
        tx_season.setText(getText(R.string.season)+" "+season.getNumber());
        TextView tx_episode = view.findViewById(R.id.episode);
        tx_episode.setText(getText(R.string.episode)+" "+season.getJmlEpisode());
        ImageView imageView = view.findViewById(R.id.imageheader);
        Glide.with(getContext()).load(SearchClient.getImagePath(getContext())+season.getPoster())
                .thumbnail(0.6f)
                .into(imageView);
        RecyclerView recyclerView = view.findViewById(R.id.list_episode);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListEpisode listEpisode = new ListEpisode(getContext(), season.getEpisodes());
        recyclerView.setAdapter(listEpisode);
        return view;
    }

    private class ListEpisode extends RecyclerView.Adapter<ListEpisode.Holder>{
        private List<Episode> episodeList;
        private Context context;

        public ListEpisode(Context context, List<Episode> list){
            this.context = context;
            this.episodeList = list;
        }
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_episode,parent,false);
            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            Episode episode = episodeList.get(position);
            holder.no.setText(episode.getNumber());
            holder.tgl.setText(episode.getAirdate());
            holder.judul.setText(episode.getName());
            holder.review.setText(episode.getOverview());
            Glide.with(getContext()).load(SearchClient.getImagePath(getContext())+episode.getPoster())
                    .thumbnail(0.6f)
                    .into(holder.poster);
        }

        @Override
        public int getItemCount() {
            return episodeList.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView no, tgl, judul, review;
            ImageView poster;
            public Holder(View itemView) {
                super(itemView);
                no = itemView.findViewById(R.id.number_episode);
                tgl = itemView.findViewById(R.id.tanggal);
                judul = itemView.findViewById(R.id.title_episode);
                review = itemView.findViewById(R.id.review);
                poster = itemView.findViewById(R.id.imagepisode);
            }
        }
    }
}
