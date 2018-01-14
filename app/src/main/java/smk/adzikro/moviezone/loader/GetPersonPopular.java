package smk.adzikro.moviezone.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Actor;

/**
 * Created by server on 11/22/17.
 */

public class GetPersonPopular extends AsyncTaskLoader<ArrayList<Actor>> {
    private ArrayList<Actor> mData;
    public boolean hasResult = false;
    private static final String TAG ="GetPersonPopular";
    private int page=1;
    private Context context;

    public GetPersonPopular(Context context, int page) {
        super(context);
        this.context = context;
        this.page = page;
        Log.e(TAG,"onCeate");
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
    public void deliverResult(final  ArrayList<Actor> data){
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
    protected void onReleaseResources(ArrayList<Actor> data) {
        //nothing to do.
    }

    public ArrayList<Actor> getResult() {
        return mData;
    }

    @Override
    public ArrayList<Actor> loadInBackground() {
        Log.e(TAG,"onLoadInBackGround");
        ArrayList<Actor> arrayList = new ArrayList<>();
        String url = SearchClient.getInet(SearchClient.LISTPOPULARPERSON,"0","",page,context);
        Log.e(TAG, url);
        JSONObject object = SearchClient.getJSONObject(context,url);
        try {
            JSONArray array = object.getJSONArray("results");
            if(array.length()>0){
                for(int i=0;i<array.length();i++){
                    Actor actor = new Actor(array.optJSONObject(i),false);
                 //   Log.e(TAG,actor.getmName());
                    arrayList.add(actor);
                }
            }
        }catch (JSONException e){
            Log.e("PEOPle",e.getMessage());
        }

        return arrayList;
    }
}

