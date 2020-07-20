package io.qxtno.showepisodegenerator;

import android.content.Context;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FavouritesFragment extends Fragment implements FavouritesAdapter.OnItemClickListener {
    private FavouritesAdapter mAdapter;
    private ArrayList<Show> showFavListDB;
    private SharedPreferences prefs;
    private boolean sortedFav;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        ShowDBHelper dbHelper = new ShowDBHelper(getActivity());

        showFavListDB = (ArrayList<Show>) dbHelper.getFavShows();

        sortInit();

        RecyclerView recyclerView = view.findViewById(R.id.favRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FavouritesAdapter(getActivity(), showFavListDB);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    private void sortInit() {
        prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        sortedFav = prefs.getBoolean("sortedFav", true);

        if (sortedFav) {
            sort();
        } else {
            sortReverse();
        }
    }

    private void sort() {
        Collections.sort(showFavListDB, new Comparator<Show>() {
            @Override
            public int compare(Show s1, Show s2) {
                return s1.getTitle().compareTo(s2.getTitle());
            }
        });
    }

    private void sortReverse() {
        Collections.sort(showFavListDB, new Comparator<Show>() {
            @Override
            public int compare(Show s1, Show s2) {
                return s2.getTitle().compareTo(s1.getTitle());
            }
        });
    }

    private void sortList() {
        prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        sortedFav = prefs.getBoolean("sortedFav", true);
        SharedPreferences.Editor editor = prefs.edit();
        if (sortedFav) {
            sortReverse();
            editor.putBoolean("sortedFav", false);
            sortedFav = prefs.getBoolean("sortedFav", false);
            Toast.makeText(getActivity(), R.string.natural_order_reversed, Toast.LENGTH_SHORT).show();
        } else {
            sort();
            editor.putBoolean("sortedFav", true);
            sortedFav = prefs.getBoolean("sortedFav", true);
            Toast.makeText(getActivity(), R.string.natural_order, Toast.LENGTH_SHORT).show();
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

        MenuItem sort = menu.findItem(R.id.action_sort);
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
        Bundle bundle = new Bundle();
        bundle.putParcelable("show", showFavListDB.get(position));

        Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.showFragment, bundle);
    }
}