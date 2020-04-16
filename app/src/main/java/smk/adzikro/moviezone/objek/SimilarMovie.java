package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by server on 11/16/17.
 */

public class SimilarMovie implements Parcelable {
    public static final Creator<SimilarMovie> CREATOR = new Creator<SimilarMovie>() {
        @Override
        public SimilarMovie createFromParcel(Parcel source) {
            return new SimilarMovie(source);
        }

        @Override
        public SimilarMovie[] newArray(int size) {
            return new SimilarMovie[size];
        }
    };
    private static final String TAG ="SimilarMovie" ;
    boolean adult=false;
    String backdrop_path;
    String genres;
    String id;
    String title, poster;
    String year;
    Movie movie;
    String overview;
    String releaseDate;
    String vote;
    String vote_count;

    public SimilarMovie(JSONObject object){
        try {
            setId(object.getString("id"));
            setTitle(object.getString("title"));
          //  Log.e(TAG,object.getString("title"));
            setBackdrop_path(object.getString("backdrop_path"));
            setPoster(object.getString("poster_path"));
            String e = object.getString("release_date");
            setReleaseDate(e);
            String spli[]=e.split("-");
            setYear(spli[0]);
            setVote(object.getString("vote_average"));
            setAdult(!object.getString("adult").equals("false"));
            setOverview(object.getString("overview"));
            setMovie(new Movie(object));
        }catch (JSONException e){
            e.getStackTrace();
        }
    }

    public SimilarMovie() {
    }

    protected SimilarMovie(Parcel in) {
        this.adult = in.readByte() != 0;
        this.backdrop_path = in.readString();
        this.genres = in.readString();
        this.id = in.readString();
        this.title = in.readString();
        this.poster = in.readString();
        this.year = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.vote = in.readString();
        this.vote_count = in.readString();
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.genres);
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.poster);
        dest.writeString(this.year);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.vote);
        dest.writeString(this.vote_count);
    }
}
