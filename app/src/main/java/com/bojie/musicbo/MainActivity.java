package com.bojie.musicbo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTextSearch;
    private Button mSearchButton;
    private RecyclerView mSearchLists;
    private SearchAdapter mSearchAdapter;
    private ArrayList<Music> mListMusic;

    private SpotifyService mSpotify;
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextSearch = (EditText) findViewById(R.id.et_search);
        mSearchButton = (Button) findViewById(R.id.btn_search);
        mSearchButton.setOnClickListener(this);
        mSearchLists = (RecyclerView) findViewById(R.id.listSearch);
        mSearchAdapter = new SearchAdapter(this);
        mSearchLists.setAdapter(mSearchAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mSearchLists.setLayoutManager(layoutManager);


        SpotifyApi api = new SpotifyApi();
        mSpotify = api.getService();

        mListMusic = new ArrayList<>();

        if (savedInstanceState != null) {
            mListMusic = savedInstanceState.getParcelableArrayList(getString(R.string.STATE_MUSIC));
        } else {
           // Todo
        }
        mSearchAdapter.setMusics(mListMusic);

    }

    public void resetSearchList() {
        if (mListMusic.size() != 0){
            mSearchAdapter.setMusics(mListMusic);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.STATE_MUSIC), mListMusic);
    }


    private void searchAndGetArtists(String keyWord) {

        new AsyncTask<String, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog= new ProgressDialog(MainActivity.this);
                mProgressDialog.setTitle("Searching");
                mProgressDialog.setMessage("Please wait while searching...");
                mProgressDialog.show();
            }

            @Override
            protected Void doInBackground(String... keyWord) {

                mListMusic = new ArrayList<>();
                ArtistsPager artistsPager = mSpotify.searchArtists(keyWord[0]);
                Pager<Artist> PagerArtists = artistsPager.artists;
                List<Artist> artistList = PagerArtists.items;
                String dummyImageUri = "";

                for (Artist artist : artistList) {
                    Music music = new Music();
                    String artistName = artist.name;
                    String id = artist.id;
                    music.setId(id);
                    music.setArtistName(artistName);
                    List<Image> listOfImage = artist.images;
                    if (listOfImage.size() > 0 && listOfImage.get(2) != null){
                        String ImageUri = listOfImage.get(2).url;
                        music.setUrlArtistThumbnail(ImageUri);
                        dummyImageUri = ImageUri;
                    } else {
                        music.setUrlArtistThumbnail(dummyImageUri);
                    }
                    mListMusic.add(music);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mSearchAdapter.setMusics(mListMusic);
                mProgressDialog.dismiss();
            }
        }.execute(keyWord);
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_search) {
            String keyWord = mEditTextSearch.getText().toString();
            searchAndGetArtists(keyWord);

            // RecyclerView onClick
            ItemClickSupport.addTo(mSearchLists).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    String id = mListMusic.get(position).getId();
                    String artistName = mListMusic.get(position).getArtistName();
                    Intent intent = new Intent(MainActivity.this, Top10TracksActivity.class);
                    intent.putExtra(getString(R.string.KEY_ID), id);
                    intent.putExtra(getString(R.string.KEY_ARTIST_NAME), artistName);
                    startActivity(intent);
                }

            });

            // RecyclerView onLongClick
//            ItemClickSupport.addTo(mSearchLists).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
//
//                @Override
//                public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
//                    Toast.makeText(getApplicationContext(), position + " has long click", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//            });
        }
    }
}
