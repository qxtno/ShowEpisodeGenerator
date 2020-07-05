package io.qxtno.showepisodegenerator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment implements FavouritesAdapter.OnItemClickListener {
    FavouritesAdapter mAdapter;
    ArrayList<Show> favList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        int[] tab = {10, 22, 43, 12, 32, 54, 12};
        Show show = new Show("friends", tab);
        favList.add(show);

        RecyclerView recyclerView = view.findViewById(R.id.favRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FavouritesAdapter(getActivity(), favList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ShowActivity.class);

        intent.putExtra("Show Item", favList.get(position));

        startActivity(intent);
    }
}