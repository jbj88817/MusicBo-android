package com.bojie.musicbo;

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

    SpotifyService mSpotify;

    //private String mJSONResponse = "";

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

    }


    private void searchAndGetArtists(String keyWord) {


        new AsyncTask<String, Void, Void>() {
            ArrayList<Music> listMusic = new ArrayList<>();
            @Override
            protected Void doInBackground(String... keyWord) {


                ArtistsPager artistsPager = mSpotify.searchArtists(keyWord[0]);
                Pager<Artist> PagerArtists = artistsPager.artists;
                List<Artist> artistList = PagerArtists.items;
                String dummyImageUri = "";

                for (Artist artist : artistList) {
                    Music music = new Music();
                    String name = artist.name;
                    String id = artist.id;
                    music.setId(id);
                    music.setName(name);
                    List<Image> listOfImage = artist.images;
                    if (listOfImage.size() > 0 && listOfImage.get(2) != null){
                        String ImageUri = listOfImage.get(2).url;
                        music.setUrlThumbnail(ImageUri);
                        dummyImageUri = ImageUri;
                    } else {
                        music.setUrlThumbnail(dummyImageUri);
                    }
                    listMusic.add(music);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mSearchAdapter.setMusics(listMusic);
            }
        }.execute(keyWord);
    }


//    private void getSearchJSON(String url) throws IOException {
//
//        if (Utils.isNetworkAvailable(this)) {
//
//            OkHttpClient client = new OkHttpClient();
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            Call call = client.newCall(request);
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//                    Toast.makeText(MainActivity.this, "Opps", Toast.LENGTH_SHORT).show();
//                    mJSONResponse = "Opps";
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    mJSONResponse = response.body().string();
//                    if (response.isSuccessful()) {
//                        parseJSONData(mJSONResponse);
//                    }
//
//                }
//            });
//
//        } else {
//            Toast.makeText(this, getString(R.string.network_unavailable_message),
//                    Toast.LENGTH_LONG).show();
//        }
//
//
//    }
//
//    private void parseJSONData(String response) {
//
//        ArrayList<Music> listMusics = new ArrayList<>();
//        if (response != null && response.length() > 0) {
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                JSONObject artistsObject = jsonObject.getJSONObject(KEY_ARTISTS);
//                JSONArray arrayMusic = artistsObject.getJSONArray(KEY_ITEMS);
//                for (int i = 0; i < arrayMusic.length(); i++) {
//                    String id = NA;
//                    String name = NA;
//                    String urlThumbnail = NA;
//                    JSONObject currentMusic = arrayMusic.getJSONObject(i);
//
//                    if (Utils.contains(currentMusic, KEY_NAME)) {
//                        name = currentMusic.getString(KEY_NAME);
//                    }
//                    if (Utils.contains(currentMusic, KEY_ID)) {
//                        id = currentMusic.getString(KEY_ID);
//                    }
//
//                    // Summary
//
//                    Music music = new Music();
//                    music.setId(id);
//                    music.setName(name);
//
//                    if (!id.equals(NA) && !name.equals(NA)) {
//                        listMusics.add(music);
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_search) {
            String keyWord = mEditTextSearch.getText().toString();
            searchAndGetArtists(keyWord);
        }
    }

//    @Override
//    public void onClick(View v) {
//        String input = mEditTextSearch.getText().toString();
//        input = input.replace(" ", "%20");
//        String url = UrlSearchBasic + input + UrlSearchTypeArtist;
//        Log.d("Bojie", url);
//        try {
//            getSearchJSON(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
