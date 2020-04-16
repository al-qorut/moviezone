package smk.adzikro.moviezone.objek;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by server on 11/27/17.
 */

public class OtherFromDirector {
    String id, title, poster, releaseDate;

    public OtherFromDirector(JSONObject object) {
        try {
            if (object.getString("job").equals("Director")) {
                setId(object.getString("id"));
                setPoster(object.getString("poster_path"));
                setTitle(object.getString("title"));
                setReleaseDate(object.getString("release_date"));
            }
        } catch (JSONException e) {
            Log.e("Dir", e.getMessage());
        }
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
