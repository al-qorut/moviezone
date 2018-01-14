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

import android.app.LoaderManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smk.adzikro.moviezone.net.SearchClient;
import smk.adzikro.moviezone.utils.StringUtils;


public class Actor implements Parcelable {

	private static final String TAG ="Actor" ;
	private String mName, mCharacter, mId, mUrl, birthday, imdb_id, biography,
			placebirth, photo, mati;
	private List<String> images = new ArrayList<>();
	private List<Crew> crews = new ArrayList<>();
	private List<Casting> castings = new ArrayList<>();
	private List<Tv> acaraTv = new ArrayList<>();

	public List<Tv> getAcaraTv() {
		return acaraTv;
	}

	public void setAcaraTv(List<Tv> acaraTv) {
		this.acaraTv = acaraTv;
	}

	public Actor(Boolean asal){

	}
	public Actor(JSONObject object, Boolean asaltiCast){
		try {
			setmId(object.getString("id"));
			setmName(object.getString("name"));
			setPhoto(object.getString("profile_path"));
		}catch (JSONException e){
			Log.e(TAG,e.getMessage());
		}
	}

	public Actor(JSONObject object) {
		try {
			Log.e(TAG,"Actor Create "+object.getString("id"));

			setmName(object.getString("name"));
			setmId(object.getString("id"));;
			setPhoto(object.getString("profile_path"));
			setBiography(object.getString("biography"));
			setBirthday(object.getString("birthday"));
			setPlacebirth(object.getString("place_of_birth"));
			setImdb_id(object.getString("imdb_id"));
			List<String> imb = new ArrayList<>();
			JSONArray img = object.getJSONObject("images").getJSONArray("profiles");
			if(img.length()>0){
				for (int i=0;i<img.length();i++){
					imb.add(img.getJSONObject(i).getString("file_path"));
				}
			}
			setImages(imb);

			List<Crew> crews1 = new ArrayList<>();
			JSONArray c = object.getJSONObject("credits").getJSONArray("crew");
			if(c.length()>0){
				for (int i=0;i<c.length();i++){
					Crew crew = new Crew(c.getJSONObject(i));
					crews1.add(crew);
				}
				setCrews(crews1);
			}
			List<Casting> castings1 = new ArrayList<>();
			JSONArray cast = object.getJSONObject("credits").getJSONArray("cast");
			if(cast.length()>0){
				for (int i=0;i<cast.length();i++){
					Casting co = new Casting(cast.getJSONObject(i));
					castings1.add(co);
				}
				setCastings(castings1);
			}

			setMati(object.getString("deathday"));
			setmUrl(object.getString("home_page"));
		}catch (JSONException e){
			Log.e(TAG,"Tah aya erot "+e.getMessage());
			e.getStackTrace();
		}
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmCharacter() {
		return mCharacter;
	}

	public void setmCharacter(String mCharacter) {
		this.mCharacter = mCharacter;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmUrl() {
		return mUrl;
	}

	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getImdb_id() {
		return imdb_id;
	}

	public void setImdb_id(String imdb_id) {
		this.imdb_id = imdb_id;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public String getPlacebirth() {
		return placebirth;
	}

	public void setPlacebirth(String placebirth) {
		this.placebirth = placebirth;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getMati() {
		return mati;
	}

	public void setMati(String mati) {
		this.mati = mati;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public List<Crew> getCrews() {
		return crews;
	}

	public void setCrews(List<Crew> crews) {
		this.crews = crews;
	}

	public List<Casting> getCastings() {
		return castings;
	}

	public void setCastings(List<Casting> castings) {
		this.castings = castings;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.mName);
		dest.writeString(this.mCharacter);
		dest.writeString(this.mId);
		dest.writeString(this.mUrl);
		dest.writeString(this.birthday);
		dest.writeString(this.imdb_id);
		dest.writeString(this.biography);
		dest.writeString(this.placebirth);
		dest.writeString(this.photo);
		dest.writeString(this.mati);
		dest.writeStringList(this.images);
		dest.writeTypedList(this.crews);
		dest.writeTypedList(this.castings);
		dest.writeTypedList(this.acaraTv);
	}

	protected Actor(Parcel in) {
		this.mName = in.readString();
		this.mCharacter = in.readString();
		this.mId = in.readString();
		this.mUrl = in.readString();
		this.birthday = in.readString();
		this.imdb_id = in.readString();
		this.biography = in.readString();
		this.placebirth = in.readString();
		this.photo = in.readString();
		this.mati = in.readString();
		this.images = in.createStringArrayList();
		this.crews = in.createTypedArrayList(Crew.CREATOR);
		this.castings = in.createTypedArrayList(Casting.CREATOR);
		this.acaraTv = in.createTypedArrayList(Tv.CREATOR);
	}

	public static final Creator<Actor> CREATOR = new Creator<Actor>() {
		@Override
		public Actor createFromParcel(Parcel source) {
			return new Actor(source);
		}

		@Override
		public Actor[] newArray(int size) {
			return new Actor[size];
		}
	};
}