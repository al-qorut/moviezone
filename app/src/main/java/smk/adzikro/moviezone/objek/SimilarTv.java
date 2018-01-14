package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smk.adzikro.moviezone.net.SearchClient;

/**
 * Created by server on 11/20/17.
 */

public class SimilarTv implements Parcelable {
    String id, title, genres, rating="0,0", releaseDate, backdrop, poster, plot;
    Tv tv;

    public Tv getTv() {
        return tv;
    }

    public void setTv(Tv tv) {
        this.tv = tv;
    }

    public SimilarTv(JSONObject object){
        try{
            setId(object.getString("id"));
            setTitle(object.getString("name"));
            setRating(object.getString("vote_average"));
            setPoster(object.getString("poster_path"));
            setBackdrop(object.getString("backdrop_path"));
            setPlot(object.getString("overview"));
            setReleaseDate(object.getString("first_air_date"));
            String g="";
            JSONArray jsonArray = object.getJSONArray("genre_ids");
            for (int i=0;i<jsonArray.length();i++) {
                int ids = jsonArray.getInt(i);
                if(i==jsonArray.length()-1)
                    g = g + Movie.getValueGenres(ids);
                else
                    g = g + Movie.getValueGenres(ids)+", ";
            }
            setGenres(g);
            setTv(new Tv(object));
        }catch (JSONException e){
            Log.e("SimilarTV", "Errot parsing data tv "+e.getMessage());}
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.genres);
        dest.writeString(this.rating);
        dest.writeString(this.releaseDate);
        dest.writeString(this.backdrop);
        dest.writeString(this.poster);
        dest.writeString(this.plot);
    }

    protected SimilarTv(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.genres = in.readString();
        this.rating = in.readString();
        this.releaseDate = in.readString();
        this.backdrop = in.readString();
        this.poster = in.readString();
        this.plot = in.readString();
    }

    public static final Parcelable.Creator<SimilarTv> CREATOR = new Parcelable.Creator<SimilarTv>() {
        @Override
        public SimilarTv createFromParcel(Parcel source) {
            return new SimilarTv(source);
        }

        @Override
        public SimilarTv[] newArray(int size) {
            return new SimilarTv[size];
        }
    };
}
