package com.bojie.musicbo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class Top10TracksActivity extends AppCompatActivity {

    private RecyclerView mTrackLists;
    private Top10TracksAdapter mTop10TracksAdapter;
    private ArrayList<Music> mListMusic;
    private SpotifyService mSpotify;
    private String mArtistName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10_tracks);


        mTrackLists = (RecyclerView) findViewById(R.id.listTracks);
        mTop10TracksAdapter = new Top10TracksAdapter(this);
        mTrackLists.setAdapter(mTop10TracksAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mTrackLists.setLayoutManager(layoutManager);


        SpotifyApi api = new SpotifyApi();
        mSpotify = api.getService();

        mListMusic = new ArrayList<>();

        Intent intent = getIntent();
        String id = intent.getStringExtra(getString(R.string.KEY_ID));
        mArtistName = intent.getStringExtra(getString(R.string.KEY_ARTIST_NAME));

        if (savedInstanceState != null) {
            mListMusic = savedInstanceState.getParcelableArrayList(getString(R.string.STATE_TRACK));
        } else {
            getTop10Tracks(id);
        }
        mTop10TracksAdapter.setMusics(mListMusic);

        // RecyclerView onClick
        ItemClickSupport.addTo(mTrackLists).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                String albumName = mListMusic.get(position).getAlbumName();
                String urlAlbumArtwork = mListMusic.get(position).getUrlLargeThumbnail();
                String trackName = mListMusic.get(position).getTrackName();
                String urlPreview = mListMusic.get(position).getUrlPreview();
                Intent intent = new Intent(Top10TracksActivity.this, StreamerActivity.class);
                intent.putExtra(getString(R.string.KEY_ARTIST_NAME), mArtistName);
                intent.putExtra(getString(R.string.KEY_ALBUM_NAME), albumName);
                intent.putExtra(getString(R.string.KEY_ALBUM_ARTWORK), urlAlbumArtwork);
                intent.putExtra(getString(R.string.KEY_TRACK_NAME), trackName);
                intent.putExtra(getString(R.string.KEY_PREVIEW_URL), urlPreview);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.STATE_TRACK), mListMusic);
    }


    private void getTop10Tracks(String id) {

        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... id) {

                Tracks tracks = mSpotify.getArtistTopTrack(id[0], getString(R.string.COUNTRY_CODE));
                List<Track> listOfTrack = tracks.tracks;
                String dummySmallImageUri = "";
                String dummyLargeImageUri = "";

                for (Track track : listOfTrack) {
                    Music music = new Music();
                    String trackName = track.name;
                    music.setTrackName(trackName);
                    AlbumSimple album = track.album;
                    String albumName = album.name;
                    music.setAlbumName(albumName);
                    List<Image> listOfImage = album.images;
                    if (listOfImage.size() > 0 && listOfImage.get(1) != null && listOfImage.get(0) != null) {
                        String imageSmallUri = listOfImage.get(1).url;
                        String imageLargeUri = listOfImage.get(0).url;
                        music.setUrlSmallThumbnail(imageSmallUri);
                        music.setUrlLargeThumbnail(imageLargeUri);
                        dummySmallImageUri = imageSmallUri;
                        dummyLargeImageUri = imageLargeUri;
                    } else {
                        music.setUrlSmallThumbnail(dummySmallImageUri);
                        music.setUrlLargeThumbnail(dummyLargeImageUri);
                    }
                    String urlPreview = track.preview_url;
                    music.setUrlPreview(urlPreview);

                    mListMusic.add(music);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mTop10TracksAdapter.setMusics(mListMusic);
            }
        }.execute(id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
