package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import smk.adzikro.moviezone.net.SearchClient;

/**
 * Created by server on 11/20/17.
 * maom we atuh ah
 */

 public class CrewMovie implements Parcelable {
    public static final Parcelable.Creator<CrewMovie> CREATOR = new Creator<CrewMovie>() {
        @Override
        public CrewMovie createFromParcel(Parcel source) {
            return new CrewMovie(source);
        }

        @Override
        public CrewMovie[] newArray(int size) {
            return new CrewMovie[size];
        }
    };
    private static final String TAG ="CrewMovie" ;
    String id, name, department, job, photo;
        public CrewMovie(JSONObject object){
            try{
                setId(object.getString("id"));
                setName(object.getString("name"));
                setJob(object.getString("job"));
                setPhoto(SearchClient.IMG_URL + "w" + SearchClient.poster_size[2] +object.getString("profile_path"));
            }catch (JSONException e){
                Log.e(TAG,e.getMessage());
            }
        }

    protected CrewMovie(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.department = in.readString();
        this.job = in.readString();
        this.photo = in.readString();
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

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
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
            dest.writeString(this.department);
            dest.writeString(this.job);
            dest.writeString(this.photo);
        }
    }

