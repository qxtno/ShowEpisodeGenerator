package io.qxtno.showepisodegenerator;

import android.os.Parcel;
import android.os.Parcelable;

class Show implements Parcelable {
    private String title;
    private int[] seasons;

    Show(String title, int[] seasons) {
        this.title = title;
        this.seasons = seasons;
    }

    protected Show(Parcel in) {
        title = in.readString();
        seasons = in.createIntArray();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeIntArray(seasons);
    }
}
