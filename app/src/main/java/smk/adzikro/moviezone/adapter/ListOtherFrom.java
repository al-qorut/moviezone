package smk.adzikro.moviezone.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.activity.DetailMovieActivity;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.objek.OtherFromDirector;

/**
 * Created by server on 11/27/17.
 */

public class ListOtherFrom
extends RecyclerView.Adapter<ListOtherFrom.Holder>{
    Context context;
    ArrayList<OtherFromDirector> xList;

    public ListOtherFrom(Context context, ArrayList<OtherFromDirector> list){
        this.context = context;
        this.xList = list;
    }
    @Override
    public ListOtherFrom.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_similar_movie, parent, false);
        Holder viewHolder = new Holder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListOtherFrom.Holder holder, int position) {
        OtherFromDirector p = xList.get(position);
        holder.txTitle.setText(p.getTitle());
        holder.itemView.setTag(p);
        Glide.with(context).load(SearchClient.getImagePath(context)+p.getPoster())
                .thumbnail(0.5f)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return xList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txTitle;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.video);
            txTitle = itemView.findViewById(R.id.title_video);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OtherFromDirector xx = ((OtherFromDirector)v.getTag());
                    Movie x = new Movie(true);
                    x.setId(Integer.valueOf(xx.getId()));
                    x.setTitle(xx.getTitle());
                    x.setCover(xx.getPoster());
                    x.setReleasedate(xx.getReleaseDate());
                    //Movie idMovie = ((OtherFromDirector)v.getTag()).getMovie();
                    ((DetailMovieActivity)context).loadNewMovie(x);
                    //  Toast.makeText(context,link,Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
