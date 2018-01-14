/*
 * Copyright (C) 2014 Michell Bak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package smk.adzikro.moviezone.objek;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smk.adzikro.moviezone.net.SearchClient;

public class Movie implements Parcelable {

	private static final String TAG ="Movie" ;
	private int id=0;
	private String title = "", originalTitle = "", plot = "", cover = "", backdrop = "",
			rating = "0.0", tagline = "", releasedate = "", imdbId = "",
			certification = "", runtime = "0", trailer = "", genres = "", cast = "",
			collectionTitle = "", collectionId = "", collectionImage = "", year = "",
			director="", budget="0", revenue="0", status, idDirector ;
	private List<String> images = new ArrayList<>();
	private boolean dewasa=false;

	public String getIdDirector() {
		return idDirector;
	}

	public void setIdDirector(String idDirector) {
		this.idDirector = idDirector;
	}

	public void addImage(List<String> img){
		images.addAll(img);
	}
	private List<Review> reviews = new ArrayList<>();
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private List<SimilarMovie> similarMovies = new ArrayList<>();
	public List<SimilarMovie> getSimilarMovies() {
		return similarMovies;
	}
	public void setSimilarMovies(List<SimilarMovie> similarMovies) {
		this.similarMovies = similarMovies;
	}
	public boolean isDewasa() {
		return dewasa;
	}
	public void setDewasa(boolean dewasa) {
		this.dewasa = dewasa;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public ArrayList<CastMovie> getCasts() {
		return casts;
	}
	public void setCasts(ArrayList<CastMovie> casts) {
		this.casts = casts;
	}
	public String getRevenue() {
		return revenue;
	}
	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}
	public List<Trailer> getmTrailer() {
		return mTrailer;
	}
	public void setmTrailer(List<Trailer> mTrailer) {
		this.mTrailer = mTrailer;
	}
	private List<Trailer> mTrailer = new ArrayList<>();
	private ArrayList<CastMovie> casts = new ArrayList<>();
	private List<CrewMovie> crews = new ArrayList<>();
	public static HashMap<String, Integer> genre = new HashMap<String, Integer>() {
	private static final long serialVersionUID = 1L;

	{
		put("action", 28);
		put("adventure", 12);
		put("animation", 16);
		put("comedy", 35);
		put("crime", 80);
		put("disaster", 105);
		put("documentary", 99);
		put("drama", 18);
		put("eastern", 82);
		put("erotic", 2916);
		put("Kids", 10762);
		put("Reality", 10764);
		put("family", 10751);
		put("fan film", 10750);
		put("fantasy", 14);
		put("film noir", 10753);
		put("fiction", 878);
		put("foreign", 10769);
		put("history", 36);
		put("holiday", 10595);
		put("horror", 27);
		put("indie", 10756);
		put("music", 10402);
		put("musical", 22);
		put("mystery", 9648);
		put("neo-noir", 10754);
		put("road movie", 1115);
		put("romance", 10749);
		put("science fiction", 878);
		put("short", 10755);
		put("sport", 9805);
		put("sporting event", 10758);
		put("sport film", 10757);
		put("suspense", 10748);
		put("tv movie", 10770);
		put("thriller", 53);
		put("war", 10752);
		put("western", 37);
		put("Action & Adventure", 10759);
		put("Sci-Fi & Fantasy", 10765);
		put("Soap",10766);
		put("News",10763);
		put("Talk",10767);
		put("War & Politics",10768);

	}
};
	//{"genres":[{"id":10759,"name":"Action & Adventure"},{"id":16,"name":"Animation"},{"id":35,"name":"Comedy"},{"id":80,"name":"Crime"},{"id":99,"name":"Documentary"},{"id":18,"name":"Drama"},{"id":10751,"name":"Family"},{"id":10762,"name":"Kids"},{"id":9648,"name":"Mystery"},{"id":10763,"name":"News"},{"id":10764,"name":"Reality"},{"id":10765,"name":"Sci-Fi & Fantasy"},{"id":10766,"name":"Soap"},{"id":10767,"name":"Talk"},{"id":10768,"name":"War & Politics"},{"id":37,"name":"Western"}]}
	//{"genres":[{"id":28,"name":"Action"},{"id":12,"name":"Adventure"},{"id":16,"name":"Animation"},{"id":35,"name":"Comedy"},{"id":80,"name":"Crime"},{"id":99,"name":"Documentary"},{"id":18,"name":"Drama"},{"id":10751,"name":"Family"},{"id":14,"name":"Fantasy"},{"id":36,"name":"History"},{"id":27,"name":"Horror"},{"id":10402,"name":"Music"},{"id":9648,"name":"Mystery"},{"id":10749,"name":"Romance"},{"id":878,"name":"Science Fiction"},{"id":10770,"name":"TV Movie"},{"id":53,"name":"Thriller"},{"id":10752,"name":"War"},{"id":37,"name":"Western"}]}
	public static String getValueGenres(int gen_id){
		for(Map.Entry<String, Integer> entry:genre.entrySet()){
			if(entry.getValue()==gen_id){
				return entry.getKey();
			}
		}
		return null;
	}

    public Movie(JSONObject object) {
		try{
			setId(object.getInt("id"));
			setTitle(object.getString("title"));
			setRating(object.getString("vote_average"));
			setCover(object.getString("poster_path"));
			setOriginalTitle(object.getString("original_title"));
			setPlot(object.getString("overview"));
			setBackdrop(object.getString("backdrop_path"));
			String date1 = object.getString("release_date");
			String g="";
			JSONArray jsonArray = object.getJSONArray("genre_ids");
			for (int i=0;i<jsonArray.length();i++) {
				int ids = jsonArray.getInt(i);
				if(i==jsonArray.length()-1) {
					g = g + getValueGenres(ids);
				}else{
					g = g + getValueGenres(ids) + ", ";
				}
			}
			setGenres(g);
			String date[] = date1.split("-");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
			Date tgl = dateFormat.parse(date1);
			SimpleDateFormat dete = new SimpleDateFormat("MMMM, dd yyyy");
			setReleasedate(dete.format(tgl));
			setYear(date[0]);
			String dws = object.getString("adult");
			setDewasa(dewasa=dws.equals("false")?false:true);

		}catch (Exception e){
			Log.e(TAG, e.getMessage());}
    }
    public boolean apakahJSonIsi(JSONObject object,String name){
		try {
			String s = object.getString(name);
			if(s.equals(null)){
				return false;
			}else{
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}



	public List<CrewMovie> getCrews() {
		return crews;
	}

	public void setCrews(List<CrewMovie> crews) {
		this.crews = crews;
	}

	public void addMovieComplete(JSONObject object){
		Log.e(TAG,"onCreate Movie detail");
		try{

				String g = "";

				JSONArray jsonArray = object.getJSONArray("genres");
				if (jsonArray.length() > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						if (i == jsonArray.length() - 1) {
							g = g + jsonArray.getJSONObject(i).getString("name");
						} else {
							g = g + jsonArray.getJSONObject(i).getString("name") + ", ";
						}
					}
					setGenres(g);
				}
/*
			if(apakahJSonIsi(object,"belongs_to_collection")) {
				setCollectionTitle(object.getJSONObject("belongs_to_collection").getString("name"));
				setCollectionId(object.getJSONObject("belongs_to_collection").getString("id"));
				setBackdrop(SearchClient.IMG_URL + "w" + SearchClient.poster_size[2] + object.getJSONObject("belongs_to_collection").getString("backdrop_path"));
		}else{
			Log.e(TAG,"belongs_to_collection kosong");
		}
			*/
			if(apakahJSonIsi(object,"budget"))
			setBudget(object.getString("budget"));
			if(apakahJSonIsi(object,"imdb_id"))
			setImdbId(object.getString("imdb_id"));
			if(apakahJSonIsi(object,"popularity"))
			setPopularity(object.getString("popularity"));
			if(apakahJSonIsi(object,"revenue"))
			setRevenue(object.getString("revenue"));
			if(apakahJSonIsi(object,"tagline"))
			setTagline(object.getString("tagline"));
			if(apakahJSonIsi(object,"homepage"))
			setHomepage(object.getString("homepage"));
			if(apakahJSonIsi(object,"status"))
			setStatus(object.getString("status"));
			//untuk mengambil image request link baru dengan CollectionId

			//Trailer
			JSONArray tra = object.getJSONObject("trailers").getJSONArray("youtube");
			if(tra.length()>0) {
				List<Trailer> trailers = new ArrayList<>();
				for (int i=0;i<tra.length();i++){
					Trailer trar = new Trailer(tra.getJSONObject(i));
					Log.e(TAG,"Ambil Trailer "+tra.getJSONObject(i).getString("source"));
					trailers.add(trar);
				}
				setmTrailer(trailers);
			}
		 //Pemain
			JSONArray cast = object.getJSONObject("credits").getJSONArray("cast");
			if(cast.length()>0) {
				ArrayList<CastMovie> casts = new ArrayList<>();
				for (int i=0;i<cast.length();i++){
					CastMovie cast1 = new CastMovie(cast.getJSONObject(i));
					casts.add(cast1);

				}
				setCasts(casts);
			}

		//SimilarMovie
			JSONArray similar = object.getJSONObject("similar_movies").getJSONArray("results");
			if(similar.length()!=0) {
				Log.e(TAG,"Ngisi SimilarMovie");
				List<SimilarMovie> similarMovies = new ArrayList<>();
				for (int i=0;i<similar.length();i++){
					SimilarMovie trar = new SimilarMovie(similar.getJSONObject(i));
					similarMovies.add(trar);
				}
				setSimilarMovies(similarMovies);
			}
			//getImage BackDrop
			JSONArray array = object.getJSONObject("images").getJSONArray("backdrops");
			if(array.length()>0){
				ArrayList<String> back = new ArrayList<>();
				for(int i=0;i<array.length();i++){
					String link = array.getJSONObject(i).getString("file_path");
					back.add(link);
				}
				setListBackDrop(back);
			}
			array = object.getJSONObject("images").getJSONArray("posters");
			if(array.length()>0){
				ArrayList<String> back = new ArrayList<>();
				for(int i=0;i<array.length();i++){
					String link = array.getJSONObject(i).getString("file_path");
					back.add(link);
				}
				setListPoster(back);
			}

			if(apakahJSonIsi(object,"poster_path"))
			images.add(object.getString("poster_path"));
			if(apakahJSonIsi(object,"backdrop_path"))
			images.add(object.getString("backdrop_path"));
			String s = object.getString("belongs_to_collection");

			if(!s.equals(null)) {
				images.add(object.getJSONObject("belongs_to_collection").getString("backdrop_path"));
				images.add(object.getJSONObject("belongs_to_collection").getString("poster_path"));
				setCollectionTitle(object.getJSONObject("belongs_to_collection").getString("name"));
				setCollectionId(object.getJSONObject("belongs_to_collection").getString("id"));
				setBackdrop(object.getJSONObject("belongs_to_collection").getString("backdrop_path"));
			}

			//Pembuat
			JSONArray crew = object.getJSONObject("credits").getJSONArray("crew");
			if(crew.length()>0) {
				Log.e(TAG,"lagi Ngisi crew ");
				List<CrewMovie> varCrewList = new ArrayList<>();

				for (int i=0;i<crew.length();i++){
					CrewMovie c = new CrewMovie(crew.getJSONObject(i));
					if(crew.getJSONObject(i).getString("job").equals("Director")) {
						setDirector(crew.getJSONObject(i).getString("name"));
						setIdDirector(crew.getJSONObject(i).getString("id"));
					}
					varCrewList.add(c);
				}
				setCrews(varCrewList);
			}
		}catch (Exception e){
			Log.e(TAG, "Tah aya eroor "+e.getMessage());}
	}
	ArrayList<String> listPoster = new ArrayList<>();
	ArrayList<String> listBackDrop = new ArrayList<>();
	public Movie(boolean asal){

	}
	public ArrayList<OtherFromDirector> getFromDirector() {
		return fromDirector;
	}

	public void setFromDirector(ArrayList<OtherFromDirector> fromDirector) {
		this.fromDirector = fromDirector;
	}

	ArrayList<OtherFromDirector> fromDirector = new ArrayList<>();

	public ArrayList<String> getListPoster() {
		return listPoster;
	}

	public void setListPoster(ArrayList<String> listPoster) {
		this.listPoster.addAll(listPoster);
	}

	public ArrayList<String> getListBackDrop() {
		return listBackDrop;
	}

	public void setListBackDrop(ArrayList<String> listBackDrop) {
		this.listBackDrop.addAll(listBackDrop);
	}

	String popularity;
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	String homepage;
	public String getPopularity() {
		return popularity;
	}
	public void setPopularity(String popularity) {
		this.popularity = popularity;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOriginalTitle() {
		return originalTitle;
	}
	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}
	public String getPlot() {
		return plot;
	}
	public void setPlot(String plot) {
		this.plot = plot;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getBackdrop() {
		return backdrop;
	}
	public void setBackdrop(String backdrop) {
		this.backdrop = backdrop;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getTagline() {
		return tagline;
	}
	public void setTagline(String tagline) {
		this.tagline = tagline;
	}
	public String getReleasedate() {
		return releasedate;
	}
	public void setReleasedate(String releasedate) {
		this.releasedate = releasedate;
	}
	public String getCertification() {
		return certification;
	}
	public void setCertification(String certification) {
		this.certification = certification;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String getTrailer() {
		return trailer;
	}
	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	public String getCast() {
		return cast;
	}
	public void setCast(String cast) {
		this.cast = cast;
	}
	public String getCollectionTitle() {
		return collectionTitle;
	}
	public void setCollectionTitle(String collectionTitle) {
		this.collectionTitle = collectionTitle;
	}
	public String getCollectionId() {
		return collectionId;
	}
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}
	public String getCollectionImage() {
		return collectionImage;
	}
	public void setCollectionImage(String collectionImage) {
		this.collectionImage = collectionImage;
	}
	public String getImdbId() {
		return imdbId;
	}
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getYear() {
		return year;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.title);
		dest.writeString(this.originalTitle);
		dest.writeString(this.plot);
		dest.writeString(this.cover);
		dest.writeString(this.backdrop);
		dest.writeString(this.rating);
		dest.writeString(this.tagline);
		dest.writeString(this.releasedate);
		dest.writeString(this.imdbId);
		dest.writeString(this.certification);
		dest.writeString(this.runtime);
		dest.writeString(this.trailer);
		dest.writeString(this.genres);
		dest.writeString(this.cast);
		dest.writeString(this.collectionTitle);
		dest.writeString(this.collectionId);
		dest.writeString(this.collectionImage);
		dest.writeString(this.year);
		dest.writeString(this.director);
		dest.writeString(this.budget);
		dest.writeString(this.revenue);
		dest.writeString(this.status);
		dest.writeStringList(this.images);
		dest.writeByte(this.dewasa ? (byte) 1 : (byte) 0);
		dest.writeTypedList(this.reviews);
		dest.writeTypedList(this.similarMovies);
		dest.writeTypedList(this.mTrailer);
		dest.writeTypedList(this.casts);
		dest.writeTypedList(this.crews);
		dest.writeString(this.popularity);
		dest.writeString(this.homepage);
	}

	protected Movie(Parcel in) {
		this.id = in.readInt();
		this.title = in.readString();
		this.originalTitle = in.readString();
		this.plot = in.readString();
		this.cover = in.readString();
		this.backdrop = in.readString();
		this.rating = in.readString();
		this.tagline = in.readString();
		this.releasedate = in.readString();
		this.imdbId = in.readString();
		this.certification = in.readString();
		this.runtime = in.readString();
		this.trailer = in.readString();
		this.genres = in.readString();
		this.cast = in.readString();
		this.collectionTitle = in.readString();
		this.collectionId = in.readString();
		this.collectionImage = in.readString();
		this.year = in.readString();
		this.director = in.readString();
		this.budget = in.readString();
		this.revenue = in.readString();
		this.status = in.readString();
		this.images = in.createStringArrayList();
		this.dewasa = in.readByte() != 0;
		this.reviews = in.createTypedArrayList(Review.CREATOR);
		this.similarMovies = in.createTypedArrayList(SimilarMovie.CREATOR);
		this.mTrailer = in.createTypedArrayList(Trailer.CREATOR);
		this.casts = in.createTypedArrayList(CastMovie.CREATOR);
		this.crews = in.createTypedArrayList(CrewMovie.CREATOR);
		this.popularity = in.readString();
		this.homepage = in.readString();
	}

	public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
		@Override
		public Movie createFromParcel(Parcel source) {
			return new Movie(source);
		}

		@Override
		public Movie[] newArray(int size) {
			return new Movie[size];
		}
	};
}