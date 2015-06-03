package com.bojie.musicbo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bojiejiang on 6/3/15.
 */
public class Music implements Parcelable {

    private String id;
    private String name;
    private String urlThumbnail;

    public Music() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
    }

    protected Music(Parcel in) {
        id = in.readString();
        name = in.readString();
        urlThumbnail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(urlThumbnail);
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
}
