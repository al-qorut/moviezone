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
 * Created by server on 11/15/17.
 */

public class Tv implements Parcelable {
    public static final Creator<Tv> CREATOR = new Creator<Tv>() {
        @Override
        public Tv createFromParcel(Parcel source) {
            return new Tv(source);
        }

        @Override
        public Tv[] newArray(int size) {
            return new Tv[size];
        }
    };
    final String TAG="Tv";
    String id, title, genres, rating = "0,0", releaseDate, backdrop, poster, plot;
    String network, status, type, lastDate, createBy, lastseason, overview;
    List<SimilarTv> similarTvList = new ArrayList<>();
    List<TrailerTv> trailerTvs = new ArrayList<>();
    ArrayList<CastMovie> castTvList = new ArrayList<>();
    ArrayList<String> imagesBack = new ArrayList<>();
    ArrayList<String> listposter = new ArrayList<>();
    Season season;

    public Tv(JSONObject object){

        try{
            setId(object.getString("id"));
            setTitle(object.getString("name"));
            setRating(object.getString("vote_average"));
            setPoster(object.getString("poster_path"));
            setBackdrop(object.getString("backdrop_path"));
            setPlot(object.getString("overview"));
            setReleaseDate(object.getString("first_air_date"));
            String g="";
          //  Log.e(TAG,getTitle());
            JSONArray jsonArray = object.getJSONArray("genre_ids");
            for (int i=0;i<jsonArray.length();i++) {
                int ids = jsonArray.getInt(i);
                if(i==jsonArray.length()-1)
                    g = g + Movie.getValueGenres(ids);
                else
                    g = g + Movie.getValueGenres(ids)+", ";
            }
            setGenres(g);
        }catch (JSONException e){
            Log.e(TAG, "Errot parsing data tv "+e.getMessage());}
    }

    public Tv(boolean bo) {
    }

