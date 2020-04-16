package smk.adzikro.moviezone.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;

import smk.adzikro.moviezone.R;
import smk.adzikro.moviezone.adapter.FavoriteAdapter;
import smk.adzikro.moviezone.databasehelper.MovieDatabase;
import smk.adzikro.moviezone.objek.MovieFavorite;
import smk.adzikro.moviezone.provider.MovieFavorites;

/**
 * Created by server on 10/22/17.
 */

public class fragment_favorite extends Fragment {
    private static final String TAG = "Favorite";
    private FavoriteAdapter adapter;
    private RecyclerView listMovie;
    private ProgressBar loading;
    private ArrayList<MovieFavorite> listDataMovie;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.favorite_layout, container, false);
        setHasOptionsMenu(true);
        Log.e(TAG, "onCreateView");
        listMovie = (RecyclerView) view.findViewById(R.id.list_movie);
        loading = (ProgressBar) view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
      //  Button button = (Button) view.findViewById(R.id.refresh);
        listMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public void hapusFavorite(MovieFavorite movieFavorite) {
        final String select = "_id=?";
        final String argh[] = new String[]{""+movieFavorite.getId()};
        new AlertDialog.Builder(getContext())
                .setTitle(movieFavorite.getTitle()+" Deleted ")
                .setMessage("Are you sure ?")
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getActivity().getContentResolver().delete(MovieFavorites.CONTENT_URI,select,argh);
                        //onBackPressed();
                        // favoritRefresh();
                    }})
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

    }
    private ArrayList<MovieFavorite> getListDataMovie(){
        ArrayList<MovieFavorite> data= new ArrayList<>();
        Cursor cursor = getContext().getContentResolver().query(MovieFavorites.CONTENT_URI,null,null,null,null);
        try {
        //    Log.e(TAG,"Tah datana "+cursor.getCount());
            if (cursor != null) {
                while (cursor.moveToNext()) {
           //         Log.e(TAG,cursor.getString(cursor.getColumnIndex(MovieDatabase.FIELD_TITLE)));
                    MovieFavorite hashMap = new MovieFavorite(cursor.getInt(cursor.getColumnIndex(MovieDatabase.FIELD_ID)));
                    hashMap.setTitle(cursor.getString(cursor.getColumnIndex(MovieDatabase.FIELD_TITLE)));
                    hashMap.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieDatabase.FIELD_RELEASE)));
                    hashMap.setPoster_path(cursor.getString(cursor.getColumnIndex(MovieDatabase.FIELD_POSTER)));
                    hashMap.setOverview(cursor.getString(cursor.getColumnIndex(MovieDatabase.FIELD_OVERVIEW)));
                    hashMap.setPopularity(cursor.getString(cursor.getColumnIndex(MovieDatabase.FIELD_POPULARITY)));
                    data.add(hashMap);
                }
            }
            cursor.close();
        }catch (Exception e){}
        return data;
    }
    public void onRefresh(){
       new LoadData().execute();
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onResume(){
      //  Log.e(TAG,"onResume");
        new LoadData().execute();
        adapter.notifyDataSetChanged();
        super.onResume();
    }
    @Override
    public void onStart(){
        Log.e(TAG,"onStart");
        new LoadData().execute();
        super.onStart();
    }
    private class LoadData extends AsyncTask<Void, Void,Void> {

        @Override
        protected void onPreExecute()
        {
       //     Log.e(TAG,"onPreExecute");
            listMovie.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            adapter = new FavoriteAdapter(getContext());
        }

        @Override
        protected Void doInBackground(Void... params) {
         //   Log.e(TAG,"doInBackground");
            listDataMovie = getListDataMovie();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            adapter.setData(listDataMovie);
            adapter.notifyDataSetChanged();
            listMovie.setAdapter(adapter);
            loading.setVisibility(View.GONE);
            listMovie.setVisibility(View.VISIBLE);
        //    Log.e(TAG,"onPostExecute");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
      //  menuInflater.inflate(R.menu.menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }
}
