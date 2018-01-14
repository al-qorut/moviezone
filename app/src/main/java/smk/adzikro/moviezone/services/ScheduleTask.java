package smk.adzikro.moviezone.services;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

/**
 * Created by server on 10/18/17.
 */

public class ScheduleTask {
    private static final String TAG ="ScheduleTask" ;
    private GcmNetworkManager gcmNetworkManager;
    Context context;

    public ScheduleTask(Context context){
        this.context =context;
        gcmNetworkManager = GcmNetworkManager.getInstance(context);
    }

    public void cancelPeriodicTask(){
        if(gcmNetworkManager !=null)
        gcmNetworkManager.cancelTask(ScheduleService.RE_ONEOFF_TAG, ScheduleService.class);
    }

    public void createOneOfTask() {
        Log.e(TAG,"createOneOfTask");
        try {
            Task periodikTask = new PeriodicTask.Builder()
                    .setService(ScheduleService.class)
                    .setPeriod(60)
                    .setFlex(30)
                    .setTag(ScheduleService.RE_ONEOFF_TAG)
                    .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                    .setPersisted(true)
                    .build();
            gcmNetworkManager.schedule(periodikTask);
        }catch (Exception e){ e.printStackTrace();}
    }
    public void scheduleUpdateWidget(){
        Log.e(TAG,"scheduleUpdateWidget");
        try {
            PeriodicTask periodic = new PeriodicTask.Builder()
                    .setService(ScheduleService.class)
                    .setPeriod(6)
                    .setFlex(2)
                    .setTag(ScheduleService.RE_UPDATE_WIDGET)
                    .setPersisted(true)
                    .setUpdateCurrent(true)
                    .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                    .setRequiresCharging(false)
                    .build();
            gcmNetworkManager.schedule(periodic);
            Log.v(TAG, "repeating task scheduled update widget");
        } catch (Exception e) {
            Log.e(TAG, "scheduling failed");
            e.printStackTrace();
        }
    }
    public void scheduleRepeat() {
        Log.e(TAG,"scheduleRepeat");
        try {
            PeriodicTask periodic = new PeriodicTask.Builder()
                    .setService(ScheduleService.class)
                    .setPeriod(6)
                    .setFlex(2)
                    .setTag(ScheduleService.RE_REPEAT_TAG)
                    .setPersisted(true)
                    .setUpdateCurrent(true)
                    .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                    .setRequiresCharging(false)
                    .build();
            gcmNetworkManager.schedule(periodic);
            Log.v(TAG, "repeating task scheduled");
        } catch (Exception e) {
            Log.e(TAG, "scheduling failed");
            e.printStackTrace();
        }
    }

    public void cancelOneOff() {
        GcmNetworkManager
                .getInstance(context)
                .cancelTask(ScheduleService.RE_ONEOFF_TAG, ScheduleService.class);
    }

    public void cancelRepeat() {
        GcmNetworkManager
                .getInstance(context)
                .cancelTask(ScheduleService.RE_REPEAT_TAG, ScheduleService.class);
    }

    public void cancelAll() {
        GcmNetworkManager
                .getInstance(context)
                .cancelAllTasks(ScheduleService.class);
    }
}
