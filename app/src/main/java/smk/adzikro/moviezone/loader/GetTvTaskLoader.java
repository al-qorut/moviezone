package smk.adzikro.moviezone.loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Movie;
import smk.adzikro.moviezone.objek.Tv;

/**
 * Created by server on 11/15/17.
 */

public class GetTvTaskLoader extends AsyncTaskLoader<ArrayList<Tv>> {
    ArrayList<Tv> mData = new ArrayList<>();
    public boolean hasResult = false;
    private static final String TAG ="GetTvTaskLoader";
    private int code=SearchClient.TVAIRINGTODAY;
    private int page=1;
    private Context context;

    public GetTvTaskLoader(Context context, int aksi, int page) {
        super(context);
        this.code=aksi;
        this.page=page;
        this.context = context;
        Log.e(TAG,"onCreate");
        onContentChanged();
    }
    @Override
    public void onStartLoading(){
        Log.e(TAG,"onStartLoading");
        if(takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(final  ArrayList<Tv> data){
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
    protected void onReleaseResources(ArrayList<Tv> data) {
        //nothing to do.
    }

    public ArrayList<Tv> getResult() {
        return mData;
    }


    @Override
    public ArrayList<Tv> loadInBackground() {
       String url = SearchClient.getInet(code,"","",page,context);
     //  Log.e(TAG,url);
       JSONObject jsonObject = SearchClient.getJSONObject(context,url);
       ArrayList<Tv> tvs = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            if(jsonArray.length()>0) {
                for (int i=0;i<jsonArray.length();i++) {
                  //  Log.e(TAG,""+jsonArray.getJSONObject(i));
                    Tv tv = new Tv(jsonArray.getJSONObject(i));
                    tvs.add(tv);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return tvs;
    }
}
