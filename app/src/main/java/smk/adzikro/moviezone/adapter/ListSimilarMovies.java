package smk.adzikro.moviezone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import smk.adzikro.moviezone.activity.DetailMovieActivity;
import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.objek.SimilarMovie;

/**
 * Created by server on 11/16/17.
 */

public class ListSimilarMovies
    extends RecyclerView.Adapter<ListSimilarMovies.Holder> {


        private static final String TAG = "ListSimilarMovie" ;
        private List<SimilarMovie> listSimiMovie;
        private Context context;

    public ListSimilarMovies(Context context, List<SimilarMovie> trailers) {
            this.context = context;
            Log.e(TAG,"onCreate");
            this.listSimiMovie = trailers;
        }

    public List<SimilarMovie> getListMovie(){
            return listSimiMovie;
       }
    public void setData(List<SimilarMovie> data){
        this.listSimiMovie= data;
    }



    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_similar_movie, parent, false);
        Holder viewHolder = new Holder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final SimilarMovie p = getListMovie().get(position);
        holder.txTitle.setText(p.getTitle());
        holder.itemView.setTag(p);
       // String url = p.getPoster();
       // Log.e(TAG,url);
        Glide.with(context).load(SearchClient.getImagePath(context)+p.getPoster())
                .thumbnail(0.5f)
                .into(holder.videoView);
    }

    @Override
    public int getItemCount() {
        return (null!=listSimiMovie?listSimiMovie.size():0);
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
                    Movie idMovie = ((SimilarMovie)v.getTag()).getMovie();
                    ((DetailMovieActivity)context).loadNewMovie(idMovie);
                  //  Toast.makeText(context,link,Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
