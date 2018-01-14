package smk.adzikro.moviezone.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import smk.adzikro.moviezone.databasehelper.MovieDatabase;

/**
 * Created by server on 10/22/17.
 */

public class MovieFavorites extends ContentProvider {
    private static final String AUTHORITY ="smk.adzikro.moviezone.provider.MovieFavorites";
    private static final String BASE_PATH ="data";
    public static final Uri CONTENT_URI= Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH);
    private static final int MOVIE_ID=1;
    private static final int MOVIE_TITLE=2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String TAG ="ContenProvider" ;

    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH,MOVIE_ID);
        uriMatcher.addURI(AUTHORITY,BASE_PATH+"/#",MOVIE_TITLE);
    }
    private MovieDatabase movieDatabase;
    SQLiteDatabase database;
    @Override
    public boolean onCreate() {
        movieDatabase = new MovieDatabase(getContext());
        database = movieDatabase.openDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e(TAG,"manggil Query ku uri");
        Cursor cursor=null;
        if(uriMatcher.match(uri)==MOVIE_ID){
            cursor = database.query(MovieDatabase.TABLE_NAME, null,null,null,null,null,MovieDatabase.FIELD_ID+" DESC");
            Log.e(TAG,"Uri sarua");
        }else{
            Log.e(TAG,"Uri teu sarua");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert(MovieDatabase.TABLE_NAME,null, values);
        if(id>0){
            Uri uri1 = ContentUris.withAppendedId(CONTENT_URI, id);
            return uri1;
        }
        throw new SQLException("Insertion Failed for URI :"+uri);
        //return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int del = 0;
        switch (uriMatcher.match(uri)){
            case MOVIE_ID:
                del = database.delete(MovieDatabase.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This Unknown URI "+uri);
        }
        return del;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int del = 0;
        switch (uriMatcher.match(uri)){
            case MOVIE_ID:
                del = database.update(MovieDatabase.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("This Unknown URI "+uri);
        }
        return del;
    }
}
