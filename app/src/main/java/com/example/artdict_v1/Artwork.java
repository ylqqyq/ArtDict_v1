package com.example.artdict_v1;

import android.os.Parcel;
import android.os.Parcelable;

public class Artwork implements Parcelable {

    int id;
    String title;
    String artist_display;
    String medium_display;
    String dimensions;
    String image_id;
    boolean is_zoomable;

    public Artwork() {

    }

    public Artwork(int id, String title, String image_id) {
        this.id = id;
        this.title = title;
        this.image_id = image_id;
    }

    public Artwork(int id, String title, String artist_display, String medium_display, String dimensions, String image_id, boolean is_zoomable) {
        this.id = id;
        this.title = title;
        this.artist_display = artist_display;
        this.medium_display = medium_display;
        this.dimensions = dimensions;
        this.image_id = image_id;
        this.is_zoomable = is_zoomable;
    }

    protected Artwork(Parcel in) {
        id = in.readInt();
        title = in.readString();
        artist_display = in.readString();
        medium_display = in.readString();
        dimensions = in.readString();
        image_id = in.readString();
        is_zoomable = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(artist_display);
        dest.writeString(medium_display);
        dest.writeString(dimensions);
        dest.writeString(image_id);
        dest.writeByte((byte) (is_zoomable ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Artwork> CREATOR = new Creator<Artwork>() {
        @Override
        public Artwork createFromParcel(Parcel in) {
            return new Artwork(in);
        }

        @Override
        public Artwork[] newArray(int size) {
            return new Artwork[size];
        }
    };
}
