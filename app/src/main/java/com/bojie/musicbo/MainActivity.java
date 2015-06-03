package com.bojie.musicbo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.bojie.musicbo.KEYS.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEditTextSearch;
    Button mSearchButton;
    public static final String UrlSearchBasic = "https://api.spotify.com/v1/search?q=";
    public static final String UrlSearchTypeArtist = "&type=artist";
    private String mJSONResponse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextSearch = (EditText) findViewById(R.id.et_search);
        mSearchButton = (Button) findViewById(R.id.btn_search);
        mSearchButton.setOnClickListener(this);

    }

    private void getSearchJSON(String url) throws IOException {

        if (Utils.isNetworkAvailable(this)) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Toast.makeText(MainActivity.this, "Opps", Toast.LENGTH_SHORT).show();
                    mJSONResponse = "Opps";
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    mJSONResponse = response.body().string();
                    if (response.isSuccessful()) {
                        parseJSONData(mJSONResponse);
                    }

                }
            });

        } else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),
                    Toast.LENGTH_LONG).show();
        }


    }

    private void parseJSONData(String response) {

        ArrayList<Music> listMusics = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject artistsObject = jsonObject.getJSONObject(KEY_ARTISTS);
                JSONArray arrayMusic = artistsObject.getJSONArray(KEY_ITEMS);
                for (int i = 0; i < arrayMusic.length(); i++) {
                    String id = NA;
                    String name = NA;
                    String urlThumbnail = NA;
                    JSONObject currentMusic = arrayMusic.getJSONObject(i);

                    if (Utils.contains(currentMusic, KEY_NAME)) {
                        name = currentMusic.getString(KEY_NAME);
                    }
                    if (Utils.contains(currentMusic, KEY_ID)) {
                        id = currentMusic.getString(KEY_ID);
                    }

                    // Summary

                    Music music = new Music();
                    music.setId(id);
                    music.setName(name);

                    if (!id.equals(NA) && !name.equals(NA)) {
                        listMusics.add(music);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

    @Override
    public void onClick(View v) {
        String input = mEditTextSearch.getText().toString();
        input = input.replace(" ", "%20");
        String url = UrlSearchBasic + input + UrlSearchTypeArtist;
        Log.d("Bojie", url);
        try {
            getSearchJSON(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
