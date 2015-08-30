package com.bojie.musicbo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class StreamerActivityFragment extends Fragment implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        SeekBar.OnSeekBarChangeListener {

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
    @Bind(R.id.track_bar)
    SeekBar mTrackProgress;

    private String mUrlPreview;
    private MediaPlayer mMediaPlayer;
    private ProgressDialog mProgressDialog;
    private Handler mHandler;
    private List<Music> mMusicList;
    private int mPosition;
    private String mArtistNameContent;

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
            mMusicList = intent.getParcelableArrayListExtra(getString(R.string.STATE_MUSIC_LIST));
            mArtistNameContent = intent.getStringExtra(getString(R.string.KEY_ARTIST_NAME));
            mPosition = intent.getIntExtra(getString(R.string.KEY_TRACK_POSITION), 0);
            updatePlayerUI();

            bufferMusic();

        }
    }

    private void bufferMusic() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(getActivity());
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

    private void updatePlayerUI() {
        String albumName = mMusicList.get(mPosition).getAlbumName();
        String urlAlbumArtwork = mMusicList.get(mPosition).getUrlLargeThumbnail();
        String trackName = mMusicList.get(mPosition).getTrackName();
        mUrlPreview = mMusicList.get(mPosition).getUrlPreview();
        mArtistName.setText(mArtistNameContent);
        mAlbumName.setText(albumName);
        mTrackName.setText(trackName);
        Picasso.with(getContext())
                .load(urlAlbumArtwork)
                .placeholder(R.drawable.image_placeholder)
                .into(mAlbumArtwork);
    }

    @OnClick(R.id.btn_play_pause)
    public void playOrPauseButton() {
        if (!mMediaPlayer.isPlaying()) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }

            btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            mMediaPlayer.start();

            setSeekBar();


        } else {
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            mMediaPlayer.pause();
        }

    }

    private void setSeekBar() {
        mTrackProgress.setOnSeekBarChangeListener(this);

        mHandler = new Handler(); //Make sure you update Seekbar on UI thread
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mMediaPlayer != null) {
                    try {
                        int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                        mTrackProgress.setProgress(mCurrentPosition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                mHandler.postDelayed(this, 500);
            }
        });
    }

    @OnClick(R.id.btn_previous)
    public void previosButton() {

        if (mPosition != 0) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mPosition--;
            updatePlayerUI();
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            bufferMusic();
            setSeekBar();
        } else {
            Toast.makeText(getActivity(), "There is no previous track.", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        btnPlayPause.setEnabled(true);
        mProgressDialog.dismiss();
        mTrackProgress.setMax(mMediaPlayer.getDuration());
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mMediaPlayer != null && fromUser) {
            mMediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
