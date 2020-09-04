package io.qxtno.showepisodegenerator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Arrays;
import java.util.Random;

public class ShowFragment extends Fragment {

    private View view;
    private Show show;
    private int id;
    private int deletedId;
    private int randomEpisode;
    private int randomSeason;
    private int[] seasons;
    private TextView resultTextView;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private String episodeInfo;

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_show, container, false);

        prefs = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            show = bundle.getParcelable("show");
        }
        assert show != null;
        id = show.getId();

        ((MainActivity)requireActivity()).toolbar.setTitle(show.getTitle());

        setUpGen();

        final ImageButton add = view.findViewById(R.id.frag_add_to_fav);

        if(!show.isFav()){
            add.setImageResource(R.drawable.ic_favorite_empty);
        }else {
            add.setImageResource(R.drawable.ic_favorite);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDBHelper dbHelper = new ShowDBHelper(requireActivity().getApplicationContext());
                if (!show.isFav()) {
                    show.setFav(true);
                    add.setImageResource(R.drawable.ic_favorite);
                    Toast.makeText(requireActivity(),R.string.fav_added,Toast.LENGTH_SHORT).show();
                } else {
                    show.setFav(false);
                    add.setImageResource(R.drawable.ic_favorite_empty);
                    Toast.makeText(requireActivity(),R.string.fav_removed,Toast.LENGTH_SHORT).show();
                }
                dbHelper.updateFav(show);
            }
        });

        editor = prefs.edit();
        editor.putInt("id", id).apply();

        setHasOptionsMenu(true);
        return view;
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (show.isCustom()) {
            inflater.inflate(R.menu.show_activity_menu, menu);

            final MenuItem edit = menu.findItem(R.id.action_edit);
            edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (show.isCustom()) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("show", show);

                        Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.editFragment, bundle);

                        return true;
                    } else {
                        Toast.makeText(getActivity(), R.string.cannot_edit, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            });

            final MenuItem delete = menu.findItem(R.id.action_delete);
            delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (show.isCustom()) {
                        ShowDBHelper dbHelper = new ShowDBHelper(requireActivity().getApplicationContext());
                        dbHelper.deleteShow(show);
                        requireActivity().onBackPressed();
                        Toast.makeText(getActivity(), R.string.delete_done, Toast.LENGTH_SHORT).show();
                        deletedId = id;
                        editor = prefs.edit();
                        editor.putInt("deletedId", deletedId).apply();
                        return true;
                    } else {
                        Toast.makeText(getActivity(), R.string.cannot_delete, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            });
        } else {
            inflater.inflate(R.menu.non_stock_menu, menu);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpGen();
    }

    private void setUpGen() {
        ShowDBHelper dbHelper = new ShowDBHelper(requireActivity().getApplicationContext());
        show = dbHelper.getShow(id);
        assert show != null;
        seasons = show.getSeasons();
        id = show.getId();

        resultTextView = view.findViewById(R.id.frag_result);
        resultTextView.setText(R.string.result_here);

        String seasonString = Arrays.toString(show.getSeasons());
        String[] items = seasonString.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\s", "").split(",");
        seasons = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                seasons[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {
                Log.i("To Array Error DBHelper", "Error while parsing string into an array");
            }
        }
        show.setSeasons(seasons);

        TextView datesTextView = view.findViewById(R.id.show_dates_text_view);
        datesTextView.setText(show.getDate());

        Button randomize = view.findViewById(R.id.frag_random);

        editor = prefs.edit();
        editor.putString("resultString", "Result will appear here").apply();

        randomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random generator = new Random();
                randomSeason = generator.nextInt(seasons.length) + 1;
                randomEpisode = generator.nextInt(seasons[randomSeason - 1]) + 1;
                String space = " ";

                episodeInfo = getResources().getString(R.string.se) +
                        randomSeason +
                        space +
                        getResources().getString(R.string.ep) +
                        randomEpisode;
                resultTextView.setText(episodeInfo);

                editor = prefs.edit();
                @SuppressLint("DefaultLocale") String resultString = "S" + String.format("%02d",randomSeason) + "E" + String.format("%02d",randomEpisode);
                editor.putString("resultString", resultString).apply();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("results",episodeInfo);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            episodeInfo = savedInstanceState.getString("results");
        }

        resultTextView = view.findViewById(R.id.frag_result);
        resultTextView.setText(episodeInfo);
    }
}
