package smk.adzikro.moviezone.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import smk.adzikro.moviezone.fragments.FragmentPersonPopular;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Movie;

/**
 * Created by server on 10/19/17.
 */

public class GetMovieTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {
    private ArrayList<Movie> mData;
    public boolean hasResult = false;
    private static final String TAG ="GetMovieTaskLoader";
    private String txtCariMovie;
    private int code=0;
    private int page=1, totPage;
    private String id_movie;
    private  Context context;

    public GetMovieTaskLoader(final Context context, String txtCariMovie, int code, int page, String id_movie){
        super(context);
        this.txtCariMovie = txtCariMovie;
        this.context = context;
        this.code = code;
        this.page = page;
        this.id_movie = id_movie;
        onContentChanged();
    }
    @Override
    public void onStartLoading(){
        if(takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final  ArrayList<Movie> data){
        mData = data;
        hasResult = true;
        super.deliverResult(data);
    }
    @Override
    protected  void onReset(){
        super.onReset();
        onStopLoading();
        if(hasResult){
            onReleaseResources(mData);
            mData = null;
            hasResult = false;
        }
    }
    public void clearData(){
        mData.clear();
    }
    protected void onReleaseResources(ArrayList<Movie> data) {
        //nothing to do.
    }

    public ArrayList<Movie> getResult() {
        return mData;
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        final ArrayList<Movie> movies = new ArrayList<>();
        String url= SearchClient.getInet(code, id_movie, txtCariMovie, page, getContext());
        Log.e(TAG,url);
        JSONObject object = SearchClient.getJSONObject(context, url);

        try {
            totPage = object.getInt("total_pages");
            if(page<=totPage) {
                JSONArray isi = object.getJSONArray("results");
                if (isi.length() > 0) {
                    for (int i = 0; i < isi.length(); i++) {
                        Movie movie = new Movie(isi.getJSONObject(i));
                        movies.add(movie);
                    }
                }
            }
        }catch (JSONException e){
            Log.e(TAG,"erro "+e.getMessage());
        }

        return movies;
    }

    
}
