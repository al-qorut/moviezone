package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by server on 11/20/17.
 */

public class CastMovie implements Parcelable {
    public static final Creator<CastMovie> CREATOR = new Creator<CastMovie>() {
        @Override
        public CastMovie createFromParcel(Parcel source) {
            return new CastMovie(source);
        }

        @Override
        public CastMovie[] newArray(int size) {
            return new CastMovie[size];
        }
    };
    String id, name, charackter, photo;
    Actor actor;

    public CastMovie(JSONObject object){
        try {
            setId(object.getString("id"));
            setName(object.getString("name"));
            setCharackter(object.getString("character"));
            setPhoto(object.getString("profile_path"));
            setActor(new Actor(object, true));
        }catch (JSONException e){
            Log.e("CastMovie","Tah eror "+e.getMessage());
        }
    }

    protected CastMovie(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.charackter = in.readString();
        this.photo = in.readString();
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharackter() {
        return charackter;
    }

    public void setCharackter(String charackter) {
        this.charackter = charackter;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.charackter);
        dest.writeString(this.photo);
    }
}
