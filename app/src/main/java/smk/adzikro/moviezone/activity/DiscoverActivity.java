package smk.adzikro.moviezone.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import smk.adzikro.moviezone.BuildConfig;
import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.custom.GridAutofitLayoutManager;
import smk.adzikro.moviezone.fragments.FragmentPeopleCrew;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.objek.Tv;

/**
 * Created by server on 11/27/17.
 */

public class DiscoverActivity extends AppCompatActivity {
    private static final String TAG ="Discover" ;
    Spinner type, sampai, genres, sort;
    String url, var_type="discover/movie?api_key="+ BuildConfig.API_KEY,
            var_tahun, var_genre, var_sort="";
    String sort_value[];
    ArrayList<Movie> listMovie = new ArrayList<>();
    ArrayList<Tv> listTv = new ArrayList<>();
    RecyclerView recyclerView;
    boolean pertama=true;
    ProgressBar loading;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.layout_discover);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        type = findViewById(R.id.type);
        sampai = findViewById(R.id.sampai);
        genres = findViewById(R.id.genre);
        sort = findViewById(R.id.sort);
        loading = findViewById(R.id.loading);
        recyclerView = findViewById(R.id.list_movie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridAutofitLayoutManager(this,0));
        init();
        new AmbilData().execute();
    }

    private void init(){
        ArrayList<String> listDari = new ArrayList<>();
        ArrayList<String> listGenre = new ArrayList<>();
        sort_value =getResources().getStringArray(R.array.sort_value);
        Date date = new Date();
        CharSequence th = DateFormat.format("yyyy", date.getTime());
        var_tahun = "&year="+th;
        int tahun = Integer.valueOf((String) th);
        for (int i=tahun+5; i>=1900;i--){
            listDari.add(""+i);
        }

        listGenre.add("All");
        for(Map.Entry<String, Integer> entry:Movie.genre.entrySet()){
             listGenre.add(entry.getKey().substring(0,1).toUpperCase()+entry.getKey().substring(1).toLowerCase());
        }
        ArrayAdapter<String> tahunlits =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listDari);
        ArrayAdapter<String> genrelits =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listGenre);
        sampai.setAdapter(tahunlits);
        sampai.setSelection(5);
        genres.setAdapter(genrelits);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    var_type="discover/movie?api_key="+ BuildConfig.API_KEY;
                }else{
                    var_type="discover/tv?api_key="+ BuildConfig.API_KEY;
                }
                if(!pertama)new AmbilData().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sampai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                var_tahun = "&primary_release_year="+sampai.getSelectedItem().toString();
                if(!pertama)new AmbilData().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        genres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    var_genre ="";
                }else {
                    var_genre = "&with_genres="+getValueGenres(genres.getSelectedItem().toString());
                }
                url = SearchClient.BASE_MOVIE_URL + var_type + var_tahun + var_genre;
                Log.e(TAG,url);
                if(!pertama)new AmbilData().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                var_sort = "&sort_by="+sort_value[position];
                url = SearchClient.BASE_MOVIE_URL + var_type + var_tahun + var_genre +var_sort;
                if(!pertama)new AmbilData().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        url = SearchClient.BASE_MOVIE_URL + var_type + var_tahun + var_genre +var_sort;
        Log.e(TAG,url);
    }

    private String getUrl(){
        String xUrl = SearchClient.BASE_MOVIE_URL + var_type + var_tahun;
        if(genres.getSelectedItemPosition()!=0) {
            var_genre = "&with_genres="+getValueGenres(genres.getSelectedItem().toString());
            xUrl = xUrl+var_genre;
        }
        var_sort = "&sort_by="+sort_value[sort.getSelectedItemPosition()];
        xUrl = xUrl+var_sort;
        Log.e(TAG, xUrl);
        return xUrl;
    }

    public static int getValueGenres(String gen_id) {
        for (Map.Entry<String, Integer> entry : Movie.genre.entrySet()) {
            if (entry.getKey().toUpperCase().equals(gen_id.toUpperCase())) {
                return entry.getValue();
            }
        }
        return 28;
    }
    private int getListType(){
        return type.getSelectedItemPosition();
    }
    private ArrayList<Movie> getListMovie(){
        ArrayList<Movie> movieArrayList= new ArrayList<>();
        JSONObject object = SearchClient.getJSONObject(this, getUrl());
        try {
            JSONArray array = object.getJSONArray("results");
            if(array.length()>0){
                for (int i=0;i<array.length();i++){
                    Movie movie = new Movie(array.getJSONObject(i));
                    movieArrayList.add(movie);
                }
            }
        }catch (JSONException e){
            Log.e(TAG,e.getMessage());
        }
        return movieArrayList;
    }

    private ArrayList<Tv> getListTv(){
        ArrayList<Tv> tvArrayList= new ArrayList<>();
        JSONObject object = SearchClient.getJSONObject(this, getUrl());
        try {
            JSONArray array = object.getJSONArray("results");
            if(array.length()>0){
                for (int i=0;i<array.length();i++){
                    Tv tv = new Tv(array.getJSONObject(i));
                    tvArrayList.add(tv);
                }
            }
        }catch (JSONException e){
            Log.e(TAG,e.getMessage());
        }
        return tvArrayList;
    }
    public class GridDataHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public GridDataHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            textView = (TextView) view.findViewById(R.id.title);
        }
    }
    ListMovie adapter;
    private class AmbilData extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute(){
            loading.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            type.setEnabled(false);
            sampai.setEnabled(false);
            genres.setEnabled(false);
            sort.setEnabled(false);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            if(type.getSelectedItemPosition()==0){
               listMovie=getListMovie();
            }else{
                listTv = getListTv();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void d){
            loading.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            type.setEnabled(true);
            sampai.setEnabled(true);
            genres.setEnabled(true);
            sort.setEnabled(true);
            if(pertama){
                pertama =false;
                adapter = new ListMovie(DiscoverActivity.this, getListType());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else{
                adapter.setType(type.getSelectedItemPosition());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class ListMovie extends RecyclerView.Adapter<GridDataHolder>{
        public static final int LIST_MOVIE=0;
        public static final int LIST_TV=1;
        Context context;
        int type;

        public ListMovie(Context context, int type){
            this.context = context;
            this.type = type;
        }
        public void setType(int type){
            this.type = type;
        }
        @Override
        public GridDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.grid_layout,parent,false);
            GridDataHolder holder = new GridDataHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(GridDataHolder holder, int position) {
            if(type==LIST_MOVIE) {
                final Movie movie = listMovie.get(position);
                Glide.with(context).load(SearchClient.getImagePath(context) + movie.getCover())
                        .thumbnail(0.7f)
                        .into(holder.imageView);
                holder.textView.setText(movie.getTitle() + "\n" + movie.getYear());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DetailMovieActivity.class);
                        intent.putExtra(DetailMovieActivity.KEY_KIRIMAN, movie);
                        startActivity(intent);
                    }
                });
            }else{
                final Tv movie = listTv.get(position);
                Glide.with(context).load(SearchClient.getImagePath(context) + movie.getPoster())
                        .thumbnail(0.7f)
                        .into(holder.imageView);
                String t[] = movie.getReleaseDate().split("-");
                holder.textView.setText(movie.getTitle() + "\n" + t[0]);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DetailTvActivity.class);
                        intent.putExtra(DetailTvActivity.DETAIL_TV, movie);
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return type==LIST_MOVIE?listMovie.size():listTv.size();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
       // getMenuInflater().inflate(R.menu.menu_poster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int m = item.getItemId();
        if(m==android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}
