package smk.adzikro.moviezone.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by server on 10/23/17.
 */

public class StackWidgetService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteFactory(this.getApplicationContext(),intent);
    }
}
