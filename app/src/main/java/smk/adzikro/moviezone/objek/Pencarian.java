package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import smk.adzikro.moviezone.net.SearchClient;

/**
 * Created by server on 11/19/17.
 */

public class Pencarian implements Parcelable {
    String id, mediaType, name, poster, release;

    public Pencarian(JSONObject object){
        try {
            setId(object.getString("id"));
            String type = object.getString("media_type");
            setMediaType(type);
            if(type.equals("tv")){
                setName(object.getString("name"));
                setPoster(object.getString("poster_path"));
                setRelease(object.getString("first_air_date"));
            }else if(type.equals("person")){
                setPoster(object.getString("profile_path"));
                setName(object.getString("name"));
                setRelease("");
            }else if(type.equals("movie")){
                setName(object.getString("title"));
                setPoster(object.getString("poster_path"));
                setRelease(object.getString("release_date"));
            }

        }catch (JSONException e){
            Log.e("Pencarian","Aya Erto "+e.getMessage());
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.mediaType);
        dest.writeString(this.name);
        dest.writeString(this.poster);
        dest.writeString(this.release);
    }

    public Pencarian() {
    }

    protected Pencarian(Parcel in) {
        this.id = in.readString();
        this.mediaType = in.readString();
        this.name = in.readString();
        this.poster = in.readString();
        this.release = in.readString();
    }

    public static final Parcelable.Creator<Pencarian> CREATOR = new Parcelable.Creator<Pencarian>() {
        @Override
        public Pencarian createFromParcel(Parcel source) {
            return new Pencarian(source);
        }

        @Override
        public Pencarian[] newArray(int size) {
            return new Pencarian[size];
        }
    };
}
