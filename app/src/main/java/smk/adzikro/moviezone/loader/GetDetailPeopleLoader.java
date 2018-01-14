package smk.adzikro.moviezone.loader;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Actor;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.objek.Tv;

/**
 * Created by server on 11/18/17.
 */

public class GetDetailPeopleLoader extends AsyncTaskLoader<Actor> {

    private static final String TAG ="getDetailPeopleLoader" ;
    private Actor actor;
    private boolean hasResult=false;
    Context context;

    public GetDetailPeopleLoader(Context context, Actor actor) {
        super(context);
        Log.e(TAG,"onCreate "+actor.getmId());
        this.context=context;
        this.actor = actor;
        onContentChanged();
    }

    @Override
    public Actor loadInBackground() {
        Log.e(TAG,"onLoadInBackground "+actor.getmId());
        String url = SearchClient.getInet(SearchClient.DETAILPEOPLE,actor.getmId(),"",0,context);
        //Log.e(TAG,"onLoadInBackground "+url);
        JSONObject object = SearchClient.getJSONObject(context,url);
        Actor actor1 = new Actor(object);

        try {
            String url1 = SearchClient.getInet(SearchClient.PEOPLE_ONTV,actor.getmId(),"",0,context);
          //  Log.e(TAG,"onAmbilDataTv "+url1);
            JSONObject jsonObject = SearchClient.getJSONObject(context,url1);
            JSONArray jsonArray = jsonObject.getJSONArray("cast");
            if(jsonArray.length()>0){
                List<Tv> acaraTv = new ArrayList<>();
                for (int i=0;i<jsonArray.length();i++){
                    Tv tv = new Tv(jsonArray.getJSONObject(i));
                    acaraTv.add(tv);
                }
                actor1.setAcaraTv(acaraTv);
            }
        }catch (JSONException e){
            Log.e(TAG,"Erot ngambil data tv "+e.getMessage());
        }
        return actor1;
    }

    @Override
    public void onStartLoading(){
        Log.e(TAG,"onStartLoading ");
        if(takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(actor);
    }

    @Override
    public void deliverResult(final Actor data){
        Log.e(TAG,"onDeliverResult ");
        actor = data;
        hasResult = true;
        super.deliverResult(data);
    }
    @Override
    protected  void onReset(){
        super.onReset();
        onStopLoading();
        if(hasResult){
            onReleaseResources(actor);
            actor = null;
            hasResult = false;
        }
    }
    protected void onReleaseResources(Actor data) {
        //nothing to do.
    }

}
