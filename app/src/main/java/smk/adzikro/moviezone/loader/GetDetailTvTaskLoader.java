package smk.adzikro.moviezone.loader;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONObject;

import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.objek.Tv;

/**
 * Created by server on 11/20/17.
 */

public class GetDetailTvTaskLoader extends AsyncTaskLoader<Tv> {
    private static final String TAG ="GetdetailTvLoader" ;
    public boolean hasResult = false;
    Context context;
    private Tv tv;

    public GetDetailTvTaskLoader(Context context, Tv tvx) {
        super(context);
        this.context = context;
        this.tv = tvx;
        onContentChanged();
    }
    @Override
    public void onStartLoading(){
        Log.e(TAG,"onStartLoading "+tv.getId());
        if(takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(tv);
    }

    @Override
    public void deliverResult(final Tv data){
        Log.e(TAG,"onDeliverResult "+tv.getId());
        tv = data;
        hasResult = true;
        super.deliverResult(data);
    }
    @Override
    protected  void onReset(){
        super.onReset();
        onStopLoading();
        if(hasResult){
            onReleaseResources(tv);
            tv = null;
            hasResult = false;
        }
    }
    protected void onReleaseResources(Tv data) {
        //nothing to do.
    }

    public Tv getResult() {
        return tv;
    }

    @Override
    public Tv loadInBackground() {
        String id = tv.getId();
        String url = SearchClient.getInet(SearchClient.TVDETAIL,id,"",0,context);
        Log.e(TAG,"Pertama "+id);
        JSONObject jsonObject = SearchClient.getJSONObject(context,url);
        Tv tv1 = tv;
        tv1.addDetail(jsonObject);

        //getEpisode
        int last = Integer.valueOf(tv1.getLastseason());
        url = SearchClient.getInet(SearchClient.EPISODE_ONTV,id,"",last,context);
     //   Log.e(TAG,"Kadua "+url);
        jsonObject = SearchClient.getJSONObject(context, url);

        tv1.addSeason(jsonObject);
        return tv1;
    }
}
