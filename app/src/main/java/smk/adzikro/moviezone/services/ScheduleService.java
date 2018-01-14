package smk.adzikro.moviezone.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import smk.adzikro.moviezone.BuildConfig;
import smk.adzikro.moviezone.activity.DetailMovieActivity;
import smk.adzikro.moviezone.activity.MainActivity;
import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.widget.StackWidgetService;

/**
 * Created by server on 10/18/17.
 */

public class ScheduleService extends GcmTaskService {

    public static final String TAG = "SheduleService";

    public static final String RE_ONEOFF_TAG = "sakali";
    public static final String RE_REPEAT_TAG = "ulang";
    public static final String RE_UPDATE_WIDGET = "update_widget";

    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.e(TAG,"onRunTask");
        if(isNotifHarian("allnotif")) {
            if (taskParams.getTag().equals(RE_ONEOFF_TAG)) {
            //    if (isNotifHarian("dailyremaider")) notifHarian();
            } else if (taskParams.getTag().equals(RE_REPEAT_TAG)) {
            //    if (isNotifHarian("upcomingremaider")) getLatestMovie();
            } else if (taskParams.getTag().equals(RE_UPDATE_WIDGET)) {
              //  getUpdateWidget();
            }
        }
        return GcmNetworkManager.RESULT_SUCCESS;
    }
    private boolean isNotifHarian(String apa){
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return preference.getBoolean(apa,true);
    }
    private void getUpdateWidget(){
        Log.e(TAG,"Update Widget");
        Intent intent = new Intent(getBaseContext(), StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 111);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.stack_poster_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.kosong_text);
    }

    private void getLatestMovie() {
        Log.e(TAG,"Show Notif Movie");
        Log.e(TAG,"Jalan");
        SyncHttpClient client = new SyncHttpClient();
        //String url = "https://api.themoviedb.org/3/movie/"+ID_MOVIE+"?api_key="+API_KEY+"&language=en-US";
        String url="https://api.themoviedb.org/3/movie/latest?api_key="+ BuildConfig.API_KEY+"&language&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String hasil = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(hasil);
                    String judulFilm = jsonObject.getString("title");
                    String deskrip = " Sekarang release";
                    String id = jsonObject.getString("id");
                    String message = judulFilm+"\n"+deskrip;
                    int notifId = 100;
                    showNotif(getApplicationContext(), judulFilm, message,notifId, id);
                }catch (Exception e){Log.e(TAG,e.getMessage());}
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG,"Gagal");
            }
        });
    }
    private void notifHarian(){
        Log.e(TAG,"Show Notif Harian");
        showNotif(getApplicationContext(), "Movie Zone", "Hallo kemana saja..",101,"");
    }
    private void showNotif(Context context, String title, String message, int notifId, String id) {
        NotificationManager notif = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent;
        if(notifId==100) {
            intent = new Intent(context, DetailMovieActivity.class);
            intent.putExtra("id",id);
        }else{
            intent = new Intent(context, MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        NotificationCompat.Builder build = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_emoticon)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.white))
                .setVibrate(new long[]{1000,1000,1000,1000})
                .setSound(alarmSound);
        build.setContentIntent(pendingIntent);
        notif.notify(notifId,build.build());
    }

    @Override
    public void onInitializeTasks(){
        super.onInitializeTasks();
        ScheduleTask scheduleTask = new ScheduleTask(this);
        scheduleTask.scheduleRepeat();
        scheduleTask.createOneOfTask();
        scheduleTask.scheduleUpdateWidget();
    }




}
