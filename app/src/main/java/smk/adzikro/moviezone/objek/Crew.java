package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by server on 11/18/17.
 */

public class Crew implements Parcelable {
    public static final Parcelable.Creator<Crew> CREATOR = new Parcelable.Creator<Crew>() {
        @Override
        public Crew createFromParcel(Parcel source) {
            return new Crew(source);
        }

        @Override
        public Crew[] newArray(int size) {
            return new Crew[size];
        }
    };
    String id;
    String departement;
    String title;
    String original_title;
    String job;
    String adult;
    String poster;
    String release_date;

    public Crew(JSONObject object){
        try {
            setId(object.getString("id"));
            setDepartement(object.getString("department"));
            setTitle(object.getString("title"));
            setOriginal_title(object.getString("original_title"));
            setJob(object.getString("job"));
            setAdult(object.getString("adult"));
            setPoster(object.getString("poster_path"));
            setRelease_date(object.getString("release_date"));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public Crew() {
    }

    protected Crew(Parcel in) {
        this.id = in.readString();
        this.departement = in.readString();
        this.title = in.readString();
        this.original_title = in.readString();
        this.job = in.readString();
        this.adult = in.readString();
        this.poster = in.readString();
        this.release_date = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.departement);
        dest.writeString(this.title);
        dest.writeString(this.original_title);
        dest.writeString(this.job);
        dest.writeString(this.adult);
        dest.writeString(this.poster);
        dest.writeString(this.release_date);
    }
}
