package io.qxtno.showepisodegenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ShowViewHolder> {
    private Context mContext;
    private ArrayList<Show> showArrayList;
    private ArrayList<Show> showArrayListFull;
    private ArrayList<Show> filteredList = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    ShowAdapter(Context context, ArrayList<Show> showsList) {
        mContext = context;
        this.showArrayList = showsList;
        showArrayListFull = new ArrayList<>(showsList);
    }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.show_item, parent, false);
        return new ShowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowViewHolder holder, int position) {
        Show currentItem = showArrayList.get(position);
        String title = currentItem.getTitle();

        holder.mTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        return showArrayList.size();
    }

    class ShowViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        ShowViewHolder(@NonNull View itemView) {
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

    public void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(showArrayListFull);
        } else {
            text = text.toLowerCase();
            for (Show show : showArrayListFull) {
                if (show.getTitle().toLowerCase().contains(text)) {
                    filteredList.add(show);
                }
            }
        }
        showArrayList.clear();
        showArrayList.addAll(filteredList);
        notifyDataSetChanged();
    }
}