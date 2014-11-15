package com.codepath.photos;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
	public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// take the data source
		InstagramPhoto photo = getItem(position);
		
		// check if we use cycle view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
		}
		
		// lookup the subview within the template
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		ImageView imagePhoto = (ImageView) convertView.findViewById(R.id.imagePhoto);
		TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
		TextView tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);

		
		// populate the subview (textfield, imageview)
		tvCaption.setText(photo.caption);
		
		// set the image height before loading
		imagePhoto.getLayoutParams().height = photo.imageHeight;
		
		// reset the image from the recycled view
		imagePhoto.setImageResource(0);
		
		// ask for the photo to be added to the image view based on the photo url
		// in the background: a. network request, b. download, c. convert to bitmap and resize it, d. insert to image view
		Picasso.with(getContext()).load(photo.imageUrl).into(imagePhoto);
		
		// set the user name
		tvUserName.setText("photo by @" + photo.username);
		
		// set the likes count
		tvLikesCount.setText("likes count: " + photo.likesCount);

		
		// return  the view  for that data item
		
		return convertView;
	}
}
