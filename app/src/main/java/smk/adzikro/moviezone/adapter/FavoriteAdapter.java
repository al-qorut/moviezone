package smk.adzikro.moviezone.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

import smk.adzikro.moviezone.activity.DetailMovieActivity;
import smk.adzikro.moviezone.activity.MainActivity;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.objek.MovieFavorite;
import smk.adzikro.moviezone.objek.OnItemClickListener;


/**
 * Created by server on 10/22/17.
 */

public class FavoriteAdapter extends
        RecyclerView.Adapter<FavoriteAdapter.Holder>{

    private static final String TAG = "FavoriteAdafter" ;
    private ArrayList<MovieFavorite> listMovie;
    private Context context;
    private OnItemClickListener onItemClickListener;
  //  private Cursor cursor;
    public FavoriteAdapter(Context context) {
        this.context = context;
     //   Log.e(TAG,"Tah on Create");
    }

    public ArrayList<MovieFavorite> getListMovie(){
        return listMovie;
    }
    public void setData(ArrayList<MovieFavorite> data){
        this.listMovie = data;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_favorite, parent, false);
        FavoriteAdapter.Holder viewHolder = new FavoriteAdapter.Holder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final MovieFavorite p = getListMovie().get(position);
        Movie m = new Movie(true);
        m.setId(p.getId());
        m.setTitle(p.getTitle());
        m.setCover(p.getPoster_path());
        holder.itemView.setTag(m);
        Glide.with(context)
                .load(SearchClient.getImagePath(context) +p.getPoster_path())
                .override(350, 550)
                .into(holder.imgPhoto);
        holder.txTitle.setText(p.getTitle());
        holder.txOverView.setText(p.getOverview());
        holder.txtRelease.setText(p.getRelease_date());
        holder.txRating.setText(p.getPopularity());
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView txTitle, txOverView, txtRelease, txRating;
        public Holder(View itemView) {
            super(itemView);
            imgPhoto = (ImageView)itemView.findViewById(R.id.img_item_photo);
            txTitle = (TextView)itemView.findViewById(R.id.tx_title);
            txOverView = (TextView)itemView.findViewById(R.id.tx_overview);
            txtRelease = (TextView)itemView.findViewById(R.id.tx_release_date);
            txRating = itemView.findViewById(R.id.tx_rating);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie = (Movie) v.getTag();
                    Intent intent = new Intent(context, DetailMovieActivity.class);
                    intent.putExtra("film",movie);
                    context.startActivity(intent);
                }
            });
        }
    }
}
