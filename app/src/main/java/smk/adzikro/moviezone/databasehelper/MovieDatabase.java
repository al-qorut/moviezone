package smk.adzikro.moviezone.databasehelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by server on 10/22/17.
 */

public class MovieDatabase {
    Context context;
    private final static int VERSION=1;
    private final static String DATABASE="datamovie";
    public final static String TABLE_NAME="FAVORITE";
    public final static String FIELD_ID="_id";
    public final static String FIELD_TITLE="title";
    public final static String FIELD_RELEASE="release";
    public final static String FIELD_OVERVIEW="overview";
    public final static String FIELD_POPULARITY="popularity";
    public final static String FIELD_POSTER="poster";

    public MovieDatabase(Context context){
        this.context = context;
    }

    public SQLiteDatabase openDatabase(){
        return new DataMovie(context).getWritableDatabase();
    }
    public boolean isMovie(int id){
        boolean hasil=false;
        SQLiteDatabase db = new DataMovie(context).getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME+" where "+FIELD_ID+"="+id,null);
        if((cursor.getCount()==0)||(cursor==null)){
            hasil = false;
        }else{
            hasil =true;
        }
        cursor.close();
        db.close();
        return hasil;
    }
    public class DataMovie extends SQLiteOpenHelper{

        public DataMovie(Context context) {
            super(context, DATABASE, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE FAVORITE ("+FIELD_ID+" int primary key, "+
                    FIELD_TITLE+" text, "+
                    FIELD_RELEASE+" text, "+
                    FIELD_OVERVIEW+" text, "+
                    FIELD_POPULARITY+" text, "+
                    FIELD_POSTER+" text)" );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
