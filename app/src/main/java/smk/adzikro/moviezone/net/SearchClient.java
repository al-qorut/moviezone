package smk.adzikro.moviezone.net;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.loopj.android.http.AsyncHttpClient;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import smk.adzikro.moviezone.BuildConfig;
import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.databasehelper.MovieDatabase;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.objek.Tv;
import smk.adzikro.moviezone.provider.MovieFavorites;

/**
 * Created by server on 11/1/17.
 */

public class SearchClient  {
//http://www.omdbapi.com/?t=Iron+Man&y=&plot=short&tomatoes=true&r=json
    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String IMG_URL = "https://image.tmdb.org/t/p/";
    public static final int UPCOMING =0;//"movie/upcoming?";
    public static final int NOWPLAYING =1;//"movie/now_playing?";
    public static final int SEARCHING =2;//search/movie?";
    public static final int DETAIL =3;///movie/id";
    public static final int TOPRATE =4;///movie/id";
    public static final int POPULAR =5;///movie/id";
    public static final int TVAIRINGTODAY =6;//"movie/upcoming?";
    public static final int TVONTHEAIR =7;//"movie/now_playing?";
    public static final int TVTOPRATE =8;///movie/id";
    public static final int TVPOPULAR =9;///movie/id";
    public static final int TVDETAIL =10;///movie/id";
    public static final int CARIGAMBAR =11;///movie/id";
    public static final int MOVIEREVIEW =12;///movie/id";
    public static final int DETAILPEOPLE =13;///movie/id";
    public static final int PEOPLE_ONTV =14;///movie/id";
    public static final int EPISODE_ONTV =15;///movie/id";
    public static final int LISTPOPULARPERSON =16;
    private static final String BASE_GOOGLE_URL = "https://www.googleapis.com/customsearch/v1?";
    private static final String API_GOOGLE = BuildConfig.API_GOOGLE;
    private static final String CX_KEY = BuildConfig.CX_GOOGLE;
    public static int[] poster_size = {92, 154, 185, 342, 500, 780};
    private static OkHttpClient mOkHttpClient;
    private AsyncHttpClient client;

    public SearchClient() {
        //  this.client = new AsyncHttpClient();
    }

    public static String getImagePath(Context context){
        String x = getKualitasImage(context);
        String h="";
        switch (x){
            case "1":
                h="w"+poster_size[2];
                break;
            case "2":
                h="w"+poster_size[1];
                break;
            case "3":
                h="w"+poster_size[0];
                break;
            }
        return IMG_URL+h;
    }

    public static String getImagePathBesar(Context context){
        String x = getKualitasImage(context);
        String h="";
        switch (x){
            case "1":
                h="w"+poster_size[5];
                break;
            case "2":
                h="w"+poster_size[4];
                break;
            case "3":
                h="w"+poster_size[3];
                break;
            }
        return IMG_URL+h;
    }

