package smk.adzikro.moviezone.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import smk.adzikro.moviezone.activity.DetailMovieActivity;
import smk.adzikro.moviezone.R;

/**
 * Implementation of App Widget functionality.
 */
public class StackPosterWidget extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "smk.adzikro.moviezone.widget.EXTRA_ITEM" ;
    public static final String EXTRA_ID_MOVIE = "smk.adzikro.moviezone.widget.EXTRA_ID_MOVIE" ;
    public static final String TOAST_ACTION = "smk.adzikro.moviezone.widget.TOAST_ACTION" ;
    private static final String TAG ="StackPosterWidget" ;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.e(TAG,"updateAppWidget");
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stack_poster_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.kosong_text);

        Intent toasInten = new Intent(context, StackPosterWidget.class);
        toasInten.setAction(StackPosterWidget.TOAST_ACTION);
        toasInten.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent tosPending = PendingIntent.getBroadcast(context,0,toasInten,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, tosPending);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i=0; i<appWidgetIds.length;i++) {
            Log.e(TAG,"onUpdate");
            /*RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stack_poster_widget);
            Intent intentDetail = new Intent(context, DetailMovieActivity.class);
            PendingIntent pendingInten = PendingIntent.getActivity(context,0,intentDetail,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.image_view,pendingInten); */
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    @Override
    public void onReceive(Context c, Intent i){
        if(i.getAction().equals(TOAST_ACTION)){
            int viewIndex = i.getIntExtra(EXTRA_ITEM, 0);
            int idMovie = i.getIntExtra(EXTRA_ID_MOVIE, 0);
            Toast.makeText(c,"Touched view "+viewIndex+" tah id Movie "+idMovie, Toast.LENGTH_LONG).show();
            Log.e(TAG,"Touched view "+viewIndex+" tah id Movie "+idMovie);
            Intent intent = new Intent(c, DetailMovieActivity.class);
            intent.putExtra("id",""+idMovie);
            c.startActivity(intent); /*

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c);
            RemoteViews views = new RemoteViews(c.getPackageName(),
                    R.layout.stack_poster_widget);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
            Intent intentDetail = new Intent(c, DetailMovieActivity.class);
            PendingIntent pendingInten = PendingIntent.getActivity(c,0,intentDetail,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.image_view,pendingInten);
            appWidgetManager.updateAppWidget(appWidgetId, views);*/

        }
        super.onReceive(c,i);
    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

