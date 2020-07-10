package io.qxtno.showepisodegenerator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class HomeFragment extends Fragment implements ShowAdapter.OnItemClickListener {

    private ArrayList<Show> showList;
    private ShowAdapter mAdapter;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private boolean sorted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ShowDBHelper dbHelper = new ShowDBHelper(getActivity());

        preferences = Objects.requireNonNull(getContext()).getSharedPreferences("preferences", Context.MODE_PRIVATE);
        boolean firstStart = preferences.getBoolean("firstStart", true);

        if (firstStart) {
            Gson gson = new Gson();
            String jsonString = JsonHelper.getJsonFromAssets(Objects.requireNonNull(getActivity()).getApplicationContext());

            Type type = new TypeToken<ArrayList<Show>>() {
            }.getType();

            ArrayList<Show> showArrayList = gson.fromJson(jsonString, type);

            assert showArrayList != null;
            for (Show show : showArrayList) {
                dbHelper.addShow(show);
            }

            preferences = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
            editor = preferences.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }

        showList = (ArrayList<Show>) dbHelper.getAllShows();

        sortInit();

        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ShowAdapter(getActivity(), showList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    private void sortInit() {
        preferences = Objects.requireNonNull(getContext()).getSharedPreferences("preferences",Context.MODE_PRIVATE);
        sorted = preferences.getBoolean("sorted",true);

        if(sorted){
            sort();
        }else {
            sortReverse();
        }
    }

    private void sort() {
        Collections.sort(showList, new Comparator<Show>() {
            @Override
            public int compare(Show s1, Show s2) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
        });
    }

    private void sortReverse(){
        Collections.sort(showList, new Comparator<Show>() {
            @Override
            public int compare(Show s1, Show s2) {
                return s2.getTitle().compareTo(s1.getTitle());
            }
        });
    }

    private void sortList(){
        preferences = Objects.requireNonNull(getContext()).getSharedPreferences("preferences",Context.MODE_PRIVATE);
        sorted = preferences.getBoolean("sorted",true);
        editor = preferences.edit();
        if(sorted){
            sortReverse();
            editor.putBoolean("sorted", false);
            sorted = preferences.getBoolean("sorted",false);
            Toast.makeText(getActivity(),R.string.natural_order_reversed,Toast.LENGTH_SHORT).show();
        }else{
            sort();
            editor.putBoolean("sorted", true);
            sorted = preferences.getBoolean("sorted",true);
            Toast.makeText(getActivity(),R.string.natural_order,Toast.LENGTH_SHORT).show();
        }
        editor.apply();
        mAdapter.notifyDataSetChanged();
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

        final MenuItem sort = menu.findItem(R.id.sort_a_z);

        sort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sortList();
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ShowActivity.class);

        intent.putExtra("Show Item", showList.get(position));

        startActivity(intent);
    }
}
