package smk.adzikro.moviezone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.objek.Movie;



public class GridAdapter extends BaseAdapter {
    Context context;
    private ArrayList<Movie> listMovie = new ArrayList<>();

    public GridAdapter(Context context){

            this.context=context;
    }
    public void setListMovie(ArrayList<Movie> da){
        this.listMovie = da;
    }
    @Override
    public int getCount() {
        return listMovie.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = listMovie.get(position);
        if(convertView==null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.grid_layout,null );
        }
        ImageView imageView = convertView.findViewById(R.id.image);
        TextView textView = convertView.findViewById(R.id.title);
            Glide.with(context).load(movie.getCover())
                    .thumbnail(0.5f)
                    .override(350, 550)
                    .into(imageView);
            textView.setText(movie.getTitle());

        return convertView;
    }
}
