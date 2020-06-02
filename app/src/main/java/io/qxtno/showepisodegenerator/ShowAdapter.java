package io.qxtno.showepisodegenerator;

import android.content.Context;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ShowViewHolder>{

    private Context mContext;


    public class ShowViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
    }
}
