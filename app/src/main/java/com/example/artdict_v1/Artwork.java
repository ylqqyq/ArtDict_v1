package com.example.artdict_v1;

import android.os.Parcel;
import android.os.Parcelable;

public class Artwork implements Parcelable {

    public String id;
    public String title;
    String artist_display;
    String medium_display;
    String dimensions;
    String description;
    String image_id;
    String source;


    public Artwork() {

    }

    public Artwork(String id, String title,String image_id,String source) {
        this.id = id;
        this.title = title;
        this.image_id = image_id;
        this.source = source;

    }

    //constructor for list display
    public Artwork(String id, String title,String source) {
        this.id = id;
        this.title = title;
        this.source = source;

    }

    public Artwork(String id, String title, String artist_display, String medium_display, String dimensions, String image_id,  String description) {
        this.id = id;
        this.title = title;
        this.artist_display = artist_display;
        this.medium_display = medium_display;
        this.dimensions = dimensions;
        this.image_id = image_id;
        this.description = description;

    }

    protected Artwork(Parcel in) {
        id = in.readString();
        title = in.readString();
        artist_display = in.readString();
        medium_display = in.readString();
        dimensions = in.readString();
        image_id = in.readString();
        description = in.readString();
        source = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(artist_display);
        dest.writeString(medium_display);
        dest.writeString(dimensions);
        dest.writeString(image_id);
        dest.writeString(description);
        dest.writeString(source);
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
