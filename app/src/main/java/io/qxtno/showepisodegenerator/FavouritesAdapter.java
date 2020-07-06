package io.qxtno.showepisodegenerator;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    private Context mContext;
    private ArrayList<FavItem> favArrayList;
    //private ArrayList<Show> favListFull;
    private OnItemClickListener mListener;
    private FavDB favDB;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    FavouritesAdapter(Context context, ArrayList<FavItem> favList) {
        mContext = context;
        this.favArrayList = favList;
        //favListFull = new ArrayList<>(favList);
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        favDB = new FavDB(mContext);
        SharedPreferences preferences = mContext.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_item, parent, false);
        return new FavouritesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        FavItem currentItem = favArrayList.get(position);
        //readCursorData(currentItem, holder);
        holder.mTextView.setText(currentItem.getItem_title());
    }

    @Override
    public int getItemCount() {
        return favArrayList.size();
    }

    class FavouritesViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        FavouritesViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.show_item_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    private void createTableOnFirstStart() {
        favDB.insertEmpty();
        SharedPreferences preferences = mContext.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

/*    private void readCursorData(FavItem favItem, RecyclerView.ViewHolder viewHolder) {
        Cursor cursor = favDB.readAllData(favItem.getKey_id());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try{
            while (cursor.moveToNext()){
                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVOURITE_STATUS));
                favItem.setFav(item_fav_status);

            }

        }finally {
            if(cursor!=null&&cursor.isClosed()){
                cursor.close();
            }
            db.close();
        }
        db.close();
    }*/
}
