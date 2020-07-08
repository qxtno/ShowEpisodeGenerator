package io.qxtno.showepisodegenerator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment implements FavouritesAdapter.OnItemClickListener {
    FavouritesAdapter mAdapter;
    ArrayList<FavItem> favList = new ArrayList<>();
    private FavDB favDB;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        ShowDBHelper dbHelper = new ShowDBHelper(getActivity());

        showFavListDB = (ArrayList<Show>) dbHelper.getFavShows();

        RecyclerView recyclerView = view.findViewById(R.id.favRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadData();


        return view;
    }

    private void loadData() {
        if (favList != null) {
            favList.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.selectFav();
        try {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_TITLE));
                String id = cursor.getString(cursor.getColumnIndex(FavDB.KEY_ID));
                String seasons = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_SEASONS));
                FavItem favItem = new FavItem(title, id, seasons);
                favList.add(favItem);
            }
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }

        mAdapter = new FavouritesAdapter(getActivity(), favList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ShowActivity.class);

        intent.putExtra("Fav Item", favList.get(position));

        startActivity(intent);
    }
}