package com.bojie.musicbo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class StreamerActivityFragment extends Fragment implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    @Bind(R.id.artist_name)
    TextView mArtistName;
    @Bind(R.id.album_name)
    TextView mAlbumName;
    @Bind(R.id.album_artwork)
    ImageView mAlbumArtwork;
    @Bind(R.id.track_name)
    TextView mTrackName;
    @Bind(R.id.btn_play_pause)
    ImageButton btnPlayPause;

    private String mUrlPreview;
    MediaPlayer mMediaPlayer;
    ProgressDialog mProgressDialog;

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
        btnPlayPause.setEnabled(false);

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

            new AsyncTask<Void, Void, Void>(){

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    mProgressDialog= new ProgressDialog(getActivity());
                    mProgressDialog.setTitle("Loading");
                    mProgressDialog.setMessage("Wait while loading...");
                    mProgressDialog.show();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mMediaPlayer.setDataSource(mUrlPreview);
                        mMediaPlayer.setOnPreparedListener(StreamerActivityFragment.this);
                        mMediaPlayer.setOnCompletionListener(StreamerActivityFragment.this);
                        mMediaPlayer.prepare(); // might take long! (for buffering, etc)
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

            }.execute();

        }
    }

    @OnClick(R.id.btn_play_pause)
    public void playOrPauseButton() {
        if (!mMediaPlayer.isPlaying()) {
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
            btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            mMediaPlayer.start();
        } else {
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            mMediaPlayer.pause();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        btnPlayPause.setEnabled(true);
        mProgressDialog.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
        mMediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
    }
}
