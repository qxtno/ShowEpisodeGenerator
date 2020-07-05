package io.qxtno.showepisodegenerator;

import android.content.Context;
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
    private ArrayList<Show> favArrayList;
    //private ArrayList<Show> favListFull;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    FavouritesAdapter(Context context, ArrayList<Show> favList) {
        mContext = context;
        this.favArrayList = favList;
        //favListFull = new ArrayList<>(favList);
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_item, parent, false);
        return new FavouritesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        Show currentItem = favArrayList.get(position);
        String title = currentItem.getTitle();
        holder.mTextView.setText(title);
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
}
