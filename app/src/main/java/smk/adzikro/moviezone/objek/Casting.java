package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import smk.adzikro.moviezone.net.SearchClient;

/**
 * Created by server on 11/18/17.
 */

public class Casting implements Parcelable {
    String id;
    String character;
    String title;
    String name;

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    Actor actor;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Casting(JSONObject object){
        try{
            setId(object.getString("id"));
            setCharacter(object.getString("character"));
            setTitle(object.getString("original_title"));
            setReleasedate(object.getString("release_date"));
            setPoster(object.getString("poster_path"));
            setActor(new Actor(object, true));
        }catch (JSONException e){
            e.getStackTrace();
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    String releasedate;
    String poster;
    String adult;

    public Casting() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.character);
        dest.writeString(this.title);
        dest.writeString(this.name);
        dest.writeParcelable(this.actor, flags);
        dest.writeString(this.releasedate);
        dest.writeString(this.poster);
        dest.writeString(this.adult);
    }

    protected Casting(Parcel in) {
        this.id = in.readString();
        this.character = in.readString();
        this.title = in.readString();
        this.name = in.readString();
        this.actor = in.readParcelable(Actor.class.getClassLoader());
        this.releasedate = in.readString();
        this.poster = in.readString();
        this.adult = in.readString();
    }

    public static final Creator<Casting> CREATOR = new Creator<Casting>() {
        @Override
        public Casting createFromParcel(Parcel source) {
            return new Casting(source);
        }

        @Override
        public Casting[] newArray(int size) {
            return new Casting[size];
        }
    };
}
