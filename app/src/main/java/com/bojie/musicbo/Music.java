package com.bojie.musicbo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bojiejiang on 6/3/15.
 */
public class Music implements Parcelable {

    private String id;
    private String artistName;
    private String trackName;
    private String albumName;
    private String urlLargeThumbnail;
    private String urlSmallThumbnail;
    private String urlArtistThumbnail;
    private String urlPreview;

    protected Music(Parcel in) {
        id = in.readString();
        artistName = in.readString();
        trackName = in.readString();
        albumName = in.readString();
        urlLargeThumbnail = in.readString();
        urlSmallThumbnail = in.readString();
        urlArtistThumbnail = in.readString();
        urlPreview = in.readString();
    }

    public Music() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(artistName);
        dest.writeString(trackName);
        dest.writeString(albumName);
        dest.writeString(urlLargeThumbnail);
        dest.writeString(urlSmallThumbnail);
        dest.writeString(urlArtistThumbnail);
        dest.writeString(urlPreview);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getUrlLargeThumbnail() {
        return urlLargeThumbnail;
    }

    public void setUrlLargeThumbnail(String urlLargeThumbnail) {
        this.urlLargeThumbnail = urlLargeThumbnail;
    }

    public String getUrlSmallThumbnail() {
        return urlSmallThumbnail;
    }

    public void setUrlSmallThumbnail(String urlSmallThumbnail) {
        this.urlSmallThumbnail = urlSmallThumbnail;
    }

    public String getUrlPreview() {
        return urlPreview;
    }

    public void setUrlPreview(String urlPreview) {
        this.urlPreview = urlPreview;
    }

    public String getUrlArtistThumbnail() {
        return urlArtistThumbnail;
    }

    public void setUrlArtistThumbnail(String urlArtistThumbnail) {
        this.urlArtistThumbnail = urlArtistThumbnail;
    }
}