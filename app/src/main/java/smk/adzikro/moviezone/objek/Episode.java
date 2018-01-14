package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by server on 11/20/17.
 */

public class Episode implements Parcelable{
    private String id;
    private String airdate;
    private String number;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAirdate() {
        return airdate;
    }

    public void setAirdate(String airdate) {
        this.airdate = airdate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    private String overview;
    private String vote="0.0";
    private String poster;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.airdate);
        dest.writeString(this.number);
        dest.writeString(this.name);
        dest.writeString(this.overview);
        dest.writeString(this.vote);
        dest.writeString(this.poster);
    }

    public Episode(JSONObject object) {
        try{
            setId(object.getString("id"));
            setAirdate(object.getString("air_date"));
            setName(object.getString("name"));
            setNumber(object.getString("episode_number"));
            setOverview(object.getString("overview"));
            setPoster(object.getString("still_path"));
            setVote(object.getString("vote_average"));
        }catch (JSONException e){
            Log.e("Episode", "eroor "+e.getMessage());
        }
    }

    protected Episode(Parcel in) {
        this.id = in.readString();
        this.airdate = in.readString();
        this.number = in.readString();
        this.name = in.readString();
        this.overview = in.readString();
        this.vote = in.readString();
        this.poster = in.readString();
    }

    public static final Parcelable.Creator<Episode> CREATOR = new Parcelable.Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel source) {
            return new Episode(source);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };
}
