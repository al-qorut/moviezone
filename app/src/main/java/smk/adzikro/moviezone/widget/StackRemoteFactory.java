package smk.adzikro.moviezone.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import smk.adzikro.moviezone.BuildConfig;
import smk.adzikro.moviezone.R;

/**
 * Created by server on 10/23/17.
 */

public class StackRemoteFactory implements
        RemoteViewsService.RemoteViewsFactory{
    private static final String TAG ="StackRemoteFactory" ;
    private List<Bitmap> mBit = new ArrayList<>();
    private List<String> title = new ArrayList<>();
    private List<String> tgl = new ArrayList<>();
    private List<Integer> id = new ArrayList<>();
  //  private ArrayList<Movie> mData = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;

    public StackRemoteFactory (Context context, Intent intent){
        Log.e(TAG,"StackRemoteFactory");
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate");
       // tambah gambar didieu
      /*  mBit.add(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.cooding));
        mBit.add(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.dicoding_default));
        mBit.add(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_ab_search));
        mBit.add(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_action_settings));
        mBit.add(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_download));
        mBit.add(BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_emoticon)); */
     //   String path = Environment.getExternalStorageDirectory().toString()+"/Moviezone/gambar";
      //  Log.d(TAG, "Path: " + path);
      //  File directory = new File(path);
      //  File[] files = directory.listFiles();
      //  Log.d(TAG, "Size: "+ files.length);
      //  for (int i = 0; i < files.length; i++)
      //  {
        //    Log.d(TAG, "FileName:" + files[i].getName());
            //mBit.add(files[i].getName());
       //     mBit.add(i,BitmapFactory.decodeFile(path+"/"+files[i].getName()));
      //  }
    }

    @Override
    public void onDataSetChanged() {
        getLatestMovie();
    }
    private void getLatestMovie() {
        SyncHttpClient client = new SyncHttpClient();

        String url="https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.API_KEY + "&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart(){
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String hasil = new String(responseBody);
                    JSONObject object = new JSONObject(hasil);
                    JSONArray list = object.getJSONArray("results");
                    for(int i=0 ; i<list.length();i++){
                        JSONObject getmovie = list.getJSONObject(i);
                       // Movie movie = new Movie(getmovie);
                       // mData.add(movie);
                      //  Log.e(TAG,"Tah "+movie.getTitle());
                        mBit.add(i, getGb("http://image.tmdb.org/t/p/w300" +getmovie.getString("poster_path")));
                        title.add(i, getmovie.getString("title"));
                        tgl.add(i, getmovie.getString("release_date"));
                        id.add(i, getmovie.getInt("id"));
                      //  Log.e(TAG,getmovie.getString("poster_path"));
                       // Movie movie = new Movie(getmovie);
                       // movies.add(movie);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(TAG,e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private Bitmap getGb(String url){
        Bitmap gb = null;
        try {
            gb = Glide.with(mContext)
                    .load(url)
                    .asBitmap()
                    .error( new ColorDrawable(mContext.getResources().getColor(R.color.nav_bottom)))
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        }catch (InterruptedException | ExecutionException e){
            Log.e(TAG,"Widget load error");
        }
        return gb;
    }
    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return tgl.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rt = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rt.setImageViewBitmap(R.id.image_view, mBit.get(position));
        rt.setTextViewText(R.id.tgl_release, tgl.get(position));
        rt.setTextViewText(R.id.txt_title, title.get(position));
       // Bitmap gb = getGb("http://image.tmdb.org/t/p/w128" +mData.get(position).getPoster_path());
       // rt.setImageViewBitmap(R.id.image_view, gb);
       // rt.setTextViewText(R.id.tgl_release, mData.get(position).getRelease_date());
       // rt.setTextViewText(R.id.txt_title, mData.get(position).getTitle());
       // Log.e(TAG,"Tah getViewat "+mData.get(position).getTitle());
        Bundle extra = new Bundle();
        extra.putInt(StackPosterWidget.EXTRA_ITEM, position);
        extra.putInt(StackPosterWidget.EXTRA_ID_MOVIE, id.get(position));
        Intent fill = new Intent();
        fill.putExtras(extra);
        rt.setOnClickFillInIntent(R.id.image_view, fill);
        return rt;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
