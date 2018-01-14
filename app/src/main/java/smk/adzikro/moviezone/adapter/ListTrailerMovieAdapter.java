package smk.adzikro.moviezone.adapter;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.List;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.objek.Trailer;

/**
 * Created by server on 11/16/17.
 */

public class ListTrailerMovieAdapter
        extends RecyclerView.Adapter<ListTrailerMovieAdapter.Holder> {


    private static final String TAG = "ListTrailer" ;
    private List<Trailer> listTraMovie;
    private Context context;

    public ListTrailerMovieAdapter(Context context, List<Trailer> trailers) {
        this.context = context;
        Log.e(TAG,"onCreate");
        listTraMovie = trailers;
    }

    public List<Trailer> getListMovie(){
        return listTraMovie;
    }
    public void setData(List<Trailer> data){
        this.listTraMovie= data;
    }



    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        Holder viewHolder = new Holder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Trailer p = getListMovie().get(position);
        holder.txTitle.setText(p.getName());
        //Uri uri = Uri.parse("https://www.youtube.com/watch?v="+p.getSource());
        //String videoID = uri.getQueryParameter("v");
        holder.itemView.setTag(p);
        String url = "https://img.youtube.com/vi/" + p.getSource() +"/0.jpg";
        //holder.videoView.setImageDrawable();
        Log.e(TAG,url);
        Glide.with(context).load(url)
                .thumbnail(0.5f)
                .into(holder.videoView);
    }

    @Override
    public int getItemCount() {
        return (null!=listTraMovie?listTraMovie.size():0);
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
                    //Uri uri = Uri.parse("https://www.youtube.com/watch?v="+p.getSource());
                    //v.getTag().
                    String link = ((Trailer)v.getTag()).getSource();
                    //Toast.makeText(v.getContext(),txTitle.getText(),Toast.LENGTH_SHORT).show();
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
