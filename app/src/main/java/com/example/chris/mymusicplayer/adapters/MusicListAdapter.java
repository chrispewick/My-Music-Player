package com.example.chris.mymusicplayer.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chris.mymusicplayer.R;
import com.example.chris.mymusicplayer.models.Album;
import com.example.chris.mymusicplayer.models.Artist;
import com.example.chris.mymusicplayer.models.Genre;
import com.example.chris.mymusicplayer.models.IMusicObject;
import com.example.chris.mymusicplayer.models.Track;

import java.util.ArrayList;

/**
 * Created by Chris on 6/14/2017.
 */
public class MusicListAdapter extends ArrayAdapter<IMusicObject> {
    private final String TAG = getClass().getSimpleName();
    private ArrayList<IMusicObject> dataList;
    private Activity activity;

    private static class TrackVH {
        protected TextView trackTitle;
        protected TextView artistName;
        protected ImageView artwork;
    }

    private static class ArtistVH {
        protected TextView artistName;
        protected TextView numberAlbumsTracks;
        protected ImageView artwork;
    }

    private static class AlbumVH {
        protected TextView albumTitle;
        protected TextView artistNameAndYear;
        protected ImageView artwork;
    }

    private static class GenreVH{
        protected TextView genreName;
    }

    public MusicListAdapter(Activity activity, ArrayList<IMusicObject> data) {
        // Using simple_list_item_1 is fine here since we override getView() later
        super(activity, android.R.layout.simple_list_item_1, data);
        Log.i("Scheduled Adapter","Constructed");
        dataList = data;
        this.activity = activity;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        //I may want to use this later
//        Log.i("Stop Adapter","Item Type: "+position);
        int retVal = 1;

        return retVal;
    }

    /**
     * Returns the view for the item at the given position, based on the object type that the list
     * contains at the given position.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        IMusicObject obj = dataList.get(position);

        if( obj instanceof Track){
//            Log.i(TAG,"Track title: "+((Track) obj).getTitle());
            TrackVH viewHolder;
            if(convertView == null){
                LayoutInflater inflater = activity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_item_track, null);

                viewHolder = new TrackVH();
                viewHolder.trackTitle = (TextView) convertView.findViewById(R.id.track_name);
                viewHolder.artistName = (TextView) convertView.findViewById(R.id.artist_name);
                viewHolder.artwork = (ImageView) convertView.findViewById(R.id.artwork_icon);

                convertView.setTag(viewHolder);
                convertView.setTag(R.id.track_name, viewHolder.trackTitle);
                convertView.setTag(R.id.artist_name, viewHolder.artistName);
                convertView.setTag(R.id.artwork_icon, viewHolder.artwork);
            } else {
                viewHolder = (TrackVH) convertView.getTag();
            }

            viewHolder.trackTitle.setText( ((Track) dataList.get(position)).getTitle());
            viewHolder.artistName.setText( ((Track) dataList.get(position)).getArtist());
            //TODO: Handle artwork
//            viewHolder.artwork.setImageBitmap( ((Track) dataList.get(position)).getArtwork());
//            viewHolder.trackTitle.setText( ((Track) dataList.get(position)).getAlbumName());
        } else if( obj instanceof Artist){
//            Log.i(TAG,"Track title: "+((Track) obj).getTitle());
            ArtistVH viewHolder;
            if(convertView == null){
                LayoutInflater inflater = activity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_item_artist, null);

                viewHolder = new ArtistVH();
                viewHolder.artistName = (TextView) convertView.findViewById(R.id.artist_name);
                viewHolder.numberAlbumsTracks = (TextView) convertView.findViewById(R.id.artist_numbers);
                viewHolder.artwork = (ImageView) convertView.findViewById(R.id.artwork_icon);

                convertView.setTag(viewHolder);
                convertView.setTag(R.id.track_name, viewHolder.artistName);
                convertView.setTag(R.id.artist_name, viewHolder.numberAlbumsTracks);
                convertView.setTag(R.id.artwork_icon, viewHolder.artwork);
            } else {
                viewHolder = (ArtistVH) convertView.getTag();
            }

            viewHolder.artistName.setText( ((Artist) dataList.get(position)).getName());
            String numbersInfo = ((Artist) dataList.get(position)).getNumberAlbums()+" Albums | "
                    + ((Artist) dataList.get(position)).getNumberTracks() + " Tracks";
            viewHolder.numberAlbumsTracks.setText(numbersInfo);
            //TODO: Handle artwork
            //BitmapFactory.decodeFile(String path)
//            viewHolder.trackTitle.setText( ((Track) dataList.get(position)).getAlbumName());
        } else if( obj instanceof Album){
//            Log.i(TAG,"Track title: "+((Track) obj).getTitle());
            AlbumVH viewHolder;
            if(convertView == null){
                LayoutInflater inflater = activity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_item_album, null);

                viewHolder = new AlbumVH();
                viewHolder.albumTitle = (TextView) convertView.findViewById(R.id.album_title);
                viewHolder.artistNameAndYear = (TextView) convertView.findViewById(R.id.album_artist_year);
                viewHolder.artwork = (ImageView) convertView.findViewById(R.id.artwork_icon);

                convertView.setTag(viewHolder);
                convertView.setTag(R.id.album_title, viewHolder.albumTitle);
                convertView.setTag(R.id.album_artist_year, viewHolder.artistNameAndYear);
                convertView.setTag(R.id.artwork_icon, viewHolder.artwork);
            } else {
                viewHolder = (AlbumVH) convertView.getTag();
            }

            viewHolder.albumTitle.setText( ((Album) dataList.get(position)).getTitle());
            String nameAndYear = ((Album) dataList.get(position)).getArtist()+" | "
                    + ((Album) dataList.get(position)).getNumberTracks() + " Tracks";
            viewHolder.artistNameAndYear.setText(nameAndYear);
            //TODO: Handle artwork
            //BitmapFactory.decodeFile(String path)
//            viewHolder.trackTitle.setText( ((Track) dataList.get(position)).getAlbumName());
        } else if(obj instanceof Genre) {
            GenreVH viewHolder;
            if(convertView == null){
                LayoutInflater inflater = activity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_item_genre, null);

                viewHolder = new GenreVH();
                viewHolder.genreName = (TextView) convertView.findViewById(R.id.genre_name);

                convertView.setTag(viewHolder);
                convertView.setTag(R.id.genre_name, viewHolder.genreName);
            } else{
                viewHolder = (GenreVH) convertView.getTag();
            }

            viewHolder.genreName.setText(((Genre) dataList.get(position)).getName());
        }
        else{
            Log.i(TAG,"Fuck, ViewHolder error");
        }

        return convertView;
    }
}