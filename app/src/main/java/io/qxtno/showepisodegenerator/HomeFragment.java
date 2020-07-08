package io.qxtno.showepisodegenerator;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class HomeFragment extends Fragment implements ShowAdapter.OnItemClickListener {

    private ArrayList<Show> showListDB;
    private ShowAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ShowDBHelper dbHelper = new ShowDBHelper(getActivity());
        SQLiteDatabase mDatabase = dbHelper.getWritableDatabase();

        SharedPreferences preferences = Objects.requireNonNull(getContext()).getSharedPreferences("preferences", Context.MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart", true);

        if(firstStart){
            Gson gson = new Gson();
            String jsonString = JsonHelper.getJsonFromAssets(Objects.requireNonNull(getActivity()).getApplicationContext());

            Type type = new TypeToken<ArrayList<Show>>() {
            }.getType();

            ArrayList<Show> showArrayList = gson.fromJson(jsonString, type);

            assert showArrayList != null;
            for (Show show : showArrayList) {
                String title = show.getTitle();
                String seasons = Arrays.toString(show.getSeasons());
                String fav = Integer.toString(show.getFav());

                ContentValues cv = new ContentValues();

                cv.put(ShowContract.ShowEntry.COLUMN_TITLE, title);
                cv.put(ShowContract.ShowEntry.COLUMN_SEASONS, seasons);
                cv.put(ShowContract.ShowEntry.COLUMN_FAV, fav);

                mDatabase.insert(ShowContract.ShowEntry.TABLE_NAME, null, cv);
            }

            SharedPreferences prefs = getContext().getSharedPreferences("preferences",Context.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }

        mDatabase = dbHelper.getReadableDatabase();
        String[] field = {ShowContract.ShowEntry.COLUMN_TITLE, ShowContract.ShowEntry.COLUMN_SEASONS, ShowContract.ShowEntry.COLUMN_FAV};
        @SuppressLint("Recycle") Cursor cursor = mDatabase.query(ShowContract.ShowEntry.TABLE_NAME, field, null, null, null, null, null);

        ArrayList<Show> showList = new ArrayList<>();

        int ititle = cursor.getColumnIndex(ShowContract.ShowEntry.COLUMN_TITLE);
        int iseasons = cursor.getColumnIndex(ShowContract.ShowEntry.COLUMN_SEASONS);
        int ifav = cursor.getColumnIndex(ShowContract.ShowEntry.COLUMN_FAV);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String title = cursor.getString(ititle);
            String seasonString = cursor.getString(iseasons);
            String fav = cursor.getString(ifav);
            String[] items = seasonString.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\s", "").split(",");
            int[] seasons = new int[items.length];
            for (int i = 0; i < items.length; i++) {
                try {
                    seasons[i] = Integer.parseInt(items[i]);
                } catch (NumberFormatException nfe) {
                    Log.i("To Array Error Home", "Error while parsing string to an array");
                }

            }
            showList.add(new Show(title, seasons, Integer.parseInt(fav)));
        }
        showListDB = showList;

        Collections.sort(showList, new Comparator<Show>() {
            @Override
            public int compare(Show s1, Show s2) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
        });


        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ShowAdapter(getActivity(), showList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ShowActivity.class);

        intent.putExtra("Show Item", showListDB.get(position));

        startActivity(intent);
    }

    /*private Cursor getAllItems(){
        return mDatabase.query(
                ShowContract.ShowEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ShowContract.ShowEntry.COLUMN_TIMESTAMP + " DESC "
        );
    }*/
}
