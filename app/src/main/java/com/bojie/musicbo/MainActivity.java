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

import java.io.IOException;

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

            }
        });

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
