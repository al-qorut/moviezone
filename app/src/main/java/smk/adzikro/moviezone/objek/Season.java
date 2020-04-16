package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by server on 11/20/17.
 */

public class Season implements Parcelable {
    public static final Creator<Season> CREATOR = new Creator<Season>() {
        @Override
        public Season createFromParcel(Parcel source) {
            return new Season(source);
        }

        @Override
        public Season[] newArray(int size) {
            return new Season[size];
        }
    };
    String id, airdate, name, poster, number, jmlEpisode;
    List<Episode> episodes = new ArrayList<>();

    public Season(JSONObject object) {
        try {
            setId(object.getString("id"));
            setAirdate(object.getString("air_date"));
            setName(object.getString("name"));
            setPoster(object.getString("poster_path"));
            setNumber(object.getString("season_number"));
            JSONArray jsonArray = object.getJSONArray("episodes");
            if (jsonArray.length() > 0) {
                List<Episode> episodes1 = new ArrayList<>();
                int x = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    Episode episode = new Episode(jsonArray.getJSONObject(i));
                    episodes1.add(episode);
                    x++;
                }
                setEpisodes(episodes1);
                setJmlEpisode(String.valueOf(x));
            }
        } catch (JSONException e) {
            Log.e("Season", "Eroor " + e.getMessage());
        }
    }

    protected Season(Parcel in) {
        this.id = in.readString();
        this.airdate = in.readString();
        this.name = in.readString();
        this.poster = in.readString();
        this.number = in.readString();
        this.jmlEpisode = in.readString();
        this.episodes = in.createTypedArrayList(Episode.CREATOR);
    }

    public String getJmlEpisode() {
        return jmlEpisode;
    }

    public void setJmlEpisode(String jmlEpisode) {
        this.jmlEpisode = jmlEpisode;
    }

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.airdate);
        dest.writeString(this.name);
        dest.writeString(this.poster);
        dest.writeString(this.number);
        dest.writeString(this.jmlEpisode);
        dest.writeTypedList(this.episodes);
    }
}
