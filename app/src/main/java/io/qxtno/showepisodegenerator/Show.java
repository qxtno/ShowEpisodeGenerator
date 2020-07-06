package io.qxtno.showepisodegenerator;

import android.os.Parcel;
import android.os.Parcelable;

class Show implements Parcelable {
    private String title;
    private int[] seasons;
    private String fav;
    private String key_id;

    Show(String title, int[] seasons, String fav, String key_id) {
        this.title = title;
        this.seasons = seasons;
        this.fav = fav;
        this.key_id = key_id;
    }

    public Show(String title, int[] seasons, String fav) {
        this.title = title;
        this.seasons = seasons;
        this.fav = fav;
    }

    public Show(String title, int[] seasons) {
        this.title = title;
        this.seasons = seasons;
    }

    protected Show(Parcel in) {
        title = in.readString();
        seasons = in.createIntArray();
        fav = in.readString();
        key_id = in.readString();
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

    String getTitle() {
        return title;
    }

    int[] getSeasons() {
        return seasons;
    }

    public String getFav() {
        return fav;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSeasons(int[] seasons) {
        this.seasons = seasons;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public static Creator<Show> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeIntArray(seasons);
        dest.writeString(fav);
        dest.writeString(key_id);
    }
}
