package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by server on 11/16/17.
 */

public class Trailer implements Parcelable {
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    private static final String TAG ="Object Trailer" ;
    String name;
    String size;
    String source;
    String type;

    public Trailer(JSONObject object){
            try {
                //JSONArray jsonArray = object.getJSONArray("youtube");
                setName(object.getString("name"));
                setSize(object.getString("size"));
                setSource(object.getString("source"));
                setType(object.getString("type"));
                Log.e(TAG,object.getString("source"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    protected Trailer(Parcel in) {
        this.name = in.readString();
        this.size = in.readString();
        this.source = in.readString();
        this.type = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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
        dest.writeString(this.name);
        dest.writeString(this.size);
        dest.writeString(this.source);
        dest.writeString(this.type);
    }
}
