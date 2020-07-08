package io.qxtno.showepisodegenerator;

import android.os.Parcel;
import android.os.Parcelable;

class Show implements Parcelable {
    private int id;
    private String title;
    private int[] seasons;
    private int fav;

    public Show() {
    }

    Show(int id, String title, int[] seasons, int fav) {
        this.id = id;
        this.title = title;
        this.seasons = seasons;
        this.fav = fav;
    }

    public Show(String title, int[] seasons, int fav) {
        this.title = title;
        this.seasons = seasons;
        this.fav = fav;
    }

    protected Show(Parcel in) {
        id = in.readInt();
        title = in.readString();
        seasons = in.createIntArray();
        fav = in.readInt();
    }

    public static final Creator<Show> CREATOR = new Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getTitle() {
        return title;
    }

    public void setSeasons(int[] seasons) {
        this.seasons = seasons;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    int[] getSeasons() {
        return seasons;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeIntArray(seasons);
        dest.writeInt(fav);
    }
}