    protected Tv(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.genres = in.readString();
        this.rating = in.readString();
        this.releaseDate = in.readString();
        this.backdrop = in.readString();
        this.poster = in.readString();
        this.plot = in.readString();
        this.network = in.readString();
        this.status = in.readString();
        this.type = in.readString();
        this.lastDate = in.readString();
        this.createBy = in.readString();
        this.lastseason = in.readString();
        this.overview = in.readString();
        this.similarTvList = in.createTypedArrayList(SimilarTv.CREATOR);
        this.trailerTvs = in.createTypedArrayList(TrailerTv.CREATOR);
        this.castTvList = in.createTypedArrayList(CastMovie.CREATOR);
        this.imagesBack = in.createStringArrayList();
        this.listposter = in.createStringArrayList();
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public void addDetail(JSONObject object){
        try{


            //Network
            String g=" ";
            JSONArray jsonArray = object.getJSONArray("networks");
            for (int i=0;i<jsonArray.length();i++) {
                String n = jsonArray.getJSONObject(i).getString("name");
                if(i==jsonArray.length()-1)
                    g = g + n;
                else
                    g = g + n+", ";
            }
            setNetwork(g);

            setStatus(object.getString("status"));
            setType(object.getString("type"));
            setLastDate(object.getString("last_air_date"));
            g=" ";
            jsonArray = object.getJSONArray("created_by");
            for (int i=0;i<jsonArray.length();i++) {
                String n = jsonArray.getJSONObject(i).getString("name");
                if(i==jsonArray.length()-1)
                    g = g + n;
                else
                    g = g + n+", ";
            }
            setCreateBy(g);
            setLastseason(object.getString("number_of_seasons"));
            setOverview(object.getString("overview"));

            //Similar TV
            jsonArray = object.getJSONObject("similar").getJSONArray("results");
            if(jsonArray.length()>0){
                List<SimilarTv> similarTvList1 = new ArrayList<>();
                for (int i=0;i<jsonArray.length();i++){
                    SimilarTv similarTv = new SimilarTv(jsonArray.getJSONObject(i));
                    similarTvList1.add(similarTv);
                }
                setSimilarTvList(similarTvList1);
            }

            //Trailer TV
            jsonArray = object.getJSONObject("videos").getJSONArray("results");
            if(jsonArray.length()>0){
                List<TrailerTv> trailerTvs1 = new ArrayList<>();
                for (int i=0;i<jsonArray.length();i++){
                    TrailerTv trailerTv = new TrailerTv(jsonArray.getJSONObject(i));
                    trailerTvs1.add(trailerTv);
                }
                setTrailerTvs(trailerTvs1);
            }

            //Backdrop
            jsonArray = object.getJSONObject("images").getJSONArray("backdrops");
            if(jsonArray.length()>0){
                ArrayList<String> back = new ArrayList<>();
                for (int i=0;i<jsonArray.length();i++){
                    String b = jsonArray.getJSONObject(i).getString("file_path");
                    back.add(b);
                }
                setImagesBack(back);
            }
            //Poster
            jsonArray = object.getJSONObject("images").getJSONArray("posters");
            if(jsonArray.length()>0){
                ArrayList<String> back = new ArrayList<>();
                for (int i=0;i<jsonArray.length();i++){
                    String b = jsonArray.getJSONObject(i).getString("file_path");
                    back.add(b);
                }
                setListposter(back);
            }

            //Casting
            jsonArray = object.getJSONObject("credits").getJSONArray("cast");
            if(jsonArray.length()>0){
                ArrayList<CastMovie> castMovieList = new ArrayList<>();
                for (int i=0;i<jsonArray.length();i++){
                    CastMovie castMovie= new CastMovie(jsonArray.getJSONObject(i));
                    castMovieList.add(castMovie);
                }
                setCastTvList(castMovieList);
            }

        }catch (JSONException e){
            Log.e("Tv","tah Erot "+e.getMessage());
        }
    }

    public void addSeason(JSONObject object){
        Season s = new Season(object);
        setSeason(s);
    }

    public ArrayList<String> getListposter() {
        return listposter;
    }

    public void setListposter(ArrayList<String> listposter) {
        this.listposter = listposter;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getLastseason() {
        return lastseason;
    }

    public void setLastseason(String lastseason) {
        this.lastseason = lastseason;
    }

    public List<SimilarTv> getSimilarTvList() {
        return similarTvList;
    }

    public void setSimilarTvList(List<SimilarTv> similarTvList) {
        this.similarTvList = similarTvList;
    }

    public List<TrailerTv> getTrailerTvs() {
        return trailerTvs;
    }

    public void setTrailerTvs(List<TrailerTv> trailerTvs) {
        this.trailerTvs = trailerTvs;
    }

    public ArrayList<CastMovie> getCastTvList() {
        return castTvList;
    }
    //  List<Season> seasons = new ArrayList<>();

    public void setCastTvList(ArrayList<CastMovie> castTvList) {
        this.castTvList = castTvList;
    }

    public ArrayList<String> getImagesBack() {
        return imagesBack;
    }

    public void setImagesBack(ArrayList<String> images) {
        this.imagesBack = images;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
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

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.genres);
        dest.writeString(this.rating);
        dest.writeString(this.releaseDate);
        dest.writeString(this.backdrop);
        dest.writeString(this.poster);
        dest.writeString(this.plot);
        dest.writeString(this.TAG);
        dest.writeString(this.network);
        dest.writeString(this.status);
        dest.writeString(this.type);
        dest.writeString(this.lastDate);
        dest.writeString(this.createBy);
        dest.writeString(this.lastseason);
        dest.writeString(this.overview);
        dest.writeTypedList(this.similarTvList);
        dest.writeTypedList(this.trailerTvs);
        dest.writeTypedList(this.castTvList);
        dest.writeStringList(this.imagesBack);
        dest.writeStringList(this.listposter);
    }
}
