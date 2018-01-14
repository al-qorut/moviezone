package smk.adzikro.moviezone.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Actor;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.objek.OtherFromDirector;
import smk.adzikro.moviezone.objek.Review;
import smk.adzikro.moviezone.objek.SimilarMovie;
import smk.adzikro.moviezone.objek.Trailer;

/**
 * Created by server on 11/16/17.
 */

public class GetDetailMovieTaskLoader extends AsyncTaskLoader<Movie> {
    private static final String TAG ="GetdetailMovieLoader" ;
    private Movie movie;
    public boolean hasResult = false;
    Context context;

    public GetDetailMovieTaskLoader(Context context, Movie movie) {
        super(context);
        this.context = context;
        this.movie=movie;
        Log.e(TAG,"onConsuctor "+movie.getId());
        onContentChanged();
    }
    @Override
    public void onStartLoading(){
        Log.e(TAG,"onStartLoading "+movie.getId());
        if(takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(movie);
    }

    @Override
    public void deliverResult(final Movie data){
        Log.e(TAG,"onDeliverResult "+movie.getId());
        movie = data;
        hasResult = true;
        super.deliverResult(data);
    }
    @Override
    protected  void onReset(){
        super.onReset();
        onStopLoading();
        if(hasResult){
            onReleaseResources(movie);
            movie = null;
            hasResult = false;
        }
    }
    protected void onReleaseResources(Movie data) {
        //nothing to do.
    }

    public Movie getResult() {
        return movie;
    }

    @Override
    public Movie loadInBackground() {
        JSONObject jsonObject = SearchClient.getJSONObject(context, SearchClient.getInet(SearchClient.DETAIL,String.valueOf(movie.getId()),"",0,context));
        Movie moviex = movie;
        moviex.addMovieComplete(jsonObject);

        //Ambil Gambar
        if(moviex.getCollectionId()!=null) {
            Log.e(TAG, "Tah id na" + moviex.getCollectionId());
            try {
                ArrayList<String> img = new ArrayList<>();
                JSONObject jsonImage = SearchClient.getJSONObject(context, SearchClient.getInet(SearchClient.CARIGAMBAR, moviex.getCollectionId(), "", 0, context));
                img.add(jsonImage.getString("poster_path"));
                img.add(jsonImage.getString("backdrop_path"));
                JSONArray jsonArray = jsonImage.getJSONObject("images").getJSONArray("backdrops");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        img.add(jsonArray.getJSONObject(i).getString("file_path"));
                    }
                    moviex.setListBackDrop(img);
                }
                jsonArray = jsonImage.getJSONObject("images").getJSONArray("posters");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        img.add(jsonArray.getJSONObject(i).getString("file_path"));
                    }
                    moviex.setListPoster(img);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Tah ada eroor dina ambil gambar " + e.getMessage());
            }
        }
        //Ambil Review
             try{
                String url = SearchClient.getInet(SearchClient.MOVIEREVIEW,String.valueOf(moviex.getId()),"",0,context);
                Log.e(TAG, url);
                JSONObject jsonReview = SearchClient.getJSONObject(context, url);
                JSONArray jsonR = jsonReview.getJSONArray("results");
               if(jsonR.length()>0){
                   List<Review> reviews = new ArrayList<>();
                   for(int i=0;i<jsonR.length();i++){
                       Review review = new Review(jsonR.getJSONObject(i));
                       reviews.add(review);
                   }
                   moviex.setReviews(reviews);
               }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG,"Tah ada eroor dina ambil review "+e.getMessage());
            }
        try{
            String url = SearchClient.getInet(SearchClient.DETAILPEOPLE,moviex.getIdDirector(),"",0,context);
            JSONObject object = SearchClient.getJSONObject(context, url);
            JSONArray array = object.getJSONObject("credits").getJSONArray("crew");
            if(array.length()>0){
                ArrayList<OtherFromDirector> oe = new ArrayList<>();
                for(int i=0;i<array.length();i++){
                    if(array.getJSONObject(i).getString("job").equals("Director")) {
                        OtherFromDirector oo = new OtherFromDirector(array.getJSONObject(i));
                        oe.add(oo);
                    }
                }
                moviex.setFromDirector(oe);
            }
        }catch (JSONException e){
            Log.e(TAG,"Error ambil Data direcvtor");
        }
        return moviex;
    }
}
