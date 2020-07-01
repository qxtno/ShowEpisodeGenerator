package io.qxtno.showepisodegenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ShowViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<Show> showArrayList;
    private ArrayList<Show> showArrayListFull;
    private OnItemClickListener mListener;

    @Override
    public Filter getFilter() {
        return showFilter;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    ShowAdapter(Context context, ArrayList<Show> showsList) {
        mContext = context;
        showArrayList = showsList;
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



    private Filter showFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Show> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(showArrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Show show : showArrayListFull) {
                    if (show.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(show);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            showArrayList.clear();
            showArrayList.addAll((ArrayList) results.values);
        }
    };

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

}
