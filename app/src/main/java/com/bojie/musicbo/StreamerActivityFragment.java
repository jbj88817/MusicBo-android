package com.bojie.musicbo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class StreamerActivityFragment extends Fragment {

    @Bind(R.id.artist_name)
    TextView mArtistName;
    @Bind(R.id.album_name)
    TextView mAlbumName;
    @Bind(R.id.album_artwork)
    ImageView mAlbumArtwork;
    @Bind(R.id.track_name)
    TextView mTrackName;

    private String mUrlPreview;

    public StreamerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_streamer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        Intent intent = getActivity().getIntent();
        if (intent != null) {
            String artistName = intent.getStringExtra(getString(R.string.KEY_ARTIST_NAME));
            String albumName = intent.getStringExtra(getString(R.string.KEY_ALBUM_NAME));
            String urlAlbumArtwork = intent.getStringExtra(getString(R.string.KEY_ALBUM_ARTWORK));
            String trackName = intent.getStringExtra(getString(R.string.KEY_TRACK_NAME));
            mArtistName.setText(artistName);
            mAlbumName.setText(albumName);
            mTrackName.setText(trackName);
            Picasso.with(getContext())
                    .load(urlAlbumArtwork)
                    .placeholder(R.drawable.image_placeholder)
                    .into(mAlbumArtwork);

            mUrlPreview = intent.getStringExtra(getString(R.string.KEY_PREVIEW_URL));
        }
    }
}
