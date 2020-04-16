package smk.adzikro.moviezone.interfaces;

import android.view.MotionEvent;

import java.util.List;

import smk.adzikro.moviezone.objek.Movie;

public interface MovieListener {
     void onShowLoading();
     void onHideLoading();
     void onError(String eror);
     List<Movie> onLoadMovie();
}