    private static String getApiUrlMovie(int aksi, int page, String id_movie){
        String hasil = "";
        switch (aksi){
            case 0:
                hasil = BASE_MOVIE_URL+"movie/upcoming?api_key="+API_KEY+"&language=en-US&page="+page;
                break;
            case 1:
                hasil = BASE_MOVIE_URL+"movie/now_playing?api_key="+API_KEY+"&language=en-US&page="+page;
                break;
            case 2:
                //https://api.themoviedb.org/3/search/multi?api_key=2a3029c7168f6653ff50397f3aad9e3e&language=en-US&page=1&include_adult=t&query=leonardo
                hasil = BASE_MOVIE_URL+"search/multi?api_key="+API_KEY+"&language=en-US&page="+page;
                break;
            case 3:
                hasil = BASE_MOVIE_URL+"movie/"+id_movie+"?api_key="+API_KEY+"&language=en-US&append_to_response=releases,trailers,credits,images,similar_movies&include_image_language=null";
                break;
            case 4:
                hasil = BASE_MOVIE_URL+"movie/top_rated?api_key="+API_KEY+"&language=en-US&page="+page;
                break;
            case 5:
                hasil = BASE_MOVIE_URL+"movie/popular?api_key="+API_KEY+"&language=en-US&page="+page;
                break;
            case 6:
                hasil = BASE_MOVIE_URL+"tv/airing_today?api_key="+API_KEY+"&language=en-US&page="+page;
                break;
            case 7:
                hasil = BASE_MOVIE_URL+"tv/on_the_air?api_key="+API_KEY+"&language=en-US&page="+page;
                break;
            case 8:
                hasil = BASE_MOVIE_URL+"tv/top_rated?api_key="+API_KEY+"&language=en-US&page="+page;
                break;
            case 9:
                hasil = BASE_MOVIE_URL+"tv/popular?api_key="+API_KEY+"&language=en-US&page="+page;
                break;
            case 10:
                hasil = BASE_MOVIE_URL+"tv/"+id_movie+"?api_key="+API_KEY+"&language=en-US&append_to_response=releases,trailers,credits,images,similar,videos,season&include_image_language=en,null";
                break;
            case 11:
                hasil = BASE_MOVIE_URL+"collection/"+id_movie+"?api_key="+API_KEY+"&append_to_response=images";
                break;
            //http://api.themoviedb.org/3/movie/83542/reviews?api_key=2a3029c7168f6653ff50397f3aad9e3e
            case 12:
                hasil = BASE_MOVIE_URL+"movie/"+id_movie+"/reviews?api_key="+API_KEY+"&language=en-US";
                break;
            case 13:
                hasil = BASE_MOVIE_URL+"person/"+id_movie+"?api_key="+API_KEY+"&language=en-US&append_to_response=videos,trailers,credits,images,release";
                break;
            case 14:
                hasil = BASE_MOVIE_URL+"person/"+id_movie+"/tv_credits?api_key="+API_KEY+"&language=en-US";
                break;
            case 15:
                hasil = BASE_MOVIE_URL+"tv/"+id_movie+"/season/"+page+"?api_key="+API_KEY+"&language=en-US";
            case 16:
                hasil = BASE_MOVIE_URL+"person/popular?api_key="+API_KEY+"&language=en-US&page="+page;
                break;
        }
        return hasil;
    }

