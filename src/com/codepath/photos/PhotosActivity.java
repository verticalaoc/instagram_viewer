package com.codepath.photos;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PhotosActivity extends Activity {
	
	public static final String CLIENT_ID = "6d5b8148906748e0a77005714b7853fa";
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotosAdapter aPhotos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos);
		
		fetchPopularPhotos();
	}

	private void fetchPopularPhotos() {
		photos = new ArrayList<InstagramPhoto>();
		
		// create an adapter and bind it  to the data in arraylist
		aPhotos = new InstagramPhotosAdapter(this, photos);
		
		ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
		lvPhotos.setAdapter(aPhotos);
		
		// https://api.instagram.com/v1/media/popular?client_id={client id}
		
		// setup popular url
		String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
		
		// create network client
		AsyncHttpClient client = new AsyncHttpClient();
		
		// trigger the request
		client.get(popularUrl, new JsonHttpResponseHandler() {
			// define success and failure callbacks
			// handle the response

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// fired once the success response back
				// data => x => images => "standard_resolution" => "url
				JSONArray photosJSON = null;
				try {
					aPhotos.clear();
					photosJSON = response.getJSONArray("data");
					for(int i=0; i< photosJSON.length();i++) {
						JSONObject photoJSON = photosJSON.getJSONObject(i); // 1,2,3,4
						InstagramPhoto photo = new InstagramPhoto();
						photo.username = photoJSON.getJSONObject("user").getString("username");
						
						if (photoJSON.getJSONObject("caption") != null) {
							photo.caption = photoJSON.getJSONObject("caption").getString("text");
						}
						photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
						photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
						photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
						photos.add(photo);
					}
					aPhotos.notifyDataSetChanged();
				} catch (JSONException e) {
					// fired this when parsing json fail
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			
		});
		
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
