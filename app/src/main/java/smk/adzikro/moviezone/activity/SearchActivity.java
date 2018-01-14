package smk.adzikro.moviezone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.custom.GridAutofitLayoutManager;
import smk.adzikro.moviezone.fragments.FragmentListMovie;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Actor;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.objek.Pencarian;

/**
 * Created by server on 11/19/17.
 */

public class SearchActivity extends AppCompatActivity
    implements GridView.OnItemClickListener{

    public static final String KEY_QUERY ="tah_query_pencarian" ;
    private static final String TAG ="SearchActivity" ;
    private String query;
    private RecyclerView recyclerView;
    private List<Pencarian> hasilPencarian= new ArrayList<>();
    private TextView tx_hasil;
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle save){
        super.onCreate(save);
        setContentView(R.layout.layout_hasil_pencarian);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
       // actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(save!=null){
            query = save.getString(KEY_QUERY);
        }else{
            query = getIntent().getStringExtra(KEY_QUERY);
        }
        recyclerView = (RecyclerView)findViewById(R.id.list_movie);
        recyclerView.setLayoutManager(new GridAutofitLayoutManager(this,0));
        tx_hasil = (TextView)findViewById(R.id.result);
        new sokCokotData().execute();
        recyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putString(KEY_QUERY, query);
        super.onSaveInstanceState(bundle);

    }


    private int jmlPage=0, total_result=0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Pencarian p = hasilPencarian.get(position);
        Log.e(TAG,p.getPoster());
        if(p.getMediaType().equals("movie")){
            Movie movie = new Movie(true);
            movie.setId(Integer.valueOf(p.getId()));
            movie.setCover(p.getPoster());
            Log.e(TAG,"tina Movie "+movie.getCover());
            movie.setTitle(p.getName());
            Intent intent = new Intent(SearchActivity.this, DetailMovieActivity.class);
            intent.putExtra("film",movie);
            startActivity(intent);
        }else {
            Actor actor = new Actor(true);
            actor.setmId(p.getId());
            actor.setPhoto(p.getPoster());
            actor.setmName(p.getName());
            Intent intent = new Intent(SearchActivity.this, DetailPeopleActivity.class);
            intent.putExtra("actor",actor);
            startActivity(intent);
        }

    }

    private  class sokCokotData extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String url = SearchClient.getInet(SearchClient.SEARCHING,"",query,1, SearchActivity.this);
            Log.e(TAG,url);
            JSONObject hasil= SearchClient.getJSONObject(SearchActivity.this,url);
            try{
                jmlPage = hasil.getInt("total_pages");
                total_result = hasil.getInt("total_results");
                List<Pencarian> ha= new ArrayList<>();
                JSONArray jsonArray = hasil.getJSONArray("results");
                if(jsonArray.length()>0){
                    for(int i=0;i<jsonArray.length();i++){
                        Pencarian pencarian = new Pencarian(jsonArray.getJSONObject(i));
                        ha.add(pencarian);
                    }
                }
                hasilPencarian = ha;
            }catch (JSONException e){
                Log.e(TAG,"error ngambil data "+e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            TampilGridCrew adapter = new TampilGridCrew(SearchActivity.this, hasilPencarian);
            recyclerView.setAdapter(adapter);
            tx_hasil.setText(total_result+" Result "+jmlPage+" Page");
        }
    }

    public class TampilGridCrew extends RecyclerView.Adapter<TampilGridCrew.Holder> {
        Context context;
        List<Pencarian> list;

        public TampilGridCrew(Context context, List<Pencarian> castings){
            this.context = context;
            this.list = castings;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.grid_layout, parent, false);
            Holder holder = new Holder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final Holder holder, int position) {
            final Pencarian p = list.get(position);
            holder.title.setText(p.getName());
            String t[] = p.getRelease().split("-");
            holder.year.setText(t[0]);
            Glide.with(context).load(SearchClient.getImagePath(context)+p.getPoster())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            holder.imageView.setImageDrawable(glideDrawable);
                            holder.imageView.setDrawingCacheEnabled(true);
                        }
                    });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(p.getMediaType().equals("movie")){
                        Movie movie = new Movie(true);
                        movie.setId(Integer.valueOf(p.getId()));
                        movie.setCover(p.getPoster());
                        Log.e(TAG,"tina Movie "+movie.getCover());
                        movie.setTitle(p.getName());
                        Intent intent = new Intent(SearchActivity.this, DetailMovieActivity.class);
                        intent.putExtra("film",movie);
                        startActivity(intent);
                    }else {
                        Actor actor = new Actor(true);
                        actor.setmId(p.getId());
                        actor.setPhoto(p.getPoster());
                        actor.setmName(p.getName());
                        Intent intent = new Intent(SearchActivity.this, DetailPeopleActivity.class);
                        intent.putExtra("actor",actor);
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        public class Holder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView title, year;
            public Holder(View itemView) {
                super(itemView);
                imageView = (ImageView)itemView.findViewById(R.id.image);
                title = (TextView)itemView.findViewById(R.id.title);
                year =(TextView)itemView.findViewById(R.id.tahun);
            }
        }
    }
}