    public static String getInet(int aksi, String id_movie, final String query, int starPage, Context context){
        String url;
        try{
            if(aksi==SEARCHING) {
                if(getAdult(context))
                url = getApiUrlMovie(aksi, starPage,"0") + "&query="+URLEncoder.encode(query, "utf-8")+"&include_adult=true";
                else
                    url = getApiUrlMovie(aksi, starPage,"0") + "&query="+URLEncoder.encode(query, "utf-8")+"&include_adult=false";
            }else{
                url = getApiUrlMovie(aksi, starPage,id_movie);
            }

            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.search_not_found,Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static OkHttpClient getOkHttpClient(Context context) {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();

            File cacheDir = context.getCacheDir();
            Cache cache = new Cache(cacheDir, 2 * 1024 * 1024);
            mOkHttpClient.setCache(cache);
        }

        return mOkHttpClient;
    }

    public static boolean getAdult(Context context){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getBoolean("adult",true);
    }

    public static boolean getTampilan(Context context){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getBoolean("view",true);
    }

    public static int getTampil(Context context){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getInt("tampil",0);
    }

    public static void setTampil(Context context, int tampil){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor =preference.edit();
        editor.putInt("tampil",tampil);
        editor.apply();
    }

    public static String getKualitasImage(Context context){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getString("kualitasimage","1");
    }

    public static JSONObject getJSONObject(Context context, String url) {
        final OkHttpClient client = getOkHttpClient(context);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.code() >= 429) {
                // HTTP error 429 and above means that we've exceeded the query limit
                // for TMDb. Sleep for 5 seconds and try again.
                Thread.sleep(5000);
                response = client.newCall(request).execute();
            }
            return new JSONObject(response.body().string());
        } catch (Exception e) { // IOException and JSONException
            return new JSONObject();
        }
    }

    public static String getStringFromJSONObject(JSONObject json, String name, String fallback) {
        try {
            String s = json.getString(name);
            if (s.equals("null"))
                return fallback;
            return s;
        } catch (Exception e) {
            return fallback;
        }
    }

    public static void loadImage(Context context, String url, final ImageView imageView, final ProgressBar progressBar){
        Glide.with(context).load(SearchClient.getImagePathBesar(context)+url)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageDrawable(resource);
                        imageView.setDrawingCacheEnabled(true);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
    public static void removeFavorite(Context context, Movie p){
        Toast.makeText(context, p.getTitle() + " Favorite Movie has remove ", Toast.LENGTH_LONG).show();
        context.getContentResolver().delete(MovieFavorites.CONTENT_URI, MovieDatabase.FIELD_ID+"="+p.getId(),null);
    }
    public static void addMovieFavorite(Context context, Movie p){
        ContentValues values = new ContentValues();
        values.put(MovieDatabase.FIELD_ID, p.getId());
        values.put(MovieDatabase.FIELD_TITLE,p.getTitle());
        values.put(MovieDatabase.FIELD_OVERVIEW,p.getPlot());
        values.put(MovieDatabase.FIELD_POPULARITY,p.getPopularity());
        values.put(MovieDatabase.FIELD_POSTER,p.getCover());
        values.put(MovieDatabase.FIELD_RELEASE,p.getReleasedate());
        MovieDatabase movieDatabase = new MovieDatabase(context);
        if(movieDatabase.isMovie(p.getId())){
            Toast.makeText(context, p.getTitle() + " has added in Favorite Movie ", Toast.LENGTH_LONG).show();
            //context.getContentResolver().
            context.getContentResolver().delete(MovieFavorites.CONTENT_URI, MovieDatabase.FIELD_ID+"="+p.getId(),null);
        }else {
            context.getContentResolver().insert(MovieFavorites.CONTENT_URI, values);
            Toast.makeText(context, p.getTitle() + " succsess add to favorite", Toast.LENGTH_LONG).show();
        }
    }

    public static void addTvFavorite(Context context, Tv p){
        ContentValues values = new ContentValues();
        values.put(MovieDatabase.FIELD_ID, p.getId());
        values.put(MovieDatabase.FIELD_TITLE,p.getTitle());
        values.put(MovieDatabase.FIELD_OVERVIEW,p.getPlot());
        values.put(MovieDatabase.FIELD_POPULARITY,p.getRating());
        values.put(MovieDatabase.FIELD_POSTER,p.getPoster());
        values.put(MovieDatabase.FIELD_RELEASE,p.getReleaseDate());
        MovieDatabase movieDatabase = new MovieDatabase(context);
        if(movieDatabase.isMovie(Integer.valueOf(p.getId()))){
            Toast.makeText(context, p.getTitle() + " has added in Favorite TV Show ", Toast.LENGTH_LONG).show();
        }else {
            context.getContentResolver().insert(MovieFavorites.CONTENT_URI, values);
            Toast.makeText(context, p.getTitle() + " succsess add to favorite", Toast.LENGTH_LONG).show();
        }
    }

    public static void share(Context context,String url){
        try {
            //File sdcard = Environment.getExternalStorageDirectory();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String sAux ="Movie Zone\n "+context.getString(R.string.app_link);
            i.putExtra(Intent.EXTRA_TEXT, sAux);

            context.startActivity(Intent.createChooser(i, "Pilih.."));
        } catch (Exception e) { Log.e("share",e.toString());
        }
    }

    public static void shareLink(Context context,String url){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String sAux ="https://www.themoviedb.org/"+url+"\nMovie Zone\n"+context.getString(R.string.app_link);
            i.putExtra(Intent.EXTRA_TEXT, sAux);

            context.startActivity(Intent.createChooser(i, "Choose.."));
        } catch (Exception e) { Log.e("share",e.toString());
        }
    }

    public static String getToken(Context context){
        String token="";


        return token;
    }

    public static void saveImage(final Context context, final String url){
        Glide.with(context)
                .asBitmap()
                .load(getImagePathBesar(context)+url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull final Bitmap resource, @Nullable final Transition<? super Bitmap> transition) {
                        saveImg(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }
    private static void saveImg(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + image.toString() + ".jpg";
        File storageDir = new File(            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/movie_zone/image");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
          //  Toast.makeText(, "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
       // return savedImagePath;
    }
    public void write(Context context, String fileName, Bitmap bitmap) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
    private static void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
       // sendBroadcast(mediaScanIntent);
    }

    private String getApiUrlGoogle() {
        return BASE_GOOGLE_URL + "&cx=" + CX_KEY + "&key=" + API_GOOGLE;
    }
}
