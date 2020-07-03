package io.qxtno.showepisodegenerator;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class HomeFragment extends Fragment implements ShowAdapter.OnItemClickListener {

    public static final String TITLE = "title";
    public static final String SEASONS = "seasons";
    private ArrayList<Show> showArrayList;
    private ShowAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Gson gson = new Gson();
        String jsonString = JsonHelper.getJsonFromAssets(Objects.requireNonNull(getActivity()).getApplicationContext());

        Type type = new TypeToken<ArrayList<Show>>() {
        }.getType();
        showArrayList = gson.fromJson(jsonString, type);

        assert showArrayList != null;
        Collections.sort(showArrayList, new Comparator<Show>() {
            @Override
            public int compare(Show s1, Show s2) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
        });


        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ShowAdapter(getActivity(), showArrayList);
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
        Show clickedItem = showArrayList.get(position);
        Bundle b = new Bundle();
        b.putIntArray(SEASONS, clickedItem.getSeasons());

        intent.putExtra(TITLE, clickedItem.getTitle());
        intent.putExtras(b);

        startActivity(intent);
    }
}
