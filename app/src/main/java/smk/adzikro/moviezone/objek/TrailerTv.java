package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by server on 11/20/17.
 */

public class TrailerTv implements Parcelable {
    public static final Parcelable.Creator<TrailerTv> CREATOR = new Parcelable.Creator<TrailerTv>() {
        @Override
        public TrailerTv createFromParcel(Parcel source) {
            return new TrailerTv(source);
        }

        @Override
        public TrailerTv[] newArray(int size) {
            return new TrailerTv[size];
        }
    };
    String id, name, source, type;
    public TrailerTv(JSONObject object){
        try {
            setId(object.getString("id"));
            setName(object.getString("name"));
            setSource(object.getString("key"));
            setType(object.getString("type"));
        }catch (JSONException e){
            Log.e("TrailerTV",e.getMessage());
        }
    }

    protected TrailerTv(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.source = in.readString();
        this.type = in.readString();
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.source);
        dest.writeString(this.type);
    }
}
