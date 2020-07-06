package io.qxtno.showepisodegenerator;

import android.os.Parcel;
import android.os.Parcelable;

public class FavItem implements Parcelable {
    private String item_title;
    private String key_id;
    private String item_seasons;

    public FavItem() {
    }

    public FavItem(String item_title, String key_id, String item_seasons) {
        this.item_title = item_title;
        this.key_id = key_id;
        this.item_seasons = item_seasons;
    }

    protected FavItem(Parcel in) {
        item_title = in.readString();
        key_id = in.readString();
        item_seasons = in.readString();
    }

    public static final Creator<FavItem> CREATOR = new Creator<FavItem>() {
        @Override
        public FavItem createFromParcel(Parcel in) {
            return new FavItem(in);
        }

        @Override
        public FavItem[] newArray(int size) {
            return new FavItem[size];
        }
    };

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getItem_seasons() {
        return item_seasons;
    }

    public void setItem_seasons(String item_seasons) {
        this.item_seasons = item_seasons;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(item_title);
        dest.writeString(item_seasons);
        dest.writeString(key_id);
    }
}
